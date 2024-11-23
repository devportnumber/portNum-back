package com.portnum.number.admin.repository;

import com.portnum.number.admin.domain.Admin;
import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.config.JpaConfig;
import com.portnum.number.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({JpaAuditingConfig.class, JpaConfig.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AdminRepositoryTest {


    private AdminCreateRequest createRequest;

    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    public void setUp(){
        createRequest = AdminCreateRequest.builder()
                .loginId("test")
                .email("test@naver.com")
                .profileUrl("https://test.com")
                .password("test")
                .nickName("test")
                .name("test")
                .build();
    }

    @Test
    public void save_테스트() throws Exception{
        //given
        Admin admin = Admin.of(createRequest, "test");

        //when
        Admin savedAdmin = adminRepository.save(admin);

        //then
        assertThat(savedAdmin).isNotNull();
        assertThat(savedAdmin.getId()).isGreaterThan(0);

        // 중복 불가 필드 대상 검증
        assertEquals(admin.getLoginId(), savedAdmin.getLoginId());
        assertEquals(admin.getEmail(), savedAdmin.getEmail());
        assertEquals(admin.getNickName(), savedAdmin.getNickName());
    }

    @Test
    public void findByLoginId_테스트() throws Exception{
        //given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        //when
        Optional<Admin> foundAdmin = adminRepository.findByLoginId(admin.getLoginId());

        //then
        assertThat(foundAdmin).isPresent();
        assertEquals(admin.getLoginId(), foundAdmin.get().getLoginId());
        assertEquals(admin.getEmail(), foundAdmin.get().getEmail());
        assertEquals(admin.getNickName(), foundAdmin.get().getNickName());
    }

    @Test
    public void existsByEmail_테스트() {
        // given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        // when
        boolean exists = adminRepository.existsByEmail(admin.getEmail());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void existsByNickName_테스트() {
        // given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        // when
        boolean exists = adminRepository.existsByNickName(admin.getNickName());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void existsByEmailWithNickName_테스트() {
        // given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        // when
        boolean exists = adminRepository.existsByEmailWithNickName(admin.getEmail(), admin.getNickName());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void findByEmailWithNickName_테스트() {
        // given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        // when
        Optional<Admin> foundAdmin = adminRepository.findByEmailWithNickName(admin.getEmail(), admin.getNickName());

        // then
        assertThat(foundAdmin).isPresent();
        assertEquals(admin.getLoginId(), foundAdmin.get().getLoginId());
        assertEquals(admin.getEmail(), foundAdmin.get().getEmail());
        assertEquals(admin.getNickName(), foundAdmin.get().getNickName());
    }

    @Test
    public void findByNickName_테스트() {
        // given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        // when
        Optional<Admin> foundAdmin = adminRepository.findByNickName(admin.getNickName());

        // then
        assertThat(foundAdmin).isPresent();
        assertEquals(admin.getLoginId(), foundAdmin.get().getLoginId());
        assertEquals(admin.getEmail(), foundAdmin.get().getEmail());
        assertEquals(admin.getNickName(), foundAdmin.get().getNickName());
    }

    @Test
    public void findByEmailWithName_테스트() {
        // given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        // when
        Optional<Admin> foundAdmin = adminRepository.findByEmailWithName(admin.getEmail(), admin.getName());

        // then
        assertThat(foundAdmin).isPresent();
        assertEquals(admin.getLoginId(), foundAdmin.get().getLoginId());
        assertEquals(admin.getEmail(), foundAdmin.get().getEmail());
        assertEquals(admin.getName(), foundAdmin.get().getName());
    }

    @Test
    public void findByEmailWithLoginId_테스트() {
        // given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        // when
        Optional<Admin> foundAdmin = adminRepository.findByEmailWithLoginId(admin.getEmail(), admin.getLoginId());

        // then
        assertThat(foundAdmin).isPresent();
        assertEquals(admin.getLoginId(), foundAdmin.get().getLoginId());
        assertEquals(admin.getEmail(), foundAdmin.get().getEmail());
        assertEquals(admin.getLoginId(), foundAdmin.get().getLoginId());
    }

    @Test
    public void findNickNameByUrlName_테스트() {
        // given
        Admin admin = Admin.of(createRequest, "test");
        adminRepository.save(admin);

        // when
        Optional<String> nickName = adminRepository.findNickNameByUrlName(admin.getUrlName());

        // then
        assertThat(nickName).isPresent();
        assertEquals(admin.getNickName(), nickName.get());
    }

    @Test
    public void 존재하지_않는_Email_조회_테스트() {
        // when
        boolean exists = adminRepository.existsByEmail("notexist@naver.com");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    public void 존재하지_않는_NickName_조회_테스트() {
        // when
        boolean exists = adminRepository.existsByNickName("notexist");

        // then
        assertThat(exists).isFalse();
    }
}