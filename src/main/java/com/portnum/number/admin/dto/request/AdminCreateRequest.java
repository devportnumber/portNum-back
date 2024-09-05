package com.portnum.number.admin.dto.request;

import lombok.Getter;

@Getter
public class AdminCreateRequest {

    private String email;

    private String nickName;

    private String name;

    private String profileUrl;

    private String password;

    public void modifyPassword(String password){
        this.password = password;
    }
}
