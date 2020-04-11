package yizhit.workerlib.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import entity.query.Queryable;

public class dictionariesData extends Queryable<dictionariesData> {
    @ExcelProperty(index = 0)
    private String name;
    @ExcelProperty(index = 1)
    private int value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
