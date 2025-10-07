package app.springdev.elastic.datasync.outbox.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EventPublish {
    /**
     * Outbox 이벤트의 집계 유형 (예: "notice", "order")
     */
    String aggregateType();

    /**
     * true인 경우, 메서드의 반환 값(Return Value)을 이벤트 페이로드로 사용
     * 이 방식은 메서드 내에서 생성/매핑된 최종 객체를 이벤트로 보낼 때 가장 적합
     */
    boolean useReturnValue() default false;

    /**
     * 이벤트를 생성하는 데 사용할 인자(argument)의 인덱스.
     * 메서드 인자 목록에서 이벤트 페이로드로 사용될 객체를 지정
     */
    // (선택 사항: 레거시 지원을 위해 남겨둘 수 있으나, 여기서는 useReturnValue만 사용)
     String payloadName() default "";
     int payloadIndex() default -1;
}
