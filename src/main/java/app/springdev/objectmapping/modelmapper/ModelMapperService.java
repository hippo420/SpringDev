package app.springdev.objectmapping.modelmapper;

import app.springdev.objectmapping.dto.SourceDto;
import app.springdev.objectmapping.dto.TargetDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@AllArgsConstructor
public class ModelMapperService {

    private final ModelMapper modelMapper;

    public String modelMapper(SourceDto sourceDto) {
        log.info("매핑전[{}] : {}",sourceDto.getClass().getSimpleName(), sourceDto);

        TargetDto targetDto = modelMapper.map(sourceDto, TargetDto.class);

        log.info("매핑후[{}] : {}",targetDto.getClass().getSimpleName(), targetDto);
        return targetDto.toString();
    }
}
