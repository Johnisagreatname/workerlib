package yizhit.workerlib.excel.pojo;

import entity.query.Queryable;

public class cultivate_video extends Queryable<cultivate_video> {
    private int cultivateId;
    private String file;
    private String url;

    public int getCultivateId() {
        return cultivateId;
    }

    public void setCultivateId(int cultivateId) {
        this.cultivateId = cultivateId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
