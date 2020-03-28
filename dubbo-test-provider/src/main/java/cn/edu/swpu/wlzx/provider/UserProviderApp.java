package cn.edu.swpu.wlzx.provider;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * user服务提供者
 * @author gongsir
 */
@EnableAutoConfiguration
@EntityScan("cn.edu.swpu.wlzx.domain")
public class UserProviderApp {

    /**
     * 以服务的方式启动，不占用web端口
     * @param args 命令参数
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(UserProviderApp.class)
                .run(args);
    }
}