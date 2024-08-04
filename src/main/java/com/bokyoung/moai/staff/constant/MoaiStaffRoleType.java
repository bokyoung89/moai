package com.bokyoung.moai.staff.constant;

import lombok.Getter;

@Getter
public enum MoaiStaffRoleType {
    CXO(Authority.CXO);

    private final String authority;

    MoaiStaffRoleType(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    private static class Authority {
        public static final String CXO = "CXO";
    }
}
