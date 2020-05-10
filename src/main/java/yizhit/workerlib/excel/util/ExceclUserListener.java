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
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
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
    private String server;

    public ExceclUserListener(String md5PublicKey, String encoding, UserModel user, String aesPublicKey, String qrCodePath, int width, int height, String server) {
        this.md5PublicKey = md5PublicKey;
        this.encoding = encoding;
        this.user = user;
        this.aesPublicKey = aesPublicKey;
        this.qrCodePath = qrCodePath;
        this.width = width;
        this.height = height;
        this.server = server;
    }

    private List<excelData> datas = new ArrayList<excelData>();

    @Override
    public void invoke(excelData o, AnalysisContext analysisContext) {
        datas.add(o);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
//        doSomething(o);//根据自己业务做处理
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //1、入库调用
        try {
            for (int i = 1; i < datas.size(); i++) {

                if (datas.get(i) == null) {
                    continue;
                }
                Integer value = 0;
                dictionaries dict = new dictionaries();
                dict.setName(datas.get(i).getWorkType());
                dict.setCategory("工种");
                dictionaries dictInfo = dict.where("[name]=#{name}").and("[category] = #{category}").first();
                if(dictInfo == null){
                    value =  dict.where("[category] = #{category}").select("max(value) as value").first(Integer.class);
                    dict.setValue(value+1);
                    dict.setColor("#41ccd3");
                    dict.setIsShow(2);
                    dict.setIsCount(2);
                    dict.insert();
                }
                alluser allusers = new alluser();
                workType workTypes = new workType();
                //身份证为空只创建工人，不创建账号
                if (datas.get(i).getCwrIdnum() == null) {
                    String eafId = UUID.randomUUID().toString().replace("-", "");
                    allusers.setEafId(eafId);       //eafID（唯一编码）
                    allusers.setEafName(datas.get(i).getEafName());         //姓名
                    allusers.setUnitId((UUID) StringUtils.cast(UUID.class, "E1518A607E764390848F188390482597"));    //深圳市政编码
                    if (datas.get(i).getEafPhone() != null) {
                        allusers.setEafPhone(datas.get(i).getEafPhone());       //电话号码
                    }
                    allusers.setCwrIdnumType("1");      //身份证类型
                    allusers.setEafUserStatus(0);       //状态：0属于自有工人
                    allusers.setCreateBy(user.getUserId());     //创建人
                    //allusers.setUserPath(user.getUserPath() + "/" + user.getUserId());      //创建人
                    allusers.insert();
                    System.out.println(i+"null>>>>>>>>>>>>>>alluser--insert");
                    if (datas.get(i).getVocational() != null) {
                        if (datas.get(i).getVocational().equals("是")) {
                            workTypes.setCertificate(datas.get(i).getVocational());
                        }
                    }
                    workTypes.setEafId(eafId);
                    workTypes.setWorkType(datas.get(i).getWorkType());
                    //workTypes.setUserPath(user.getUserPath() + "/" + user.getUserId());
                    workTypes.setCreateBy(user.getUserId());
                    workTypes.setCreateOn(new Date());
                    workTypes.insert();
                    System.out.println(i+"null>>>>>>>>>>>>>>workType--insert");
                    continue;
                }
//                allusers.setCwrIdnum(datas.get(i).getCwrIdnum());
//                alluser alluser = allusers.where("[cwrIdnum]=#{cwrIdnum}").first();
//                if(alluser != null){
//                    allusers.setId(alluser.getUserId());
//                    allusers.setEafUserStatus(0);
//                    alluser.where("[id]=#{id}").update("[eafUserStatus]=#{eafUserStatus}");
//                    System.out.println(i+"已存在>>>>>>>>>>>>>>alluser--update-----"+datas.get(i).getCwrIdnum());
//                    System.out.println("\n");
//                }else{
                    user users = new user();
                    users.setUsername(datas.get(i).getCwrIdnum());
                    String passWord = "";
                    if (StringUtils.isEmpty(datas.get(i).getCwrIdnum()) || datas.get(i).getCwrIdnum().length() < 6) {
                        passWord = "123456";
                    } else {
                        passWord = datas.get(i).getCwrIdnum().substring(datas.get(i).getCwrIdnum().length() - 6);
                    }
                    users.setPassword(EncryptionUtil.md5(passWord, md5PublicKey, encoding));
                    //users.setUserPath(user.getUserPath() + "/" + user.getUserId());
                    users.setCreateOn(new Date());
                    Integer userId = users.insert();
                    System.out.println(i+"新增>>>>>>>>>>>>>>users--insert");

                    //alluser
                    String eafId = UUID.randomUUID().toString().replace("-", "");
                    allusers.setEafName(datas.get(i).getEafName());
                    allusers.setEafId(eafId);
                    allusers.setUnitId((UUID) StringUtils.cast(UUID.class, "E1518A607E764390848F188390482597"));

                    allusers.setEafPhone(datas.get(i).getEafPhone());
                    allusers.setCwrIdnumType("1");
                    allusers.setCwrIdnum(datas.get(i).getCwrIdnum());
                    allusers.setEafUserStatus(0);
                    allusers.setCreateBy(user.getUserId());

                    String vaildCode = EncryptionUtil.md5(EncryptionUtil.encryptByAES(userId.toString(), users.getUsername() + aesPublicKey), "UTF-8");
                    String IdumPass = users.getUsername() + vaildCode;
                    String token = EncryptionUtil.encryptByAES(IdumPass, aesPublicKey);
                    String url = server + "/mobile/details?token=" + token + "&eafid=" + eafId;
                    String filename = users.getUsername() + ".png";
                    byte[] binary = QRCodeUtil.creatRrCode(url, width, height);
                    String path = UploadUtils.upload(qrCodePath + "/workerlib/people/code", filename, binary);
                    allusers.setQrCode(path);
                    if (datas.get(i).getCwrIdnum() != null && datas.get(i).getCwrIdnum().length() > 0 && datas.get(i).getCwrIdnum().length() >= 18) {
                        String sCardNum = datas.get(i).getCwrIdnum().substring(16, 17);
                        if (Integer.parseInt(sCardNum) % 2 != 0) {
                            allusers.setSex(1);
                        } else {
                            allusers.setSex(2);
                        }
                        allusers.setYear(Double.parseDouble(datas.get(i).getCwrIdnum().substring(6, 10)));
                        allusers.setMonth(Double.parseDouble(datas.get(i).getCwrIdnum().substring(12, 14)));
                    }
                    allusers.insert();
                    System.out.println(i+"新增>>>>>>>>>>>>>>alluser--insert");

                    //职业证书
                    if(datas.get(i).getVocational() != null){
                        if (datas.get(i).getVocational().equals("有")) {
                            workTypes.setCertificate(datas.get(i).getVocational());
                        }
                    }

                    workTypes.setEafId(eafId);
                    workTypes.setWorkType(datas.get(i).getWorkType());
                    //workTypes.setUserPath(user.getUserPath() + "/" + user.getUserId());
                    workTypes.setCreateBy(user.getUserId());
                    workTypes.insert();
                    System.out.println(i+"新增>>>>>>>>>>>>>>workType--insert");

                    usergrouprole usergrouproles = new usergrouprole();
                    String userGroupRoleId = UUID.randomUUID().toString().replace("-", "");
                    usergrouproles.setUserGroupRoleId(userGroupRoleId);
                    usergrouproles.setUserId(userId);
                    usergrouproles.setUserPath("");
                    usergrouproles.setCreateOn(new Date());
                    usergrouproles.setCreateBy(user.getUserId());
                    usergrouproles.insert();
                    System.out.println(i+"新增>>>>>>>>>>>>>>usergrouprole--insert");
                    System.out.println("\n");
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void doSomething(alluser dict) {

    }
}
