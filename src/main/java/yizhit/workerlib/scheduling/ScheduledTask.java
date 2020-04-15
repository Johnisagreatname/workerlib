package yizhit.workerlib.scheduling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import yizhit.workerlib.timer.InsertArchives;
import yizhit.workerlib.timer.InsertProject;

/**
 *@ClassName ScheduledTask
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/14  16:34
 */
@Component
public class ScheduledTask {

    private static final Logger logger = LogManager.getLogger(ScheduledTask.class);

    /**
     * @Author xieya
     * @Description
     * @Date 2020/4/14 20:30
     * @param
     * @return void
     **/
//    @Scheduled(cron = "0/5 * * * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void InsertArchives() {
        logger.info("请求工程接口进来了");
        try {
            InsertArchives insertArchives = new InsertArchives();
            insertArchives.archives(1, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void InsertProject() {
        logger.info("请求项目进来了");
        try {
            InsertProject insertProject = new InsertProject();
            insertProject.project(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}