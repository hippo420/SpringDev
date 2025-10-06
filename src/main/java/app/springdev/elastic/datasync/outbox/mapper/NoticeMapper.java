package app.springdev.elastic.datasync.outbox.mapper;

import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.Noti;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface NoticeMapper {

    NoticeDocument toNoticeDocument(Noti noti);
}
