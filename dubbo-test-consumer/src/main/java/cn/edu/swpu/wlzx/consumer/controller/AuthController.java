package cn.edu.swpu.wlzx.consumer.controller;

import cn.edu.swpu.wlzx.consumer.common.auth.service.AuthService;
import cn.edu.swpu.wlzx.consumer.common.utils.MiscUtil;
import cn.edu.swpu.wlzx.consumer.common.utils.Result;
import io.swagger.annotations.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author gongsir
 * @date 2020/3/21 20:51
 * 编码不要畏惧变化，要拥抱变化
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "平台安全认证",value = "维护：龚涛、2020-3-21")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    @ApiOperation(value = "登录认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account",value = "用户账号"),
            @ApiImplicitParam(name = "password",value = "用户密码")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "login success"),
            @ApiResponse(code = 422,message = "输入有误"),
            @ApiResponse(code = 401,message = "Unauthorized or invalid token")
    })
    public ResponseEntity<Result> login(@Valid Login login,
                                        BindingResult bindingResult) {
        // 输入验证
        if (bindingResult.hasErrors()) {
            Result result = MiscUtil.getValidateError(bindingResult);
            Assert.notNull(result,"字段信息输入有误");
            return ResponseEntity.ok(result);
        }
        logger.info("=====>请求登录，登录信息：{}",login);

        // 生成token
        final String token = authService.login(login.getAccount(), login.getPassword());
        Result result = new Result(100,"login success");
        result.putData("token",token);
        return ResponseEntity.ok(result);
    }
}

@Data
class Login implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "账号必填")
    private String account;

    @NotNull(message = "密码必填")
    @Size(min = 6,max = 16,message = "密码6-16位")
    private String password;
}
