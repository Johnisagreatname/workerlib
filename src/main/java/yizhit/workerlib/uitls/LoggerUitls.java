package yizhit.workerlib.uitls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *@ClassName Logger
 *@Description TODO
 *@Author xieya
 *@Date 2020/4/7  10:34
 */
public class LoggerUitls {

    private static final Logger logger = LoggerFactory.getLogger(LoggerUitls.class);

    /**
     * @Author xieya
     * @Description
     * @Date 2020/4/7 11:33
     * @param msg
     * @param objs
     * @return java.lang.String
     **/
    public static void logInfo(String msg, Object... objs) {
        StringBuffer sb = new StringBuffer();
        sb.append(msg);
        int i = 0;
        for (Object obj : objs) {
            if (obj == null) {
                logger.info(msg);
            }
            sb.append(",");
            sb.append("参数" + (i + 1) + "=" + obj);
            i++;
        }
        logger.info(sb.toString());
    }

    public static void main(String[] args) {
        logInfo("haha", 123, 456, 789);
    }
}