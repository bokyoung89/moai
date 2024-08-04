package com.bokyoung.moai.aspect;

import java.util.Collection;
import com.bokyoung.moai.common.security.UserDetailsImpl;
import com.bokyoung.moai.staff.constant.MoaiStaffRoleType;
import com.bokyoung.moai.staff.domain.MoaiStaff;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class MoaiAuthCheck {

    public static void checkAuthorities(MoaiStaffRoleType type, String userId) {
        if (!isSamePrincipal(userId))
            throw new IllegalArgumentException("ID is not matched. Please check Token validity");
        if (!isUserInRole(type.getAuthority()))
            throw new IllegalArgumentException("Authentication failed. Role should be Checked");
    }

    private static boolean isSamePrincipal(String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MoaiStaff principal = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        return principal != null && id != null && id.equals(principal.getUserId());
    }

    private static boolean isUserInRole(String roleType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (roleType.equals(MoaiStaffRoleType.CXO.getAuthority()) || roleType.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public static String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            String userId = userDetails.getUser().getUserId();
            return userId.isEmpty() ? null : userId;
        }
        return null;
    }

}
