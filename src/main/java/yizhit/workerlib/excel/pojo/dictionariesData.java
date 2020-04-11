package yizhit.workerlib.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import entity.query.Queryable;

public class dictionariesData {
    @ExcelProperty(index = 1)
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
