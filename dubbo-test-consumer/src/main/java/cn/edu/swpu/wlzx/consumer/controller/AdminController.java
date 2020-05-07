package cn.edu.swpu.wlzx.consumer.controller;

import cn.edu.swpu.wlzx.api.user.UserService;
import cn.edu.swpu.wlzx.consumer.common.utils.Result;
import cn.edu.swpu.wlzx.consumer.model.SysRecord;
import cn.edu.swpu.wlzx.consumer.service.IRecordService;
import cn.edu.swpu.wlzx.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 平台管理相关接口：各方用户管理、平台使用记录管理
 * @author gongsir
 * @date 2020/4/5 18:05
 * 编码不要畏惧变化，要拥抱变化
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "平台管理接口",
        value = "维护：龚涛、2020-4-5")
public class AdminController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 本地服务
     */
    @Autowired
    private IRecordService recordService;

    /**
     * 远程服务
     */
    @Reference(version = "1.0.0")
    private UserService userService;

    @Reference(version = "1.0.0")
    private cn.edu.swpu.wxzx.keys.UserService keyService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @GetMapping(path = "/code2Data")
    @ApiOperation(value = "实时查看密文解密结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户账号"),
            @ApiImplicitParam(name = "key",value = "当次秘钥"),
            @ApiImplicitParam(name = "code",value = "密文code"),
    })
    public String code2Data(String username,String key, String code) {
        return "明文数据:" + keyService.decode(username,code,key);
    }

    @GetMapping(path = "record/list")
    @ApiOperation(value = "平台管理员查看整个平台的数据和模型使用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_num",value = "页码，默认1"),
            @ApiImplicitParam(name = "page_size",value = "每页数量，默认5"),
            @ApiImplicitParam(name = "username",value = "用户账号（条件查询，可以为空，默认显示所有）"),
    })
    public ResponseEntity<Result> getAllRecords(@RequestParam(value = "page_num",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "page_size",defaultValue = "5") int pageSize,
                                                @RequestParam(value = "username",defaultValue = "") String username) {
        Page<SysRecord> allRecord = recordService.findAllRecord(pageNum-1, pageSize, username);
        Result result = new Result(100,"success");
        result.putData("records",allRecord);
        logger.info("查询平台数据和模型使用记录，查询到{}条记录",allRecord.getNumber());
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "user/list")
    @ApiOperation(value = "平台管理员查看整个平台用户注册信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_num",value = "页码，默认1"),
            @ApiImplicitParam(name = "page_size",value = "每页数量，默认5"),
            @ApiImplicitParam(name = "username",value = "用户账号（条件查询，可以为空，默认显示所有）"),
    })
    public ResponseEntity<Result> getAllUsers(@RequestParam(value = "page_num",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "page_size",defaultValue = "5") int pageSize,
                                              @RequestParam(value = "username",defaultValue = "") String username) {
        Page<User> allUser = userService.findAllUser(pageNum-1, pageSize, username);
        Result result = new Result(100,"success");
        result.putData("users",allUser);
        logger.info("获取用户列表，查询到{}条记录",allUser.getNumber());
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "user")
    @ApiOperation(value = "管理员后台添加用户")
    public ResponseEntity<Result> addUserByAdmin(User user) {
        user.setId(null);
        // 密码处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUser = userService.saveUser(user);
        Result result = new Result(100,"用户添加成功");
        result.putData("user",saveUser);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "user/{id}")
    @ApiOperation(value = "管理员后台删除用户")
    public ResponseEntity<Result> deleteUserByAdmin(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new Result(100,"用户删除成功"));
    }

    @PutMapping(path = "user")
    @ApiOperation(value = "管理员后台更新用户")
    public ResponseEntity<Result> updateUserByAdmin(User user) {
        User updateUser = userService.updateUser(user);
        Result result = new Result(100,"用户更新成功");
        result.putData("user",updateUser);
        return ResponseEntity.ok(result);
    }
}
