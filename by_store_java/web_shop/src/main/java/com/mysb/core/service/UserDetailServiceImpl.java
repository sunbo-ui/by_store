package com.mysb.core.service;

import com.mysb.core.pojo.seller.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    //自动调用
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*定义权限集合*/
        List<GrantedAuthority> authority = new ArrayList<>();
        SimpleGrantedAuthority role_seller = new SimpleGrantedAuthority("ROLE_SELLER");
        authority.add(role_seller);
        if(username == null){
            return null;
        }

        Seller seller = sellerService.findById(username);

        if(seller != null){
            if("1".equals(seller.getStatus())){
                return new User(seller.getSellerId(),seller.getPassword(),authority);
            }
        }
        return null;
    }

}
