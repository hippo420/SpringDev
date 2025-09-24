package app.springdev.socket.tcp.netty.advanced;

public class MyMessage {
    private String type;
    private String sessionId;
    private String payload;

    public MyMessage() {}
    public MyMessage(String type, String sessionId, String payload) {
        this.type = type;
        this.sessionId = sessionId;
        this.payload = payload;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    @Override
    public String toString() {
        return "MyMessage{" +
                "type='" + type + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
