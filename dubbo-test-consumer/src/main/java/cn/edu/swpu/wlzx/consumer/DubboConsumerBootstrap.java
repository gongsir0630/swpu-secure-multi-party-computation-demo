package cn.edu.swpu.wlzx.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DubboConsumerBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(DubboConsumerBootstrap.class);

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerBootstrap.class,args);
    }

}
