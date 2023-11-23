package com.project.appointment.controller;

import com.project.appointment.common.Result;
import com.project.appointment.controller.domain.LoginDTO;
import com.project.appointment.controller.domain.UserRequest;
import com.project.appointment.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "无权限接口列表")
@RestController
@Slf4j
public class WebController {
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

}
