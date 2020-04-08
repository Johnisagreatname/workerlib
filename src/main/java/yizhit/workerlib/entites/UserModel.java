package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@DataSource(value = "workerlib2")
@Tablename("user")
public class UserModel extends Queryable<UserModel> {

    @AutoIncrement
    @Fieldname("id")
    private Integer id;

    @PrimaryKey
    @Fieldname("username")
    private String username;

    @Fieldname("password")
    private String password;

    @Fieldname("${entity.table.reservedField.userPath:userPath}")
    private String path;

    @Fieldname("${entity.table.reservedField.createOn:createOn}")
    private Date createOn;

    @Fieldname("${entity.table.reservedField.createBy:createBy}")
    private Integer createBy;

    @Fieldname("${entity.table.reservedField.modifyOn:modifyOn}")
    private Date modifyOn;

    @Fieldname("${entity.table.reservedField.modifyBy:modifyBy}")
    private Integer modifyBy;

    @Fieldname("status")
    private Integer status;

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

    public Date getModifyOn() {
        return modifyOn;
    }

    public void setModifyOn(Date modifyOn) {
        this.modifyOn = modifyOn;
    }

    public Integer getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Integer modifyBy) {
        this.modifyBy = modifyBy;
    }

    public void setUserGroupRoleModels(List<UserGroupRoleModel> userGroupRoleModels) {
        this.userGroupRoleModels = userGroupRoleModels;
    }

    @Exclude
    private List<UserGroupRoleModel> userGroupRoleModels;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
