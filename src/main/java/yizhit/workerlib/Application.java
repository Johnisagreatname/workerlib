package yizhit.workerlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

@EnableWebFlux
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"yizhit.workerlib", "ccait.ccweb"},
        exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

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

        log.info("Workerlib has been started!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public static boolean isUat()
    {
        return uat;
    }

    private static void setUat( boolean isUat )
    {
        Application.uat = isUat;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(Application.class);
    }
}
