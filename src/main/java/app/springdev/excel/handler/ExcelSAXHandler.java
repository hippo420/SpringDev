package app.springdev.excel.handler;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class ExcelSAXHandler extends DefaultHandler {
    private final List<String[]> rowList = new ArrayList<>();
    private List<String> currentRow = new ArrayList<>();
    private boolean isValue = false;
    private StringBuilder value = new StringBuilder();
    private String cellType;
    private SharedStringsTable sst;

    public ExcelSAXHandler(SharedStringsTable sst) {
        this.sst = sst;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "row" -> currentRow = new ArrayList<>();
            case "c" -> cellType = attributes.getValue("t"); // 셀 타입: s(shared), str, b, n 등
            case "v" -> {
                value.setLength(0);
                isValue = true;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (isValue) value.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("v".equals(qName)) {
            String raw = value.toString();
            String finalValue = switch (cellType != null ? cellType : "") {
                case "s" -> sst.getItemAt(Integer.parseInt(raw)).getString();
                case "b" -> raw.equals("1") ? "TRUE" : "FALSE";
                default -> raw; // n (number), str, inlineStr 등
            };
            currentRow.add(finalValue);
            isValue = false;
        } else if ("row".equals(qName)) {
            rowList.add(currentRow.toArray(new String[0]));
        }
    }

    public List<String[]> getRowList() {
        return rowList;
    }
}
