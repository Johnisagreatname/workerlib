package yizhit.workerlib.excel.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import entity.query.Queryable;
import entity.query.annotation.Fieldname;

import java.util.UUID;

public class alluser extends Queryable<alluser> {
    @ExcelIgnore
    private Integer id;
    @ExcelProperty(value = "姓名", index = 2)
    private String eafName;
    @ExcelIgnore
    private String eafId;
    @Fieldname("unit_id")
    private UUID unitId;
    @ExcelProperty(value = "联系方式", index = 30)
    private String eafPhone;
    @ExcelIgnore
    private int eafUserStatus;
    //默认为1
    @ExcelIgnore
    private String cwrIdnumType;
    @ExcelProperty(value = "身份证号码", index = 5)
    private String cwrIdnum;
    @ExcelProperty(value = "身份证住址", index = 29)
    private String cwrIdaddr;
    @ExcelIgnore
    private Double year;
    @ExcelIgnore
    private Double month;
    @ExcelIgnore
    private Double Sex;
    @Fieldname("qr_code")
    private String  qrCode;
    private int createBy;

    public String getEafName() {
        return eafName;
    }

    public void setEafName(String eafName) {
        this.eafName = eafName;
    }

    public String getEafId() {
        return eafId;
    }

    public void setEafId(String eafId) {
        this.eafId = eafId;
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

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public String getCwrIdaddr() {
        return cwrIdaddr;
    }

    public void setCwrIdaddr(String cwrIdaddr) {
        this.cwrIdaddr = cwrIdaddr;
    }

    public Double getYear() {
        return year;
    }

    public void setYear(Double year) {
        this.year = year;
    }

    public Double getMonth() {
        return month;
    }

    public void setMonth(Double month) {
        this.month = month;
    }

    public Double getSex() {
        return Sex;
    }

    public void setSex(Double sex) {
        Sex = sex;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEafUserStatus() {
        return eafUserStatus;
    }

    public void setEafUserStatus(int eafUserStatus) {
        this.eafUserStatus = eafUserStatus;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }
}
