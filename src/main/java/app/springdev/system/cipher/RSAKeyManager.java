package app.springdev.system.cipher;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class RSAKeyManager {

    private final KeyPair keyPair;

    public RSAKeyManager() {
        this.keyPair = generateKeyPair();
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    /**
     * 공개키 전체를 X.509 형식으로 Base64 인코딩한 문자열 반환
     */
    public String getPublicKeyBase64() {
        PublicKey publicKey = keyPair.getPublic();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 암호문(Base64 문자열)을 복호화하여 평문 반환
     */
    public String decryptHex(String encryptedHex) {
        try {
            byte[] encryptedBytes = hexToBytes(encryptedHex); // ← 여기 중요
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("RSA 복호화 실패", e);
        }
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Hex 문자열의 길이는 짝수여야 합니다.");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 공개키에서 modulus, exponent를 16진수(hex)로 추출
     */
    public Map<String, String> getPublicKeyModulusExponentHex() {
        try {
            PublicKey publicKey = keyPair.getPublic();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

            String modulusHex = publicKeySpec.getModulus().toString(16);
            String exponentHex = publicKeySpec.getPublicExponent().toString(16);

            Map<String, String> keyMap = new HashMap<>();
            keyMap.put("modulus", modulusHex);
            keyMap.put("exponent", exponentHex);
            return keyMap;
        } catch (Exception e) {
            throw new RuntimeException("공개키 사양 추출 실패", e);
        }
    }

    /**
     * 키쌍 생성 (2048비트 고정)
     */
    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048); // Java 11에서도 OK
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("RSA 키 생성 실패", e);
        }
    }
}


