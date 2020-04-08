package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Fieldname;
import entity.query.annotation.Tablename;

import java.io.Serializable;

@Tablename("checkworkce")
@DataSource(value = "workerlib2")
public class CheckWorkceInfo extends Queryable<CheckWorkceInfo> implements Serializable {

    @Fieldname("checkworkce_id")
    private String eafId;           //记录id
    private String eafCreator;      //创建人
    private String eafModifytime;   //更新时间
    private String eafCreatetime;   //创建时间
    private String eafModifier;     //更新人
    private String cwrPrjid;        //工程id
    private String cwrComid;        //企业id
    private String cwrGrpid;
    private String cwrUserid;       //人员id
    private String cwrEquid;
    private String cwrPasstime;     //通过时间
    private String cwrDirection;    //出入方向
    private String cwrPassmode;
    private String cwrPasspho;      //通行照片
    private String cwrPsntype;
    private String cwrPassdate;
    private String toTicwrStat;
    private String cwrLongitude;
    private String cwrLatitude;
    private String cwrAddress;      //地址
    private String cwrUsertype;
    private String cwrIdnum;        //证件号码
    private String cwrProcessStatus;

    public CheckWorkceInfo(){}

    public String getEafId() {
        return eafId;
    }

    public void setEafId(String eafId) {
        this.eafId = eafId;
    }

    public String getEafCreator() {
        return eafCreator;
    }

    public void setEafCreator(String eafCreator) {
        this.eafCreator = eafCreator;
    }

    public String getEafModifytime() {
        return eafModifytime;
    }

    public void setEafModifytime(String eafModifytime) {
        this.eafModifytime = eafModifytime;
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

    public String getCwrGrpid() {
        return cwrGrpid;
    }

    public void setCwrGrpid(String cwrGrpid) {
        this.cwrGrpid = cwrGrpid;
    }

    public String getCwrUserid() {
        return cwrUserid;
    }

    public void setCwrUserid(String cwrUserid) {
        this.cwrUserid = cwrUserid;
    }

    public String getCwrEquid() {
        return cwrEquid;
    }

    public void setCwrEquid(String cwrEquid) {
        this.cwrEquid = cwrEquid;
    }

    public String getCwrPasstime() {
        return cwrPasstime;
    }

    public void setCwrPasstime(String cwrPasstime) {
        this.cwrPasstime = cwrPasstime;
    }

    public String getCwrDirection() {
        return cwrDirection;
    }

    public void setCwrDirection(String cwrDirection) {
        this.cwrDirection = cwrDirection;
    }

    public String getCwrPassmode() {
        return cwrPassmode;
    }

    public void setCwrPassmode(String cwrPassmode) {
        this.cwrPassmode = cwrPassmode;
    }

    public String getCwrPasspho() {
        return cwrPasspho;
    }

    public void setCwrPasspho(String cwrPasspho) {
        this.cwrPasspho = cwrPasspho;
    }

    public String getCwrPsntype() {
        return cwrPsntype;
    }

    public void setCwrPsntype(String cwrPsntype) {
        this.cwrPsntype = cwrPsntype;
    }

    public String getCwrPassdate() {
        return cwrPassdate;
    }

    public void setCwrPassdate(String cwrPassdate) {
        this.cwrPassdate = cwrPassdate;
    }

    public String getToTicwrStat() {
        return toTicwrStat;
    }

    public void setToTicwrStat(String toTicwrStat) {
        this.toTicwrStat = toTicwrStat;
    }

    public String getCwrLongitude() {
        return cwrLongitude;
    }

    public void setCwrLongitude(String cwrLongitude) {
        this.cwrLongitude = cwrLongitude;
    }

    public String getCwrLatitude() {
        return cwrLatitude;
    }

    public void setCwrLatitude(String cwrLatitude) {
        this.cwrLatitude = cwrLatitude;
    }

    public String getCwrAddress() {
        return cwrAddress;
    }

    public void setCwrAddress(String cwrAddress) {
        this.cwrAddress = cwrAddress;
    }

    public String getCwrUsertype() {
        return cwrUsertype;
    }

    public void setCwrUsertype(String cwrUsertype) {
        this.cwrUsertype = cwrUsertype;
    }

    public String getCwrIdnum() {
        return cwrIdnum;
    }

    public void setCwrIdnum(String cwrIdnum) {
        this.cwrIdnum = cwrIdnum;
    }

    public String getCwrProcessStatus() {
        return cwrProcessStatus;
    }

    public void setCwrProcessStatus(String cwrProcessStatus) {
        this.cwrProcessStatus = cwrProcessStatus;
    }


}
