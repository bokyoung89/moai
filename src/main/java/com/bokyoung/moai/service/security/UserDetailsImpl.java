package com.bokyoung.moai.service.security;

import com.bokyoung.moai.constant.MoaiStaffRole;
import com.bokyoung.moai.domain.MoaiStaff;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayDeque;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final MoaiStaff moaiStaff;

    public UserDetailsImpl(MoaiStaff moaiStaff) {
        this.moaiStaff = moaiStaff;
    }

    public MoaiStaff getUser() {
        return moaiStaff;
    }

    @Override
    public String getPassword() {
        return moaiStaff.getPassword();
    }

    @Override
    public String getUsername() {
        return moaiStaff.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        MoaiStaffRole role = MoaiStaffRole.valueOf(moaiStaff.getRole());
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayDeque<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

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
