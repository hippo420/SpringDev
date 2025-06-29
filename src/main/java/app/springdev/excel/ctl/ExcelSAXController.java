package app.springdev.excel.ctl;


import app.springdev.excel.handler.ExcelSAXHandler;
import app.springdev.excel.svc.ExcelSAXService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/excelSAX")
@RequiredArgsConstructor
public class ExcelSAXController {

    private final ExcelSAXService excelSAXService;
    //View
    @GetMapping("/form")
    public String uploadForm() {
        return "excel/indexSAX"; // HTML or Mustache template
    }
    //View
    @GetMapping("/form1")
    public String uploadForm1() {
        return "excel/uploadSAX"; // HTML or Mustache template
    }

    //API
    @PostMapping("/upload")
    public void readExcel(MultipartFile file, Model model) {
        IOUtils.setByteArrayMaxOverride(500 * 1024 * 1024);
        try (OPCPackage pkg = OPCPackage.open(file.getInputStream())) {
            XSSFReader reader = new XSSFReader(pkg);
            SharedStringsTable sst = (SharedStringsTable) reader.getSharedStringsTable();

            XMLReader parser = XMLReaderFactory.createXMLReader();
            ExcelSAXHandler handler = new ExcelSAXHandler(sst);
            parser.setContentHandler(handler);

            try (InputStream sheet = reader.getSheetsData().next()) {
                parser.parse(new InputSource(sheet));
            }

            List<String[]> rows = handler.getRowList();
            log.info("업로드된 행수: {}", rows.size());
            model.addAttribute("rowCount", rows.size());

            // TODO: DB 저장 등
        } catch (Exception e) {
            log.error("엑셀 처리 오류: {}", e.getMessage(), e);
            model.addAttribute("error", "엑셀 처리 실패: " + e.getMessage());
        }
    }
}
