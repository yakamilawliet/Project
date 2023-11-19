package com.project.appointment.service.impl;

import com.project.appointment.common.Result;
import com.project.appointment.entity.User;
import com.project.appointment.mapper.UserMapper;
import com.project.appointment.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.appointment.token.JWTToken;
import com.project.appointment.utils.RedisUtils;
import com.project.appointment.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-11-16
 */
@Slf4j
@Service
@Lazy
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JWTUtils tokenUtils;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Result miniLogin(String openid, String sessionKey) {
        User user;
        user = userService.loadUserByOpenid(openid);

        if (user == null) {
            userMapper.insertOpenid(openid);
            user = userService.loadUserByOpenid(openid);
            // user为空，后续返回只有openid
        }

        // jwt来生成token
        String token = tokenUtils.generateToken(user);
        Map<String, Object> map = new HashMap<>(2);
        map.put("tokenHead", tokenHead);
        map.put("token", token);
        map.put("userInfo", user);
        map.put("openid", openid);
        map.put("sessionKey", sessionKey);

        // shiro认证
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new JWTToken(token));
        } catch (AuthenticationException e) {
            // TODO 是否还需要进一步异常处理？
            throw new AuthenticationException("认证失败");
        }

        // 登录成功
        return Result.success(map);
    }

    @Override
    public User loadUserByOpenid(String openid) {
        // 判断缓存中是否存在用户信息 存在则直接从缓存中取，不存在则查询数据库并把数据存入缓存
        User user;
        if(redisUtils.haskey("user:" + openid)) {
            // 存在则直接从缓存中取
            user = (User) redisUtils.getValue("user:" + openid);
            redisUtils.expire("user:" + openid, 60);
        } else {
            user = userMapper.findByOpenid(openid);
            // 并把数据存入缓存
            redisUtils.setValueTime("user:" + openid, user, 60);
        }
        return user;
    }
}
