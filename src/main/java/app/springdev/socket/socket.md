# Socket
## #️⃣ 기초 네트워크 이해
목표: Socket 개념을 이해하고, TCP/UDP의 차이를 명확히 구분.

+ 학습내용
    + OSI 7계층 중 TCP/IP 4계층 개념
    + TCP vs UDP (연결 지향 / 비연결 지향, 신뢰성, 속도 차이)
    + Socket이란? (IP + Port로 통신하는 창구)

+   실습
    + 자바 ServerSocket과 Socket으로 간단한 채팅 프로그램 만들기
    + 서버: ServerSocket server = new ServerSocket(8080);
    + 클라이언트: Socket socket = new Socket("localhost", 8080);
    + I/O 스트림(InputStream, OutputStream)을 사용한 송수신.

---

## 2️⃣ TCP Socket 심화
목표: 멀티 클라이언트, 스레드, 성능 고려 학습.

+ 학습내용
    + Blocking I/O vs Non-blocking I/O (NIO)
    + 멀티 스레드 기반 서버 설계
    + Reactor 패턴 (Netty 같은 프레임워크 기본 구조 이해)

+ 실습
    + 스레드풀 기반 TCP 서버
    + NIO로 에코 서버 구현
    + Netty 기본 서버/클라이언트 예제

---

## 3️⃣ WebSocket 이해 (TCP 위의 프로토콜)
목표: 왜 WebSocket이 필요한지, HTTP와 TCP 차이 위에 WebSocket 위치를 이해.

+ 학습내용
    + HTTP vs WebSocket (Request/Response vs 양방향 지속 연결)
    + WebSocket 핸드셰이크 과정 (HTTP Upgrade)
    + 메시지 프레임 구조 (Text, Binary, Ping/Pong)

+ 실습
    + 자바 표준 WebSocket API (javax.websocket) 사용.
    + Spring WebSocket (@EnableWebSocket, WebSocketHandler) 기반 예제.
    + STOMP 프로토콜 적용 vs 직접 핸들러 작성 비교.

---

## 4️⃣ Spring에서의 실무 적용
목표: 서비스에서 실시간 기능 구현.

+ 학습내용
    + Spring Boot에서 WebSocket 서버 구현.
    + SimpMessagingTemplate / STOMP를 활용한 메시지 브로커 기반 채팅.
    + Redis Pub/Sub 연동으로 다중 서버 확장.

+ 실습
    + 단순 채팅방 구현 → 사용자별/방별 관리.
    + 실시간 알림 시스템 (ex: 주식 시세, 알림, IoT).

---

## 5️⃣ TCP & WebSocket 비교 및 활용

+ TCP Socket
    + 저수준 제어 가능 (바이너리, 커스텀 프로토콜).
    + IoT, 게임 서버, 고성능 네트워크 서버에 적합.


+   WebSocket
    + 브라우저 호환성 + 실시간성.
    + 채팅, 알림, 대시보드 등에 적합.

---

## ✅ 추천 학습 순서
+ TCP Socket 기초 (ServerSocket/Socket) → 네트워크 기본기
+ TCP 심화 (NIO, Netty) → 성능과 확장성
+ WebSocket 기본 (Java API) → 프로토콜 구조 이해
+ Spring WebSocket + STOMP → 실무용 구조 익히기
+ Redis 연동 및 분산 환경 확장 → 대규모 서비스 대비