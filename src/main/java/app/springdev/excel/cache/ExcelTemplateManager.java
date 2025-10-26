package app.springdev.excel.cache;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExcelTemplateManager implements Closeable {

    private final Map<Integer, byte[]> templateCache = new ConcurrentHashMap<>();

    public XSSFWorkbook getTemplate(int templateNo) throws IOException {
        // 캐시에 없다면 로드
        if (!templateCache.containsKey(templateNo)) {
            synchronized (this) {
                if (!templateCache.containsKey(templateNo)) {
                    try (InputStream is = getClass().getResourceAsStream("/excel/template" + templateNo + ".xlsx");
                         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        if (is == null) {
                            throw new FileNotFoundException("Template not found: template" + templateNo + ".xlsx");
                        }
                        is.transferTo(baos);
                        templateCache.put(templateNo, baos.toByteArray());
                    }
                }
            }
        }

        // 캐시된 바이트 배열로 새로운 Workbook 생성
        return new XSSFWorkbook(new ByteArrayInputStream(templateCache.get(templateNo)));
    }

    @Override
    public void close() {
        templateCache.clear();
    }
}
