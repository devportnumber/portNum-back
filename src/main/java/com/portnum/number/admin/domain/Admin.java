package com.portnum.number.admin.domain;


import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.admin.dto.request.AdminModifyRequest;
import com.portnum.number.global.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@SQLDelete(sql = "UPDATE Admin SET deleted = true WHERE admin_id = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String urlName;

    private String profileUrl;

    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @Column(nullable = false)
    private Boolean isRqPwChange = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;


    /* 생성 메서드 */

    public void modifyNameAndProfile(String name, String profileUrl){
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public void modifyPassword(String password) {
        this.password = password;
        this.isRqPwChange = Boolean.FALSE;
    }

    public void modifyIsRqPwChange(){
        this.isRqPwChange = Boolean.TRUE;
    }

}
