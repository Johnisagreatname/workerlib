package yizhit.workerlib.timer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.tool.util.RequestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import yizhit.workerlib.entites.Group;
import yizhit.workerlib.entites.PageNum;
import yizhit.workerlib.entites.Privilege;
import yizhit.workerlib.entites.ProjectInfo;
import yizhit.workerlib.entites.RoleModel;
import yizhit.workerlib.interfaceuilt.FinalUtil;
import yizhit.workerlib.uitls.DateUtils;
import yizhit.workerlib.uitls.ParamUitls;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *@ClassName InsertProject
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/15  15:41
 */
public class InsertProject {

    private static final Logger logger = LogManager.getLogger(InsertProject.class);

    public void project(int pageIndex, int projectIndex, Boolean enableTasks) {
        if (enableTasks != null && !enableTasks) {
            return;
        }

        logger.info("页码pageIndex=" + pageIndex);

        try {
            //查询数据库页码
            PageNum pageNum = new PageNum();
            pageNum.setPageName("project");
            if (pageIndex == 1) {
                List<PageNum> pageNumList = pageNum.where("[page_name]=#{pageName}").select("page_index").query(PageNum.class);
                Integer dbNum = pageNumList.get(0).getPageIndex();
                if (dbNum > 1) {
                    pageIndex = dbNum;
                }
            }

            //组装项目接口入参
            logger.info("组装项目接口入参开始");
            String project = ParamUitls.setParam(pageIndex, ParamUitls.project, null, null);

            logger.info("入参project=" + project);
            HashMap<String, String> header = ParamUitls.setHeader();
            String projectResult = RequestUtils.post(FinalUtil.url, project, header);
            logger.info("返回参数projectResult=" + projectResult);

            JSONObject projectJson = JSONObject.parseObject(projectResult);
            if (projectJson.containsKey("code") && projectJson.get("code").equals("0")) {
                JSONArray array = projectJson.getJSONObject("data").getJSONArray("list");
                int total = Integer.parseInt(String.valueOf(projectJson.getJSONObject("data").get("total")));
                int pageSize = Integer.parseInt(String.valueOf(projectJson.getJSONObject("data").get("pageSize")));
                int intTotal = total / pageSize;
                //项目
                List<ProjectInfo> projectInfoList = JSONArray.parseArray(array.toJSONString(), ProjectInfo.class);

                pageIndex++;

                for (ProjectInfo projectInfo : projectInfoList) {

                    //获取Group表的id
                    String groupId = UUID.randomUUID().toString().replace("-", "");
                    Group group = new Group();
                    group.setGroupId(groupId);
                    group.setUserPath("0/1");
                    group.setCreateOn(DateUtils.getCurrentDate());
                    group.setGroupName(projectInfo.getCwrPrjName());

                    //查找管理员id
                    RoleModel roleModel = new RoleModel();
                    roleModel.setRoleName("管理员");
                    RoleModel returnRoleModel = roleModel.where("[roleName]=#{roleName}").first();

                    //设置实体
                    Privilege privilege = setPrivilege(groupId, returnRoleModel.getRoleId());

                    ProjectInfo projectInfos = new ProjectInfo();
                    projectInfos.setEafId(projectInfo.getEafId());
                    //查找是否有相同的公司
                    ProjectInfo projects = projectInfos.where("[project_id]=#{eafId}").first();
                    logger.info("projectInfo=" + JSONObject.toJSON(project));

                    if (projects == null) {
                        projectInfo.insert();
                        group.insert();
                        privilege.insert();
                        logger.info(projectIndex++ + group.getGroupName() + "条插入数据成功");
                    } else {
                        //跟新数据
                        projectInfo.where("[project_id]=#{eafId}").update("" +
                                "[modifyOn]=#{eafModifytime}," +
                                "[createOn]=#{eafCreatetime}," +
                                "[project_address]=#{cwrPrjAddr}," +
                                "[project_name]=#{cwrPrjName}," +
                                "[project_brief]=#{cwrPrjJian}," +
                                "[status]=#{cwrPrjStatus}," +
                                "[cwrPrjType]=#{cwrPrjType}," +
                                "[cwrPrjCode]=#{cwrPrjCode}," +
                                "[end_time]=#{cwrEndDate}," +
                                "[start_time]=#{cwrStartDate}," +
                                "[construction]=#{cwrJsUnit}," +
                                "[organization]=#{cwrSgUnit}," +
                                "[supervising]=#{cwrControlUnit}");
                        group.where("[groupName]=#{groupName}").update("[groupName]=#{groupName}");
                        logger.info(group.getGroupName() + "项目数据更新成功");
                        logger.info(projectIndex++);
                    }
                }
                logger.info("项目总数量=" + projectIndex);
                //递归
                if (pageIndex <= intTotal + 1) {
                    pageNum.setPageIndex(pageIndex);
                    pageNum.where("[page_name]=#{pageName}").update("[page_index]=#{pageIndex}");
                    project(pageIndex, projectIndex, enableTasks);
                }

                //清空页码数据
                pageNum.setPageIndex(1);
                pageNum.where("[page_name]=#{pageName}").update("[page_index]=#{pageIndex}");
                logger.info("项目执行完成");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Author xieya
     * @Description 设置实体
     * @Date 2020/4/15 16:12
     * @param groupId
     * @param roleId
     * @return yizhit.workerlib.entites.Privilege
     **/
    private Privilege setPrivilege(String groupId, UUID roleId) {
        Privilege privilege = new Privilege();
        //获取Privilege表的id
        String privilegeId = UUID.randomUUID().toString().replace("-", "");
        privilege.setPrivilegeId(privilegeId);
        privilege.setGroupId(groupId);
        privilege.setRoleId(roleId);
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
        privilege.setUserPath("0/1");
        privilege.setCreateOn(DateUtils.getCurrentDate());
        return privilege;
    }
}