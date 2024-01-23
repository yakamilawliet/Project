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

    LoginDTO loginByPhoneNumber(String phoneNumber, String Code);


    User saveUser(String phoneNumber);
}
