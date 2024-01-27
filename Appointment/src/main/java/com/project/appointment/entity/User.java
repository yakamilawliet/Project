package com.project.appointment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
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
* @since 2024-01-27
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

    // 昵称
    @ApiModelProperty("昵称")
    @Alias("昵称")
    private String username;

    // 角色
    @ApiModelProperty("角色")
    @Alias("角色")
    private String role;

    // 手机号
    @ApiModelProperty("手机号")
    @Alias("手机号")
    private String phoneNumber;

    // 微信小程序openid，每个用户对应一个，且唯一
    @ApiModelProperty("微信小程序openid，每个用户对应一个，且唯一")
    @Alias("微信小程序openid，每个用户对应一个，且唯一")
    private String openid;

    // 用户唯一标识
    @ApiModelProperty("用户唯一标识")
    @Alias("用户唯一标识")
    private String uid;

    // 用户头像
    @ApiModelProperty("用户头像")
    @Alias("用户头像")
    private String avatarUrl;

    // 用户头像上一次更新时间
    @ApiModelProperty("用户头像上一次更新时间")
    @Alias("用户头像上一次更新时间")
    private LocalDateTime avatarUpdateTime;
}