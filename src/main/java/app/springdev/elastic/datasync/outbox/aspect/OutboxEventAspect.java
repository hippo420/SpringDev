package app.springdev.elastic.datasync.outbox.aspect;

import app.springdev.elastic.datasync.outbox.OutBoxRepository;
import app.springdev.elastic.datasync.outbox.annotation.EventPublish;
import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class OutboxEventAspect {
    private final ObjectMapper objectMapper;
    private final OutBoxRepository outBoxRepository;
    /**
     * @OutboxEvent 어노테이션이 붙은 메서드 주변(Around)에서 동작
     */
    @Around("@annotation(app.springdev.elastic.datasync.outbox.annotation.EventPublish)")
    // 중요: 호출된 비즈니스 메서드의 트랜잭션에 참여해야 합니다.
    // 비즈니스 메서드와 Outbox 저장이 같은 트랜잭션으로 묶여야 원자성이 보장됩니다.
    public Object handleOutboxEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("OutboxEventAspect.handleOutboxEvent");
        // 1. 비즈니스 메서드 실행
        Object result = joinPoint.proceed();


        // 2. 어노테이션 및 인자 정보 가져오기
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EventPublish eventAnnotation = method.getAnnotation(EventPublish.class);

        String aggregateType = eventAnnotation.aggregateType();
        Object payloadObject = null;
        int payloadIndex = -9999;
        String payloadName ="";
        String errMsg = "";

        //1. 페이로드로 리턴객체 사용
        if (eventAnnotation.useReturnValue()) {
            payloadObject = result;
        } else {
            //2. 파라미터 인덱스로 페이로드 처리
            if(eventAnnotation.payloadIndex() > 0)
            {
                payloadIndex = eventAnnotation.payloadIndex();
                payloadObject = joinPoint.getArgs()[payloadIndex];
            }
            //2. 파라미터명 인덱스로 페이로드 처리
            else if(eventAnnotation.payloadName() != null && !eventAnnotation.payloadName().isEmpty())
            {
                String[] parameterNames = signature.getParameterNames();
                Object[] args = joinPoint.getArgs();
                for (int i = 0; i < parameterNames.length; i++) {
                    if (parameterNames[i].equals(payloadName)) {
                        payloadObject = args[i];
                        break;
                    }
                }
            }else{
                errMsg ="@EventPublish에 useReturnValue, payloadName, payloadIndex 중 하나를 설정하세요";
                throw new IllegalStateException(errMsg);
            }

        }

        if (payloadObject == null) {
            // DB 저장이 실패하여 null이 반환되었거나, void 메서드를 잘못 호출한 경우
            throw new IllegalStateException("이벤트 페이로드 객체(메서드 반환 값)가 null입니다.");
        }

        // 페이로드(Payload) 생성
        String payloadJson = objectMapper.writeValueAsString(payloadObject);
        log.info("payloadJson: {}",payloadJson);
        Object aggregateId;

        try {
            Method getIdMethod = payloadObject.getClass().getMethod("getId");
            aggregateId = getIdMethod.invoke(payloadObject);
        } catch (Exception e) {
            throw new IllegalStateException("getId가 없습니다.");// getId가 없으면 UUID 생성
        }

        // 5. OutboxEvent 객체 생성 및 저장
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType(aggregateType)
                .aggregateId((Long) aggregateId)
                .payload(payloadJson)
                .status("PENDING") // 상태는 PENDING으로 시작
                .build();

        outBoxRepository.save(outboxEvent);

        // 6. 비즈니스 메서드의 반환 값을 그대로 리턴
        return result;
    }
}
