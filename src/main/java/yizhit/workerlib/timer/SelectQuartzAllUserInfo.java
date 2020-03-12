package yizhit.workerlib.timer;

import ccait.ccweb.utils.EncryptionUtil;
import ccait.ccweb.utils.FastJsonUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.query.Datetime;
import entity.tool.util.RequestUtils;
import entity.tool.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Value;
import yizhit.workerlib.entites.*;
import yizhit.workerlib.interfaceuilt.FinalUtil;
import yizhit.workerlib.interfaceuilt.SHA256;
import yizhit.workerlib.trigger.AllUserTrigger;

import java.util.*;

@DisallowConcurrentExecution
public class SelectQuartzAllUserInfo {
    private static final Logger log = LogManager.getLogger(SelectQuartzAllUserInfo.class);

    @Value("${entity.security.encrypt.MD5.publicKey:ccait}")
    private String md5PublicKey;

    @Value("${entity.encoding:UTF-8}")
    private String encoding;

    @Value("${entity.upload.workerlib.people.code.path}")
    private String qrCodePath;     //图片地址

    @Value("${qrcode.width}")

    private int width;

    @Value("${qrcode.height}")
    private int height;

    @Value("${entity.security.encrypt.AES.publicKey:ccait}")
    private String aesPublicKey;

    @Value("${qrcode.server}")      //IP
    private String server ;

    @Value("${enableTasks:false}")
    private Boolean enableTasks;

    public void batchInsertArchivesInfo(){

        if(!enableTasks) {
            return;
        }

        // 数据库数据
        System.out.println("查询项目下人员工作正在进入处理...");
        JSONObject params = new JSONObject();
        JSONArray array = null;
        int pageIndex = 0;
        AllUserInfo js = null;
        try {
            //校验验码
            JSONObject jsonObject = new JSONObject();
            //查询工程ID
            ProjectInfo projectInfo = new ProjectInfo();
            List<ProjectInfo> projectInfoList = projectInfo.where("1=1").select(" project_id ").query(ProjectInfo.class);

            //查找工人id
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
                log.info("角色已存在");
            }

            TimerProfile timerProfile = new TimerProfile();
            timerProfile.setKey("alluser");
            List<TimerProfile> profiles = timerProfile.where("[key]=#{key}").query();

            List<AllUserInfo> allUserInfoListByInsert = new ArrayList<AllUserInfo>() ;

            for (ProjectInfo projectInfoitem:projectInfoList) {
                timerProfile.setPid(projectInfoitem.getEafId());
                Optional<TimerProfile> optionals = profiles.stream().filter(a -> a.getPid().equals(projectInfoitem.getEafId())).findFirst();
                TimerProfile currentTimerProfile = null;
                if(optionals.isPresent()) {
                    currentTimerProfile = optionals.get();
                }

                if(currentTimerProfile != null) {
                    pageIndex = currentTimerProfile.getValue();
                }else{
                    timerProfile.setValue(1);
                    Integer i = timerProfile.insert();
                    pageIndex = 1;
                }

                //拼接密文
                StringBuilder sb = new StringBuilder();
                jsonObject.put("prjid",projectInfoitem.getEafId());
                jsonObject.put("pageNum", pageIndex);
                String formatDate = Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                sb.append("appid=appid1").append("&data="+jsonObject.toJSONString()).append("&format=json").append("&method=user.info").append("&nonce=123456").append("&timestamp="+formatDate).append("&version=1.0").append("&appsecret=123456");
                String hex = sb.toString().toLowerCase();
                System.out.println(hex);
                String s = SHA256.getSHA256StrJava(hex);
                System.out.println("cd:"+s);
                System.out.println(formatDate);
                System.out.println(projectInfoitem.getEafId());
                //发送请求
                params.put("method","user.info");
                params.put("format","json");
                params.put("version","1.0");
                params.put("appid","appid1");
                params.put("timestamp",formatDate);
                params.put("nonce","123456");
                params.put("sign",s);
                params.put("data",jsonObject.toJSONString());
                String str = params.toJSONString();
                log.info("params:  " + str);
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");
                String result = RequestUtils.post(FinalUtil.url, str, header );
                JSONObject json = JSONObject.parseObject(result);

                List<AllUserInfo> allUserInfoList = new ArrayList<AllUserInfo>() ;
                // 数据获取正确
                if(json.containsKey("code") && json.get("code").equals("0")){
                    array = json.getJSONObject("data").getJSONArray("list");
                    String text = array.toJSONString();
                    allUserInfoList =  FastJsonUtils.toList(text, AllUserInfo.class);
                    if (allUserInfoList.size() == 500){
                        pageIndex++;
                    }
                    for(AllUserInfo info:allUserInfoList){
                        allUserInfoListByInsert.add(info);
                    }

                }else {
                    log.error("获取所有工程id失败=============================================================》  " + result);
                }
            }

            for(AllUserInfo info : allUserInfoListByInsert) {
                try {
                    //给user表插入所有人员信息
                    UserModel userModel = new UserModel();
                    userModel.setUsername(info.getCwrIdnum());
                    String passWord = "";
                    if (StringUtils.isEmpty(info.getCwrIdnum()) || info.getCwrIdnum().length() < 6) {
                        passWord = "123456";
                    } else {
                        passWord = info.getCwrIdnum().substring(info.getCwrIdnum().length() - 6);
                    }
                    userModel.setPassword(EncryptionUtil.md5(passWord, md5PublicKey, encoding));
                    userModel.setPath("0/1");
                    userModel.setCreateBy(Integer.valueOf(1));
                    userModel.setCreateOn(new Date());
                    js = new AllUserInfo().where(String.format("[eafId]='%s'", info.getEafId())).first();

                    UserModel currentUser = userModel.where("[username]=#{username}").first();
                    if (currentUser == null) {
                        info = AllUserTrigger.genQrCode(FastJsonUtils.convert(info, Map.class), userModel,md5PublicKey, aesPublicKey, qrCodePath, encoding, width, height,server,false);
                    }

                    else {
                        if(js == null) {
                            AllUserInfo temp = AllUserTrigger.genQrCode(FastJsonUtils.convert(info, Map.class), userModel, md5PublicKey, aesPublicKey, qrCodePath, encoding, width, height, server, false);
                            info.setQrCode(temp.getQrCode());
                        }
                        info.setUserid(currentUser.getId());
                        info.setCreateBy(currentUser.getId());
                    }

                    //给UserGroupRole表插入所有人员信息
                    UserGroupRoleModel userGroupRoleModel = new UserGroupRoleModel();
                    String userGroupRoleId = UUID.randomUUID().toString().replace("-", "");
                    userGroupRoleModel.setUserGroupRoleId(userGroupRoleId);
                    userGroupRoleModel.setRoleId(roleId.getRoleId());
                    userGroupRoleModel.setPath("0/1");
                    userGroupRoleModel.setCreateOn(new Date());
                    userGroupRoleModel.setCreateBy(info.getCreateBy());
                    userGroupRoleModel.setUserId(info.getUserid());
                    if("工人".equals(roleId.getRoleName()) && !userGroupRoleModel.where("[userId]=#{userId}")
                            .and("[roleId]=#{roleId}").exist()) {
                        userGroupRoleModel.insert();
                    }
                }
                catch (Exception ex) {
                    log.error("fail to set user/group/role: =============================================================>");
                    log.error("插入所有人员信息出错： =============================================================>",ex);
                    log.error(new Date());
                }

                AllUserInfo allUserInfoByUpdate = null;
                try {
                    allUserInfoByUpdate = new AllUserInfo();
                    allUserInfoByUpdate.setEafId(info.getEafId());
                    allUserInfoByUpdate.setEafName(info.getEafName());
                    allUserInfoByUpdate.setEafPhone(info.getEafPhone());
                    allUserInfoByUpdate.setCwrIdnumType(info.getCwrIdnumType());
                    allUserInfoByUpdate.setCwrIdnum(info.getCwrIdnum());
                    allUserInfoByUpdate.setCwrIdphotoScan(info.getCwrIdphotoScan());
                    allUserInfoByUpdate.setCwrPhoto(info.getCwrPhoto());
                    allUserInfoByUpdate.setEafCreatetime(info.getEafCreatetime());
                    allUserInfoByUpdate.setEafModifytime(info.getEafModifytime());
                    allUserInfoByUpdate.setCwrIdaddr(info.getCwrIdaddr());
                    allUserInfoByUpdate.setEafCreator(info.getEafCreator());
                    allUserInfoByUpdate.setEafModifier(info.getEafModifier());
                    allUserInfoByUpdate.setCwrStatus(info.getCwrStatus());
                    allUserInfoByUpdate.setEafStatus(info.getEafStatus());
                    allUserInfoByUpdate.setQrCode(info.getQrCode());
                    allUserInfoByUpdate.setCreateBy(info.getUserid());
                    if(StringUtils.isNotEmpty(info.getCwrIdnum())) {
                        allUserInfoByUpdate.setYear(Integer.parseInt(info.getCwrIdnum().substring(6, 10)));
                        allUserInfoByUpdate.setMonth(Integer.parseInt(info.getCwrIdnum().substring(10,12)));
                        info.setYear(allUserInfoByUpdate.getYear());
                        info.setMonth(allUserInfoByUpdate.getMonth());
                        int Sex = Integer.parseInt(info.getCwrIdnum().substring(16,17));
                        if (Sex % 2 == 0){
                            allUserInfoByUpdate.setSex(2);
                            info.setSex(2);
                        }else {
                            allUserInfoByUpdate.setSex(1);
                            info.setSex(1);
                        }
                    }

                    Integer id = null;
                    if (js == null){
                        id = info.insert();
                    }else {
                        allUserInfoByUpdate.where("[eafId]=#{eafId}").update("[eafName]=#{eafName},[eafPhone]=#{eafPhone},[cwrIdnumType]=#{cwrIdnumType}," +
                                "[cwrIdnum]=#{cwrIdnum},[id_card_front]=#{cwrIdphotoScan},[cwrPhoto]=#{cwrPhoto}," +
                                "[eafCreatetime]=#{eafCreatetime},[eafModifytime]=#{eafModifytime},[cwrIdaddr]=#{cwrIdaddr}," +
                                "[eafCreator]=#{eafCreator},[eafModifier]=#{eafModifier},[cwrStatus]=#{cwrStatus}," +
                                "[eafStatus]=#{eafStatus}, [createBy]=#{createBy}");
                    }
                }catch (Exception ee){
                    if (js == null) {
                        log.error("项目下人员数据插入失败: =============================================================>");
                        log.error(FastJsonUtils.convertObjectToJSON(info));
                    }
                    else {
                        log.error("项目下人员数据更新失败: ------------------------------------------------------------>");
                        log.error(FastJsonUtils.convertObjectToJSON(allUserInfoByUpdate));
                    }
                    log.error("--------------------------------------------------------------------------------");
                    log.error(ee);
                }
                Runtime.getRuntime().gc();
                Thread.sleep(1000);
            }
            timerProfile.setValue(pageIndex);
            timerProfile.where("[key]=#{key}").and("[pid]=#{pid}").update("[value]=#{value}");
            System.out.println("数据插入完成!");
        } catch (Exception e) {
            log.error(e);
        }
    }

}
