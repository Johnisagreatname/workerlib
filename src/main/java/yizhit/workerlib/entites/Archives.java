package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;

@DataSource(value = "workerlib2", rxjava2 = false)
public class Archives extends Queryable<Archives> {
    public String getIdNumber() {
        return id_number;
    }

    public void setIdNumber(String id_number) {
        this.id_number = id_number;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String id_number;
    private String photo;
}
