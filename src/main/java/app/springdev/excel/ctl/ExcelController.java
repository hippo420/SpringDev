package app.springdev.excel.ctl;

import app.springdev.excel.svc.ExcelService;
import lombok.AllArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/excel")
@AllArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/form")
    public String uploadForm() {
        return "excel/upload";  // upload.mustache or upload.html
    }

    @RequestMapping("/upload")
    public String handleExcelUpload(@RequestParam("file") MultipartFile file, Model model) throws Exception {

        // OOM발생 -> POI 내부에서 388MB짜리 배열을 만들려고 했는데,
        // 기본 제한 100MB(100,000,000 bytes)를 초과해서 에러 발생
        // Apache POI의 IOUtils 클래스는 보안 및 OOM 방지를 위해 최대 크기를 제한함
        IOUtils.setByteArrayMaxOverride(500 * 1024 * 1024);
        excelService.readExcel(file, model);


        return "excel/uploadResult";
    }
}
