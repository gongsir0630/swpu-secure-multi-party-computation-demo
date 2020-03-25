package cn.edu.swpu.wlzx.consumer.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gongsir
 * @date 2020/3/21 16:21
 * 编码不要畏惧变化，要拥抱变化
 */
@RestController
@Api(tags = "密钥相关接口",value = "维护：龚涛、2020-3-21")
public class KeyController {
    private static final Logger logger = LoggerFactory.getLogger(KeyController.class);

    /**
     * rpc调用 密钥 服务
     */
//    @Reference
//    private UserService userService;
//
//    @GetMapping("/key/{username}")
//    public ResponseEntity<Result> getPublicKey(@PathVariable("username") String username) {
//        String publicKey = userService.insert(username);
//        logger.info("=====>> 用户:{} 获取公钥:{}",username,publicKey);
//        Result result = new Result(100,"success");
//        result.putData("public_key",publicKey);
//        return ResponseEntity.ok(result);
//    }
}
