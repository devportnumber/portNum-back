package com.portnum.number.global.common.config;

import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 암호화는 저장중이거나 전송하려는 데이터를 보호하는 데 사용되는 대칭 암호화 알고리즘이다.
 * 고정된 크기의 데이터 블록에 데이터를 암호화하는 블록 암호 암호화 알고리즘.
 * AES 암호화는 데이터의 발신자와 수신자 간 공유되는 SecretKey 사용 <- 일반 텍스트 암호화 및 복호화하는데 사용
 * AES 암호화는 SecretKey 길이에 따라 종류가 다른데 128, 192, 256bit 지원
 */
@Component
public class Aes128Config {
    private static final Charset ENCODING_TYPE = StandardCharsets.UTF_8;
    private static final String INSTANCE_TYPE = "AES/CBC/PKCS5Padding";

    @Value("${aes.secret-key}")
    private String secretKey;
    private IvParameterSpec ivParameterSpec;
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    @PostConstruct
    public void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];   // 16bytes = 128bits
        secureRandom.nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
        secretKeySpec = new SecretKeySpec(secretKey.getBytes(ENCODING_TYPE), "AES");
        cipher = Cipher.getInstance(INSTANCE_TYPE);
    }

    // AES 암호화
    public String encryptAes(String plaintext) {
        try {
            // 새로운 IV 생성
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(ENCODING_TYPE));

            // IV와 암호화된 데이터를 함께 저장 (IV + encrypted)
            byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
            System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);

            return new String(Base64.getEncoder().encode(encryptedWithIv), ENCODING_TYPE);
        } catch (Exception e) {
            throw new GlobalException(Code.INTERNAL_ERROR, "Encrypt Error");
        }
    }


    // AES 복호화
    public String decryptAes(String ciphertext) {
        try {
            byte[] decoded = Base64.getDecoder().decode(ciphertext.getBytes(ENCODING_TYPE));

            // IV와 암호화된 데이터 분리
            byte[] iv = new byte[16];
            byte[] encrypted = new byte[decoded.length - iv.length];
            System.arraycopy(decoded, 0, iv, 0, iv.length);
            System.arraycopy(decoded, iv.length, encrypted, 0, encrypted.length);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return new String(cipher.doFinal(encrypted), ENCODING_TYPE);
        } catch (Exception e) {
            throw new GlobalException(Code.INTERNAL_ERROR, "Decrypt Error");
        }
    }

}
