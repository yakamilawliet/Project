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
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

private static final long serialVersionUID = 1L;

    // 序号
    @ApiModelProperty("序号")
    @Alias("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 用户名
    @ApiModelProperty("用户名")
    @Alias("用户名")
    private String username;

    // 角色
    @ApiModelProperty("角色")
    @Alias("角色")
    private String role;

    // 手机号
    @ApiModelProperty("手机号")
    @Alias("手机号")
    private String phoneNumber;

    // 用户唯一标识
    @ApiModelProperty("用户唯一标识")
    @Alias("用户唯一标识")
    private String uid;

    // 微信唯一标识
    @ApiModelProperty("微信唯一标识")
    @Alias("微信唯一标识")
    private String openid;
}