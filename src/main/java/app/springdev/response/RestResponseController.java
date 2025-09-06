package app.springdev.response;

import app.springdev.request.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/response")
public class RestResponseController {

    //1. @ResponseBody
    @GetMapping("/body")
    public UserDto body() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("JackBody");
        userDto.setEmail("jackBody@naver.com");
        userDto.setAge(25);

        return userDto;
    }

    //2. ResponseEntity<T>
    @GetMapping("/entity")
    public ResponseEntity<UserDto> entity() {
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("JackEntity");
        userDto.setEmail("JackEntity@gmail.com");
        userDto.setAge(50);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Token", "MyToken")
                .body(userDto);
    }

    //3. 파일 응답
    @PostMapping("/download")
    public ResponseEntity<Resource>  download() {
        ClassPathResource resource = new ClassPathResource("data/test.pdf");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/downloadstream")
    public ResponseEntity<StreamingResponseBody> downloadstream() throws IOException {
        ClassPathResource file = new ClassPathResource("data/test.pdf");
        long length = file.contentLength();

        InputStream inputStream = new FileInputStream(file.getFile().getAbsoluteFile());

        StreamingResponseBody body = outputStream -> {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.pdf")
                .contentLength(length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(body);
    }

}
