package app.springdev.session.redis.ctl;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/session")
public class SessionRedisController {

    private final RedisTemplate<String, Object> redisTemplate;

    public SessionRedisController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String USER_ID_SESSION_KEY = "userId";
    private static final String LOGIN_TIME_SESSION_KEY = "loginTime";

    @GetMapping("/info")
    public String index(HttpSession session, Model model, @RequestParam(required = false) String userId) {
        if (userId == null) {
            userId = "undefined";
        }

        String sessionId = session.getId();
        String redisKey = "session:" + userId;
        session.setAttribute(USER_ID_SESSION_KEY, userId);
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loginTime = now.format(formatter);

        // 서버 이름 가져오기 (Docker 환경변수)
        String serverName = System.getenv("SERVER_NAME");
        if (serverName == null) {
            serverName = System.getenv("HOSTNAME");
        }
        if (serverName == null) {
            serverName="localhost";
        }

        // Redis에 count, sessionId 저장
        ops.set(redisKey + ":sessionId", sessionId, Duration.ofMinutes(5));
        ops.set(redisKey + ":loginTime", loginTime, Duration.ofMinutes(5));
        ops.set(redisKey + ":server", serverName, Duration.ofMinutes(5));
        Long count = countSessionsByUserId(userId+":sessionId");

        model.addAttribute("userId", userId);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("loginTime", loginTime);
        model.addAttribute("server", serverName);
        model.addAttribute("count", count);

        return "/session/index"; // Mustache 템플릿
    }

    @GetMapping("/logic")
    public String logic(HttpSession session, Model model, @RequestParam(required = false) String userId) {
        log.info("logic 진입");
        String userId_key = (String) session.getAttribute(USER_ID_SESSION_KEY);

        if (userId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인이 필요합니다.");
        }

        String redisKey = "session:" + userId_key;
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // Redis에서 count 가져오기

        Long count = countSessionsByUserId(userId_key+":sessionId");
        String loginTime = (String) ops.get(redisKey + ":loginTime");
        String server = (String) ops.get(redisKey + ":server");
        String sessionId = session.getId();

        model.addAttribute("userId", userId_key == null ? "undefined" : userId_key);
        model.addAttribute("sessionId", sessionId == null ? "undefined" : sessionId);
        model.addAttribute("loginTime", loginTime == null ? "undefined" : loginTime);
        model.addAttribute("server", server == null ? "undefined" : server);
        model.addAttribute("count", count == null ? 0L : count);

        log.info("userId : {}",userId);
        log.info("sessionId : {}",sessionId);
        log.info("loginTime : {}",loginTime);
        log.info("server : {}",server);
        log.info("count : {}",count);

        return "/session/info"; // Mustache 템플릿
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session, Model model) {
        String userId = (String) session.getAttribute(USER_ID_SESSION_KEY);
        log.info("접속자[{}] 로그아웃 처리",userId);
        if (userId != null) {
            String pattern = "session:" + userId + ":*";
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok("Logout Successful"); // 200 OK와 함께 응답
    }

    public long countSessionsByUserId(String userId) {
        // 실제 Spring Session이 저장하는 키 패턴에 맞게 수정해야 합니다.
        // 예를 들어 "spring:session:sessions:유저ID"와 같은 형식일 수 있습니다.
        String pattern = "session:" + userId + "*";

        long count = 0;
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).build();

        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                cursor.next();
                count++;
            }
        }

        return count;
    }
}
