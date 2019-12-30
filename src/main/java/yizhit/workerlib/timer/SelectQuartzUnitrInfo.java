package yizhit.workerlib.timer;

import ccait.ccweb.utils.FastJsonUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.query.Datetime;
import entity.tool.util.RequestUtils;
import org.quartz.DisallowConcurrentExecution;
import yizhit.workerlib.entites.TimerProfile;
import yizhit.workerlib.entites.UnitrInfo;
import yizhit.workerlib.interfaceuilt.FinalUtil;
import yizhit.workerlib.interfaceuilt.SHA256;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SelectQuartzUnitrInfo {
    public void batchInsertUnitrInfo(String uid){
        // 数据库数据
        System.out.println("查询公司表工作正在进入处理...");
        JSONObject params = new JSONObject();
        JSONArray array = null;
        int pageIndex = 0;
        try {
            StringBuilder sb = new StringBuilder();
            JSONObject jsonObject = new JSONObject();
//            TimerProfile timerProfile = new TimerProfile();
//            timerProfile.setKey("unit");
//            timerProfile = timerProfile.where("[key]=#{key}").first();
//            if(timerProfile!=null) {
//                pageIndex = timerProfile.getValue();
//            }
            //拼接密文
            jsonObject.put("comid", uid);
//            jsonObject.put("pageNum", pageIndex);
            String formatDate = Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            sb.append("appid=appid1").append("&data="+jsonObject.toJSONString()).append("&format=json").append("&method=company.info").append("&nonce=123456").append("&timestamp="+formatDate).append("&version=1.0").append("&appsecret=123456");
            String hex = sb.toString().toLowerCase();
            String s = SHA256.getSHA256StrJava(hex);
            System.out.println("cd:"+s);
            System.out.println(formatDate);

            //发送请求
            params.put("method","company.info");
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

            List<UnitrInfo> allUnitrInfoList = new ArrayList<UnitrInfo>();
            // 数据获取正确
            if(json.containsKey("code") && json.get("code").equals("0")){
                array = json.getJSONObject("data").getJSONArray("list");
                allUnitrInfoList = (List<UnitrInfo>) FastJsonUtils.toList(array.toJSONString(), UnitrInfo.class);
                if (allUnitrInfoList.size() == 500){
                    pageIndex++;
                }
                for(UnitrInfo item : allUnitrInfoList) {
                   try {
                       UnitrInfo unitrInfo = new UnitrInfo();
                       unitrInfo.setEafId(item.getEafId());
                       UnitrInfo js = unitrInfo.where("unit_id=#{eafId}").first();
                       if (js == null){
                           Integer i = item.insert();
                       }else {
                           unitrInfo.setCwrComName(item.getCwrComName());
                           unitrInfo.setCwrCode(item.getCwrCode());
                           unitrInfo.setCwrComCode(item.getCwrComCode());
                           unitrInfo.setCwrComFaren(item.getCwrComFaren());
                           unitrInfo.setCwrComAddr(item.getCwrComAddr());
                           unitrInfo.setCwrComStatus(item.getCwrComStatus());
                           unitrInfo.setEafCreator(item.getEafCreator());
                           unitrInfo.setEafCreatetime(item.getEafCreatetime());
                           unitrInfo.setEafModifier(item.getEafModifier());
                           unitrInfo.setCwrComType(item.getCwrComType());
                           unitrInfo.where("unit_id=#{eafId}").update("[unit_name]=#{cwrComName},[unit_number]=#{cwrCode},[project_license]=#{cwrComCode}," +
                                   "[principal]=#{cwrComFaren},[userPath]=#{cwrComAddr},[status]=#{cwrComStatus}," +
                                   "[createBy]=#{eafCreator},[createOn]=#{eafCreatetime},[modifyBy]=#{eafModifier}," +
                                   "[modifyOn]=#{eafModifytime},[cwrComType]=#{cwrComType}");
                       }
                   }catch (Exception e){e.printStackTrace();}
                }
                System.out.println("数据插入完成!");
//                timerProfile.setValue(pageIndex);
//                timerProfile.where("[key]=#{key}").update("[value]=#{value}");
            }
            else {
                System.out.println("error:  " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
