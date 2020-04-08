package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Fieldname;
import entity.query.annotation.Tablename;

/**
 * 工程下的用户信息表
 */
@Tablename("archives")
@DataSource(value = "workerlib2")
public class ArchivesInfoUpdate extends Queryable<ArchivesInfoUpdate> {

    @Fieldname("id")
    private String userid;          //用户ID

    @Fieldname("project_id")
    private String cwrPrjid;        //工程id

    private String cwrComid;        //企业ID

    @Fieldname("name")
    private String eafName;         //姓名

    @Fieldname("phone")
    private String eafPhone;         //手机号

    private String cwrIdnumTyp;     //证件类型

    @Fieldname("id_number")
    private String cwrIdnum;        //证件号码

    @Fieldname("createOn")
    private String eafCreatetime;   //创建时间

    @Fieldname("modifyBy")
    private String eafModifier;     //更新人

    @Fieldname("modifyOn")
    private String eafModifytime;   //更新时间

    @Fieldname("createBy")
    private String eafCreator;      //创建人

    private String eafRLeftid;

    private String eafId;

    private String cwrUserStatus;   //状态

    private String cwrWorkclassId;

    private String cwrWorktype;

    private String cwrUserIn;

    private String cwrUserOut;

    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCwrPrjid() {
        return cwrPrjid;
    }

    public void setCwrPrjid(String cwrPrjid) {
        this.cwrPrjid = cwrPrjid;
    }

    public String getCwrComid() {
        return cwrComid;
    }

    public void setCwrComid(String cwrComid) {
        this.cwrComid = cwrComid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getCwrIdnumTyp() {
        return cwrIdnumTyp;
    }

    public void setCwrIdnumTyp(String cwrIdnumTyp) {
        this.cwrIdnumTyp = cwrIdnumTyp;
    }

    public String getCwrIdnum() {
        return cwrIdnum;
    }

    public void setCwrIdnum(String cwrIdnum) {
        this.cwrIdnum = cwrIdnum;
    }

    public String getEafCreatetime() {
        return eafCreatetime;
    }

    public void setEafCreatetime(String eafCreatetime) {
        this.eafCreatetime = eafCreatetime;
    }

    public String getEafModifier() {
        return eafModifier;
    }

    public void setEafModifier(String eafModifier) {
        this.eafModifier = eafModifier;
    }

    public String getEafModifytime() {
        return eafModifytime;
    }

    public void setEafModifytime(String eafModifytime) {
        this.eafModifytime = eafModifytime;
    }

    public String getEafCreator() {
        return eafCreator;
    }

    public void setEafCreator(String eafCreator) {
        this.eafCreator = eafCreator;
    }

    public String getEafRLeftid() {
        return eafRLeftid;
    }

    public void setEafRLeftid(String eafRLeftid) {
        this.eafRLeftid = eafRLeftid;
    }

    public String getEafId() {
        return eafId;
    }

    public void setEafId(String eafId) {
        this.eafId = eafId;
    }

    public String getCwrUserStatus() {
        return cwrUserStatus;
    }

    public void setCwrUserStatus(String cwrUserStatus) {
        this.cwrUserStatus = cwrUserStatus;
    }

    public String getCwrWorkclassId() {
        return cwrWorkclassId;
    }

    public void setCwrWorkclassId(String cwrWorkclassId) {
        this.cwrWorkclassId = cwrWorkclassId;
    }

    public String getCwrWorktype() {
        return cwrWorktype;
    }

    public void setCwrWorktype(String cwrWorktype) {
        this.cwrWorktype = cwrWorktype;
    }

    public String getCwrUserIn() {
        return cwrUserIn;
    }

    public void setCwrUserIn(String cwrUserIn) {
        this.cwrUserIn = cwrUserIn;
    }

    public String getCwrUserOut() {
        return cwrUserOut;
    }

    public void setCwrUserOut(String cwrUserOut) {
        this.cwrUserOut = cwrUserOut;
    }
//    private String eafRRightclsid;
//    private String cwrUserType;
//    private String cwrIskeyps;
//    private String cwrIsLeader;




}
