
package yizhit.workerlib.excel.util;

import ccait.ccweb.model.UserModel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import yizhit.workerlib.excel.pojo.*;
import java.sql.SQLException;
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
    private List<coursewareData> datas = new ArrayList<coursewareData>();
    @Override
    public void invoke(coursewareData o, AnalysisContext analysisContext) {
        datas.add(o);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
//        doSomething(o);//根据自己业务做处理
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        dictionaries dict = new dictionaries();
        courseware cour = new courseware();
        cultivate cul = new cultivate();
        cultivate upCul = new cultivate();

        Integer value = 0;
        int courId = 0;

        for(int i=1;i<datas.size();i++){
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



    private void doSomething(alluser dict){

    }
}
