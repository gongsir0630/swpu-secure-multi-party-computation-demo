package cn.edu.swpu.wlzx.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author gongsir
 * @date 2020/3/21 14:30
 * 编码不要畏惧变化，要拥抱变化
 */
@Data
@Entity
@Table(name = "db_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username",unique = true,nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "phone")
    private String phone;

    private String mail;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "role",nullable = false)
    private String role;

    @Column(name = "login_status")
    private String loginStatus;
}
