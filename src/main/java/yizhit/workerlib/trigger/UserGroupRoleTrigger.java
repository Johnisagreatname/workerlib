



package yizhit.workerlib.trigger;

import ccait.ccweb.annotation.OnInsert;
import ccait.ccweb.annotation.Trigger;
import ccait.ccweb.filter.CCWebRequestWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import yizhit.workerlib.entites.RoleModel;
import yizhit.workerlib.entites.UserModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
@Trigger(tablename = "usergrouprole") //触发器注解
public class UserGroupRoleTrigger {

    private static final Logger log = LogManager.getLogger(UserGroupRoleTrigger.class);
    private static RoleModel role;

    @PostConstruct
    private void construct() throws SQLException {

        if(role != null) {
            return;
        }
        role = new RoleModel();
        role.setRoleName("工人");
        role = role.where("[roleName]=#{roleName}").first();
    }


    /***
     * 新增数据事件
     * @param list （提交的数据）
     * @param request （当前请求）
     * @throws Exception
     */
    @OnInsert
    public void onInsert(List<Map<String, Object>> list, HttpServletRequest request) throws Exception {

        try {
            if(role == null) {
                return;
            }

            for (Map item : list) {
                if(item.containsKey("roleId")) {
                    continue;
                }

                if(item.containsKey("userId")) {
                    continue;
                }

                if(role.getRoleName().equals(item.get("roleId"))) {
                    UserModel user = new UserModel();
                    user.setId(Integer.parseInt(item.get("userId").toString()));
                    user.where("[id]=#{id}").update("[password]=''");
                }
            }
            CCWebRequestWrapper wrapper = (CCWebRequestWrapper) request;
            wrapper.setPostParameter(list);
        }

        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
