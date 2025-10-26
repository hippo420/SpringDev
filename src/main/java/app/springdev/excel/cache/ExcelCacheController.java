package app.springdev.excel.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/excel-cache")
public class ExcelCacheController {

    @Autowired
    private ExcelCacheService excelCacheService;
    @GetMapping("/createFile")
    public  void createFile(){
        excelCacheService.createFile();
    }

    @GetMapping("/createFileNoCache")
    public  void createFileNoCache(){
        excelCacheService.createFileNoCache();
    }
}
