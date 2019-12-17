package yizhit.workerlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.reactive.config.EnableWebFlux;
import yizhit.workerlib.timer.SelectQuartzAllUserInfo;
import yizhit.workerlib.timer.SelectQuartzArvhivesInfo;
import yizhit.workerlib.timer.SelectQuartzProjectInfof;
import yizhit.workerlib.timer.SelectQuartzUnitrInfo;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;


//@EnableHystrixDashboard
//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableHystrix
//@EnableZuulProxy
@EnableWebFlux
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"ccait.ccweb", "yizhit.workerlib"},
        exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class Application {

    private static final Logger log = LogManager.getLogger( Application.class );
    private static boolean uat = false;
    public static void main(String[] args) throws FileNotFoundException, MalformedURLException {

        String flag = System.getProperty("isuat");
        if("true".equals( flag )) {
            setUat( true );
        }

        else {
            setUat( false );
        }

        SpringApplication.run(Application.class, args);


        System.out.println("Workerlib has been started!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public static boolean isUat()
    {
        return uat;
    }

    private static void setUat( boolean isUat )
    {
        Application.uat = isUat;
    }
}
