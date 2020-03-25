package cn.edu.swpu.wlzx.consumer.controller;

import cn.edu.swpu.wlzx.api.UserService;
import cn.edu.swpu.wlzx.consumer.mail.MailService;
import cn.edu.swpu.wlzx.consumer.common.auth.service.AuthService;
import cn.edu.swpu.wlzx.consumer.common.utils.IpAddrUtil;
import cn.edu.swpu.wlzx.consumer.common.utils.MiscUtil;
import cn.edu.swpu.wlzx.consumer.common.utils.Result;
import cn.edu.swpu.wlzx.domain.User;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import lombok.Data;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @Reference(version = "1.0.0")
    private UserService userService;
    @Resource
    private MailService mailService;
    @Autowired
    private StringRedisTemplate redisTemplate;

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
                                        BindingResult bindingResult,
                                        @RequestParam(value = "role",defaultValue = "user") String role) {
        // 输入验证
        if (bindingResult.hasErrors()) {
            Result result = MiscUtil.getValidateError(bindingResult);
            Assert.notNull(result,"字段信息输入有误");
            return ResponseEntity.ok(result);
        }
        logger.info("=====>请求登录，登录信息：{}, 角色：{}",login,role);
        // 查询用户登录信息
        User user = userService.findUserByUsername(login.getAccount());
        if (null == user) {
            return ResponseEntity.ok(
                    new Result(101,"用户名错误")
            );
        }
        if (!role.equals(user.getRole())) {
            return ResponseEntity.ok(new Result(102,"没有该角色权限"));
        }

        // 生成token
        final String token = authService.login(login.getAccount(), login.getPassword());
        Result result = new Result(100,"login success");
        result.putData("token",token);
        logger.info("=====>登录成功:{}", JSON.toJSONString(result));
        return ResponseEntity.ok(result);
    }

    /**
     * 验证用户名是否存在
     * @param username 用户名
     * @return 存在状态
     */
    @ApiOperation(value = "验证用户是否已经存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account",value = "用户账号")
    })
    @ApiResponses({
            @ApiResponse(code = 101,message = "该账号已被注册"),
            @ApiResponse(code = 100,message = "该账号可以注册")
    })
    @GetMapping(path = "check/{account}")
    public ResponseEntity<Result> checkUsername(@PathVariable("account") String username) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(new Result(101,"该账号已被注册"));
        }
        return ResponseEntity.ok(new Result(100,"该账号可以注册"));
    }

    /**
     * 发送短信验证码
     * @param mail 邮箱
     * @param request 请求信息
     * @return 验证码发送状态
     */
    @ApiOperation(value = "获取邮箱验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mail", value = "邮箱号码"),
            @ApiImplicitParam(name = "account",value = "用户注册时填写的账号",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "验证码发送状态：success表示发送成功，其他msg均为失败")
    })
    @PostMapping(path = "code")
    public ResponseEntity<Result> getAuthCode(@RequestParam("mail") String mail,
                                              HttpServletRequest request) {
        String ip = IpAddrUtil.getIpAddress(request);
        logger.info("=====>>验证码请求ip地址：{}",ip);
        Boolean flag = mailService.sendAuthCode(mail);
        if (flag) {
            return ResponseEntity.ok(new Result(100,"send code success"));
        }
        return ResponseEntity.ok(new Result(101,"send code failure"));
    }

    @ApiOperation(value = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "验证码"),
            @ApiImplicitParam(name = "account",value = "用户注册时填写的账号",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 101,message = "验证码错误"),
            @ApiResponse(code = 102,message = "账号已存在"),
            @ApiResponse(code = 100,message = "注册成功")
    })
    @PostMapping(path = "register")
    public ResponseEntity<Result> registerUser(@Valid User user,
                                               BindingResult bindingResult,
                                               @RequestParam("code") String code) {
        // 验证码校验
        if (!code.equalsIgnoreCase(redisTemplate.opsForValue().get(user.getMail()))) {
            return ResponseEntity.ok(new Result(101,"验证码错误"));
        }
        if (bindingResult.hasErrors()) {
            Result res = MiscUtil.getValidateError(bindingResult);
            assert res != null;
            return ResponseEntity.ok(res);
        }
        // 验证账户是否存在
        if (userService.findUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.ok(new Result(102,"账号已存在"));
        }
        // 账户不存在，进入注册
        // 授权登录
        user.setLoginStatus("true");
        // 角色
        user.setRole("user");
        // 存库
        User u = userService.saveUser(user);
        Result result = new Result(100,"register success");
        result.putData("user",u);
        logger.info("=====>> 用户注册成功：{}",u);
        return ResponseEntity.ok(result);
    }
}

/**
 * 登录信息类
 */
@Data
class Login implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "账号必填")
    private String account;

    @NotNull(message = "密码必填")
    @Size(min = 6,max = 16,message = "密码6-16位")
    private String password;
}
