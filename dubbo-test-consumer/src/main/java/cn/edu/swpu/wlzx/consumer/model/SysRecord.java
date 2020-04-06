package cn.edu.swpu.wlzx.consumer.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 平台计算记录
 * @author gongsir
 * @date 2020/4/5 10:17
 * 编码不要畏惧变化，要拥抱变化
 */
@Data
@Entity
@Table(name = "db_consumer")
public class SysRecord implements Serializable {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 计算发起人
     */
    @Column(name = "username",nullable = false)
    private String username;

    /**
     * 计算模型id
     */
    @Column(name = "al_id",nullable = false)
    private Integer alId;

    /**
     * 计算参数
     */
    @Column(name = "params",columnDefinition = "text")
    private String params;

    /**
     * 计算结果
     */
    @Column(name = "result",columnDefinition = "text")
    private String result;

    /**
     * 计算时间
     */
    @Column(name = "start_time")
    private Date startTime;
}
