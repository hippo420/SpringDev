package app.springdev.objectmapping.modelmapper;

import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.Noti;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    // ModelMapper 자체를 공통 빈으로 등록
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // 공통 기본 설정만 유지 (예: null 스킵, 매칭 전략 등)
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        return mapper;
    }
}
