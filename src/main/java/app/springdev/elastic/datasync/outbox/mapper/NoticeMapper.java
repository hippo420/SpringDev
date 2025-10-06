package app.springdev.elastic.datasync.outbox.mapper;

import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.Noti;
import org.mapstruct.Mapper;

@Mapper
public interface NoticeMapper {

    NoticeDocument toNoticeDocument(Noti noti);
}
