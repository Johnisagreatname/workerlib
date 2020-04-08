package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;

import java.io.Serializable;

@DataSource(value = "workerlib2")
public class Group extends Queryable<Group> implements Serializable {
    private String groupId;         //分组ID
    private String groupName;       //分组名称
    private String description;
    private String userPath;
    private String modifyOn;      //更新时间
    private int modifyBy;           //更新人
    private String createOn;        //创建时间
    private int createBy;           //创建人

    public String getGroupId() {
        return groupId;
    }


    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    public String getModifyOn() {
        return modifyOn;
    }

    public void setModifyOn(String modifyOn) {
        this.modifyOn = modifyOn;
    }

    public int getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(int modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }
}
