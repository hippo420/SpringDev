package app.springdev.excel.ctl;

import com.github.pjfanning.xlsx.StreamingReader;
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

@Controller
@RequestMapping("excel")
public class ExcelStreamController {
    @GetMapping("/form")
    public String uploadForm() {
        return "excel/upload"; // HTML or Mustache template
    }

    @PostMapping("/upload")
    public String handleStreamingExcelUpload(@RequestParam("file") MultipartFile file, Model model) {
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
                    cell.setCellType(CellType.STRING);
                    rowData.add(cell.getStringCellValue());
                }
                rowList.add(rowData.toArray(new String[0]));
            }

            // TODO: DB에 저장하거나 큐 처리 등
            // ex) excelUploadService.bulkInsert(rowList);

            model.addAttribute("rowCount", rowList.size());
        } catch (Exception e) {
            model.addAttribute("error", "엑셀 처리 실패: " + e.getMessage());
            return "excel/uploadResult";
        }

        return "excel/uploadResult";
    }
}
