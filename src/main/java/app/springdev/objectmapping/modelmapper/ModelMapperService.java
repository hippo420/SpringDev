package app.springdev.objectmapping.modelmapper;

import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.Noti;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ModelMapperService {

    public void modelMapper(Noti noti) {
        log.info("매핑전[{}] : {}",noti.getClass().getSimpleName(), noti);

        NoticeDocument noticeDocument = new NoticeDocument();

        log.info("매핑후[{}] : {}",noticeDocument.getClass().getSimpleName(), noticeDocument);
    }
}
