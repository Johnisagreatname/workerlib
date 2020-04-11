
package yizhit.workerlib.excel.util;

import ccait.ccweb.model.UserModel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import yizhit.workerlib.excel.pojo.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExceclWorkTypeListener extends AnalysisEventListener<dictionariesData> {
    private String md5PublicKey;
    private String encoding;
    private UserModel user;
    private String aesPublicKey;
    private String qrCodePath;     //图片地址
    private int width;
    private int height;
    private String server ;
    public ExceclWorkTypeListener(String md5PublicKey, String encoding, UserModel user, String aesPublicKey, String qrCodePath, int width, int height, String server){
        this.md5PublicKey = md5PublicKey;
        this.encoding = encoding;
        this.user= user;
        this.aesPublicKey = aesPublicKey;
        this.qrCodePath =qrCodePath;
        this.width = width;
        this.height = height;
        this.server = server;
    }
    private List<dictionariesData> datas = new ArrayList<dictionariesData>();
    @Override
    public void invoke(dictionariesData o, AnalysisContext analysisContext) {
        datas.add(o);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
//        doSomething(o);//根据自己业务做处理
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        dictionaries dict = new dictionaries();

        Integer value = 0;
        int courId = 0;

        for(int i=1;i<datas.size();i++){
            try {
            if(datas.get(i).getName() == null){
                continue;
            }
            dict.setName(datas.get(i).getName());
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private void doSomething(alluser dict){

    }
}
