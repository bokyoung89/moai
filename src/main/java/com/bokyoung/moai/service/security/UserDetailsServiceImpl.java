package com.bokyoung.moai.service.security;

import com.bokyoung.moai.domain.MoaiStaff;
import com.bokyoung.moai.repository.MoaiStaffRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private MoaiStaffRepository moaiStaffRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MoaiStaff moaiStaff = moaiStaffRepository.findByUserId(userId).orElseThrow(() ->
            new UsernameNotFoundException("Not found" + userId));
        return new UserDetailsImpl(moaiStaff);
    }
}
