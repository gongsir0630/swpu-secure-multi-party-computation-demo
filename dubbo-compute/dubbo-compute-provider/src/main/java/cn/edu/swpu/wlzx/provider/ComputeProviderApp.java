package cn.edu.swpu.wlzx.provider;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * @author gongsir
 */
@EnableAutoConfiguration
public class ComputeProviderApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ComputeProviderApp.class)
                .run(args);
    }
}