package com.portnum.number.global.security.custom;

import com.portnum.number.admin.entity.RoleType;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Slf4j
public class CustomAuthorityUtils {

    public static List<GrantedAuthority> createAuthorities(RoleType role){
        return List.of(new SimpleGrantedAuthority(role.getRoleType()));
    }

    public static boolean verifiedRole(RoleType role){
        if(role == null){
            throw new IllegalStateException("역할이 없습니다.");
        } else{
            for(RoleType roleType : RoleType.values()){
                if(roleType.equals(role)){
                    return true;
                }
            }
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Authorized Role");
        }
    }
}