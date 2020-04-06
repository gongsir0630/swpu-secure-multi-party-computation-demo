package cn.edu.swpu.wlzx.consumer.controller;

import cn.edu.swpu.wlzx.api.compute.LcService;
import cn.edu.swpu.wlzx.consumer.common.utils.MiscUtil;
import cn.edu.swpu.wlzx.consumer.common.utils.Result;
import cn.edu.swpu.wlzx.domain.Algorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 描述：
 * 浪潮方：算法模型管理（发布、删除）
 * 企业用户：查询可使用的模型，调用模型进行计算
 * @author gongsir
 * @date 2020/4/2 20:40
 * 编码不要畏惧变化，要拥抱变化
 */
@RestController
@Api(tags = "浪潮算法模型管理接口",
        value = "维护：龚涛、2020-4-2")
public class LangChaoController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 引入浪潮服务
     */
    @Reference(version = "1.0.0")
    private LcService lcService;

    @ApiOperation(value = "企业用户获取可以使用的计算模型")
    @GetMapping("/user/al")
    public ResponseEntity<Result> getListByUser(@RequestParam(value = "name",required = false,defaultValue = "") String name,
                                                @RequestParam(value = "page_num",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "page_size",defaultValue = "5") int pageSize) {
        Result result = new Result(100,"success");
        // 企业用户只能查看已经发布的模型
        result.putData("list",lcService.findAll(name,Boolean.TRUE.toString()));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "管理员对平台的模型进行管理")
    @GetMapping("/lc/al")
    public ResponseEntity<Result> getByAdmin(@RequestParam(value = "name",required = false,defaultValue = "") String name,
                                                @RequestParam(value = "status",required = false,defaultValue = "") String status,
                                                @RequestParam(value = "page_num",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "page_size",defaultValue = "5") int pageSize) {
        Result result = new Result(100,"success");
        // 企业用户只能查看已经发布的模型
        result.putData("list",lcService.findAll(name,status));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "管理员发布心得计算模型")
    @PostMapping("/lc/al")
    public ResponseEntity<Result> addAlByAdmin(@Valid Algorithm algorithm,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Result error = MiscUtil.getValidateError(bindingResult);
            assert error != null;
            return ResponseEntity.ok(error);
        }
        Algorithm al = lcService.insertAl(algorithm);
        Result result = new Result(100,"success");
        result.putData("al",al);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "管理员单个指定模型")
    @DeleteMapping("/lc/al/{id}")
    public ResponseEntity<Result> deleteAlByAdmin(@PathVariable("id") int id) {
        lcService.deleteAlById(id);
        return ResponseEntity.ok(new Result(100,"success"));
    }

    @ApiOperation(value = "管理员更新某歌计算模型的信息")
    @PutMapping("/lc/al/{id}")
    public ResponseEntity<Result> updateAlByAdmin(@PathVariable("id") int id,
                                                  Algorithm algorithm) {
        algorithm.setId(id);
        Algorithm al = lcService.updateAl(algorithm);
        if (null != al) {
            return ResponseEntity.ok(new Result(100,"success"));
        }
        return ResponseEntity.ok(new Result(101,"id不正确"));
    }
}
