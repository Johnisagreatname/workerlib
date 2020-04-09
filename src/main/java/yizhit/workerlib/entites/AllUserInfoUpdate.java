package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Tablename;

import java.io.Serializable;

@Tablename("alluser")
@DataSource(value = "workerlib2", rxjava2 = false)
public class AllUserInfoUpdate extends Queryable<AllUserInfoUpdate> implements Serializable {

    private Integer id;
    private String eafId;           //用户id

    private String eafName;         //用户名

    private  String unit_id;        //企业id

    private String eafPhone;        //联系号码

    private String cwrIdnumType;    //证件类型

    private String cwrIdnum;        //身份证号码

    private String cwrIdphotoScan;  //身份证扫描件

    private String cwrPhoto;        //照片

    private String eafCreatetime;   //创建时间

    private String eafModifytime;   //更新时间

    private String cwrIdaddr;       //用户地址

    private String eafCreator;      //创建人

    private String eafModifier;     //更新人

    private String cwrStatus;

    private String eafStatus;

    public String getUnitId() {
        return unit_id;
    }

    public void setUnitId(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getEafId() {
        return eafId;
    }

    public void setEafId(String eafId) {
        this.eafId = eafId;
    }

    public String getEafName() {
        return eafName;
    }

    public void setEafName(String eafName) {
        this.eafName = eafName;
    }

    public String getEafPhone() {
        return eafPhone;
    }

    public void setEafPhone(String eafPhone) {
        this.eafPhone = eafPhone;
    }

    public String getCwrIdnumType() {
        return cwrIdnumType;
    }

    public void setCwrIdnumType(String cwrIdnumType) {
        this.cwrIdnumType = cwrIdnumType;
    }

    public String getCwrIdnum() {
        return cwrIdnum;
    }

    public void setCwrIdnum(String cwrIdnum) {
        this.cwrIdnum = cwrIdnum;
    }

    public String getCwrIdphotoScan() {
        return cwrIdphotoScan;
    }

    public void setCwrIdphotoScan(String cwrIdphotoScan) {
        this.cwrIdphotoScan = cwrIdphotoScan;
    }

    public String getCwrPhoto() {
        return cwrPhoto;
    }

    public void setCwrPhoto(String cwrPhoto) {
        this.cwrPhoto = cwrPhoto;
    }

    public String getEafCreatetime() {
        return eafCreatetime;
    }

    public void setEafCreatetime(String eafCreatetime) {
        this.eafCreatetime = eafCreatetime;
    }

    public String getEafModifytime() {
        return eafModifytime;
    }

    public void setEafModifytime(String eafModifytime) {
        this.eafModifytime = eafModifytime;
    }

    public String getCwrIdaddr() {
        return cwrIdaddr;
    }

    public void setCwrIdaddr(String cwrIdaddr) {
        this.cwrIdaddr = cwrIdaddr;
    }

    public String getEafCreator() {
        return eafCreator;
    }

    public void setEafCreator(String eafCreator) {
        this.eafCreator = eafCreator;
    }

    public String getEafModifier() {
        return eafModifier;
    }

    public void setEafModifier(String eafModifier) {
        this.eafModifier = eafModifier;
    }

    public String getCwrStatus() {
        return cwrStatus;
    }

    public void setCwrStatus(String cwrStatus) {
        this.cwrStatus = cwrStatus;
    }

    public String getEafStatus() {
        return eafStatus;
    }

    public void setEafStatus(String eafStatus) {
        this.eafStatus = eafStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
