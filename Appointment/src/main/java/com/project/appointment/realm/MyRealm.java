package com.project.appointment.realm;

import com.project.appointment.entity.User;
import com.project.appointment.service.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author Oliver
 * @Date 2023/11/17 10:20
 * @注释
 */
@Component
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private IUserService userService; //自定义授权方法

    @Value("${shiro.salt}")
    private String salt;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //自定义登录认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1 获取用户身份信息
        String openid = token.getPrincipal().toString();
        //2 调用业务层获取用户信息(数据库中)
        User user = userService.loadUserByOpenid(openid);
        //3 判断并将数据完成封装
        if(user!=null){
                AuthenticationInfo info = new SimpleAuthenticationInfo( token.getPrincipal(),
                        user.getPassword(), ByteSource.Util.bytes(salt), token.getPrincipal().toString()
                );
                return info;
        }
        return null;
    }
}
