package com.bokyoung.moai.common.security;

import com.bokyoung.moai.domain.MoaiStaff;
import com.bokyoung.moai.repository.MoaiStaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MoaiStaffRepository moaiStaffRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MoaiStaff moaiStaff = moaiStaffRepository.findByUserId(userId).orElseThrow(() ->
            new UsernameNotFoundException("Not found" + userId));
        return new UserDetailsImpl(moaiStaff);
    }
}
