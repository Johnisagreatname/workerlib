package yizhit.workerlib.entites;


import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Fieldname;
import entity.query.annotation.PrimaryKey;
import entity.query.annotation.Tablename;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@DataSource(value = "workerlib2")
@Tablename("role")
public class RoleModel extends Queryable<RoleModel> {
    @PrimaryKey
    @Fieldname("roleId")
    private UUID roleId;

    @Fieldname("roleName")
    private String roleName;

    @Fieldname("description")
    private String description;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public UUID getRoleId() {
        return this.roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
