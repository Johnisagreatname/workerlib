package yizhit.workerlib.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Test {

    @Scheduled(cron="* * * * * ?")
    public void testtt() {
        System.out.print("\r\n谢亚是傻逼！！！");
    }
}
