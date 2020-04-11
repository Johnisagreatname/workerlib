package yizhit.workerlib.excel.pojo;

import entity.query.Queryable;

import java.util.Date;

public class workType extends Queryable<workType> {
    private String eafId;
    private String workType;
    private String certificate;
    private String userPath;
    private Date createOn;
    private Integer createBy;

    public String getEafId() {
        return eafId;
    }

    public void setEafId(String eafId) {
        this.eafId = eafId;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }
}
