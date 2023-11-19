package com.project.appointment.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.project.appointment.common.Result;
import com.project.appointment.service.IUserService;
import com.project.appointment.utils.HttpUtils;
import com.project.appointment.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 微信小程序接口
 */
@Slf4j
@RestController
@RequestMapping("/mini")
@Api(tags = "小程序相关接口")
public class MiniController {

    @Value("${mini.appid}")
    private String appid;

    @Value("${mini.appsecret}")
    private String appsecret;

    @Autowired
    @Lazy
    private IUserService userService;

    @ApiOperation(value = "微信小程序登录")
    @GetMapping("/login")
    public Result login(String code) {
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
        return userService.miniLogin(openid, sessionKey);
    }
}
