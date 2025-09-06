package app.springdev.response;

import app.springdev.request.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/response")
public class ViewResponseController {

    //1.뷰 반환
    @GetMapping("/view")
    public String view(Model model) {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Jack");
        userDto.setEmail("jack@gmail.com");
        userDto.setAge(25);

        model.addAttribute("user", userDto);
        return "/response/sample";
    }


    @GetMapping("/rest")
    public String view1(Model model) {


        return "/response/sample1";
    }

}
