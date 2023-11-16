package com.project.appointment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import cn.hutool.core.annotation.Alias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

/**
* <p>
* 
* </p>
*
*
* @since 2023-11-16
*/
@Getter
@Setter
@ApiModel(value = "Role对象", description = "")
public class Role implements Serializable {

private static final long serialVersionUID = 1L;

    // 序号
    @ApiModelProperty("序号")
    @Alias("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 标识
    @ApiModelProperty("标识")
    @Alias("标识")
    private String type;

    // 名称
    @ApiModelProperty("名称")
    @Alias("名称")
    private String name;
}