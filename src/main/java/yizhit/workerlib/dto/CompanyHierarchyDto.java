package yizhit.workerlib.dto;

import entity.query.annotation.Fieldname;

import java.io.Serializable;

/**
 *@ClassName CompanyHierarchyDto
 *@Description 接收数据DTO
 *@Author xieya
 *@Date 2020/4/27  14:05
 */
public class CompanyHierarchyDto implements Serializable {

    /**记录唯一ID*/
    private String id;
    /**机构ID*/
    private String o_id;
    /**机构编号*/
    private String o_orgNo;
    /**机构名称*/
    private String o_orgName;
    /**机构是否启用*/
    private Boolean o_enableFlag;
    /**机构类型*/
    private String o_deptTypeNo;
    /**父节点ID*/
    private String pid;
    /**层级码编号*/
    private String innerCode;
    /**排序号*/
    private Integer orderNo;
    /**是否叶子*/
    private Boolean isLeaf;
    /**树节点名称*/
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getO_id() {
        return o_id;
    }

    public void setO_id(String o_id) {
        this.o_id = o_id;
    }

    public String getO_orgNo() {
        return o_orgNo;
    }

    public void setO_orgNo(String o_orgNo) {
        this.o_orgNo = o_orgNo;
    }

    public String getO_orgName() {
        return o_orgName;
    }

    public void setO_orgName(String o_orgName) {
        this.o_orgName = o_orgName;
    }

    public Boolean getO_enableFlag() {
        return o_enableFlag;
    }

    public void setO_enableFlag(Boolean o_enableFlag) {
        this.o_enableFlag = o_enableFlag;
    }

    public String getO_deptTypeNo() {
        return o_deptTypeNo;
    }

    public void setO_deptTypeNo(String o_deptTypeNo) {
        this.o_deptTypeNo = o_deptTypeNo;
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