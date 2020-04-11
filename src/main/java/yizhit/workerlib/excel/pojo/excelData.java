package yizhit.workerlib.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

public class excelData  {
    @ExcelProperty(value = "姓名", index = 2)
    private String eafName;
    @ExcelProperty(value = "岗位", index = 4)
    private String workType;
    @ExcelProperty(value = "身份证号码", index = 5)
    private String cwrIdnum;
    @ExcelProperty(value = "性别", index = 8)
    private String sex;
    @ExcelProperty(value = "职业技术资格", index = 15)
    private String vocational;
    @ExcelProperty(value = "联系方式", index = 17)
    private String eafPhone;

    public String getEafName() {
        return eafName;
    }

    public void setEafName(String eafName) {
        this.eafName = eafName;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getCwrIdnum() {
        return cwrIdnum;
    }

    public void setCwrIdnum(String cwrIdnum) {
        this.cwrIdnum = cwrIdnum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getVocational() {
        return vocational;
    }

    public void setVocational(String vocational) {
        this.vocational = vocational;
    }

    public String getEafPhone() {
        return eafPhone;
    }

    public void setEafPhone(String eafPhone) {
        this.eafPhone = eafPhone;
    }
}
