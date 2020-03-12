package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Exclude;
import entity.query.annotation.Fieldname;
import entity.query.annotation.Tablename;

import java.io.Serializable;

/**
 * 所有人员信息
 */
@Tablename("alluser")
@DataSource("workerlib2")
public class AllUserInfo extends Queryable<AllUserInfo> implements Serializable {

    private String eafId;           //用户id

    private String eafName;         //用户名

    private String eafPhone;        //联系号码

    private String cwrIdnumType;    //证件类型

    private String cwrIdnum;        //身份证号码

    private int createBy;

    @Fieldname("id_card_front")
    private String cwrIdphotoScan;  //身份证扫描件

    private String cwrPhoto;        //照片

    private String eafCreatetime;   //创建时间

    private String eafModifytime;   //更新时间

    private String cwrIdaddr;       //用户地址

    private String eafCreator;      //创建人

    private String eafModifier;     //更新人

    private int year;            //年

    private int month;           //月

    private int Sex;             //性别

    private String cwrStatus;

    private String eafStatus;

    @Fieldname("qr_code")
    private String qr_code;

    @Exclude
    private Integer userid;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getQrCode() {
        return qr_code;
    }

    public void setQrCode(String qr_code) {
        this.qr_code = qr_code;
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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }


//    private String cwrCertifiDate;
//    private String eafQfjg;
//    private String eafYxtime;
//    private String cwrWorktypeone;
//    private String cwrWorktypetwo;
//    private String cwrWorktypethrell;
//    private String cwrSkillone;
//    private String cwrSkilltwo;;
//    private String cwrSkillthree;
//    private String cwrJobcertify;
//    private String cwrContractno;
//    private String cwrSafeedu;
//    private String cwrSchool;
//    private String cwrMajor;
//    private String cwrGradudate;
//    private String cwrWphoneone;
//    private String cwrWphonetwo;
//    private String cwrWphonethre;
//    private String cwrDepart;
//    private String cwrWorkdepart;
//    private String cwrPhonetwo;
//    private String cwrPhonethree;
//    private String cwrContractD;
//    private String cwrContracttimell;
//    private String cwrCertifi;
//    private String cwrCertifiId;
//    private String cwrCertifiFile;
//    private String cwrSecure;
//    private String cwrSafe;
//    private String cwrSafeTime;
//    private String cwrEpcCode;
//    private String cwrAccessNum;
//    private String cwrAccessCode;
//    private String cwrPosNum;
//    private String cwrUserClass;
//    private String cwrWorkclassId;
//    private String cwrNum;
//    private String cwrIspower;
//    private String cwrOrd;
//    private String eafOrg;
//    private String eafEmail;
//    private String eafLoginname;
//    private String eafLevel;
//    private String eafSort;
//    private String eafStatus;
//    private String cwrAge;
//    private String cwrAddr;
//    private String cwrJiguan;
//    private String cwrIdphoto;
//    private String cwrSex;
//    private String cwrIsidentify;
//    private String cwrJobno;
//    private String cwrContract;
//    private String cwrNation;
//    private String cwrBirthday;
//    private String cwrPolitics;
//    private String cwrMarital;
//    private String cwrEducation;
//    private String cwrComid;
//    private String cwrType;
//    private String cwrIstrain;
//    private String cwrContactman;
//    private String cwrContactphonell;
//    private String cwrIdphotoscan2ll;

}
