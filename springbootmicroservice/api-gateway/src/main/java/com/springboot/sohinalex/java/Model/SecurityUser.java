package com.springboot.sohinalex.java.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class SecurityUser implements UserDetails {

    private  final user_info userinfo;

    public SecurityUser(user_info userinfo) {
        this.userinfo = userinfo;
    }



    public int getID(){
        return userinfo.getId();
    }
    @Override
    public String getPassword() {
        return userinfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userinfo.getUsername();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(userinfo
                        .getRole()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();}


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }





    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
