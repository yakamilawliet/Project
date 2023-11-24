package com.project.appointment.service;

import com.project.appointment.controller.domain.LoginDTO;
import com.project.appointment.controller.domain.UserRequest;
import com.project.appointment.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * @since 2023-11-16
 */
public interface IUserService extends IService<User> {
    LoginDTO login(UserRequest user);

    void register(UserRequest user);

    LoginDTO checkAccount(String openid, String sessionkey);

    User loadUserByOpenid(String openid);

    User loadUserByUid(String uid);
}
