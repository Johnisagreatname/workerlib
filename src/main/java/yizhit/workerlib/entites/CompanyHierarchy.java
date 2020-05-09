package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Fieldname;
import entity.query.annotation.Tablename;

import java.io.Serializable;

/**
 *@ClassName CompanyHierarchyDto
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/27  11:37
 */
@Tablename("company_hierarchy")
@DataSource(value = "workerlib2", rxjava2 = false)
public class CompanyHierarchy extends Queryable<CompanyHierarchy> implements Serializable {

    /**记录唯一ID*/
    private String id;
    /**机构ID*/
    @Fieldname("o_id")
    private String oId;
    /**机构编号*/
    @Fieldname("o_org_no")
    private String oOrgNo;
    /**机构名称*/
    @Fieldname("o_org_name")
    private String oOrgName;
    /**机构是否启用*/
    @Fieldname("o_enable_flag")
    private Boolean oEnableFlag;
    /**机构类型*/
    @Fieldname("o_dept_type_no")
    private String oDeptTypeNo;
    /**父节点ID*/
    private String pid;
    /**层级码编号*/
    @Fieldname("inner_code")
    private String innerCode;
    /**排序号*/
    @Fieldname("order_no")
    private Integer orderNo;
    /**是否叶子*/
    @Fieldname("is_leaf")
    private Boolean isLeaf;
    /**树节点名称*/
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getoOrgNo() {
        return oOrgNo;
    }

    public void setoOrgNo(String oOrgNo) {
        this.oOrgNo = oOrgNo;
    }

    public String getoOrgName() {
        return oOrgName;
    }

    public void setoOrgName(String oOrgName) {
        this.oOrgName = oOrgName;
    }

    public Boolean getoEnableFlag() {
        return oEnableFlag;
    }

    public void setoEnableFlag(Boolean oEnableFlag) {
        this.oEnableFlag = oEnableFlag;
    }

    public String getoDeptTypeNo() {
        return oDeptTypeNo;
    }

    public void setoDeptTypeNo(String oDeptTypeNo) {
        this.oDeptTypeNo = oDeptTypeNo;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}