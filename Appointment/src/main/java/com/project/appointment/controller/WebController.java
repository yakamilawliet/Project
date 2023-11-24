package com.project.appointment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.project.appointment.common.Result;
import com.project.appointment.controller.domain.LoginDTO;
import com.project.appointment.controller.domain.UserRequest;
import com.project.appointment.service.IUserService;
import com.project.appointment.utils.HttpUtils;
import com.project.appointment.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "无权限接口列表")
@RestController
@Slf4j
public class WebController {

    @Value("${mini.appid}")
    private String appid;

    @Value("${mini.appsecret}")
    private String appsecret;
    @Resource
    IUserService userService;
    @GetMapping(value = "/")
    @ApiOperation(value = "版本校验接口")
    public String version() {
        String ver = "appointment-0.0.1-SNAPSHOT";  // 应用版本号
        Package aPackage = WebController.class.getPackage();
        String title = aPackage.getImplementationTitle();
        String version = aPackage.getImplementationVersion();
        if (title != null && version != null) {
            ver = String.join("-", title, version);
        }
        return ver;
    }


    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口")
    public Result login(@RequestBody UserRequest user) {
        LoginDTO res = userService.login(user);
        return Result.success(res);
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册接口")
    public Result register(@RequestBody UserRequest user) {
        userService.register(user);
        return Result.success();
    }

    @PostMapping("/wechat/login")
    @ApiOperation(value = "微信用户登录接口")
    public Result wechatlogin(String code) {
        if (StringUtils.isEmpty(code)) {
            return Result.error("登录失败, 请联系管理员！");
        }
        // 构建get请求
        String url = "https://api.weixin.qq.com/sns/jscode2session?" + "appid=" +
                appid +
                "&secret=" +
                appsecret +
                "&js_code=" +
                code +
                "&grant_type=authorization_code";
        // 发送请求
        String result = HttpUtils.getResponse(url);
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");
        log.info("微信小程序唯一标识：{}", openid);
        return Result.success(userService.checkAccount(openid, sessionKey));
    }

}
