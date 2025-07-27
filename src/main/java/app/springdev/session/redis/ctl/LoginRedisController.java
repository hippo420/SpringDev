package app.springdev.session.redis.ctl;

import app.springdev.session.redis.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/session/redis")
public class LoginRedisController {


    @Autowired
    private UserSessionRegistry userSessionRegistry;

    @GetMapping("login")
    public String loginView() {
        return "session/login";
    }

    @PostMapping("prcLogin")
    public String prcLogin(HttpServletRequest request, @ModelAttribute User user) {
        log.info("아이디 : {}, 비번: {}",user.getUserId(),user.getPassword());
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("최근조회상품", "아이폰, 폴드7");
        log.info("생성된 세션: {}",session.getId());
        // Session의 유효 시간 설정 (1800초 = 30분)
        session.setMaxInactiveInterval(1800);
        userSessionRegistry.registerSession(user.getUserId(), session.getId());

        return "redirect:/session/redis/home?userId="+user.getUserId();
    }

    @GetMapping("home")
    public void goHome(HttpServletRequest request, @RequestParam String userId) {
        log.info("아이디 : {} - SESSION: [{}]",userId,request.getSession().getId());
    }
}
