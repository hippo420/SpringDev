package app.springdev.excel.ctl;

import app.springdev.excel.svc.ExcelService;
import lombok.AllArgsConstructor;
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


        excelService.readExcel(file, model);


        return "excel/uploadResult";
    }
}
