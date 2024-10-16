package com.portnum.number.admin.entity;


import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.admin.dto.request.AdminModifyRequest;
import com.portnum.number.global.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@SQLDelete(sql = "UPDATE Admin SET deleted = true WHERE admin_id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String name;

    private String profileUrl;

    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;


    /* 생성 메서드 */
    public static Admin of(AdminCreateRequest request){
        Admin newAdmin = new Admin();

        newAdmin.modifyAdmin(request);

        return newAdmin;
    }


    /* 수정 메서드 */
    private void modifyAdmin(AdminCreateRequest request){
        this.email = request.getEmail();
        this.nickName = request.getNickName();
        this.name = request.getName();
        this.profileUrl = request.getProfileUrl();
        this.roleType = RoleType.INFLUENCER;
        this.password = request.getPassword();
    }

    public void modifyAdmin(AdminModifyRequest request){
        this.name = request.getName();
        this.profileUrl = request.getProfileUrl();
    }

    public void modifyPassword(String password) {
        this.password = password;
    }
}
