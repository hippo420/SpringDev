package app.springdev.excel.ctl;

import com.github.pjfanning.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Controller
@RequestMapping("/excelStream")
public class ExcelStreamController {
    @GetMapping("/form")
    public String uploadForm() {
        return "excel/uploadStream"; // HTML or Mustache template
    }

    @PostMapping("/upload")
    public String handleStreamingExcelUpload(@RequestParam("file") MultipartFile file, Model model) {
        log.info("엑셀 스트림방식 처리 [{}]MB",file.getSize()/1024);
        List<String[]> rowList = new ArrayList<>();

        try (InputStream is = file.getInputStream();
                Workbook workbook = StreamingReader.builder()
                        .rowCacheSize(100)       // 한 번에 읽을 최대 행 수
                        .bufferSize(4096)        // 버퍼 사이즈
                        .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {

                    String cellValue = "";
                    switch (cell.getCellType()) {
                        case STRING -> cellValue = cell.getStringCellValue();
                        case NUMERIC -> cellValue = String.valueOf(cell.getNumericCellValue());
                        case BOOLEAN -> cellValue = String.valueOf(cell.getBooleanCellValue());
                        case FORMULA -> cellValue = cell.getCellFormula(); // 또는 직접 계산값 사용
                        case BLANK -> cellValue = "";
                    }
                    rowData.add(cellValue);
                }
                rowList.add(rowData.toArray(new String[0]));
            }

            // TODO: DB에 저장하거나 큐 처리 등
            // ex) excelUploadService.bulkInsert(rowList);
            log.info("업로드된 행수 : {}", rowList.size());
            model.addAttribute("rowCount", rowList.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            model.addAttribute("error", "엑셀 처리 실패: " + e.getMessage());
            return "excel/uploadResult";
        }

        return "excel/uploadResult";
    }
}
