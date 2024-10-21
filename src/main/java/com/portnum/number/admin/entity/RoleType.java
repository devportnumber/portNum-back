package com.portnum.number.admin.entity;

import lombok.Getter;

@Getter
public enum RoleType {
    PORT("ROLE_PORT"), INFLUENCER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String roleType;

    RoleType(String roleType){
        this.roleType = roleType;
    }
}
