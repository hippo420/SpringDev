package app.springdev.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/request")
public class RequestController {

    //1.쿼리 파라미터
    @GetMapping("/queryParam")
    public void queryParam(@RequestParam String param, HttpServletRequest request) {
        printRequest(request);
        log.info("파리미터 : {}", param);
    }

    @GetMapping("/queryParam1")
    public void queryParam1(@RequestParam(required = false, defaultValue = "UNDEFINED") String param, HttpServletRequest request) {
        printRequest(request);
        log.info("파리미터 : {}", param == null ? "파라미터가 없습니다." : param);
    }

    //2.URL 경로 변수
    @GetMapping("/users/{id}/{opt}")
    public void getUser(@PathVariable(required = false) Long id,@PathVariable(required = false) String opt, HttpServletRequest request) {
        printRequest(request);
        log.info("파리미터 : {}", id == null ? "파라미터가 없습니다." : id);
        log.info("파리미터 : {}", opt == null ? "파라미터가 없습니다." : opt);
    }

    //3.JSON (또는 XML) 데이터를 객체로 받기
    @PostMapping("/users")
    public void createUser(@RequestBody UserDto userDto, HttpServletRequest request) {
        printRequest(request);
        log.info("요청 사용자 데이터");
        log.info("id: {}", userDto.getId());
        log.info("name: {}", userDto.getName());
        log.info("age: {}", userDto.getAge());
        log.info("email: {}", userDto.getEmail());
    }

    //4.폼 데이터나 쿼리 파라미터를 객체로 매핑
    @PostMapping("/register")
    public void register(@ModelAttribute UserDto userDto, HttpServletRequest request) {
        printRequest(request);
        log.info("요청 사용자 데이터");
        log.info("id: {}", userDto.getId());
        log.info("name: {}", userDto.getName());
        log.info("age: {}", userDto.getAge());
        log.info("email: {}", userDto.getEmail());
    }

    //4.폼 데이터나 쿼리 파라미터를 객체로 매핑
    @RequestMapping("/register1")
    public void register(@ModelAttribute String userId, HttpServletRequest request) {
        printRequest(request);
        log.info("요청 사용자 데이터");
        log.info("id: {}", userId);
    }

    //5.요청 헤더 받기
    @GetMapping("/auth")
    public void auth(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        printRequest(request);
        log.info("토큰 : [{}]",token);
    }

    //6.파일 업로드
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) {
        return "파일명: " + file.getOriginalFilename();
    }

    public void printRequest(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            log.info("Parameter : [{}] , Value : [{}]", entry.getKey(),Arrays.toString(entry.getValue()));
        }
    }
}
