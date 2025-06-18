package app.springdev.excel.svc;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class ExcelService {

    public void readExcel(MultipartFile file, Model model) throws Exception {
        List<String[]> rowList = new ArrayList<>();


        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> cellList = new ArrayList<>();
                for (Cell cell : row) {
                    cell.setCellType(CellType.STRING); // 모든 셀을 문자열로 처리
                    cellList.add(cell.getStringCellValue());
                }
                rowList.add(cellList.toArray(new String[0]));
            }

            // TODO: 여기에 대용량 데이터를 DB에 일괄 저장 처리
            // ex) service.bulkInsert(rowList);
            log.info("업로드된 행수 : {}", rowList.size());
            model.addAttribute("rowCount", rowList.size());
        }catch (Exception e){
            log.error("엑셀 파일 처리 중 오류 발생 -> ",e.getMessage());
            model.addAttribute("error", "엑셀 파일 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
