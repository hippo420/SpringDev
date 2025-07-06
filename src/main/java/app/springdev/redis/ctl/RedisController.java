package app.springdev.redis.ctl;

import app.springdev.redis.svc.RedisStringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Controller
@RequestMapping("redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisStringService redisService;

    @GetMapping("/index")
    public String redisIndex() {
        return "redis/index";
    }

    @GetMapping("/string")
    public String stringView() {
        return "redis/string";
    }

    @PostMapping("/string/set")
    public ResponseEntity stringSet(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String token = payload.get("token");
        log.info("userId = " + userId + ", token = " + token);
        return redisService.saveToken(userId,token);
    }

    @ResponseBody
    @GetMapping("/string/get")
    public List<Map<String, String>> stringGet(@RequestParam String userId) {
        log.info("[Key:userId 조회] > {}", userId);

        return redisService.getToken(userId);
    }


    @GetMapping("/list")
    public String listView(Model model) {
        model.addAttribute("messages", List.of("안녕하세요", "Redis 공부 중", "좋아요!"));
        return "redis/list";
    }

    @GetMapping("/hash")
    public String hashView(Model model) {
        List<Map<String, String>> fields = List.of(
                Map.of("key", "name", "value", "홍길동"),
                Map.of("key", "email", "value", "hong@example.com")
        );
        model.addAttribute("fields", fields);
        return "redis/hash";
    }

    @GetMapping("/set")
    public String setView(Model model) {
        model.addAttribute("members", List.of("user1", "user2", "user3"));
        return "redis/set";
    }

    @GetMapping("/sortedset")
    public String sortedSetView(Model model) {
        List<Map<String, Object>> rankings = List.of(
                Map.of("rank", 1, "user", "player1", "score", 300),
                Map.of("rank", 2, "user", "player2", "score", 250),
                Map.of("rank", 3, "user", "player3", "score", 200)
        );
        model.addAttribute("rankings", rankings);
        return "redis/sortedset";
    }

    @GetMapping("/hyperloglog")
    public String hyperLogLogView(Model model) {
        model.addAttribute("date", "2025-07-05");
        model.addAttribute("count", 1234);
        return "redis/hyperloglog";
    }

    @GetMapping("/bitmap")
    public String bitmapView(Model model) {
        List<Map<String, Object>> attendances = List.of(
                Map.of("userId", 1, "attended", true),
                Map.of("userId", 2, "attended", false),
                Map.of("userId", 3, "attended", true)
        );
        model.addAttribute("date", "2025-07-05");
        model.addAttribute("attendances", attendances);
        return "redis/bitmap";
    }

    @GetMapping("/geospatial")
    public String geospatialView(Model model) {
        List<Map<String, Object>> stores = List.of(
                Map.of("name", "CU 강남점", "latitude", 37.498, "longitude", 127.027),
                Map.of("name", "GS25 서초점", "latitude", 37.501, "longitude", 127.024)
        );
        model.addAttribute("stores", stores);
        return "redis/geospatial";
    }

    @GetMapping("/stream")
    public String streamView(Model model) {
        List<Map<String, String>> messages = List.of(
                Map.of("timestamp", "10:00", "user", "dev1", "message", "시스템 시작"),
                Map.of("timestamp", "10:05", "user", "dev2", "message", "처리 완료")
        );
        model.addAttribute("messages", messages);
        return "redis/stream";
    }
}
