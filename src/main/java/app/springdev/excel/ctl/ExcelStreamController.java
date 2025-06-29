package app.springdev.excel.ctl;

import app.springdev.excel.entity.MedicalRecord;
import app.springdev.excel.svc.ExcelStreamService;
import com.github.pjfanning.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
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
import java.util.Arrays;
import java.util.List;
@Slf4j
@Controller
@RequestMapping("/excelStream")
@RequiredArgsConstructor
public class ExcelStreamController {

    private final ExcelStreamService excelStreamService;
    //View
    @GetMapping("/form")
    public String uploadForm() {
        return "excel/index"; // HTML or Mustache template
    }
    //View
    @GetMapping("/form1")
    public String uploadForm1() {
        return "excel/uploadStream"; // HTML or Mustache template
    }

    //API
    //현재코드도 rowList에 모든 데이터를 저장하기 때문에 OOM발생우려 있음
    //->읽은 데이터를 메모리에 모두 올리지 않고, 일정 단위로 바로 DB에 저장해야됨
    @PostMapping("/upload")
    public void handleStreamingExcelUpload(@RequestParam("file") MultipartFile file, Model model) {
        log.info("엑셀 스트림방식 처리 [{}]MB",file.getSize()/1024);
        List<String[]> rowList = new ArrayList<>();
        long start = System.currentTimeMillis();
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
            List<MedicalRecord> records = new ArrayList<>();
            for (String[] data : rowList) {
                // 헤더 행은 건너뛰기 (ex: "보험자 구분" 같은 값)
                if (data[0].contains("보험자") || data.length < 9) continue;

                try {
                    MedicalRecord record = MedicalRecord.builder()
                            .insurerType(data[0])
                            .treatmentYear(data[1])
                            .age(data[2])
                            .gender(data[3])
                            .institutionType(data[4])
                            .institutionAddress(data[5])
                            .treatmentType(data[6])
                            .patientCount(data[7])
                            .hospitalDays(data[8])
                            .build();

                    records.add(record);
                } catch (Exception e) {
                    // 로그로 출력하고 해당 행 건너뜀
                    System.err.println("행 파싱 실패: " + Arrays.toString(data));
                }
            }
            long end = System.currentTimeMillis();
            log.info("parsing total time [{}]ms", end - start);
            // TODO: DB에 저장하거나 큐 처리 등
            start = System.currentTimeMillis();
            //excelStreamService.bulkInsert(records);
            //excelStreamService.saveAll(records);
            excelStreamService.batchInsertMybatis(records);
            end = System.currentTimeMillis();
            log.info("bulk insert total time [{}]ms", end - start);

            log.info("업로드된 행수 : {}", rowList.size());
            model.addAttribute("rowCount", rowList.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            model.addAttribute("error", "엑셀 처리 실패: " + e.getMessage());
            //return "excel/uploadResult";
        }

        //return "excel/uploadResult";
    }

    @PostMapping("/StreamExcelUpload")
    public void StreamExcelUpload(@RequestParam("file") MultipartFile file, Model model) {
        log.info("엑셀 스트림방식 처리 [{} MB]", file.getSize() / 1024 / 1024);

        long start = System.currentTimeMillis();
        int successCount = 0;
        int skipCount = 0;
        List<MedicalRecord> batchList = new ArrayList<>();

        try (InputStream is = file.getInputStream();
                Workbook workbook = StreamingReader.builder()
                        .rowCacheSize(100)       // 한 번에 읽을 행 수
                        .bufferSize(4096)        // 버퍼 사이즈
                        .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 헤더 건너뛰기

                String[] data = new String[9];
                for (int i = 0; i < 9; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    data[i] = (cell != null) ? getCellString(cell) : "";
                }

                // 필터링 (ex: 헤더 포함 또는 열 부족)
                if (data[0].contains("보험자") || data.length < 9) {
                    skipCount++;
                    continue;
                }

                try {
                    MedicalRecord record = MedicalRecord.builder()
                            .insurerType(data[0])
                            .treatmentYear(data[1])
                            .age(data[2])
                            .gender(data[3])
                            .institutionType(data[4])
                            .institutionAddress(data[5])
                            .treatmentType(data[6])
                            .patientCount(data[7])
                            .hospitalDays(data[8])
                            .build();

                    batchList.add(record);
                    successCount++;

                    if (batchList.size() >= 500) {
                        excelStreamService.batchInsertMybatis(batchList);
                        batchList.clear();
                    }
                } catch (Exception e) {
                    log.warn("행 파싱 실패: {}", Arrays.toString(data), e);
                    skipCount++;
                }
            }

            if (!batchList.isEmpty()) {
                excelStreamService.batchInsertMybatis(batchList);
            }

            long end = System.currentTimeMillis();
            log.info("전체 처리 시간: {}ms", (end - start));
            log.info("성공 행수: {}, 스킵 행수: {}", successCount, skipCount);

            model.addAttribute("rowCount", successCount);


        } catch (Exception e) {
            log.error("엑셀 처리 실패", e);
            model.addAttribute("error", "엑셀 처리 실패: " + e.getMessage());

        }
    }

    private String getCellString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula(); // 또는 계산된 값 사용
            case BLANK, _NONE, ERROR -> "";
        };
    }


}
