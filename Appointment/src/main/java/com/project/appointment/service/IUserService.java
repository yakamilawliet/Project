package com.project.appointment.service;

import com.project.appointment.common.Result;
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

    /**
     * 登录接口
     * @param openid 登录参数： 账号和密码
     * @return 返回token，用token去获取资源
     */
    Result miniLogin(String openid, String sessionKey);

    User loadUserByOpenid(String openid);
}
