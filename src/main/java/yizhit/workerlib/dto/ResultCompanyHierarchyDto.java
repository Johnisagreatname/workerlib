package yizhit.workerlib.dto;

/**
 *@ClassName ResultCompanyHierarchyDto
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/27  11:38
 */
public class ResultCompanyHierarchyDto {

    private Object data;
    private String msg;
    private Boolean success;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}