package yizhit.workerlib.excel.util;

import ccait.ccweb.model.UserModel;
import ccait.ccweb.utils.EncryptionUtil;
import ccait.ccweb.utils.FastJsonUtils;
import ccait.ccweb.utils.UploadUtils;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import entity.tool.util.StringUtils;
import yizhit.workerlib.excel.pojo.*;
import yizhit.workerlib.interfaceuilt.QRCodeUtil;
import yizhit.workerlib.trigger.AllUserTrigger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExceclListener extends AnalysisEventListener<coursewareData> {
    private String md5PublicKey;
    private String encoding;
    private UserModel user;
    private String aesPublicKey;
    private String qrCodePath;     //图片地址
    private int width;
    private int height;
    private String server ;
    public ExceclListener(String md5PublicKey,String encoding,UserModel user,String aesPublicKey,String qrCodePath,int width,int height,String server){
        this.md5PublicKey = md5PublicKey;
        this.encoding = encoding;
        this.user= user;
        this.aesPublicKey = aesPublicKey;
        this.qrCodePath =qrCodePath;
        this.width = width;
        this.height = height;
        this.server = server;
    }
//    private List<excelData> datas = new ArrayList<excelData>();
    private List<coursewareData> datas = new ArrayList<coursewareData>();
    @Override
    public void invoke(coursewareData o, AnalysisContext analysisContext) {
        datas.add(o);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
//        doSomething(o);//根据自己业务做处理
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        dictionaries dict = new dictionaries();
        courseware cour = new courseware();
        cultivate cul = new cultivate();
        cultivate upCul = new cultivate();
        cultivate_archives culArc = new cultivate_archives();

        Integer value = 0;
        int coursewareId = 0;
        int cultivateId = 0;
        int courId = 0;

        for(int i=0;i<datas.size();i++){
            try {
            if(datas.get(i).getCourseType() == null){
                continue;
            }
            dict.setName(datas.get(i).getCourseType());
            dict.setCategory("课程类型");
            dictionaries dictInfo = dict.where("[name]=#{name}").and("[category] = #{category}").first();
            if(dictInfo == null){
               value =  dict.where("[category] = #{category}").select("max(value) as value").first(Integer.class);
               dict.setValue(value+1);
               dict.setColor("#41ccd3");
               dict.setIsShow(2);
               dict.setIsCount(2);
               dict.insert();
            }

            cour.setTitle(datas.get(i).getTitle());
            courseware courInfo = cour.where("[title]=#{title}").first();
            if(courInfo == null){
                cour.setTitle(datas.get(i).getTitle());
                cour.setCourse(datas.get(i).getCourseType());
                cour.setTotalHours(datas.get(i).getTotal_hours());
                cour.setTeachingMethod("现场培训");
                cour.setWhether("是");
                cour.setTypeWork("普工");
                cour.setStatus(2);
                cour.setCreateBy(user.getId());
                cour.insert();
            }
            courId =  cour.where("[title]=#{title}").select("id").first(Integer.class);
            cul.setCoursewareBrief(datas.get(i).getTitle());
            cul.setStartTime(datas.get(i).getCoursewareDate().replace("/","-"));
            cultivate cultivate = cul.where("[courseware_brief]=#{coursewareBrief}").and("[startTime]=#{startTime}").select().first();
            if(cultivate == null){
                cul.setCourseId(courId);
                cul.setPeoples(1);
                cul.setStatus(2);
                cul.setState("已完成");
                cul.setMark(datas.get(i).getCourseRamk());
                cul.setTrainingInstitution(datas.get(i).getTrainingInstitution());
                cul.setTrainingTeacher(datas.get(i).getTrainingTeacher());
                cul.setTrainingAddress(datas.get(i).getTrainingInstitution());
                cul.setCreateOn(datas.get(i).getCoursewareDate().replace("/","-"));
                cul.setCourseName(datas.get(i).getTitle());
                cul.setCreateBy(user.getId());
                cul.insert();
            }else {
                upCul.setPeoples(cultivate.getPeoples()+1);
                upCul.setId(cultivate.getId());
                upCul.where("[id]=#{id}").update("[peoples]=#{peoples}");
            }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//
//        //1、入库调用
//        for (int i=1;i<datas.size();i++){
//            try {
//                alluser allusers  = new alluser();
//                user users = new user();
//                usergrouprole usergrouproles = new usergrouprole();
//                //user
//                users.setUsername(datas.get(i).getCwrIdnum());
//                String passWord = "";
//                if (StringUtils.isEmpty(datas.get(i).getCwrIdnum()) || datas.get(i).getCwrIdnum().length() < 6) {
//                    passWord = "123456";
//                } else {
//                    passWord = datas.get(i).getCwrIdnum().substring(datas.get(i).getCwrIdnum().length() - 6);
//                }
//                users.setPassword(EncryptionUtil.md5(passWord, md5PublicKey, encoding));
//                users.setUserPath("0/1");
//                users.setCreateOn(new Date());
//                Integer userId = users.insert();
//                users.setId(userId);
//                users.where("[id]=#{id}").update("[createBy]=#{id}");
//
//                //alluser
//                String eafId = UUID.randomUUID().toString().replace("-", "");
//                allusers.setEafName(datas.get(i).getEafName());
//                allusers.setEafId(eafId);
//                allusers.setUnitId((UUID) StringUtils.cast(UUID.class, "E1518A607E764390848F188390482597"));
//                allusers.setEafPhone(datas.get(i).getEafPhone());
//                allusers.setCwrIdnumType("1");
//                allusers.setCwrIdnum(datas.get(i).getCwrIdnum());
//                allusers.setCwrIdaddr(datas.get(i).getCwrIdaddr());
//                allusers.setEafUserStatus(0);
//                allusers.setCreateBy(userId);
//                if(datas.get(i).getCwrIdnum() == null){
//                    allusers.insert();
//                    continue;
//                }
//
//                String vaildCode = EncryptionUtil.md5(EncryptionUtil.encryptByAES(userId.toString(), users.getUsername() + aesPublicKey), "UTF-8");
//                String IdumPass = users.getUsername() + vaildCode;
//                String token = EncryptionUtil.encryptByAES(IdumPass, aesPublicKey);
//                String url = server + "/mobile/details?token=" + token + "&eafid=" + eafId;
//                String filename = users.getUsername() + ".png";
//                byte[] binary = QRCodeUtil.creatRrCode(url,width,height);
//                String path = UploadUtils.upload(qrCodePath + "/workerlib/people/code" ,filename,binary);
//                allusers.setQrCode(path);
//                if(datas.get(i).getCwrIdnum() != null && datas.get(i).getCwrIdnum().length()>0 && datas.get(i).getCwrIdnum().length()>=18) {
//                    String sCardNum = datas.get(i).getCwrIdnum().substring(16, 17);
//                    if (Integer.parseInt(sCardNum) % 2 != 0) {
//                        allusers.setSex((double) 1);
//                    } else {
//                        allusers.setSex((double) 2);
//                    }
//                    allusers.setYear(Double.parseDouble(datas.get(i).getCwrIdnum().substring(6,10)));
//                    allusers.setMonth(Double.parseDouble(datas.get(i).getCwrIdnum().substring(12, 14)));
//                }
//                allusers.insert();
//
//
//
//                String userGroupRoleId = UUID.randomUUID().toString().replace("-", "");
//                usergrouproles.setUserGroupRoleId(userGroupRoleId);
//                usergrouproles.setUserId(userId);
//                usergrouproles.setUserPath("0/1");
//                usergrouproles.setCreateOn(new Date());
//                usergrouproles.setCreateBy(userId);
//                usergrouproles.insert();
//            } catch (SQLException | NoSuchAlgorithmException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }


    private void doSomething(alluser dict){

    }
}
