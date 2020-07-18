package com.mysb.core.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    LoginSerivce loginService;

    public void setLoginService(LoginSerivce loginService) {
        this.loginService = loginService;
    }

    //自动调用
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*定义权限集合*/
        List<GrantedAuthority> authority = new ArrayList<>();
        SimpleGrantedAuthority role_seller = new SimpleGrantedAuthority("ROLE_ADMIN");
        authority.add(role_seller);
        if (username == null) {
            return null;
        }

        com.mysb.core.pojo.user.User user = loginService.user(username);
        if (user != null) {
            return new User(user.getUsername(), "{noop}"+user.getPassword(), authority);
        }
        return null;
    }

}
