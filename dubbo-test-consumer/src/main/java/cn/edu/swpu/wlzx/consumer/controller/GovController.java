package cn.edu.swpu.wlzx.consumer.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 *      2、申请审批
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
@Api
public class GovController {
}
