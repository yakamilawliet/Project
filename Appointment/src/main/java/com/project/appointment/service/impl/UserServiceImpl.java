package com.project.appointment.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.project.appointment.common.Constants;
import com.project.appointment.controller.domain.LoginDTO;
import com.project.appointment.controller.domain.UserRequest;
import com.project.appointment.entity.User;
import com.project.appointment.exception.ServiceException;
import com.project.appointment.mapper.UserMapper;
import com.project.appointment.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    public LoginDTO login(UserRequest user) {
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
}
