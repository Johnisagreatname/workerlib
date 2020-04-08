package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;

import java.io.Serializable;

@DataSource(value = "workerlib2")
public class ProjectWorkType extends Queryable<ProjectWorkType> implements Serializable {
    private String eafId;       //用户id
    private String projectId;   //项目id
    private String workType;    //工种
    private String userPath;    //地址
    private String modifyBy;    //更新人
    private String modifyOn;    //更新时间
    private String createOn;    //创建时间
    private String createBy;    //创建人

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

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

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyOn() {
        return modifyOn;
    }

    public void setModifyOn(String modifyOn) {
        this.modifyOn = modifyOn;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
