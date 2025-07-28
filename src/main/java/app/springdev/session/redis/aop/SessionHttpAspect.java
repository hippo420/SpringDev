package app.springdev.session.redis.aop;

import app.springdev.session.http.ctl.LoginHttpController;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class SessionHttpAspect {

    // goHome 메서드에만 적용 중 (원하면 전체 컨트롤러로 확장 가능)
    @Around("execution(public * app.springdev.session.http..ctl..goHome(..))")
    public Object checkSession(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP 세션 체크 진입: method = {}", joinPoint.getSignature());

        // 현재 HTTP 요청 객체 가져오기
        Object[] args = joinPoint.getArgs();
        String sessionId ="";
        for (Object arg : args) {
            if (arg instanceof String) {
                String userId = (String) arg;
                sessionId = LoginHttpController.userSessionMap.get(userId);
                if(sessionId == null || sessionId=="" )
                {
                    log.error("AOP - {} 세션없음", userId );
                    return false;
                }else {
                    log.info("AOP - userId = {}, sessionId= {}", userId, sessionId);
                }
            }
        }
        return joinPoint.proceed();
    }

}
