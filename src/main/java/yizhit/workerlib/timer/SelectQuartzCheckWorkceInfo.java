package yizhit.workerlib.timer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.query.Datetime;
import entity.tool.util.FastJsonUtils;
import entity.tool.util.RequestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Value;
import yizhit.workerlib.entites.AllUserInfo;
import yizhit.workerlib.entites.CheckWorkceInfo;
import yizhit.workerlib.entites.ProjectInfo;
import yizhit.workerlib.entites.TimerProfile;
import yizhit.workerlib.interfaceuilt.FinalUtil;
import yizhit.workerlib.interfaceuilt.SHA256;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@DisallowConcurrentExecution
public class SelectQuartzCheckWorkceInfo {

    private static final Logger log = LogManager.getLogger(SelectQuartzCheckWorkceInfo.class);

    @Value("${enableTasks:false}")
    private Boolean enableTasks;


    public void batchInsertCheckWorkceInfo() {

        if(enableTasks!=null && !enableTasks) {
            return;
        }

        // 数据库数据
        System.out.println("查询考勤表数据正在进入处理...");
        JSONObject params = new JSONObject();
        JSONArray array = null;
        int pageIndex = 0;
        try {
            //拼接校验码
            JSONObject jsonObject = new JSONObject();
            ProjectInfo projectInfo = new ProjectInfo();
            //查询工程ID
            List<ProjectInfo> projectInfoList = projectInfo.where("1=1").select(" project_id ").query(ProjectInfo.class);
            for (ProjectInfo projectInfoitem : projectInfoList) {
                TimerProfile timerProfile = new TimerProfile();
                timerProfile.setKey("checkworkce");
                timerProfile.setPid(projectInfoitem.getEafId());
                TimerProfile currentTimerProfile = timerProfile.where("[key]=#{key}").and("[pid]=#{pid}").first();
                if(currentTimerProfile!=null) {
                    pageIndex = currentTimerProfile.getValue();
                }else{
                    timerProfile.setValue(1);
                    Integer i = timerProfile.insert();
                    pageIndex = 1;
                }
                //拼接密文
                StringBuilder sb = new StringBuilder();
                jsonObject.put("prjid", projectInfoitem.getEafId());
                String formatDate = Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                sb.append("appid=appid1").append("&data=" + jsonObject.toJSONString()).append("&format=json").append("&method=project.atte.list").append("&nonce=123456").append("&timestamp=" + formatDate).append("&version=1.0").append("&appsecret=123456");
                String hex = sb.toString().toLowerCase();
                log.info(hex);
                String sign = SHA256.getSHA256StrJava(hex);

                //发送请求
                params.put("method", "project.atte.list");
                params.put("format", "json");
                params.put("version", "1.0");
                params.put("appid", "appid1");
                params.put("timestamp", formatDate);
                params.put("nonce", "123456");
                params.put("sign", sign);
                params.put("data", jsonObject.toJSONString());
                String str = params.toJSONString();
                log.info("params:  " + str);
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");
                String result = RequestUtils.post(FinalUtil.url, str, header);
                JSONObject json = JSONObject.parseObject(result);

                // 数据获取正确
                if (json.containsKey("code") && json.get("code").equals("0")) {
                    array = json.getJSONObject("data").getJSONArray("list");
                    String text = array.toJSONString();
                    List<CheckWorkceInfo> checkWorkceInfosList = FastJsonUtils.toList(text,   CheckWorkceInfo.class);
                    if (checkWorkceInfosList.size() == 500){
                        pageIndex++;
                    }
                    for (CheckWorkceInfo info : checkWorkceInfosList) {
                        try {
                            CheckWorkceInfo checkWorkceInfo = new CheckWorkceInfo();
                            checkWorkceInfo.setCheckworkceId(info.getCheckworkceId());
                            checkWorkceInfo.setCwrPrjid(info.getCwrPrjid());
                            CheckWorkceInfo js = checkWorkceInfo.where("[checkworkce_id]=#{checkworkceId}").and("[cwrPrjid]=#{cwrPrjid}").first();
                            if (js == null){
                                info.insert();
                            }else {
                                checkWorkceInfo.setEafCreator(info.getEafCreator());
                                checkWorkceInfo.setEafModifytime(info.getEafModifytime());
                                checkWorkceInfo.setEafCreatetime(info.getEafCreatetime());
                                checkWorkceInfo.setEafModifier(info.getEafModifier());
                                checkWorkceInfo.setCwrComid(info.getCwrComid());
                                checkWorkceInfo.setCwrGrpid(info.getCwrGrpid());
                                checkWorkceInfo.setCwrUserid(info.getCwrUserid());
                                checkWorkceInfo.setCwrEquid(info.getCwrEquid());
                                checkWorkceInfo.setCwrPasstime(info.getCwrPasstime());
                                checkWorkceInfo.setCwrDirection(info.getCwrDirection());
                                checkWorkceInfo.setCwrPassmode(info.getCwrPassmode());
                                checkWorkceInfo.setCwrPasspho(info.getCwrPasspho());
                                checkWorkceInfo.setCwrPsntype(info.getCwrPsntype());
                                checkWorkceInfo.setCwrPassdate(info.getCwrPassdate());
                                checkWorkceInfo.setToTicwrStat(info.getToTicwrStat());
                                checkWorkceInfo.setCwrLongitude(info.getCwrLongitude());
                                checkWorkceInfo.setCwrLatitude(info.getCwrLatitude());
                                checkWorkceInfo.setCwrAddress(info.getCwrAddress());
                                checkWorkceInfo.setCwrUsertype(info.getCwrUsertype());
                                checkWorkceInfo.setCwrIdnum(info.getCwrIdnum());
                                checkWorkceInfo.setCwrProcessStatus(info.getCwrProcessStatus());
                                checkWorkceInfo
                                        .where("[checkworkceId]=#{checkworkce_id}")
                                        .and("[cwrPrjid]=#{cwrPrjid}").update("[eafCreator]=#{eafCreator},[eafModifytime]=#{eafModifytime},[eafCreatetime]=#{eafCreatetime}," +
                                                                                                                             "[eafModifier]=#{eafModifier},[cwrPrjid]=#{cwrPrjid},[cwrComid]=#{cwrComid}," +
                                                                                                                             "[cwrGrpid]=#{cwrGrpid},[cwrUserid]=#{cwrUserid},[cwrEquid]=#{cwrEquid}," +
                                                                                                                             "[cwrPasstime]=#{cwrPasstime},[cwrDirection]=#{cwrDirection},[cwrPassmode]=#{cwrPassmode}," +
                                                                                                                             "[cwrPasspho]=#{cwrPasspho},[cwrPsntype]=#{cwrPsntype},[cwrPassdate]=#{cwrPassdate}," +
                                                                                                                             "[toTicwrStat]=#{toTicwrStat},[cwrLongitude]=#{cwrLongitude},[cwrLatitude]=#{cwrLatitude}," +
                                                                                                                             "[cwrAddress]=#{cwrAddress},[cwrUsertype]=#{cwrUsertype},[cwrIdnum]=#{cwrIdnum}," +
                                                                                                                             "[cwrProcessStatus]=#{cwrProcessStatus}");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    log.info("数据插入完成!");
                    timerProfile.setValue(pageIndex);
                    timerProfile.where("[key]=#{key}").and("[pid]=#{pid}").update("[value]=#{value}");
                } else {
                    log.error("error:  " + result);
                }
                Runtime.getRuntime().gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
