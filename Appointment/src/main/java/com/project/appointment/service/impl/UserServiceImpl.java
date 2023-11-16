package com.project.appointment.service.impl;

import com.project.appointment.entity.User;
import com.project.appointment.mapper.UserMapper;
import com.project.appointment.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
