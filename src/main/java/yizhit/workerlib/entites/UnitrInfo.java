package yizhit.workerlib.entites;


import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Fieldname;
import entity.query.annotation.Tablename;

@Tablename("unit")
@DataSource(value = "workerlib2", rxjava2 = false)
public class UnitrInfo extends Queryable<UnitrInfo> {
    @Fieldname("unit_id")
    private String  eafId;              //ID

    @Fieldname("unit_name")
    private String  cwrComName;         //名称

    @Fieldname("unit_number")
    private String  cwrCode;            //单位编号

    @Fieldname("project_license")       //施工单位许可证
    private String  cwrComCode;          //社会统一信用代码或组织机构代码

    @Fieldname("principal")
    private String  cwrComFaren;        //负责人

    @Fieldname("userPath")
    private String  cwrComAddr;         //地址

    @Fieldname("status")
    private String  cwrComStatus;       //状态

    @Fieldname("createBy")
    private String  eafCreator;         //创建人

    @Fieldname("createOn")
    private String  eafCreatetime;      //创建时间

    private String  modifyBy;           //更新人

    @Fieldname("modifyOn")
    private String  eafModifytime;      //更新时间

    private String  cwrComType;

    public String getEafId() {
        return eafId;
    }

    public void setEafId(String eafId) {
        this.eafId = eafId;
    }

    public String getCwrComName() {
        return cwrComName;
    }

    public void setCwrComName(String cwrComName) {
        this.cwrComName = cwrComName;
    }

    public String getCwrCode() {
        return cwrCode;
    }

    public void setCwrCode(String cwrCode) {
        this.cwrCode = cwrCode;
    }

    public String getCwrComCode() {
        return cwrComCode;
    }

    public void setCwrComCode(String cwrComCode) {
        this.cwrComCode = cwrComCode;
    }

    public String getCwrComFaren() {
        return cwrComFaren;
    }

    public void setCwrComFaren(String cwrComFaren) {
        this.cwrComFaren = cwrComFaren;
    }

    public String getCwrComAddr() {
        return cwrComAddr;
    }

    public void setCwrComAddr(String cwrComAddr) {
        this.cwrComAddr = cwrComAddr;
    }

    public String getCwrComStatus() {
        return cwrComStatus;
    }

    public void setCwrComStatus(String cwrComStatus) {
        this.cwrComStatus = cwrComStatus;
    }

    public String getEafCreator() {
        return eafCreator;
    }

    public void setEafCreator(String eafCreator) {
        this.eafCreator = eafCreator;
    }

    public String getEafCreatetime() {
        return eafCreatetime;
    }

    public void setEafCreatetime(String eafCreatetime) {
        this.eafCreatetime = eafCreatetime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String eafModifier) {
        this.modifyBy = eafModifier;
    }

    public String getEafModifytime() {
        return eafModifytime;
    }

    public void setEafModifytime(String eafModifytime) {
        this.eafModifytime = eafModifytime;
    }

    public String getCwrComType() {
        return cwrComType;
    }

    public void setCwrComType(String cwrComType) {
        this.cwrComType = cwrComType;
    }
//    private String  cwrComContac;
//    private String  cwrComMail;
//    private String  cwrComPhone;
//    private String  cwrNameBefor;
//    private String  cwrCapital;
//    private String  cwrBaseBank;
//    private String  cwrBaseNum;
//    private String  cwrOtherBank;
//    private String  cwrOtherNum;
//    private String  cwrPostCode;
//    private String  cwrComJian;









}
