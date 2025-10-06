package app.springdev.objectmapping.mapstruct;

import app.springdev.objectmapping.dto.Company;
import app.springdev.objectmapping.dto.ExtraDto;
import app.springdev.objectmapping.dto.SourceDto;
import app.springdev.objectmapping.dto.TargetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

//ibatis랑 구분처리 componentModel
@Mapper(imports = UUID.class,componentModel = "spring")
public interface DtoMapper {
    //DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    @Mapping(source="sourceDto.contents",target="content")
    @Mapping(source="sourceDto.sender",target="sender", defaultValue = "SYSTEM")
    @Mapping(source="sourceDto.views",target="views",ignore=true)
    @Mapping(source="sourceDto.company",target="company",qualifiedByName = "typeToEnum")
    @Mapping(source="extraDto.created",target="created")
    @Mapping(source="extraDto.updated",target="updated")
    TargetDto toTargetDto(SourceDto sourceDto, ExtraDto extraDto, String add_param1, String add_param2);

    // SourceDto -> TargetDto 매핑

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
