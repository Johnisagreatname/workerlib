package yizhit.workerlib.timer;

import ccait.ccweb.utils.FastJsonUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.query.Datetime;
import entity.tool.util.RequestUtils;
import entity.tool.util.StringUtils;
import entity.tool.util.ThreadUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Value;
import yizhit.workerlib.entites.*;
import yizhit.workerlib.interfaceuilt.FinalUtil;
import yizhit.workerlib.interfaceuilt.SHA256;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;


@DisallowConcurrentExecution
public class SelectQuartzArvhivesInfo {
    private static final Logger log = LogManager.getLogger(SelectQuartzArvhivesInfo.class);

    @Value("${enableTasks:false}")
    private Boolean enableTasks;


    public void batchInsertArvhivesInfo(){

        if(!enableTasks) {
            return;
        }

        // 数据库数据
        System.out.println("查询工程员工表工作正在进入处理...");
        SelectQuartzUnitrInfo selectQuartzUnitrInfo = new SelectQuartzUnitrInfo();
        JSONObject params = new JSONObject();
        JSONArray array = null;
        int pageIndex = 0;
        try {
            //校验验码
            JSONObject jsonObject = new JSONObject();
            ProjectInfo projectInfo = new ProjectInfo();
            //查询工程ID
            List<ProjectInfo> projectInfoList = projectInfo.where("1=1").select(" project_id ").query(ProjectInfo.class);
            //把当前表的分页用户全部查出来
            TimerProfile timerProfile = new TimerProfile();
            timerProfile.setKey("archives");
            List<TimerProfile> profiles = timerProfile.where("[key]=#{key}").query();

            List<ArchivesInfo> arvhivesInfoListByInsert = new ArrayList<ArchivesInfo>() ;

            for (ProjectInfo projectInfoitem:projectInfoList) {
               try{
                   timerProfile.setPid(projectInfoitem.getEafId());
                   Optional<TimerProfile> optionals = profiles.stream().filter(a -> a.getPid().equals(projectInfoitem.getEafId())).findFirst();
                   TimerProfile currentTimerProfile = null;
                   if (optionals.isPresent()) {
                       currentTimerProfile = optionals.get();
                   }

                   if (currentTimerProfile != null) {
                       pageIndex = currentTimerProfile.getValue();
                   } else {
                       timerProfile.setValue(1);
                       Integer i = timerProfile.insert();
                       pageIndex = 1;
                   }

                   //拼接密文
                   StringBuilder sb = new StringBuilder();
                   jsonObject.put("prjid", projectInfoitem.getEafId());
                   jsonObject.put("pageNum", pageIndex);
                   String formatDate = Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                   sb.append("appid=appid1").append("&data=" + jsonObject.toJSONString()).append("&format=json").append("&method=com2user.info").append("&nonce=123456").append("&timestamp=" + formatDate).append("&version=1.0").append("&appsecret=123456");
                   String hex = sb.toString().toLowerCase();
                   String s = SHA256.getSHA256StrJava(hex);
                   log.info("cd:" + s);
                   log.info(formatDate);
                   //发送请求
                   params.put("method", "com2user.info");
                   params.put("format", "json");
                   params.put("version", "1.0");
                   params.put("appid", "appid1");
                   params.put("timestamp", formatDate);
                   params.put("nonce", "123456");
                   params.put("sign", s);
                   params.put("data", jsonObject.toJSONString());
                   String str = params.toJSONString();
                   log.info("params:  " + str);
                   HashMap<String, String> header = new HashMap<String, String>();
                   header.put("Content-Type", "application/json");
                   String result = RequestUtils.post(FinalUtil.url, str, header);
                   JSONObject json = JSONObject.parseObject(result);

                   List<ArchivesInfo> archivesInfoList = new ArrayList<ArchivesInfo>();
                   // 数据获取正确
                   if (json.containsKey("code") && json.get("code").equals("0")) {
                       array = json.getJSONObject("data").getJSONArray("list");
                       String text = array.toJSONString();
                       archivesInfoList = FastJsonUtils.toList(text, ArchivesInfo.class);
                       if (archivesInfoList.size() == 500) {
                           pageIndex++;
                       }
                       for (ArchivesInfo info : archivesInfoList) {
                           arvhivesInfoListByInsert.add(info);
                       }
                   }else {
                       System.out.println("error:  " + result);
                   }
               }catch (Exception e1){
                   log.error("获取所有工程id失败：========================================================》",e1);
               }
            }

            for(ArchivesInfo info:arvhivesInfoListByInsert){
                ThreadUtils.async(new Runnable(){
                    @Override
                    public void run() {
                        importProjectWorkerHistory(info);
                    }
                });

                ThreadUtils.async(new Runnable(){
                    @Override
                    public void run() {
                        importProjectWorkerType(info);
                    }
                });

                ThreadUtils.async(new Runnable(){
                    @Override
                    public void run() {
                        importArchives(info, selectQuartzUnitrInfo);
                    }
                });
            }

            timerProfile.setValue(pageIndex);
            timerProfile.where("[key]=#{key}").and("[pid]=#{pid}").update("[value]=#{value}");
            System.out.println("数据插入完成!");
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void importArchives(ArchivesInfo info, SelectQuartzUnitrInfo selectQuartzUnitrInfo) {
        try {
            if(info.getEafName() != null && !Pattern.compile("[0-9]*").matcher(info.getEafName()).matches()){
                info.setLeave(2);
                if("进行中".equals(info.getCwrUserStatus())) {
                    info.setLeave(1);
                }
                if(StringUtils.isEmpty(info.getCwrComid())) {
                    selectQuartzUnitrInfo.batchInsertUnitrInfo(info.getCwrComid());
                }
                ArchivesInfo archivesInfo = new ArchivesInfo();
                archivesInfo.setUserid(info.getUserid());
                archivesInfo.setCwrPrjid(info.getCwrPrjid());
                archivesInfo.setEafId(info.getEafId());
                archivesInfo.setCwrUserStatus(info.getCwrUserStatus());
                archivesInfo.setCwrComid(info.getCwrComid());
                archivesInfo.setEafName(info.getEafName());
                archivesInfo.setEafPhone(info.getEafPhone());
                archivesInfo.setCwrIdnumType(info.getCwrIdnumType());
                archivesInfo.setCwrIdnum(info.getCwrIdnum());
                archivesInfo.setCwrWorkClass(info.getCwrWorkClass());
                archivesInfo.setCwrWorkName(info.getCwrWorkName());
                archivesInfo.setEafCreatetime(info.getEafCreatetime());
                archivesInfo.setEafModifier(info.getEafModifier());
                archivesInfo.setEafCreator(info.getEafCreator());
                archivesInfo.setEafRLeftid(info.getEafRLeftid());
                archivesInfo.setLeave(info.getLeave());
                archivesInfo.setCwrWorkclassId(info.getCwrWorkclassId());
                archivesInfo.setCwrWorktype(info.getCwrWorktype());
                archivesInfo.setCwrUserIn(info.getCwrUserIn());
                archivesInfo.setCwrUserOut(info.getCwrUserOut());
                ArchivesInfo js = archivesInfo.where("[archives_id]=#{userid}").and("[project_id]=#{cwrPrjid}").first();

                UserModel account = new UserModel();
                account.setUsername(info.getCwrIdnum());
                account = account.where("username=#{username}").first();
                if(account != null) {
                    archivesInfo.setCreateBy(account.getId());
                }

                if (js == null ){
                    Integer i = info.insert();
                }else if ("结束".equals(info.getCwrUserStatus())){
                    archivesInfo.setLeave(2);
                    archivesInfo.where("[archives_id]=#{userid}").and("[project_id]=#{cwrPrjid}").update("[cwrUserStatus]=#{cwrUserStatus},[eafId]=#{eafId},[unit_id]=#{cwrComid}," +
                            "[name]=#{eafName},[phone]=#{eafPhone},[cwrIdnumType]=#{cwrIdnumType}," +
                            "[id_number]=#{cwrIdnum},[CwrWorkClass]=#{CwrWorkClass},[work_type]=#{CwrWorkName}," +
                            "[createOn]=#{eafCreatetime},[modifyBy]=#{createBy},[modifyOn]=#{eafModifytime}," +
                            "[createBy]=#{createBy},[eafRLeftid]=#{eafRLeftid},[cwrWorkclassId]=#{cwrWorkclassId}," +
                            "[cwrWorktype]=#{cwrWorktype},[cwrUserIn]=#{cwrUserIn},[cwrUserOut]=#{cwrUserOut}," +
                            "[leave]=#{leave}");
                }else{
                    //不更新状态
                    archivesInfo.where("[archives_id]=#{userid}").and("[project_id]=#{cwrPrjid}").update("[cwrUserOut]=#{cwrUserOut},[eafId]=#{eafId},[unit_id]=#{cwrComid}," +
                            "[name]=#{eafName},[phone]=#{eafPhone},[cwrIdnumType]=#{cwrIdnumType}," +
                            "[id_number]=#{cwrIdnum},[CwrWorkClass]=#{CwrWorkClass},[work_type]=#{CwrWorkName}," +
                            "[createOn]=#{eafCreatetime},[modifyBy]=#{createBy},[modifyOn]=#{eafModifytime}," +
                            "[createBy]=#{createBy},[eafRLeftid]=#{eafRLeftid},[cwrWorkclassId]=#{cwrWorkclassId}," +
                            "[cwrWorktype]=#{cwrWorktype},[cwrUserIn]=#{cwrUserIn}");
                }
            }
        }catch (Exception e5){
            log.error("插入项目下的人员信息出错： =============================================================>",e5);
            log.error(new Date());
            log.error(e5);
        }
    }

    private void importProjectWorkerType(ArchivesInfo info) {
        try {

            //给所有人员表插入单位ID
            AllUserInfoUpdate allUserInfoUpdate = new AllUserInfoUpdate();
            allUserInfoUpdate.setCwrIdnum(info.getCwrIdnum());
            allUserInfoUpdate.setUnitId(info.getCwrComid());
            allUserInfoUpdate.where("[cwrIdnum]=#{cwrIdnum}").update("[unit_id]=#{unitId}");

            if(StringUtils.isEmpty(info.getCwrWorkName())) {
                return;
            }

            //给所有工种表导入工种信息
            ProjectWorkType projectWorkType = new ProjectWorkType();
            projectWorkType.setEafId(info.getUserid());
            projectWorkType.setProjectId(info.getCwrPrjid());
            projectWorkType.setWorkType(info.getCwrWorkName());
            projectWorkType.setCreateBy("1");
            projectWorkType.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            projectWorkType.setUserPath("0/1");
            ProjectWorkType jsEafid = projectWorkType.where("[eafId] = #{eafId}")
                    .and("[projectId]=#{projectId}").and("[WorkType]=#{workType}").first();
            if (jsEafid == null){
                projectWorkType.insert();
            }

            //给工种表导入工种信息
            WorkType workType = new WorkType();
            workType.setEafId(info.getUserid());
            workType.setWorkType(info.getCwrWorkName());
            workType.setCreateBy("1");
            workType.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            WorkType jstype_id  = workType.where("[eafId] = #{eafId}").and("[WorkType]=#{workType}").first();
            if (jstype_id == null) {
                workType.insert();
            }
        }catch(Exception e3){
            log.error(e3);
        }
    }

    private void importProjectWorkerHistory(ArchivesInfo info) {
        try{
            //给历史记录表
            InvoLvedproject invoLvedproject = new InvoLvedproject();
            invoLvedproject.setArchivesId(info.getUserid());
            invoLvedproject.setProjectId(info.getCwrPrjid());
            invoLvedproject.setUnitId(info.getCwrComid());
            invoLvedproject.setStartTime(info.getCwrUserIn());
            invoLvedproject.setEnd_time(info.getCwrUserOut());
            invoLvedproject.setCreateBy(1);
            invoLvedproject.setCreateOn(Datetime.format(Datetime.now(), "yyyy-MM-dd HH:mm:ss"));
            InvoLvedproject js_id  = invoLvedproject.where("[archives_id] = #{archives_id}").and("[project_id] = #{project_id}").first();
            if (js_id == null){
                invoLvedproject.insert();
            }else{
                invoLvedproject.where("[archives_id] = #{archives_id}").and("[project_id] = #{project_id}").update("[unit_id] = #{unit_id},[start_time] = #{start_time},[end_time] = #{end_time}," +
                        "[createOn] = #{createOn},[createBy] = #{createBy}");
            }
        }

        catch(Exception e) {
            log.error(e);
        }
    }


    /**
     * 给工程人员表插入头像
     */
    public void updateArchivesPhoto(){

        if(!enableTasks) {
            return;
        }

        AllUserInfo info = new AllUserInfo();
        try {
            log.info("正在执行更新工人头像： =============================================================>");
            List<AllUserInfo> infoList = info.select("cwrIdnum,cwrPhoto").query();
            for (AllUserInfo userInfo:infoList) {
                ArchivesInfoUpdate archivesInfo = new ArchivesInfoUpdate();
                archivesInfo.setCwrIdnum(userInfo.getCwrIdnum());
                archivesInfo.setPhoto(userInfo.getCwrPhoto());
                archivesInfo.where("id_number=#{cwrIdnum}").update("photo=#{photo}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("更新工人头像出错： =============================================================>",e);
        }
    }

}
