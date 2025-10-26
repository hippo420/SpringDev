package app.springdev.excel.cache;

import app.springdev.excel.cache.entity.AccountBalance;
import app.springdev.excel.cache.repo.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ExcelCacheService {
    private final AccountBalanceRepository repository1;
    private final FinancialProductRepository repository2;
    private final PortfolioSummaryRepository repository3;
    private final StockStatusRepository repository4;
    private final TradeHistoryRepository repository5;

    public  void createFile(){

    }

    public void createFileNoCache() {
        String[] filename = {"고객 계좌 잔액 현황","거래 내역","금융상품소개","포트폴리오 요약","금융상품 공시정보"};

        for (int i = 1; i <= 5; i++) {
            //excel\template\template1.xlsx
            String templateFilePath = "D:\\workspace\\SpringDev\\src\\main\\resources\\excel\\template\\template" + i + ".xlsx";
            String outputFilePath = "D:\\workspace\\SpringDev\\src\\main\\resources\\excel\\result\\" + filename[i-1] + ".xlsx";
            ClassPathResource resource = new ClassPathResource(templateFilePath);
            try{
                int rowNo = 3;
                Row row =null;
                Cell cell0 = null;
                Cell cell1 = null;
                Cell cell2 = null;
                Cell cell3 = null;
                Cell cell4 = null;

                CellStyle cellStyle0 = null;
                CellStyle cellStyle1 = null;
                CellStyle cellStyle2 = null;
                CellStyle cellStyle3 = null;
                CellStyle cellStyle4 = null;

                FileInputStream fis = new FileInputStream(templateFilePath);
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0);
                fis.close();

                if(i == 1)
                {
                    List<AccountBalance> res1 = repository1.findAll();
                    for (AccountBalance vo : res1) {
                        row = sheet.createRow(rowNo++);

                        cell0 = row.createCell(0);
                        cell0.setCellValue(vo.getAccountNo());
                        cellStyle0 = cell0.getCellStyle();
                        cell0.setCellStyle(cellStyle0); // ⭐ 복사된 스타일 적용

                        cell1 = row.createCell(1);
                        cell1.setCellValue(vo.getProfitRate());
                        cellStyle1 = cell1.getCellStyle();
                        cell1.setCellStyle(cellStyle1); // ⭐ 복사된 스타일 적용

                        cell2 = row.createCell(2);
                        cell2.setCellValue(vo.getDeposit());
                        cellStyle2 = cell2.getCellStyle();
                        cell2.setCellStyle(cellStyle2); // ⭐ 복사된 스타일 적용

                        cell3 = row.createCell(3);
                        cell3.setCellValue(vo.getCustomerId());
                        cellStyle3 = cell3.getCellStyle();
                        cell3.setCellStyle(cellStyle3); // ⭐ 복사된 스타일 적용

                        cell4 = row.createCell(4);
                        cell4.setCellValue(vo.getEvalAmount());
                        cellStyle4 = cell4.getCellStyle();
                        cell4.setCellStyle(cellStyle4); // ⭐ 복사된 스타일 적용

                    }

                }
                else if(i==2)
                {

                }
                else if(i==3)
                {

                }
                else if(i==4)
                {

                }
                else if(i==5)
                {

                }

                // 6. 파일 저장
                FileOutputStream fos = new FileOutputStream(outputFilePath);
                workbook.write(fos);
                fos.close();
                workbook.close();

                System.out.println("엑셀 파일 작성이 완료되었습니다: " + outputFilePath);
            }
            catch(IOException e ){
                e.printStackTrace();
            }
        }

    }
}
