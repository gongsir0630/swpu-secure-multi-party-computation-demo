package cn.edu.swpu.wlzx.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author gongsir
 * 临时关闭DataSource的自动化配置
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DubboConsumerApp {

    private static final Logger logger = LoggerFactory.getLogger(DubboConsumerApp.class);

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApp.class,args);
    }

}
