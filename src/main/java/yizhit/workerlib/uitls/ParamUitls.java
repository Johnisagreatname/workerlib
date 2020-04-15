package yizhit.workerlib.uitls;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yizhit.workerlib.interfaceuilt.SHA256;
import yizhit.workerlib.timer.InsertArchives;

/**
 *@ClassName paramUitls
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/15  15:37
 */
public class ParamUitls {

    private static final Logger logger = LogManager.getLogger(InsertArchives.class);

    /**查询所有数据*/
    public static final String com2user = "com2user.info";
    /**查询项目数据*/
    public static final String project = "project.info";

    /**
     * @Author xieya
     * @Description 组装入参
     * @Date 2020/4/14 16:52
     * @param
     * @return java.lang.String
     **/
    public static String setParam(int pageIndex, String method) {
        //拼接密文
        StringBuilder sb = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("prjid", projectInfoitem.getEafId());
        jsonObject.put("pageNum", pageIndex);
        sb.append("appid=appid1").append("&data=" + jsonObject.toJSONString()).append("&format=json").append("&method=" + method).append("&nonce=123456").append("&timestamp=" + DateUtils.getCurrentDate()).append("&version=1.0").append("&appsecret=123456");
        String hex = sb.toString().toLowerCase();
        String sign = SHA256.getSHA256StrJava(hex);
        logger.info("签名sign:" + sign);
        JSONObject params = new JSONObject();
        params.put("method", method);
        params.put("format", "json");
        params.put("version", "1.0");
        params.put("appid", "appid1");
        params.put("timestamp", DateUtils.getCurrentDate());
        params.put("nonce", "123456");
        params.put("sign", sign);
        params.put("data", jsonObject.toJSONString());
        String str = params.toJSONString();
        return str;
    }
}