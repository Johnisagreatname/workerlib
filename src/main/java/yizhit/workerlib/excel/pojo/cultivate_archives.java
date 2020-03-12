package yizhit.workerlib.excel.pojo;

import entity.query.Queryable;
import entity.query.annotation.Fieldname;

import java.util.Date;

public class cultivate_archives extends Queryable<cultivate_archives> {
    private String archivesStatus;
    @Fieldname("cultivate_id")
    private int cultivate_id;
    @Fieldname("archives_id")
    private String archives_id;
    private Date createOn;
    private int createBy;
    private int whether;

    public String getArchivesStatus() {
        return archivesStatus;
    }

    public void setArchivesStatus(String archivesStatus) {
        this.archivesStatus = archivesStatus;
    }

    public int getCultivate_id() {
        return cultivate_id;
    }

    public void setCultivate_id(int cultivate_id) {
        this.cultivate_id = cultivate_id;
    }

    public String getArchives_id() {
        return archives_id;
    }

    public void setArchives_id(String archives_id) {
        this.archives_id = archives_id;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public int getWhether() {
        return whether;
    }

    public void setWhether(int whether) {
        this.whether = whether;
    }
}
