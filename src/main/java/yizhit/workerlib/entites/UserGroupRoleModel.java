package yizhit.workerlib.entites;


import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Fieldname;
import entity.query.annotation.PrimaryKey;
import entity.query.annotation.Tablename;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.UUID;

@DataSource(value = "workerlib2")
@Tablename("UserGroupRole")
public class UserGroupRoleModel extends Queryable<UserGroupRoleModel> {

    private static final Logger log = LogManager.getLogger( ccait.ccweb.model.UserGroupRoleModel.class );

    @PrimaryKey
    @Fieldname("userGroupRoleId")
    private String userGroupRoleId;

    @Fieldname("userId")
    private Integer userId;

    @Fieldname("roleId")
    private UUID roleId;

    @Fieldname("${entity.table.reservedField.userPath:userPath}")
    private String path;

    @Fieldname("${entity.table.reservedField.createOn:createOn}")
    private Date createOn;

    @Fieldname("${entity.table.reservedField.createBy:createBy}")
    private Integer createBy;

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

    public String getUserGroupRoleId() {
        return userGroupRoleId;
    }

    public void setUserGroupRoleId(String userGroupRoleId) {
        this.userGroupRoleId = userGroupRoleId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
