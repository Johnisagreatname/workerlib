package yizhit.workerlib.excel.controller;

import ccait.ccweb.controllers.BaseController;
import entity.tool.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yizhit.workerlib.entites.AllUserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static yizhit.workerlib.trigger.AllUserTrigger.genQrCode;

@RestController
@RequestMapping("/asyncapi/user")
public class UserController extends BaseController {
    @Value("${entity.security.encrypt.MD5.publicKey:ccait}")
    private String md5PublicKey;
    @Value("${entity.encoding:UTF-8}")
    private String encoding;
    @Value("${entity.security.encrypt.AES.publicKey:ccait}")
    private String aesPublicKey;
    @Value("${entity.upload.workerlib.people.code.path}")
    private String qrCodePath;     //图片地址
    @Value("${qrcode.width}")
    private int width;
    @Value("${qrcode.height}")
    private int height;
    @Value("${qrcode.server}")      //IP
    private String server ;
    private static final Logger log = LogManager.getLogger(UserController.class);
    @PostMapping("/register")
    public Map insert(@RequestBody AllUserInfo user, HttpServletRequest request) throws Exception {
        Map<String, Object> maps = new HashMap<String, Object>();
        try {
            Map map = JsonUtils.convert(user, Map.class);
            user = genQrCode(map, null, md5PublicKey, aesPublicKey, qrCodePath, encoding, width, height, server, true);
            if(user.getStatus()!= null && user.getStatus() == false){
                maps.put("data",Collections.emptyList());
                maps.put("status",-1);
                maps.put("message","用户已存在！");
            }else{
                maps.put("data",Collections.emptyList());
                maps.put("status",0);
                maps.put("message","注册成功！");
                user.insert();
            }

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }



        return maps;
    }
}

