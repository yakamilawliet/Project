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


    @Override
    public LoginDTO loginByPhoneNumber(String phoneNumber, String Code) {
        String tCode = RedisUtils.getCacheObject(phoneNumber);
        if(tCode == null) {
            throw new ServiceException("未获取验证码");
        }
        if (!tCode.equals(Code)) {
            throw new ServiceException("验证码错误");
        }
        User dbUser;
        try {
            dbUser = getOne(new UpdateWrapper<User>().eq("phone_number", phoneNumber));
        } catch (Exception e) {
            throw new RuntimeException("数据库异常");
        }
        if ( dbUser == null){
            dbUser = saveUser(phoneNumber);
        }
        // 登录
        StpUtil.login(dbUser.getPhoneNumber());
        String tokenValue = StpUtil.getTokenInfo().getTokenValue();

        return LoginDTO.builder().user(dbUser).token(tokenValue).build();
    }


//    @Override
//    public LoginDTO login(UserRequest user) {
//        String openid = user.getOpenid();
//        if (!StringUtils.isEmpty(openid)) {
//            userMapper.insertOpenid(openid);
//            RedisUtils.setCacheObject("wxuser:"+ openid, user, 60, TimeUnit.MINUTES);
//        }
//        User dbUser;
//        try {
//            dbUser = getOne(new UpdateWrapper<User>().eq("username", user.getUsername()));
//        } catch (Exception e) {
//            throw new RuntimeException("数据库异常");
//        }
//
//        if (dbUser == null) {
//            throw new ServiceException("未找到用户");
//        }
//        if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
//            throw new ServiceException("用户名或密码错误");
//        }
//        // 登录
//        StpUtil.login(dbUser.getUid());
//        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
//        return LoginDTO.builder().user(dbUser).token(tokenValue).build();
//    }

    @Override
    public User saveUser(String phoneNumber) {
        User user = new User();
        String name = Constants.USER_NAME_PREFIX + DateUtil.format(new Date(),
                    Constants.DATE_RULE_YYYYMMDD) + RandomUtil.randomString(4);
        user.setUsername(name);
        // 设置手机号
        user.setPhoneNumber(phoneNumber);   // 后期需要对手机号做加密
        // 设置 uid 唯一标识
        user.setUid(IdUtil.fastSimpleUUID());
        user.setRole("USER");
        try {
            save(user);
        } catch (Exception e) {
            throw new RuntimeException("注册失败", e);
        }
        return user;
    }


}
