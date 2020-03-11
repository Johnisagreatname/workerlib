package yizhit.workerlib.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

public class excelData  {
    @ExcelProperty(value = "姓名", index = 2)
    private String eafName;

    @ExcelProperty(value = "联系方式", index = 30)
    private String eafPhone;

    @ExcelProperty(value = "身份证号码", index = 5)
    private String cwrIdnum;
    @ExcelProperty(value = "性别", index = 8)
    private String se;
    @ExcelProperty(value = "身份证住址", index = 29)
    private String cwrIdaddr;

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

    public String getCwrIdnum() {
        return cwrIdnum;
    }

    public void setCwrIdnum(String cwrIdnum) {
        this.cwrIdnum = cwrIdnum;
    }

    public String getSe() {
        return se;
    }

    public void setSe(String se) {
        this.se = se;
    }

    public String getCwrIdaddr() {
        return cwrIdaddr;
    }

    public void setCwrIdaddr(String cwrIdaddr) {
        this.cwrIdaddr = cwrIdaddr;
    }
}
