package app.springdev.session.redis.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class SessionRedisAspect {


    // goHome 메서드에만 적용 중 (원하면 전체 컨트롤러로 확장 가능)
    @Around("execution(* app.springdev.session.redis..ctl..test(..)) &&!execution(* app.springdev.session.redis..ctl..info(..)) &&!execution(* app.springdev.session.redis..ctl..index(..))")
    public Object checkSession(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP 세션 체크 진입: method = {}", joinPoint.getSignature().toShortString());

        // RequestContextHolder를 사용하여 HttpServletRequest 객체 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("웹 요청 컨텍스트를 찾을 수 없습니다. (비동기, 스케줄러 등)");
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false); // 세션이 없으면 새로 생성하지 않음

        if (session != null && session.getAttribute("userId") != null) {
            String sessionId = session.getId();
            String userId = (String) session.getAttribute("userId");

            log.info("------------ Session 정보 ------------");
            log.info("* Session ID: {}", sessionId);
            log.info("* User ID:    {}", userId);
            log.info("-------------------------------------");

            return joinPoint.proceed();
        } else {
            log.warn("사용자의 세션이 존재하지 않거나 유효하지 않습니다.");
            // 세션이 없으면 해당 요청을 중단하고 로그인 페이지로 리다이렉트하거나 오류 응답 반환 가능
            // 예시: throw new IllegalStateException("세션이 유효하지 않습니다.");
            return null; // 실행 중단
        }
    }

}
