package yizhit.workerlib.excel.util;

import ccait.ccweb.model.UserModel;
import ccait.ccweb.utils.EncryptionUtil;
import ccait.ccweb.utils.UploadUtils;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import entity.tool.util.StringUtils;
import yizhit.workerlib.excel.pojo.*;
import yizhit.workerlib.interfaceuilt.QRCodeUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ExceclUserListener extends AnalysisEventListener<excelData> {
    private String md5PublicKey;
    private String encoding;
    private UserModel user;
    private String aesPublicKey;
    private String qrCodePath;     //图片地址
    private int width;
    private int height;
    private String server ;
    public ExceclUserListener(String md5PublicKey, String encoding, UserModel user, String aesPublicKey, String qrCodePath, int width, int height, String server){
        this.md5PublicKey = md5PublicKey;
        this.encoding = encoding;
        this.user= user;
        this.aesPublicKey = aesPublicKey;
        this.qrCodePath =qrCodePath;
        this.width = width;
        this.height = height;
        this.server = server;
    }
    private List<excelData> datas = new ArrayList<excelData>();
//    private List<coursewareData> datas = new ArrayList<coursewareData>();
    @Override
    public void invoke(excelData o, AnalysisContext analysisContext) {
        datas.add(o);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
//        doSomething(o);//根据自己业务做处理
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        //1、入库调用
        for (int i=1;i<datas.size();i++){
            try {
                alluser allusers  = new alluser();
                user users = new user();
                usergrouprole usergrouproles = new usergrouprole();
                //user
                if(datas.get(i).getCwrIdnum() == null){
                    String eafId = UUID.randomUUID().toString().replace("-", "");
                    allusers.setEafName(datas.get(i).getEafName());
                    allusers.setEafId(eafId);
                    allusers.setUnitId((UUID) StringUtils.cast(UUID.class, "E1518A607E764390848F188390482597"));
                    allusers.setEafPhone(datas.get(i).getEafPhone());
                    allusers.setCwrIdnumType("1");
                    allusers.setCwrIdnum(datas.get(i).getCwrIdnum());
                    allusers.setCwrIdaddr(datas.get(i).getCwrIdaddr());
                    allusers.setEafUserStatus(0);
                    allusers.setCreateBy(1);
                    allusers.insert();
                    continue;
                }
                users.setUsername(datas.get(i).getCwrIdnum());
                String passWord = "";
                if (StringUtils.isEmpty(datas.get(i).getCwrIdnum()) || datas.get(i).getCwrIdnum().length() < 6) {
                    passWord = "123456";
                } else {
                    passWord = datas.get(i).getCwrIdnum().substring(datas.get(i).getCwrIdnum().length() - 6);
                }
//                users.setPassword(EncryptionUtil.md5(passWord, md5PublicKey, encoding));
                users.setPassword("");
                users.setUserPath(user.getPath()+"/"+user.getId());
                users.setCreateOn(new Date());
                Integer userId = users.insert();
                users.setId(userId);
                users.setUserPath(user.getPath()+"/"+user.getId()+"/"+userId);
                users.where("[id]=#{id}").update("[createBy]=#{id},[userPath]=#{userPath}");

                //alluser
                String eafId = UUID.randomUUID().toString().replace("-", "");
                allusers.setEafName(datas.get(i).getEafName());
                allusers.setEafId(eafId);
                allusers.setUnitId((UUID) StringUtils.cast(UUID.class, "E1518A607E764390848F188390482597"));
                allusers.setEafPhone(datas.get(i).getEafPhone());
                allusers.setCwrIdnumType("1");
                allusers.setCwrIdnum(datas.get(i).getCwrIdnum());
                allusers.setCwrIdaddr(datas.get(i).getCwrIdaddr());
                allusers.setEafUserStatus(0);
                allusers.setCreateBy(userId);

                String vaildCode = EncryptionUtil.md5(EncryptionUtil.encryptByAES(userId.toString(), users.getUsername() + aesPublicKey), "UTF-8");
                String IdumPass = users.getUsername() + vaildCode;
                String token = EncryptionUtil.encryptByAES(IdumPass, aesPublicKey);
                String url = server + "/mobile/details?token=" + token + "&eafid=" + eafId;
                String filename = users.getUsername() + ".png";
                byte[] binary = QRCodeUtil.creatRrCode(url,width,height);
                String path = UploadUtils.upload(qrCodePath + "/workerlib/people/code" ,filename,binary);
                allusers.setQrCode(path);
                if(datas.get(i).getCwrIdnum() != null && datas.get(i).getCwrIdnum().length()>0 && datas.get(i).getCwrIdnum().length()>=18) {
                    String sCardNum = datas.get(i).getCwrIdnum().substring(16, 17);
                    if (Integer.parseInt(sCardNum) % 2 != 0) {
                        allusers.setSex((double) 1);
                    } else {
                        allusers.setSex((double) 2);
                    }
                    allusers.setYear(Double.parseDouble(datas.get(i).getCwrIdnum().substring(6,10)));
                    allusers.setMonth(Double.parseDouble(datas.get(i).getCwrIdnum().substring(12, 14)));
                }
                allusers.insert();



                String userGroupRoleId = UUID.randomUUID().toString().replace("-", "");
                usergrouproles.setUserGroupRoleId(userGroupRoleId);
                usergrouproles.setUserId(userId);
                usergrouproles.setUserPath(user.getPath()+"/"+user.getId()+"/"+userId);
                usergrouproles.setCreateOn(new Date());
                usergrouproles.setCreateBy(userId);
                usergrouproles.insert();
            } catch (SQLException | NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void doSomething(alluser dict){

    }
}
