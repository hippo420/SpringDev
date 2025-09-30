package app.springdev.socket.web.echo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.net.http.WebSocket;

@Slf4j
@Component
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //핸들러 등록
        //웹소켓 요청을 처리할 엔드포인트 지정(WebSocketHandler, String... paths )
        registry.addHandler(new EchoHandler(),"/ws/echo","/ws/echo1")
                .setAllowedOrigins("*")  //Cors 도메인 설정

//                .setAllowedOriginPatterns("*")   //대신 패턴 기반으로 설정 가능
//                .addInterceptors(new HttpSessionHandshakeInterceptor()) //핸드셰이크 과정에서 인터셉터 추가(인증, 세션, 검증, 로깅)
//                .withSockJS()            //SockJS 지원 추가
//                    .setHeartbeatTime(10000) //클라이언트와 서버 간 하트비트 주기(ms)
//                    .setDisconnectDelay(5000) //연결 끊김을 감지하는 지연 시간(ms)
//                    .setStreamBytesLimit(1000000) //스트리밍 전송 시 최대 바이트 수 제한
//                    .setClientLibraryUrl("https://cdn.jsdelivr.net/sockjs/1.1.4/sockjs.min.js") //클라이언트에서 사용할 SockJS 라이브러리 URL 지정
//                    .setTransportHandlers() //지원할 전송 방식 직접 설정
//                    .setSessionCookieNeeded(true)  //세션 쿠키 필요 여부
//                    .setHttpMessageCacheSize(5000) //HTTP 메시지 캐시 크기
//                    .setWebSocketEnabled(true)  //WebSocket 사용 여부 (비활성화 가능)
;

    }
}
