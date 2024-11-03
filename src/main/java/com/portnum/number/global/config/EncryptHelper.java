package com.portnum.number.global.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Configuration
class BcryptConfig implements EncryptHelper {
    @Override
    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}

public interface EncryptHelper {

    String encrypt(String password);    // 암호화
    boolean isMatch(String password, String hashed); // 해시된 함수와 비교해서 맞는지 비교
}