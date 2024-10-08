package com.portnum.number.global.security.custom;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.entity.RoleType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CustomUserDetails extends Admin implements UserDetails {

    private Long id;
    private String loginId;
    private String email;
    private RoleType roleType;
    private String password;
    private String nickName;
    private Boolean isRqPwChange;
    private String urlName;


    private CustomUserDetails(Admin admin){
        this.id = admin.getId();
        this.loginId = admin.getLoginId();
        this.email = admin.getEmail();
        this.password = admin.getPassword();
        this.roleType = admin.getRoleType();
        this.nickName = admin.getNickName();
        this.isRqPwChange = admin.getIsRqPwChange();
        this.urlName = admin.getUrlName();
    }

    private CustomUserDetails(String email, RoleType role){
        this.email = email;
        this.roleType = role;
    }

    private CustomUserDetails(String email, RoleType role, String password) {
        this.email = email;
        this.roleType = role;
        this.password = password;
    }

    public static CustomUserDetails of(Admin admin){
        return new CustomUserDetails(admin);
    }

    public static CustomUserDetails of(String email, RoleType role){
        return new CustomUserDetails(email, role);
    }

    public static CustomUserDetails of(String email, RoleType role, String password) {
        return new CustomUserDetails(email, role, password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return CustomAuthorityUtils.createAuthorities(roleType);
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}