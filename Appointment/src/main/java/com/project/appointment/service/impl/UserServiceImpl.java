package com.project.appointment.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.project.appointment.common.Constants;
import com.project.appointment.utils.RedisUtils;
import com.project.appointment.controller.domain.LoginDTO;
import com.project.appointment.controller.domain.UserRequest;
import com.project.appointment.entity.User;
import com.project.appointment.exception.ServiceException;
import com.project.appointment.mapper.UserMapper;
import com.project.appointment.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.appointment.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-11-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public LoginDTO login(UserRequest user) {
        String openid = user.getOpenid();
        if (!StringUtils.isEmpty(openid)) {
            userMapper.insertOpenid(openid);
            RedisUtils.setCacheObject("wxuser:"+ openid, user, 60, TimeUnit.MINUTES);
        }
        User dbUser;
        try {
            dbUser = getOne(new UpdateWrapper<User>().eq("username", user.getUsername()));
        } catch (Exception e) {
            throw new RuntimeException("数据库异常");
        }

        if (dbUser == null) {
            throw new ServiceException("未找到用户");
        }
        if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        // 登录
        StpUtil.login(dbUser.getUid());
        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
        return LoginDTO.builder().user(dbUser).token(tokenValue).build();
    }

    @Override
    public LoginDTO checkAccount(String openid, String sessionkey) {
        User user = loadUserByOpenid(openid);
        // 判断是否已关联账号
        if (user == null) {
            // 没有则直接返回前端openid后选择关联账号
            return LoginDTO.builder().openid(openid).sessionkey(sessionkey).build();
        } else {
            // 有则直接登录
            StpUtil.login(user.getUid());
            String tokenValue = StpUtil.getTokenInfo().getTokenValue();
            return LoginDTO.builder().user(user).token(tokenValue).build();
        }
    }

    @Override
    public void register(UserRequest user) {
        try {
            User saveUser = new User();
            BeanUtils.copyProperties(user, saveUser);
            saveUser.setRole("USER");
            saveUser(saveUser);
        } catch (Exception e) {
            throw new ServiceException("数据库异常", e);
        }
    }

    public void saveUser(User user) {
        User dbUser = null;
        dbUser = getOne(new UpdateWrapper<User>().eq("username", user.getUsername()));
        if (dbUser != null) {
            throw new ServiceException("用户已注册");
        }
        if (StrUtil.isBlank(user.getNickname())) {
            String nickname = Constants.USER_NAME_PREFIX + DateUtil.format(new Date(),
                    Constants.DATE_RULE_YYYYMMDD) + RandomUtil.randomString(4);
            user.setNickname(nickname);
        }
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword("12415");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword())); //  BCrypt 加密
        // 设置 uid 唯一标识
        user.setUid(IdUtil.fastSimpleUUID());
        try {
            save(user);
        } catch (Exception e) {
            throw new RuntimeException("注册失败", e);
        }
    }

    @Override
    public User loadUserByOpenid(String openid) {
        // 判断缓存中是否存在用户信息 存在则直接从缓存中取，不存在则查询数据库并把数据存入缓存
        User user;
        if(RedisUtils.hasKey("wxuser:" + openid)) {
            // 存在则直接从缓存中取
            user = (User) RedisUtils.getCacheObject("wxuser:" + openid);
            RedisUtils.setExpireTime("wxuser:" + openid, 60, TimeUnit.MINUTES);
        } else {
            user = userMapper.findByOpenid(openid);
            // 并把数据存入缓存
            RedisUtils.setCacheObject("wxuser:" + openid, user, 60, TimeUnit.MINUTES);
        }
        return user;
    }

    @Override
    public User loadUserByUid(String uid) {
        // 判断缓存中是否存在用户信息 存在则直接从缓存中取，不存在则查询数据库并把数据存入缓存
        User user;
        if(RedisUtils.hasKey("user:" + uid)) {
            // 存在则直接从缓存中取
            user = (User) RedisUtils.getCacheObject("user:" + uid);
            RedisUtils.setExpireTime("user:" + uid, 60, TimeUnit.MINUTES);
        } else {
            user = userMapper.findByUid(uid);
            // 并把数据存入缓存
            RedisUtils.setCacheObject("user:" + uid, user, 60, TimeUnit.MINUTES);
        }
        return user;
    }

}
