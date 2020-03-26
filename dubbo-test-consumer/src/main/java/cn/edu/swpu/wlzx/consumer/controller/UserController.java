package cn.edu.swpu.wlzx.consumer.controller;

import cn.edu.swpu.wlzx.api.user.DemoService;
import cn.edu.swpu.wlzx.api.user.UserService;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gongsir
 * @date 2020/3/21 15:06
 * 编码不要畏惧变化，要拥抱变化
 */
@RestController
@Api(tags = "用户信息管理接口",
        value = "维护：龚涛、2020-3-21",
        hidden = true)
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Reference(version = "1.0.0")
    private DemoService demoService;

    @Reference(version = "1.0.0")
    private UserService userService;

    @GetMapping("/test/sayHello")
    public String sayHello() {
        logger.info(demoService.sayHello("gongsir"));
        return demoService.sayHello("gongsir");
    }
}
