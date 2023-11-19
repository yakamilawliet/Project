package com.project.appointment.realm;

import com.project.appointment.entity.User;
import com.project.appointment.service.IUserService;
import com.project.appointment.token.JWTToken;
import com.project.appointment.utils.JWTUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Lazy
    private IUserService userService; //自定义授权方法

    @Autowired
    private JWTUtils jwtUtils;
    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //自定义登录认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // token -> openid
        String jwtToken = (String) token.getCredentials();
        String openid = jwtUtils.getOpenidByToken(jwtToken);

        if (openid == null) {
            throw new AuthenticationException(" token错误，请重新登入！");
        }

        if(jwtUtils.isExpiration(jwtToken)){
            throw new AuthenticationException(" token过期，请重新登入！");
        }

        User user = userService.loadUserByOpenid(openid);

        if (user == null) {
            throw new AccountException("账号不存在!");
        }

        return new SimpleAuthenticationInfo(user, jwtToken, getName());
    }
}
