package yizhit.workerlib.trigger;

import ccait.ccweb.annotation.OnInsert;
import ccait.ccweb.annotation.Trigger;
import ccait.ccweb.filter.CCWebRequestWrapper;
import ccait.ccweb.utils.EncryptionUtil;
import ccait.ccweb.utils.UploadUtils;
import entity.tool.util.FastJsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import yizhit.workerlib.entites.AllUserInfo;
import yizhit.workerlib.entites.UserGroupRoleModel;
import yizhit.workerlib.entites.UserModel;
import yizhit.workerlib.interfaceuilt.QRCodeUtil;
import yizhit.workerlib.timer.SelectQuartzAllUserInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Scope("prototype")
@Trigger(tablename = "alluser") //触发器注解
@Order(value = 100)
public class AllUserTrigger {

    private static final Logger log = LogManager.getLogger(AllUserTrigger.class);

    @Value("${entity.upload.workerlib.alluser.qr_code.path}")
    private String qrCodePath;     //图片地址

    @Value("${qrcode.width}")
    private int width;

    @Value("${qrcode.height}")
    private int height;

    @Value("${entity.security.encrypt.AES.publicKey:ccait}")
    private String md5PublicKey;

    @Value("${entity.security.encrypt.AES.publicKey:ccait}")
    private String aesPublicKey;

    @Value("${entity.encoding:UTF-8}")
    private String encoding;

    @Value("${qrcode.server}")      //IP
    private String qrcodeServer;

    /***
     * 新增数据事件
     * @param list （提交的数据）
     * @param request （当前请求）
     * @throws Exception
     */
    @OnInsert
    public void onInsert(List<Map<String, Object>> list, HttpServletRequest request) throws Exception {
        try {
            for (Map item : list) {
                genQrCode(item, null, md5PublicKey, aesPublicKey, qrCodePath, encoding, width, height, qrcodeServer, true);
            }
            CCWebRequestWrapper wrapper = (CCWebRequestWrapper) request;
            wrapper.setPostParameter(list);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }



    public static AllUserInfo genQrCode(Map item, UserModel userModel, String md5PublicKey, String aesPublicKey, String qrCodePath, String encoding, int width, int height, String server, boolean formTrigger) throws NoSuchAlgorithmException, java.sql.SQLException, IOException {

        try {
            if(userModel == null) {
                userModel = new UserModel();
            }

            String Idnum = (String) item.get("cwrIdnum");
            userModel.setUsername(Idnum);
            UserModel user = userModel.where("[username]=#{username}").first();
            if(user != null){
                item.put("status",false);
                return FastJsonUtils.convert(item, AllUserInfo.class);
            }
            if (Idnum.length() < 6) {
                userModel.setPassword(EncryptionUtil.md5("123456", md5PublicKey, encoding));
            } else {
                userModel.setPassword(EncryptionUtil.md5(Idnum.substring(Idnum.length() - 6), md5PublicKey, encoding));
            }
            if(item.get("createBy") == null) {
                userModel.setCreateBy(Integer.valueOf(1));
            }
            else {
                userModel.setCreateBy((Integer) item.get("createBy"));
            }

            userModel.setCreateOn(new Date());

            if(item.get("userPath") != null) {
                userModel.setPath((String)item.get("userPath"));
            }else {
                userModel.setPath("0/1");
            }
            Integer userid = userModel.getId()==null ? 0 : userModel.getId().intValue();

            if(userid < 1) {
                userid = userModel.insert();
                UserModel userModel1 = new UserModel();
                userModel1.setUsername(Idnum);
                UserModel userModel2 = userModel1.where("[username]=#{username}").first();
                userModel2.where("[id]=#{id}").update("[createBy]=#{id}");
            }

            UserGroupRoleModel userGroupRoleModel = new UserGroupRoleModel();
            String userGroupRoleId = UUID.randomUUID().toString().replace("-", "");
            userGroupRoleModel.setUserGroupRoleId(userGroupRoleId);
            userGroupRoleModel.setRoleId(UUID.fromString("703d28f4-a813-4a9b-87fb-971d0e31f9e5"));
            if(item.get("userPath") != null) {
                userGroupRoleModel.setPath((String)item.get("userPath"));
            }else {
                userGroupRoleModel.setPath("0/1");
            }

            userGroupRoleModel.setCreateOn(new Date());
            userGroupRoleModel.setCreateBy((Integer) item.get("createBy"));
            userGroupRoleModel.setUserId(userid);
            userGroupRoleModel.insert();
            if(!formTrigger) {
                item.put("userid", userid);
            }
            if (item.get("eafId") == null){
                item.put("eafId", UUID.randomUUID().toString().replace("-", ""));
            }
            UserModel js = userModel.where("[username]=#{username}").first();
            if (js != null){
                //把账号和密码拼接起来
                String vaildCode = EncryptionUtil.md5(EncryptionUtil.encryptByAES(js.getId().toString(), js.getUsername() + aesPublicKey), "UTF-8");
                String IdumPass = js.getUsername() + vaildCode;
                log.info("IdumPass="+IdumPass);
                log.info("aesPublicKey="+aesPublicKey);
                String token = EncryptionUtil.encryptByAES(IdumPass, aesPublicKey);
                String url = server + "/mobile/details?token=" + token + "&eafid=" + item.get("eafId");
                String filename = js.getUsername() + ".png";
                byte[] binary = QRCodeUtil.creatRrCode(url,width,height);
                String path = UploadUtils.upload(qrCodePath + "/workerlib/people/code" ,filename,binary);
                item.put("qrCode", path);
            }
        }

        catch (Exception e) {
            log.error("生成二维码出错==================================>");
            if(e.getStackTrace()!=null && e.getStackTrace().length > 0) {
                log.error("行号：" + e.getStackTrace()[0].getLineNumber());
            }
            log.error(e);
        }

        return FastJsonUtils.convert(item, AllUserInfo.class);
    }
}
