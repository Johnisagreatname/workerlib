package yizhit.workerlib.trigger;

import ccait.ccweb.annotation.*;
import ccait.ccweb.filter.CCWebRequestWrapper;
import entity.query.Datetime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import yizhit.workerlib.entites.Group;
import yizhit.workerlib.entites.Privilege;
import yizhit.workerlib.entites.RoleModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
@Trigger(tablename = "project") //触发器注解
@Order(value = 100)
public class PrjectTrigger {

    private static final Logger log = LogManager.getLogger(PrjectTrigger.class);

    /***
     * 新增数据事件
     * @param list （提交的数据）
     * @param request （当前请求）
     * @throws Exception
     */
    @OnInsert
    public void onInsert(List<Map<String, Object>> list, HttpServletRequest request) throws Exception {
        try {
            String groupId = UUID.randomUUID().toString().replace("-", "");
            for(Map item : list) {
                Group group = new Group();
                group.setGroupId(groupId);
                group.setGroupName((String)item.get("project_name"));
                group.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                if(item.get("userPath") ==null){
                    group.setUserPath("0");
                }else {
                    group.setUserPath(item.get("userPath").toString());
                }

                group.insert();

                //查找管理员id
                RoleModel roleModel= new RoleModel();
                roleModel.setRoleName("管理员");
                RoleModel roleId = roleModel.where("[roleName]=#{roleName}").first();

                //获取Privilege表的id
                String privilegeId = UUID.randomUUID().toString().replace("-", "");
                Privilege privilege = new Privilege();
                privilege.setPrivilegeId(privilegeId);
                privilege.setGroupId(groupId);
                privilege.setRoleId(roleId.getRoleId());
                privilege.setCanAdd(1);
                privilege.setCanDelete(1);
                privilege.setCanUpdate(1);
                privilege.setCanView(1);
                privilege.setCanDownload(1);
                privilege.setCanPreview(1);
                privilege.setCanUpload(1);
                privilege.setCanExport(1);
                privilege.setCanImport(1);
                privilege.setCanDecrypt(1);
                privilege.setCanList(1);
                privilege.setCanQuery(1);
                privilege.setScope(4);
                if(item.get("userPath")==null){
                    privilege.setUserPath("0");
                }else {
                    privilege.setUserPath(item.get("userPath").toString());
                }
                privilege.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                privilege.insert();
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