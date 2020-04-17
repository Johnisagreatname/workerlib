package yizhit.workerlib.timer;

import ccait.ccweb.utils.EncryptionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.query.Datetime;
import entity.tool.util.FastJsonUtils;
import entity.tool.util.RequestUtils;
import entity.tool.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yizhit.workerlib.entites.AllUserInfo;
import yizhit.workerlib.entites.PageNum;
import yizhit.workerlib.entites.Privilege;
import yizhit.workerlib.entites.ProjectInfo;
import yizhit.workerlib.entites.RoleModel;
import yizhit.workerlib.entites.TimerProfile;
import yizhit.workerlib.entites.UserGroupRoleModel;
import yizhit.workerlib.entites.UserModel;
import yizhit.workerlib.interfaceuilt.FinalUtil;
import yizhit.workerlib.trigger.AllUserTrigger;
import yizhit.workerlib.uitls.DateUtils;
import yizhit.workerlib.uitls.ParamUitls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *@ClassName InsertUser
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/15  18:56
 */
public class InsertUser {

    private static final Logger logger = LogManager.getLogger(InsertUser.class);

    public void user(String md5PublicKey, String aesPublicKey, String qrCodePath, int width, int height, String server) {

        try {
            //查询工程ID
            ProjectInfo projectInfo = new ProjectInfo();

            //查找工人id   写死的
            RoleModel roleModel= new RoleModel();
            roleModel.setRoleName("工人");
            RoleModel roleId = roleModel.where("[roleName]=#{roleName}").first();

            Privilege privilege = new Privilege();
            String privilegeId = UUID.randomUUID().toString().replace("-", "");
            privilege.setPrivilegeId(privilegeId);
            privilege.setRoleId(roleId.getRoleId());
            privilege.setCanAdd(1);
            privilege.setCanDelete(0);
            privilege.setCanUpdate(0);
            privilege.setCanView(1);
            privilege.setCanDownload(1);
            privilege.setCanPreview(1);
            privilege.setCanUpload(1);
            privilege.setCanExport(1);
            privilege.setCanImport(0);
            privilege.setCanDecrypt(0);
            privilege.setCanList(1);
            privilege.setCanQuery(1);
            privilege.setScope(5);
            privilege.setUserPath("0/1");
            privilege.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            Privilege rolejs = privilege.where("[roleId]=#{roleId}").first();
            if (rolejs == null){
                privilege.insert();
            }else{
                logger.info("角色已存在");
            }

            TimerProfile timerProfile = new TimerProfile();
            timerProfile.setKey("alluser");
            List<TimerProfile> profiles = timerProfile.where("[key]=#{key}").query();

            //查询所有工程
            List<ProjectInfo> projectInfoList = projectInfo.where("1=1").select("project_id").query(ProjectInfo.class);

            //项目工程页码
            for (ProjectInfo projectInfoitem : projectInfoList) {
                timerProfile.setPid(projectInfoitem.getEafId());
                Optional<TimerProfile> optionals = profiles.stream().filter(a -> a.getPid().equals(projectInfoitem.getEafId())).findFirst();
                TimerProfile currentTimerProfile = null;
                if(optionals.isPresent()) {
                    currentTimerProfile = optionals.get();
                }

                if(currentTimerProfile == null){
                    timerProfile.setValue(1);
                    timerProfile.insert();
                }
                String eafId = projectInfoitem.getEafId();
                allUser(1, eafId, md5PublicKey, aesPublicKey, qrCodePath, width, height, server, roleId);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Author xieya
     * @Description
     * @Date 2020/4/16 17:21
     * @param userPageNum
     * @param eafId
     * @return void
     **/
    private void allUser(int userPageNum, String eafId, String md5PublicKey, String aesPublicKey, String qrCodePath, int width, int height, String server, RoleModel roleId){
        try {
            String user = ParamUitls.setParam(userPageNum, ParamUitls.user, eafId, null);
            logger.info("入参user=" + user);
            HashMap<String, String> header = ParamUitls.setHeader();
            //根据工程id查询人员信息
            String userResult = RequestUtils.post(FinalUtil.url, user, header);
            logger.info("返回参数userResult=" + userResult);
            JSONObject userJson = JSONObject.parseObject(userResult);

            int total = Integer.parseInt(String.valueOf(userJson.getJSONObject("data").get("total")));
            int pageSize = Integer.parseInt(String.valueOf(userJson.getJSONObject("data").get("pageSize")));
            int intTotal = total / pageSize;

            // 数据获取正确
            if (userJson.containsKey("code") && userJson.get("code").equals("0")) {
                JSONArray array = userJson.getJSONObject("data").getJSONArray("list");
                if(array == null || array.size() == 0){
                    //项目下没有返回数据
                    return;
                }

                userPageNum++;

                String text = array.toJSONString();
                List<AllUserInfo> userList = JSONArray.parseArray(text, AllUserInfo.class);

                AllUserInfo allUserInfoByDb = null;

                for (AllUserInfo allUserInfo : userList) {
                    try {
                        //给user表插入所有人员信息
                        UserModel userModel = new UserModel();
                        userModel.setUsername(allUserInfo.getCwrIdnum());
                        String passWord = "";
                        if (StringUtils.isEmpty(allUserInfo.getCwrIdnum()) || allUserInfo.getCwrIdnum().length() < 6) {
                            passWord = "123456";
                        } else {
                            passWord = allUserInfo.getCwrIdnum().substring(allUserInfo.getCwrIdnum().length() - 6);
                        }
                        userModel.setPassword(EncryptionUtil.md5(passWord, md5PublicKey, FinalUtil.encoding));
                        userModel.setPath("0/1");
                        userModel.setCreateBy(Integer.valueOf(1));
                        userModel.setCreateOn(DateUtils.getCurrentDateTime());
                        //根据id查询一条数据
                        allUserInfoByDb = new AllUserInfo().where(String.format("[eafId]='%s'", allUserInfo.getEafId())).first();

                        UserModel currentUser = userModel.where("[username]=#{username}").first();
                        if(currentUser != null && currentUser.getId().intValue() != currentUser.getCreateBy().intValue()){
                            currentUser.where("[id]=#{id}").update("[createBy]=#{id}");
                        }

                        if (currentUser == null) {
                            allUserInfo = AllUserTrigger.genQrCode(FastJsonUtils.convert(allUserInfo, Map.class), userModel, md5PublicKey, aesPublicKey, qrCodePath, FinalUtil.encoding, width, height, server, false);
                        } else {
                            if (allUserInfoByDb == null) {
                                AllUserInfo temp = AllUserTrigger.genQrCode(FastJsonUtils.convert(allUserInfo, Map.class), userModel, md5PublicKey, aesPublicKey, qrCodePath, FinalUtil.encoding, width, height, server, false);
                                allUserInfo.setQrCode(temp.getQrCode());
                            }
                            allUserInfo.setUserid(currentUser.getId());
                            allUserInfo.setCreateBy(currentUser.getId());
                        }

                        //给UserGroupRole表插入所有人员信息
                        UserGroupRoleModel userGroupRoleModel = new UserGroupRoleModel();
                        String userGroupRoleId = UUID.randomUUID().toString().replace("-", "");
                        userGroupRoleModel.setUserGroupRoleId(userGroupRoleId);
                        userGroupRoleModel.setRoleId(roleId.getRoleId());
                        userGroupRoleModel.setPath("0/1");
                        userGroupRoleModel.setCreateOn(new Date());
                        userGroupRoleModel.setCreateBy(allUserInfo.getCreateBy());
                        userGroupRoleModel.setUserId(allUserInfo.getUserid());
                        if ("工人".equals(roleId.getRoleName()) && !userGroupRoleModel.where("[userId]=#{userId}")
                                .and("[roleId]=#{roleId}").exist()) {
                            userGroupRoleModel.insert();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    AllUserInfo allUserInfoByUpdate = new AllUserInfo();
                    allUserInfoByUpdate.setEafId(allUserInfo.getEafId());
                    allUserInfoByUpdate.setEafName(allUserInfo.getEafName());
                    allUserInfoByUpdate.setEafPhone(allUserInfo.getEafPhone());
                    allUserInfoByUpdate.setCwrIdnumType(allUserInfo.getCwrIdnumType());
                    allUserInfoByUpdate.setCwrIdnum(allUserInfo.getCwrIdnum());
                    allUserInfoByUpdate.setCwrIdphotoScan(allUserInfo.getCwrIdphotoScan());
                    allUserInfoByUpdate.setCwrPhoto(allUserInfo.getCwrPhoto());
                    allUserInfoByUpdate.setEafCreatetime(allUserInfo.getEafCreatetime());
                    allUserInfoByUpdate.setEafModifytime(allUserInfo.getEafModifytime());
                    allUserInfoByUpdate.setCwrIdaddr(allUserInfo.getCwrIdaddr());
                    allUserInfoByUpdate.setEafCreator(allUserInfo.getEafCreator());
                    allUserInfoByUpdate.setEafModifier(allUserInfo.getEafModifier());
                    allUserInfoByUpdate.setCwrStatus(allUserInfo.getCwrStatus());
                    allUserInfoByUpdate.setEafStatus(allUserInfo.getEafStatus());
                    allUserInfoByUpdate.setQrCode(allUserInfo.getQrCode());
                    allUserInfoByUpdate.setCreateBy(allUserInfo.getUserid());
                    if (StringUtils.isNotEmpty(allUserInfo.getCwrIdnum()) && allUserInfo.getCwrIdnum().length() == 18) {
                        logger.info("身份证号码=" + allUserInfo.getCwrIdnum());

                        allUserInfoByUpdate.setYear(Integer.parseInt(allUserInfo.getCwrIdnum().substring(6, 10)));
                        allUserInfoByUpdate.setMonth(Integer.parseInt(allUserInfo.getCwrIdnum().substring(10, 12)));
                        allUserInfo.setYear(allUserInfoByUpdate.getYear());
                        allUserInfo.setMonth(allUserInfoByUpdate.getMonth());
                        int sex = Integer.parseInt(allUserInfo.getCwrIdnum().substring(16, 17));
                        if (sex % 2 == 0) {
                            allUserInfoByUpdate.setSex(2);
                            allUserInfo.setSex(2);
                        } else {
                            allUserInfoByUpdate.setSex(1);
                            allUserInfo.setSex(1);
                        }
                    }

                    if (allUserInfoByDb == null) {
                        allUserInfo.insert();
                    } else {
                        allUserInfoByUpdate.where("[eafId]=#{eafId}").update("[eafName]=#{eafName},[eafPhone]=#{eafPhone},[cwrIdnumType]=#{cwrIdnumType}," +
                                "[cwrIdnum]=#{cwrIdnum},[id_card_front]=#{cwrIdphotoScan},[cwrPhoto]=#{cwrPhoto}," +
                                "[eafCreatetime]=#{eafCreatetime},[eafModifytime]=#{eafModifytime},[cwrIdaddr]=#{cwrIdaddr}," +
                                "[eafCreator]=#{eafCreator},[eafModifier]=#{eafModifier},[cwrStatus]=#{cwrStatus}," +
                                "[eafStatus]=#{eafStatus}, [createBy]=#{createBy}");
                    }
                }

                PageNum pageNum = new PageNum();
                pageNum.setPageName("user");

                //递归
                if (userPageNum <= intTotal + 1) {
                    pageNum.setPageIndex(userPageNum);
                    pageNum.where("[page_name]=#{pageName}").update("[page_index]=#{pageIndex}");
                    allUser(userPageNum, eafId, md5PublicKey, aesPublicKey, qrCodePath, width, height, server, roleId);
                }

                //清空页码数据
                pageNum.setPageIndex(1);
                pageNum.where("[page_name]=#{pageName}").update("[page_index]=#{pageIndex}");
                logger.info("一个项目下的人员执行完成");

            } else {
                logger.error("接口返回数据错误");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}