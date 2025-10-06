package app.springdev.objectmapping.mapstruct;

import app.springdev.objectmapping.dto.Company;
import app.springdev.objectmapping.dto.ExtraDto;
import app.springdev.objectmapping.dto.SourceDto;
import app.springdev.objectmapping.dto.TargetDto;
import org.mapstruct.*;

import java.util.UUID;

/**
 * componentModel : 매퍼를 빈으로 만들어야 하는 경우, 아래와 같이 설정하면 빈으로 등록
 * unmappedTargetPolicy : Target 필드는 존재하는데 source의 필드가 없는 경우에 대한 정책
    #ERROR : 매핑 대상이 없는 경우, 빌드 시 Error 이 발생
    #WARN : 매핑 대상이 없는 경우, 빌드 시 warn 이 발생
    #IGNORE  : 매핑 대상이 없는 경우 무시하고 매핑

 * nullValueMapMappingStrategy : source가 null 인 경우에 제어할 수 있는 null 정책
    #RETURN_NULL    : source가 null 일 경우, target을 null 로 설정
    #RETURN_DEFAULT : source가 null 일 경우, default 값으로 설정
 * nullValueIterableMappingStrategy : source가 null 인 경우 iterables나 map에 해당되는 정책
    #RETURN_NULL    : source가 null 일 경우, target을 null 로 설정
    #RETURN_DEFAULT : iterable에는 collection이 매핑 되며, map은 빈 map 으로 매핑
 */
@Mapper(imports = UUID.class
        , componentModel = "spring"
        , unmappedTargetPolicy = ReportingPolicy.IGNORE //ERROR,WARN
        , nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_NULL     //RETURN_DEFAULT
        , nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL //RETURN_DEFAULT
)
public interface DtoMapper {
    //DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    @Mapping(source="sourceDto.contents",target="content")
    @Mapping(source="sourceDto.sender",target="sender", defaultValue = "SYSTEM")
    @Mapping(source="sourceDto.views",target="views",ignore=true)
    @Mapping(source="sourceDto.company",target="company",qualifiedByName = "typeToEnum")
    @Mapping(source="extraDto.created",target="created")
    @Mapping(source="extraDto.updated",target="updated")
    TargetDto toTargetDto(SourceDto sourceDto, ExtraDto extraDto, String add_param1, String add_param2);


    @Mapping(source="sourceDto.company",target="company",ignore=true)    //필드명 다른경우 매핑
    @Mapping(source="sourceDto.contents",target="content")
    @Mapping(source="sourceDto.sender",target="sender", defaultValue = "SYSTEM")     //기본값
    @Mapping(source="sourceDto.views",target="views",ignore=true) //특정 컬럼 무시
    @Mapping(source="sourceDto.uniqueValue",target="uniqueValue", defaultExpression = "java(UUID.randomUUID().toString())")
    TargetDto toTargetDtoNormal(SourceDto sourceDto);

    //추가 DTO매핑
    @Mapping(source="sourceDto.company",target="company",ignore=true)
    @Mapping(source="sourceDto.contents",target="content")
    @Mapping(source="extraDto.created",target="created")
    @Mapping(source="extraDto.updated",target="updated")
    TargetDto toTargetDtoExtra(SourceDto sourceDto, ExtraDto extraDto);

    @Mapping(source="sourceDto.company",target="company",ignore=true)
    @Mapping(source="sourceDto.contents",target="content")
    @Mapping(source="extraDto.created",target="created")
    @Mapping(source="extraDto.updated",target="updated")
    TargetDto toTargetDtoParam(SourceDto sourceDto, ExtraDto extraDto, String add_param1, String add_param2);

    //custom Setter
    @Mapping(source="sourceDto.company",target="company",qualifiedByName = "typeToEnum")
    @Mapping(source="sourceDto.contents",target="content")
    TargetDto toTargetDtoCustomMethod(SourceDto sourceDto);

    @Named("typeToEnum")
    default Company typeToEnum(String type) {
        switch (type.toUpperCase()) {
            case "SKT":
                return Company.SKT;
            case "KT":
                return Company.KT;
            default:
                return Company.LGU;
        }
    }

}
