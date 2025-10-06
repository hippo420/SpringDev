package app.springdev.objectmapping;

import app.springdev.objectmapping.dto.ExtraDto;
import app.springdev.objectmapping.dto.SourceDto;
import app.springdev.objectmapping.mapstruct.MapStructService;
import app.springdev.objectmapping.modelmapper.ModelMapperService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mapper")
@AllArgsConstructor
public class ObjMapperController {

    private final MapStructService mapStructService;
    private final ModelMapperService modelMapperService;

    @RequestMapping("/model")
    @ResponseBody
    public String model(@RequestBody SourceDto sourceDto) {
        return modelMapperService.modelMapper(sourceDto);
    }

    @RequestMapping("/map")
    @ResponseBody
    public String map(@RequestBody SourceDto sourceDto) {
        ExtraDto extraDto = new ExtraDto("20240101","20241001");
        String param1 = "파라미터1";
        String param2 = "파라미터2";
        return mapStructService.toTargetDto(sourceDto,extraDto,param1,param2);
    }
}
