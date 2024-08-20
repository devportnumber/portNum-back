package com.portnum.number.global.common.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdentityGeneratorUtil {
    public String generateIdentity() {
        return UUID.randomUUID().toString();
    }
}
