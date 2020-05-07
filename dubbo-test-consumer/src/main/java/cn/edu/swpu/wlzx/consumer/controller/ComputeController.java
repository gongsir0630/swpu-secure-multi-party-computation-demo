package cn.edu.swpu.wlzx.consumer.controller;

import cn.edu.swpu.wlzx.api.compute.ComputeService;
import cn.edu.swpu.wlzx.api.compute.LcService;
import cn.edu.swpu.wlzx.consumer.common.utils.Result;
import cn.edu.swpu.wlzx.consumer.model.SysRecord;
import cn.edu.swpu.wlzx.consumer.service.IRecordService;
import cn.edu.swpu.wlzx.domain.Algorithm;
import cn.edu.swpu.wxzx.keys.KeyService;
import cn.edu.swpu.wxzx.keys.UserService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongsir
 * @date 2020/4/2 17:40
 * 编码不要畏惧变化，要拥抱变化
 */
@RestController
@RequestMapping("/user/compute")
@Api(tags = "计算接口",
        value = "维护：龚涛、2020-4-2")
public class ComputeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 引入远程服务
     */
    @Reference(version = "1.0.0")
    private KeyService keyService;
    @Reference(version = "1.0.0")
    private UserService userService;
    @Reference(version = "1.0.0",group = "test1")
    private ComputeService computeService;
    @Reference(version = "1.0.0")
    private LcService lcService;

    /**
     * 引入本地服务
     */
    @Autowired
    private IRecordService recordService;

    private String getUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @ApiOperation(value = "个人授额评估模型")
    @GetMapping(path = "/test1")
    public ResponseEntity<Result> testCompute(String a, String b, String key) {
        // new 一个record记录本次计算信息
        SysRecord record = new SysRecord();
        // 查询计算模型信息
        Algorithm al = lcService.findByUrl("/test1");
        Algorithm algorithm = new Algorithm();
        if (null != al) {
            record.setAlId(al.getId());
        } else {
            record.setAlId(-1);
        }

        Result result = new Result(100,"success");
        int row = 0;
        logger.info("========== 数据封装开始 ==========");
        result.putData(String.valueOf(row += 1),"========== 数据封装开始 ==========");
        Map<String,String> params = new HashMap<>(4);
        final String username = this.getUsername();
        // 封装参数
        params.put("username",username);
        logger.info("当前用户：{}",username);
        result.putData(String.valueOf(row += 1),"当前用户："+username);
        params.put("publicKey",key);
        logger.info("当前公钥：{}",key);
        result.putData(String.valueOf(row += 1),"当前公钥："+key);
        params.put("A",a);
        logger.info("数据A：{}",a);
        result.putData(String.valueOf(row += 1),"数据A："+a);
        params.put("B",b);
        logger.info("数据B：{}",b);
        result.putData(String.valueOf(row += 1),"数据B："+b);
        logger.info("========== 数据封装结束 ==========");
        result.putData(String.valueOf(row += 1),"========== 数据封装结束 ==========");
        logger.info("========== 模型计算开始 ==========");
        result.putData(String.valueOf(row += 1),"========== 模型计算开始 ==========");
        // 计算
        String code = computeService.compute(params);
        logger.info("密文计算结果：{}",code);
        result.putData(String.valueOf(row += 1),"密文计算结果："+code);
        String rs = userService.decode(username, code, key);
        logger.info("密文解密结果：{}",rs);
        result.putData(String.valueOf(row += 1),"密文解密结果："+rs);
        logger.info("========== 模型计算结束 ==========");
        result.putData(String.valueOf(row += 1),"========== 模型计算结束 ==========");

        // 存储计算过程在平台
        // 计算发起人
        record.setUsername(username);
        // 计算所用参数
        record.setParams(JSONObject.toJSONString(params));
        // 计算密文结果,明文结果
        record.setResult("明文结果:"+rs+", 密文结果:"+code);
        // 计算时间
        record.setStartTime(new Date());
        // 存储db
        recordService.saveRecord(record);

        return ResponseEntity.ok(result);
    }
}
