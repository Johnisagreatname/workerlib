package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;

import java.io.Serializable;

/**
 *@ClassName PageNum
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/15  16:25
 */
@DataSource(value = "workerlib2", rxjava2 = false)
public class PageNum extends Queryable<PageNum> implements Serializable {

    private int id;
    private int pageIndex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}