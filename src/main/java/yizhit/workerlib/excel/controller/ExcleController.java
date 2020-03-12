package yizhit.workerlib.excel.controller;

import ccait.ccweb.annotation.AccessCtrl;
import ccait.ccweb.controllers.BaseController;
import ccait.ccweb.utils.ImageUtils;
import ccait.ccweb.utils.UploadUtils;
import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yizhit.workerlib.excel.pojo.coursewareData;
import yizhit.workerlib.excel.pojo.excelData;
import yizhit.workerlib.excel.util.ExceclListener;
import yizhit.workerlib.excel.util.ExceclUserListener;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/asyncapi/import")
@AccessCtrl
public class ExcleController extends BaseController {
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

    @PostMapping("/courseware")
    public void importExcel(@RequestBody Map<String, Object> file) throws IOException {
        if(file.get("file") == null) {
            return;
        }
        byte[] bytes = ImageUtils.getBytesForBase64(file.get("file").toString());
        InputStream inputStream = new ByteArrayInputStream(bytes);
        EasyExcel.read(inputStream, coursewareData.class, new ExceclListener(md5PublicKey,encoding,this.getLoginUser(),aesPublicKey,qrCodePath,width,height,server)).sheet().doRead();

    }
    @PostMapping("/user")
    public void importUserExcel(@RequestBody Map<String, Object> file) throws IOException {
        if(file.get("file") == null) {
            return;
        }
        byte[] bytes = ImageUtils.getBytesForBase64(file.get("file").toString());
        InputStream inputStream = new ByteArrayInputStream(bytes);
        EasyExcel.read(inputStream, excelData.class, new ExceclUserListener(md5PublicKey,encoding,this.getLoginUser(),aesPublicKey,qrCodePath,width,height,server)).sheet().doRead();

    }
}
