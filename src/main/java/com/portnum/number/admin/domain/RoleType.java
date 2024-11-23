package com.portnum.number.admin.domain;

import lombok.Getter;

@Getter
public enum RoleType {
    PORT("ROLE_PORT"), INFLUENCER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String roleType;

    RoleType(String roleType){
        this.roleType = roleType;
    }

    public static RoleType fromRoleType(String roleType) {
        for (RoleType role : RoleType.values()) {
            if (role.getRoleType().equals(roleType)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role type: " + roleType);
    }
}
