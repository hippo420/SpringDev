package app.springdev.excel.svc;

import com.github.pjfanning.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
@Service
public class ExcelStreamService {

    public void readLargeExcel(String filePath) throws Exception {
        try (InputStream is = new FileInputStream(new File(filePath));
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)       // 메모리에 유지할 최대 row 수
                     .bufferSize(4096)        // InputStream buffer size
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    System.out.print(cell.toString() + "\t");
                }
                System.out.println();
            }
        }
    }
}
