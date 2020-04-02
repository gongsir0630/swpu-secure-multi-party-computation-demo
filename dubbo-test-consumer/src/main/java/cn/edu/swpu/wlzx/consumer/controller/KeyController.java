package cn.edu.swpu.wlzx.consumer.controller;

import cn.edu.swpu.wlzx.consumer.common.utils.Result;
import cn.edu.swpu.wxzx.keys.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Reference(version = "1.0.0")
    private UserService userService;

    /**
     * 从认证信息中获取username
     * @return username
     */
    private String  getUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @ApiOperation(value = "用户获取数据加密的公钥")
    @GetMapping("user/key/")
    public ResponseEntity<Result> getPublicKey() {
        final String username = this.getUsername();
        String publicKey = userService.insert(username);
        logger.info("=====>> 用户:{} 获取公钥:{}",username,publicKey);
        Result result = new Result(100,"success");
        result.putData("public_key",publicKey);
        return ResponseEntity.ok(result);
    }
}
