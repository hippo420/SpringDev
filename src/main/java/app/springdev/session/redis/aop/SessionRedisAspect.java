package app.springdev.session.redis.aop;

import app.springdev.session.redis.ctl.UserSessionRegistry;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class SessionRedisAspect {

    private static final String SESSION_KEY = "SESSION";
    private static final String REDIS_SESSION_KEY = ":sessions:";

    @Value("${spring.session.redis.namespace}")
    private String namespace;


    @Autowired
    private UserSessionRegistry userSessionRegistry;

    // goHome 메서드에만 적용 중 (원하면 전체 컨트롤러로 확장 가능)
    @Around("execution(public * app.springdev.session.redis..ctl..goHome(..))")
    public Object checkSession(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP 세션 체크 진입: method = {}", joinPoint.getSignature());

        // 현재 HTTP 요청 객체 가져오기
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof String) {
                String userId = (String) arg;
                log.info("AOP - userId = {}", userId);
                String storedSessionId = userSessionRegistry.getSessionId(userId);
                // 세션 없거나 인증 정보 없음
                if ("".equals(storedSessionId) ) {
                    log.warn("세션 없음 또는 로그인 정보 없음. 접근 거부.");
                    throw new IllegalStateException("로그인이 필요합니다.");
                }else{
                    log.info("AOP - userId = {}, sessionId= {}", userId, storedSessionId);
                }
            }
        }

        return joinPoint.proceed();
    }

}
