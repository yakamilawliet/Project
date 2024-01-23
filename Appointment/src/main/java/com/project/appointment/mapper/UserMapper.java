package com.project.appointment.mapper;

import com.project.appointment.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-11-16
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 微信小程序进入添加信息
     * @param openid 微信小程序唯一标识
     */
    @Insert("insert into user(open_id) values (#{openid})")
    void insertOpenid(@Param("openid") String openid);

    User findByOpenid(@Param("openid") String openid);

    User findByUid(@Param("uid") String uid);

    User findByPhoneNumber(@Param("phone_number") String phoneNumber);
}
