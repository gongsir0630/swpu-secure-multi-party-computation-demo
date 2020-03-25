package cn.edu.swpu.wlzx.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author gongsir
 * @date 2020/3/21 14:30
 * 编码不要畏惧变化，要拥抱变化
 */
@Data
@Entity
@Table(name = "db_user")
@ApiModel
public class User implements Serializable {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id,自动生成",example = "1")
    private Integer id;

    /**
     * 用户账号
     */
    @Column(name = "username",unique = true,nullable = false)
    @ApiModelProperty(value = "用户账号")
    @NotNull(message = "账号必填")
    private String username;

    /**
     * 用户密码
     */
    @Column(name = "password",nullable = false)
    @ApiModelProperty(value = "密码：6-16未字符组成")
    @NotNull(message = "密码必填")
    @Size(min = 6,max = 16)
    private String password;

    /**
     * 手机号码
     */
    @Column(name = "phone")
    @ApiModelProperty(value = "大陆手机号（选填）")
    private String phone;

    /**
     * 电子邮箱
     */
    @Column(name = "mail")
    @ApiModelProperty(value = "个人邮箱：用于找回密码和注册")
    @NotNull(message = "邮箱必填")
    private String mail;

    /**
     * 姓名
     */
    @Column(name = "real_name")
    @ApiModelProperty(value = "用户姓名")
    @NotNull(message = "姓名必填")
    private String realName;

    /**
     * 角色类型
     */
    @Column(name = "role",nullable = false)
    @ApiModelProperty(value = "无需填写")
    private String role;

    /**
     * 账号状态
     */
    @Column(name = "login_status")
    @ApiModelProperty(value = "无需填写")
    private String loginStatus;
}
