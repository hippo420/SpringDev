# 프로젝트 구조 예시
## 1) 기본 Echo 서버

## 2) 프로토콜 처리 (netty-echo-protocol)
 - 메시지 경계 처리: LineBasedFrameDecoder
 - 직렬화: Jackson 으로 JSON 처리

## 3) netty-echo-protocol (프레이밍 + JSON)
 - 테스트: send JSON line terminated with \n. Example JSON
```json
{"type":"greet","sessionId":"s1","payload":"hello"}
```

## 4) netty-echo-performance (튜닝 예제)

## 5) netty-echo-stability (IdleState, 연결 관리, Backpressure)

## 6) netty-echo-architecture (Kafka + Redis + 간단 모니터링 예시)
 - 실제 배포 시:

Kafka 연결 설정(보안, acks, retries) 튜닝
Redis 클러스터, sentinel 구성
Prometheus 노출용 HTTP 엔드포인트 (Micrometer + simple HTTP server) 추가
