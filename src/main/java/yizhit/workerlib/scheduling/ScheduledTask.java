package yizhit.workerlib.scheduling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import yizhit.workerlib.timer.InsertArchives;
import yizhit.workerlib.timer.InsertProject;
import yizhit.workerlib.timer.InsertUser;

/**
 *@ClassName ScheduledTask
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/14  16:34
 */
@Component
public class ScheduledTask {

    private static final Logger logger = LogManager.getLogger(ScheduledTask.class);

    @Value("${entity.security.encrypt.MD5.publicKey:ccait}")
    private String md5PublicKey;

    //图片地址
    @Value("${entity.upload.workerlib.people.code.path}")
    private String qrCodePath;

    @Value("${qrcode.width}")
    private int width;

    @Value("${qrcode.height}")
    private int height;

    @Value("${entity.security.encrypt.AES.publicKey:ccait}")
    private String aesPublicKey;

    //IP
    @Value("${qrcode.server}")
    private String server;

    @Value("${enableTasks:false}")
    private Boolean enableTasks;

    /**
     * @Author xieya
     * @Description
     * @Date 2020/4/14 20:30
     * @param
     * @return void
     **/
//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 2 * * ?")
    public void InsertArchives() {
        logger.info("请求工程接口进来了");
        try {
            InsertArchives insertArchives = new InsertArchives();
            insertArchives.archives(1, 0, enableTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 1 * * ?")
    public void InsertProject() {
        logger.info("请求项目进来了");
        try {
            InsertProject insertProject = new InsertProject();
            insertProject.project(1, 0, enableTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 3 * * ?")
    public void InsertUser() {
        logger.info("请求项目下的user进来了");
        try {
            InsertUser insertUser = new InsertUser();
            insertUser.user(md5PublicKey, aesPublicKey, qrCodePath, width, height, server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}