package cn.edu.swpu.wlzx.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 算法模型实体
 * @author gongsir
 * @date 2020/3/28 14:00
 * 编码不要畏惧变化，要拥抱变化
 */
@Entity
@Table(name = "db_algorithm")
@ApiModel
@Data
public class Algorithm implements Serializable {
    /**
     * 算法id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id,自动生成",example = "1")
    private Integer id;

    /**
     * 模型名称
     */
    @Column(name = "al_name",unique = true,nullable = false)
    @ApiModelProperty(value = "模型名称")
    @NotNull(message = "名称必填")
    private String name;

    /**
     * 模型描述
     */
    @Column(name = "al_desc",nullable = false)
    @ApiModelProperty(value = "模型描述")
    private String description;

    /**
     * 数据使用说明
     */
    @Column(name = "al_data")
    @ApiModelProperty(value = "模型使用数据说明")
    private String data;

    /**
     * 模型发布状态
     */
    @Column(name = "al_status")
    @ApiModelProperty("模型发布状态：发布、未发布")
    private String status;
}
