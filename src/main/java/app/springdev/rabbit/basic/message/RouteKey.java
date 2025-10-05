package app.springdev.rabbit.basic.message;

public enum RouteKey {
    // 라우팅 키로 사용될 문자열을 생성자에서 설정
    CHAT("trade.chat.*"),
    NOTICE("trade.notice.*"),
    ETC("trade.*");

    private final String routingKey;

    RouteKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
