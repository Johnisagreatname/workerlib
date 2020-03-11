package yizhit.workerlib.excel.pojo;

import entity.query.Queryable;
import entity.query.annotation.Fieldname;

import java.util.Date;

public class cultivate extends Queryable<cultivate> {
    private int id;
    @Fieldname("courseware_brief")
    private String coursewareBrief;
    @Fieldname("course_id")
    private int courseId;

    private String startTime;
    private int peoples;
    private int status;
    private String state;
    private String mark;
    private String createOn;
    private int createBy;
    @Fieldname("course_name")
    private String courseName;
    private String trainingInstitution;
    private String trainingTeacher;
    private String trainingAddress;

    public String getCoursewareBrief() {
        return coursewareBrief;
    }

    public void setCoursewareBrief(String coursewareBrief) {
        this.coursewareBrief = coursewareBrief;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getPeoples() {
        return peoples;
    }

    public void setPeoples(int peoples) {
        this.peoples = peoples;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getTrainingAddress() {
        return trainingAddress;
    }

    public void setTrainingAddress(String trainingAddress) {
        this.trainingAddress = trainingAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
