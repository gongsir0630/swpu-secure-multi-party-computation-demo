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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
@RequestMapping("/gov")
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
    @RequestMapping(value = "/allUser",method = RequestMethod.GET)
    public  ResponseEntity<Result> selectAllApply(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        List<User> userList1=govAUserService.selectAllApply(page,pageSize);
        List<User> userList2=govBUserService.selectAllApply(page,pageSize);
        if (userList1.isEmpty()&&userList2.isEmpty()){
            logger.info("=====>查询失败");
            return ResponseEntity.ok(new Result(101,"查询失败"));
        }
        Result result=new Result(100,"查询成功");
        logger.info("=====>查询成功");
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
    @RequestMapping(value = "/{gov}/checkApply/{id}",method = RequestMethod.PUT)
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
    @PostMapping("/{gov}/insert")
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
    @RequestMapping(value = "/{gov}/delete",method = RequestMethod.DELETE)
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
    @RequestMapping(value = "/{gov}/update",method = RequestMethod.PUT)
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
    @RequestMapping(value = "/{gov}/selectAll",method = RequestMethod.GET)
    public ResponseEntity<Result> selectAllDataA(@PathVariable("gov") String gov,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        String govA="a",govB="b";
        if (gov.equals(govA)){
            List<Data> dataList=dataAService.selectAllData(page,pageSize);
            if (dataList.isEmpty()){
                logger.info("=====>查询数据A失败");
                return ResponseEntity.ok(new Result(101,"查询失败"));
            }
            Result result=new Result(100,"查询成功");
            result.putData("dataA",dataList);
            logger.info("=====>查询数据A成功",JSON.toJSONString(dataList));
            return ResponseEntity.ok(result);
        }
        if (gov.equals(govB)){
            List<Data> dataList=dataBService.selectAllData(page,pageSize);
            if (dataList.isEmpty()){
                logger.info("=====>查询数据B失败");
                return ResponseEntity.ok(new Result(101,"查询失败"));
            }
            Result result=new Result(100,"查询成功");
            result.putData("dataB",dataList);
            logger.info("=====>查询数据B成功",JSON.toJSONString(dataList));
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(new Result(101,"查询失败"));
    }
}
