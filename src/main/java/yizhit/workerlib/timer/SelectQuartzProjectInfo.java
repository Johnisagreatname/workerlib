package yizhit.workerlib.timer;

import ccait.ccweb.model.RoleModel;
import ccait.ccweb.utils.FastJsonUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.query.Datetime;
import entity.tool.util.RequestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import yizhit.workerlib.entites.Group;
import yizhit.workerlib.entites.Privilege;
import yizhit.workerlib.entites.ProjectInfo;
import yizhit.workerlib.entites.TimerProfile;
import yizhit.workerlib.interfaceuilt.FinalUtil;
import yizhit.workerlib.interfaceuilt.SHA256;

import java.util.*;

@DisallowConcurrentExecution
public class SelectQuartzProjectInfo {

    private static final Logger log = LogManager.getLogger(SelectQuartzProjectInfo.class);

    public  void batchInsertProjectInfo(){
        // 数据库数据
        System.out.println("查询所有工程工作正在进入处理...");
        JSONObject params = new JSONObject();
        JSONArray array = null;
        int pageIndex = 0;
        try {
            StringBuilder sb = new StringBuilder();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idnum","445122199309195215");
            TimerProfile timerProfile = new TimerProfile();
            timerProfile.setKey("project");
            TimerProfile currentTimerProfile = timerProfile.where("[key]=#{key}").first();
            if(currentTimerProfile!=null) {
                pageIndex = currentTimerProfile.getValue();
            }else{
                timerProfile.setValue(1);
                Integer i = timerProfile.insert();
                pageIndex = 1;
            }
            //拼接校验码
            jsonObject.put("pageNum", pageIndex);
            String formatDate = Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            sb.append("appid=appid1").append("&data="+jsonObject.toJSONString()).append("&format=json").append("&method=project.info").append("&nonce=123456").append("&timestamp=").append(formatDate).append("&version=1.0").append("&appsecret=123456");
            String hex = sb.toString().toLowerCase();
            String s = SHA256.getSHA256StrJava(hex);
            System.out.println("cd:"+s);

            params.put("method","project.info");
            params.put("format","json");
            params.put("version","1.0");
            params.put("appid","appid1");
            params.put("timestamp",formatDate);
            params.put("nonce","123456");
            params.put("sign",s);
            params.put("data",jsonObject.toJSONString());
            String str = params.toJSONString();
            System.out.println("params:  " + str);
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Content-Type", "application/json");
            String result = RequestUtils.post(FinalUtil.url, str, header );
            JSONObject json = JSONObject.parseObject(result);

            List<ProjectInfo> list = new ArrayList<ProjectInfo>();
            // 数据获取正确
            if(json.containsKey("code") && json.get("code").equals("0")){
                array = json.getJSONObject("data").getJSONArray("list");
                list = (List<ProjectInfo>) FastJsonUtils.toList(array.toJSONString(), ProjectInfo.class);
                if (list.size() == 500){
                    pageIndex++;
                }
                for(ProjectInfo item : list) {

                    ProjectInfo js = null;
                    ProjectInfo projectInfo = null;
                    try {
                        //获取Group表的id
                        String groupId = UUID.randomUUID().toString().replace("-", "");
                        Group group = new Group();
                        group.setGroupId(groupId);
                        group.setUserPath("0/1");
                        group.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        group.setGroupName(item.getCwrPrjName());

                        //查找管理员id
                        RoleModel roleModel= new RoleModel();
                        roleModel.setRoleName("管理员");
                        RoleModel roleId = roleModel.where("[roleName]=#{roleName}").first();

                        //获取Privilege表的id
                        String privilegeId = UUID.randomUUID().toString().replace("-", "");
                        Privilege privilege = new Privilege();
                        privilege.setPrivilegeId(privilegeId);
                        privilege.setGroupId(groupId);
                        privilege.setRoleId(roleId.getRoleId());
                        privilege.setCanAdd(1);
                        privilege.setCanDelete(1);
                        privilege.setCanUpdate(1);
                        privilege.setCanView(1);
                        privilege.setCanDownload(1);
                        privilege.setCanPreviewDoc(1);
                        privilege.setCanUpload(1);
                        privilege.setCanExport(1);
                        privilege.setCanImport(1);
                        privilege.setCanDecrypt(1);
                        privilege.setCanList(1);
                        privilege.setCanQuery(1);
                        privilege.setScope(4);
                        privilege.setUserPath("0/1");
                        privilege.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

                        projectInfo = new ProjectInfo();
                        projectInfo.setEafId(item.getEafId());
                        //查找是否有相同的公司
                        js = projectInfo.where("[project_id]=#{eafId}").first();
                        if (js == null) {
                            Integer i = item.insert();
                            group.insert();
                            privilege.insert();
                        } else {
                            projectInfo.setEafModifytime(item.getEafModifytime());
                            projectInfo.setEafCreatetime(item.getEafCreatetime());
                            projectInfo.setCwrPrjAddr(item.getCwrPrjAddr());
                            projectInfo.setCwrPrjName(item.getCwrPrjName());
                            projectInfo.setCwrPrjJian(item.getCwrPrjJian());
                            projectInfo.setCwrPrjStatus(item.getCwrPrjStatus());
                            projectInfo.setCwrPrjType(item.getCwrPrjType());
                            projectInfo.setCwrPrjCode(item.getCwrPrjCode());
                            projectInfo.setCwrEndDate(item.getCwrEndDate());
                            projectInfo.setCwrStartDate(item.getCwrStartDate());
                            projectInfo.setCwrJsUnit(item.getCwrJsUnit());
                            projectInfo.setCwrSgUnit(item.getCwrSgUnit());
                            projectInfo.setCwrControlUnit(item.getCwrControlUnit());
                            projectInfo.where("[project_id]=#{eafId}").update("[modifyOn]=#{eafModifytime},[createOn]=#{eafCreatetime},[project_address]=#{cwrPrjAddr}," +
                                    "[project_name]=#{cwrPrjName},[project_brief]=#{cwrPrjJian},[status]=#{cwrPrjStatus}," +
                                    "[cwrPrjType]=#{cwrPrjType},[cwrPrjCode]=#{cwrPrjCode},[end_time]=#{cwrEndDate}," +
                                    "[start_time]=#{cwrStartDate},[construction]=#{cwrJsUnit},[organization]=#{cwrSgUnit}," +
                                    "[supervising]=#{cwrControlUnit}");
                            group.where("[groupName]=#{groupName}").update("[groupName]=#{groupName}");
                        }
                    }
                    catch (Exception e) {
                        if (js == null) {
                            log.error("fail to insert: =============================================================>");
                            log.error(FastJsonUtils.convertObjectToJSON(item));
                        }
                        else {
                            log.error("fail to update: ------------------------------------------------------------>");
                            log.error(FastJsonUtils.convertObjectToJSON(projectInfo));
                        }

                        log.error("--------------------------------------------------------------------------------");
                        log.error(e);
                    }
                }
                System.out.println("数据插入完成!");
                timerProfile.setValue(pageIndex);
                timerProfile.where("[key]=#{key}").update("[value]=#{value}");
            }
            else {
                System.out.println("error:  " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
