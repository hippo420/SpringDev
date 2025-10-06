package app.springdev.elastic.datasync.outbox.hook;

import app.springdev.elastic.datasync.Noti;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotiCreatedEvent {
    private final Noti noti;
}
