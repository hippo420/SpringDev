package app.springdev.objectmapping.mapstruct;

import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.Noti;
import app.springdev.objectmapping.dto.ExtraDto;
import app.springdev.objectmapping.dto.SourceDto;
import app.springdev.objectmapping.dto.TargetDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class MapStructService {

    private final DtoMapper dtoMapper;

    public String toTargetDto(SourceDto sourceDto, ExtraDto extraDto,String add_param1, String add_param2) {
        log.info("매핑전[{}] : {}",sourceDto.getClass().getSimpleName(), sourceDto);
        log.info("매핑전[{}] : {}",extraDto.getClass().getSimpleName(), extraDto);
        log.info("추가 파라미터1 : {}",add_param1);
        log.info("추가 파라미터2 : {}",add_param2);

        TargetDto targetDto = dtoMapper.toTargetDto(sourceDto,extraDto,add_param1,add_param2);

        log.info("매핑후[{}] : {}",targetDto.getClass().getSimpleName(), targetDto);
        return targetDto.toString();
    }

    public String mapStructMapperNormal(SourceDto sourceDto) {
        log.info("매핑전[{}] : {}",sourceDto.getClass().getSimpleName(), sourceDto);

        TargetDto targetDto = dtoMapper.toTargetDtoNormal(sourceDto);

        log.info("매핑후[{}] : {}",targetDto.getClass().getSimpleName(), targetDto);
        return targetDto.toString();
    }

    public String mapStructMapperExtra(SourceDto sourceDto, ExtraDto extraDto) {
        log.info("매핑전[{}] : {}",sourceDto.getClass().getSimpleName(), sourceDto);
        log.info("매핑전[{}] : {}",extraDto.getClass().getSimpleName(), extraDto);

        TargetDto targetDto = dtoMapper.toTargetDtoExtra(sourceDto,extraDto);

        log.info("매핑후[{}] : {}",targetDto.getClass().getSimpleName(), targetDto);
        return targetDto.toString();
    }

    public String mapStructMapperParam(SourceDto sourceDto, ExtraDto extraDto,String add_param1, String add_param2) {
        log.info("매핑전[{}] : {}",sourceDto.getClass().getSimpleName(), sourceDto);
        log.info("매핑전[{}] : {}",extraDto.getClass().getSimpleName(), extraDto);
        log.info("추가 파라미터1 : {}",add_param1);
        log.info("추가 파라미터2 : {}",add_param2);

        TargetDto targetDto = dtoMapper.toTargetDtoParam(sourceDto,extraDto,add_param1,add_param2);

        log.info("매핑후[{}] : {}",targetDto.getClass().getSimpleName(), targetDto);
        return targetDto.toString();
    }
}
