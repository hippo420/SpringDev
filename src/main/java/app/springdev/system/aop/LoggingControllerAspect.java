package app.springdev.system.aop;

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
public class LoggingControllerAspect {
    // 컨트롤러에 있는 모든 public 메서드 대상
    @Around("execution(public * app.springdev..ctl..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 리턴 타입이 String이고, View 반환 추정 시 제외
        if (method.getReturnType() == String.class) {
            return joinPoint.proceed();
        }
        log.info("컨트롤러 진입 >>> {}", joinPoint.getSignature().getClass().getSimpleName());
        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            log.info(" Method : [{}] => 실행시간 : {}ms", method.getName(), (end - start));
        }
    }
}
