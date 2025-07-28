package app.springdev.session.spring.ctl;

import app.springdev.session.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping("/session/spring")
public class LoginSpringController {

    public static Map<String, String> userSessionMap = new ConcurrentHashMap<>();

    @GetMapping("login")
    public String loginView() {
        return "session/loginspring";
    }

    @PostMapping("prcLogin")
    public String prcLogin(HttpServletRequest request, @ModelAttribute User user) {
        log.info("아이디 : {}, 비번: {}",user.getUserId(),user.getPassword());
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("최근조회상품", "아이폰, 폴드7");
        log.info("생성된 세션: {}",session.getId());
        userSessionMap.put(user.getUserId(), session.getId());
        // Session의 유효 시간 설정 (1800초 = 30분)
        session.setMaxInactiveInterval(1800);

        return "redirect:/session/spring/home?userId="+user.getUserId();
    }

    @GetMapping("home")
    public void goHome(HttpServletRequest request, @RequestParam String userId) {

        log.info("{}사용자 세션 - [{}]",userId, userSessionMap.get(userId));
        log.info("비지니스 로직 실행!!");

    }
}
