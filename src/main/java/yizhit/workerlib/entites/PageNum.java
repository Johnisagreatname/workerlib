package yizhit.workerlib.entites;

import entity.query.Queryable;
import entity.query.annotation.DataSource;
import entity.query.annotation.Fieldname;
import entity.query.annotation.Tablename;
import org.davidmoten.rx.jdbc.annotations.Column;

import java.io.Serializable;

/**
 *@ClassName PageNum
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/15  16:25
 */
@Tablename("page_num")
@DataSource(value = "workerlib2", rxjava2 = false)
public class PageNum extends Queryable<PageNum> implements Serializable {

    private int id;
    @Fieldname("page_index")
    private int pageIndex;
    @Fieldname("page_name")
    private String pageName;

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

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}