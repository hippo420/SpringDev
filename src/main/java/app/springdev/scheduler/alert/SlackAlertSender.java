package app.springdev.scheduler.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@Deprecated
@Component
@RequiredArgsConstructor
public class SlackAlertSender implements AlertSender {

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void send(String title, String message) {
        Map<String, Object> payload = Map.of(
                "text", "*"+title+"*\n"+message
        );
        restTemplate.postForObject(webhookUrl, payload, String.class);
    }
}
