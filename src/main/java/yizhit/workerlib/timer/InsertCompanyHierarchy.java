package yizhit.workerlib.timer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yizhit.workerlib.dto.CompanyHierarchyDto;
import yizhit.workerlib.entites.CompanyHierarchy;
import yizhit.workerlib.dto.ResultCompanyHierarchyDto;
import yizhit.workerlib.entites.PageNum;
import yizhit.workerlib.uitls.HttpUitls;
import yizhit.workerlib.uitls.ParamUitls;

import java.lang.reflect.Type;
import java.util.List;

/**
 *@ClassName InsertCompanyHierarchy
 *@Description 市政公司层级关系
 *@Author xieya
 *@Date 2020/4/27  11:28
 */
public class InsertCompanyHierarchy {

    private static final Logger logger = LogManager.getLogger(InsertCompanyHierarchy.class);

    public void companyHierarchy() {
        String jsonStr = HttpUitls.postForJson(ParamUitls.companyHierarchyUrl, null, "市政公司层级关系数据接口");
        ResultCompanyHierarchyDto resultCompanyHierarchy = JSONObject.parseObject(jsonStr, ResultCompanyHierarchyDto.class);
        if (!resultCompanyHierarchy.getSuccess()) {
            logger.info("接口返回状态失败");
        }

        String dataStr = JSONObject.toJSONString(resultCompanyHierarchy.getData());
        JSONObject jsonObject = JSONObject.parseObject(dataStr);
        Object orgDepartmentInfo = jsonObject.get("OrgDepartmentInfo");

        String orgDepartmentInfoJsonStr = JSONObject.toJSONString(orgDepartmentInfo);

        List<CompanyHierarchyDto> companyHierarchyList = JSONObject.parseArray(orgDepartmentInfoJsonStr, CompanyHierarchyDto.class );

        try {
            CompanyHierarchy companyHierarchy = new CompanyHierarchy();
            List<CompanyHierarchy> companyHierarchyListByDb = companyHierarchy.where("1=1").query();
            for (CompanyHierarchy companyHierarchyByDb :companyHierarchyListByDb){
                companyHierarchyByDb.delete();
            }

            //循环插入数据库
            for (CompanyHierarchyDto companyHierarchyDto : companyHierarchyList) {
                CompanyHierarchy companyHierarchy1 = setCompanyHierarchy(companyHierarchyDto);
                companyHierarchy1.insert();
            }
            logger.info("程序执行完成");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private CompanyHierarchy setCompanyHierarchy(CompanyHierarchyDto companyHierarchyDto){
        CompanyHierarchy companyHierarchy = new CompanyHierarchy();
        companyHierarchy.setId(companyHierarchyDto.getId());
        companyHierarchy.setInnerCode(companyHierarchyDto.getInnerCode());
        companyHierarchy.setLeaf(companyHierarchyDto.getLeaf());
        companyHierarchy.setName(companyHierarchyDto.getName());
        companyHierarchy.setOrderNo(companyHierarchyDto.getOrderNo());
        companyHierarchy.setPid(companyHierarchyDto.getPid());
        companyHierarchy.setoDeptTypeNo(companyHierarchyDto.getO_deptTypeNo());
        companyHierarchy.setoEnableFlag(companyHierarchyDto.getO_enableFlag());
        companyHierarchy.setoId(companyHierarchyDto.getO_id());
        companyHierarchy.setoOrgName(companyHierarchyDto.getO_orgName());
        companyHierarchy.setoOrgNo(companyHierarchyDto.getO_orgNo());
        return companyHierarchy;
    }
}