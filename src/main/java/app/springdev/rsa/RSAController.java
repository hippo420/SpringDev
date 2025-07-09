package app.springdev.rsa;

import app.springdev.system.cipher.RSAKeyManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("rsa")
public class RSAController {
    @Autowired
    private RSAKeyManager rsaKeyManager;


    @GetMapping("publickeyHex")
    public Map<String,String> publickeyHex() {
        String key = rsaKeyManager.getPublicKeyBase64();
        Map<String, String> hexMap = rsaKeyManager.getPublicKeyModulusExponentHex();
        log.info("publickey : {}",key);
        hexMap.entrySet().stream().forEach(entry -> {log.info("key: [{}], value: [{}]",entry.getKey(),entry.getValue());});
        return hexMap;
    }

    @GetMapping("publickey")
    public String publickey() {
        String key = rsaKeyManager.getPublicKeyBase64();
        log.info("publickey : {}",key);
        return key;
    }

    @GetMapping("decrypt")
    public void decrypt(@RequestParam String encData) {

        String decData = rsaKeyManager.decryptHex(encData);
        log.info("decData : {}",decData);
    }
}
