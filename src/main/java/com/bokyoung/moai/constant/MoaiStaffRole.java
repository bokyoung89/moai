package com.bokyoung.moai.constant;

import lombok.Getter;

@Getter
public enum MoaiStaffRole {
    CXO(Authority.CXO);

    private final String authority;

    MoaiStaffRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    private static class Authority {
        public static final String CXO = "CXO";
    }
}