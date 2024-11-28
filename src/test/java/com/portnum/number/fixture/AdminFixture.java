package com.portnum.number.fixture;

import com.portnum.number.admin.domain.Admin;
import com.portnum.number.admin.domain.RoleType;
import com.portnum.number.admin.dto.request.AdminCreateRequest;

public class AdminFixture {
    public static Admin createAdmin(){
        return Admin.builder()
                .id(1L)
                .loginId("test")
                .email("test@naver.com")
                .profileUrl("https://test.com")
                .password("test")
                .nickName("test")
                .urlName("test")
                .name("test")
                .deleted(false)
                .isRqPwChange(false)
                .roleType(RoleType.PORT)
                .build();
    }
}
