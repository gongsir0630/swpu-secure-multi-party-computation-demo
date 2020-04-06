package cn.edu.swpu.wlzx.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @author gongsir
 */
@SpringBootApplication
@EntityScan("cn.edu.swpu.wlzx.consumer.model")
public class DubboConsumerApp {

    private static final Logger logger = LoggerFactory.getLogger(DubboConsumerApp.class);

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApp.class,args);
    }

}
