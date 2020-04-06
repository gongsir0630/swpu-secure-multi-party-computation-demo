package cn.edu.swpu.wlzx.consumer.controller;

import cn.edu.swpu.wlzx.consumer.common.utils.Result;
import cn.edu.swpu.wxzx.domain.Data;
import cn.edu.swpu.wxzx.domain.User;
import cn.edu.swpu.wxzx.gov.DataService;
import cn.edu.swpu.wxzx.gov.GovUserService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：
 *  接口调用者：政府管理人员 一级域：/gov 需要有政府管理员身份才能调用/gov下的所有接口
 *  注意：
 *      不单独区分 政府A 和 政府B 的管理员身份，政府管理员可以直管理多方数据源的使用
 *      数据源区分采用动态参数选择某一请求应该操作的数据源
 *      例如：
 *      某一次请求的url为： /gov/a/get 表示查看数据源a的数据
 *      某一次请求的url为： /gov/b/get 表示查看数据源b的数据
 *      所有接口按此逻辑实现，避免代码冗余
 *  接口：
 *      1、查看所有用户申请（一个用户至少含有2条申请a、b）
 *      2、审核审批
 *      3、政府数据管理（增删改查）
 *  接口返回：
 *      1、所有接口返回采用ResponseEntity<Result>封装返回
 *      2、所有接口和接口类需加swagger注解说明接口信息
 * @author gongsir
 * @date 2020/3/28 14:32
 * 编码不要畏惧变化，要拥抱变化
 */
@RestController
@Api(tags = "政府数据管理接口",value = "维护：曾文杰 2020-03-28")
public class GovController {

    private static final Logger logger = LoggerFactory.getLogger(GovController.class);

    @Reference(version = "1.0.0",group = "govA")
    private GovUserService govAUserService;

    @Reference(version = "1.0.0",group = "govB")
    private GovUserService govBUserService;

    @Reference(version = "1.0.0",group = "govA")
    private DataService dataAService;

    @Reference(version = "1.0.0",group = "govB")
    private DataService dataBService;

    private String getUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    /**
     * 用户申请使用数据
     * @return 申请状态
     */
    @ApiOperation(value = "用户申请使用数据",notes = "用户申请，政府审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gov",value = "政府源区分")
    })
    @ApiResponses({
            @ApiResponse(code = 105,message = "已审核"),
            @ApiResponse(code = 104,message = "审核中"),
            @ApiResponse(code = 103,message = "审核失败")
    })
    @PostMapping("/user/{gov}/insertApply")
    public ResponseEntity<Result> insertApply(@PathVariable("gov") String gov){
        String govA="a",govB="b";
        String username=this.getUsername();
        if (gov.equals(govA)){
            if (govAUserService.insertApply(username)==1){
                logger.info("=====>申请已审核!!!");
                return ResponseEntity.ok(new Result(105,"已审核"));
            }else {
                logger.info("=====>申请正在审核!!!");
                return ResponseEntity.ok(new Result(104,"审核中"));
            }
        }
        if (gov.equals(govB)){
            if (govBUserService.insertApply(username)==0){
                logger.info("=====>申请正在审核!!!");
                return ResponseEntity.ok(new Result(104,"审核中"));
            }else {
                logger.info("=====>申请已审核!!!");
                return ResponseEntity.ok(new Result(105,"已审核"));
            }
        }
        return ResponseEntity.ok(new Result(103,"审核失败"));
    }

    /**
     * 删除用户申请
     * @return 删除状态
     */
    @ApiOperation(value = "政府删除用户申请",notes = "政府审核，删除申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gov",value = "政府源区分"),
            @ApiImplicitParam(name = "id",value = "用户申请id")
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "已删除"),
            @ApiResponse(code = 101,message = "删除失败")
    })
    @DeleteMapping("/gov/{gov}/deleteApply/{id}")
    public ResponseEntity<Result> deleteApply(@PathVariable("gov") String gov,@PathVariable("id") int id){
        String govA="a",govB="b";
        if (gov.equals(govA)){
            govAUserService.deleteApply(id);
            logger.info("=====>删除成功!!!");
            return ResponseEntity.ok(new Result(100,"已删除"));
        }
        if (gov.equals(govB)){
            govBUserService.deleteApply(id);
            logger.info("=====>删除成功!!!");
            return ResponseEntity.ok(new Result(100,"已删除"));
        }
        return ResponseEntity.ok(new Result(101,"删除失败"));
    }

    /**
     * 用户获取数据
     * @return 数据密文
     */
    @ApiOperation(value = "用户获取数据",notes = "用户获取密文数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gov",value = "政府源区分"),
            @ApiImplicitParam(name = "publicKey",value = "公钥")
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "获取成功"),
            @ApiResponse(code = 101,message = "获取失败")
    })
    @PostMapping("/user/{gov}/applyData/{publicKey}")
    public ResponseEntity<Result> applyData(@PathVariable("gov") String gov,@PathVariable("publicKey") String publicKey){
        String govA="a",govB="b";
        String username=this.getUsername();
        if (gov.equals(govA)){
            Result result=new Result(100,"数据获取成功");
            result.putData("dataA",govAUserService.applyData(username,publicKey));
            return ResponseEntity.ok(result);
        }
        if (gov.equals(govB)){
            Result result=new Result(100,"数据获取成功");
            result.putData("dataB",govBUserService.applyData(username,publicKey));
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(new Result(101,"数据获取失败"));
    }

    /**
     *  查询所有用户审批
     * @return 用户信息以及查询结果
     */
    @ApiOperation(value = "查询用户信息",notes ="用户的申请状态以及数据使用情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据")
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "查询成功"),
            @ApiResponse(code = 101,message = "查询失败")
    })
    @GetMapping(value = "/gov/allUserApply")
    public  ResponseEntity<Result> selectAllApply(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        List<User> userList1=govAUserService.selectAllApply(page,pageSize);
        List<User> userList2=govBUserService.selectAllApply(page,pageSize);
        Result result=new Result(100,"查询成功");
        logger.info("=====>查询成功");
        logger.info("=====>userA:{}",userList1);
        logger.info("=====>userB:{}",userList2);
        result.putData("userA",userList1);
        result.putData("userB",userList2);
        return ResponseEntity.ok(result);
    }

    /**
     * 审核用户的对数据源A,B的申请
     * @return 审核结果
     */
    @ApiOperation(value = "审核申请",notes = "审核用户的对数据A的申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gov",value = "政府源区分"),
            @ApiImplicitParam(name = "id",value = "用户id")
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "审核成功"),
            @ApiResponse(code = 101,message = "审核失败")
    })
    @PutMapping(value = "/gov/{gov}/checkApply/{id}")
    public ResponseEntity<Result> reviewApplyA(@PathVariable("gov") String gov ,@PathVariable("id") int id){
        String govA="a",govB="b";
        if (gov.equals(govA)){
            if(govAUserService.updateApply(id)){
                logger.info("=====>数据A审核成功");
                return ResponseEntity.ok(new Result(100,"审核成功"));
            }
        }
        if (gov.equals(govB)){
            if(govBUserService.updateApply(id)){
                logger.info("=====>数据B审核成功");
                return ResponseEntity.ok(new Result(100,"审核成功"));
            }
        }
        return ResponseEntity.ok(new Result(101,"审核失败"));
    }

    /**
     * 增加数据A,B
     * @return 插入结果
     */
    @ApiOperation(value = "增加数据",notes = "在数据源A,B增加数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gov",value = "数据源区分"),
            @ApiImplicitParam(name = "data",value = "数据")
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "插入成功"),
            @ApiResponse(code = 101,message = "插入失败")
    })
    @PostMapping("/gov/{gov}/insertData")
    public ResponseEntity<Result> insertDataA(@PathVariable("gov") String gov,@RequestParam("data") String data){
        String govA="a",govB="b";
        if (gov.equals(govA)){
            if (dataAService.insertData(data)){
                logger.info("=====>数据A插入成功");
                return ResponseEntity.ok(new Result(100,"插入成功"));
            }
        }
        if (gov.equals(govB)){
            if (dataBService.insertData(data)){
                logger.info("=====>数据B插入成功");
                return ResponseEntity.ok(new Result(100,"插入成功"));
            }
        }
        return ResponseEntity.ok(new Result(101,"插入失败"));
    }

    /**
     * 删除数据A,B
     * @param id
     * @return 删除结果
     */
    @ApiOperation(value = "删除数据",notes = "在数据源A,B删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gov",value = "数据源区分"),
            @ApiImplicitParam(name = "id",value = "数据主键id")
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "删除成功"),
            @ApiResponse(code = 101,message = "删除失败")
    })
    @DeleteMapping(value = "/gov/{gov}/deleteData")
    public ResponseEntity<Result> deleteDataA(@PathVariable("gov") String gov,@RequestParam("id") int id){
        String govA="a",govB="b";
        if (gov.equals(govA)) {
            if (dataAService.deleteData(id)){
                logger.info("=====>数据A删除成功");
                return ResponseEntity.ok(new Result(100,"删除成功"));
            }
        }
        if (gov.equals(govB)) {
            if (dataBService.deleteData(id)){
                logger.info("=====>数据B删除成功");
                return ResponseEntity.ok(new Result(100,"删除成功"));
            }
        }
        return ResponseEntity.ok(new Result(101,"删除失败"));
    }

    /**
     * 修改数据A,B
     * @param data
     * @return 修改结果
     */
    @ApiOperation(value = "修改数据",notes = "在数据源A,B修改数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gov",value = "数据源区分"),
            @ApiImplicitParam(name = "data",value = "数据实体")
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "修改成功"),
            @ApiResponse(code = 101,message = "修改失败")
    })
    @PutMapping(value = "/gov/{gov}/updateData")
    public ResponseEntity<Result> updateDataA(@PathVariable("gov") String gov,Data data){
        String govA="a",govB="b";
        if (gov.equals(govA)){
            if (dataAService.updateData(data)){
                logger.info("=====>数据A修改成功");
                return ResponseEntity.ok(new Result(100,"修改成功"));
            }
        }
        if (gov.equals(govB)){
            if (dataBService.updateData(data)){
                logger.info("=====>数据B修改成功");
                return ResponseEntity.ok(new Result(100,"修改成功"));
            }
        }
        return ResponseEntity.ok(new Result(101,"修改失败"));
    }

    /**
     * 查询所有数据A,B
     * @return 查询结果
     */
    @ApiOperation(value = "查询数据",notes = "分页查询数据A,B")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gov",value = "数据源区分"),
            @ApiImplicitParam(name="page",value = "页数"),
            @ApiImplicitParam(name = "pageSize",value = "每页数据")
    })
    @ApiResponses({
            @ApiResponse(code = 100,message = "查询成功"),
            @ApiResponse(code = 101,message = "查询失败")
    })
    @GetMapping(value = "/gov/{gov}/selectAllData")
    public ResponseEntity<Result> selectAllDataA(@PathVariable("gov") String gov,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        String govA="a",govB="b";
        if (gov.equals(govA)){
            List<Data> dataList=dataAService.selectAllData(page,pageSize);
            Result result=new Result(100,"查询成功");
            result.putData("dataA",dataList);
            logger.info("=====>查询数据A成功:{}",JSON.toJSONString(dataList));
            return ResponseEntity.ok(result);
        }
        if (gov.equals(govB)){
            List<Data> dataList=dataBService.selectAllData(page,pageSize);
            Result result=new Result(100,"查询成功");
            result.putData("dataB",dataList);
            logger.info("=====>查询数据B成功:{}",JSON.toJSONString(dataList));
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(new Result(101,"查询失败"));
    }
}
