package yizhit.workerlib.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

public class coursewareData {
    @ExcelProperty(index = 3)
    private String name;
    @ExcelProperty(index = 4)
    private String idNum;
    @ExcelProperty(index = 10)
    private String title;
    @ExcelProperty(index = 5)
    private String courseType;
    @ExcelProperty(index = 7)
    private String total_hours;
    @ExcelProperty(index = 6)
    private String coursewareDate;
    @ExcelProperty(index = 11)
    private String courseRamk;
    @ExcelProperty(index = 8)
    private String trainingInstitution;
    @ExcelProperty(index = 9)
    private String trainingTeacher;

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseType() {
        return courseType;
    }

    public String getTrainingInstitution() {
        return trainingInstitution;
    }

    public void setTrainingInstitution(String trainingInstitution) {
        this.trainingInstitution = trainingInstitution;
    }

    public String getTrainingTeacher() {
        return trainingTeacher;
    }

    public void setTrainingTeacher(String trainingTeacher) {
        this.trainingTeacher = trainingTeacher;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getTotal_hours() {
        return total_hours;
    }

    public void setTotal_hours(String total_hours) {
        this.total_hours = total_hours;
    }

    public String getCoursewareDate() {

        return coursewareDate;
    }

    public void setCoursewareDate(String coursewareDate) {
        this.coursewareDate = coursewareDate;
    }

    public String getCourseRamk() {
        return courseRamk;
    }

    public void setCourseRamk(String courseRamk) {
        this.courseRamk = courseRamk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
