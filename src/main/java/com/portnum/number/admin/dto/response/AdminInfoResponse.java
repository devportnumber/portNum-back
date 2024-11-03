package com.portnum.number.admin.dto.response;

import com.portnum.number.admin.entity.Admin;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminInfoResponse {

    private Long adminId;

    private String email;

    private String nickName;

    private String name;

    private String profileUrl;

    private String urlName;


    public static AdminInfoResponse from(Admin admin){
        return AdminInfoResponse.builder()
                .adminId(admin.getId())
                .email(admin.getEmail())
                .nickName(admin.getNickName())
                .name(admin.getName())
                .profileUrl(admin.getProfileUrl())
                .urlName(admin.getUrlName())
                .build();
    }
}
