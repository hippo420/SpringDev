package app.springdev.rabbit.common.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageWrapper<T> {
    private String messageId = UUID.randomUUID().toString();
    private String service = "NOTI-SERVICE";
    private LocalDateTime createdAt = LocalDateTime.now();
    private T payload;
}
