package com.ytbdmhy.utils;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @Copyright:
 * @Description: POI工具类
 * @author: miaohaoyun
 * @since:
 * @history: created in 10:02 2019-06-11 created by miaohaoyun
 * @Remarks: poi 3.17; poi-ooxml 3.17; poi-scratchpad 3.17
 */
public class POIUtilMhy2 {

    // 导出excel的每sheet的最大行数限制
    private static final int sheetSize = 800000;

    public static void exportExcel(String exportPath, String[] firstRow, LinkedList<Object[]> dataList) {
        if (dataList == null || dataList.size() == 0)
            return;
        if (!exportPath.toLowerCase().endsWith(".xls") && !exportPath.toLowerCase().endsWith(".xlsx"))
            exportPath += ".xlsx";
        // 创建工作簿
        Workbook workbook;
        if (dataList.size() > 60000) {
            workbook = new SXSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        int index = 0;
        Sheet sheet = null;
        boolean hasFirst = firstRow.length > 0;
        int i = 0;
        for (Object[] data : dataList) {
            if (index % sheetSize == 0) {
                sheet = workbook.createSheet();
                // 创建单元格格式
                CellStyle cellStyle = workbook.createCellStyle();
                // 创建字体
                Font font = workbook.createFont();
                // 设置字体大小
                font.setFontHeightInPoints((short) 11);
                // 设置字体是否加粗
                font.setBold(true);
                // 设置字体名称
                font.setFontName("Calibri");
                // 在样式应用设置的字体
                cellStyle.setFont(font);
                // 设置字体换行
                cellStyle.setWrapText(false);
                if (firstRow.length > 0) hasFirst = true;
                i = 0;
            }
            if (hasFirst) {
                Row row = sheet.createRow(0);
                // 设置行高
                row.setHeight((short) (20 * 15));
                for (String colData : firstRow) {
                    // 创建对应的单元格并设置单元格的数据类型为文本
                    Cell cell = row.createCell(i, CellType.STRING);
                    // 设置单元格的数值
                    cell.setCellValue(StringUtils.isEmpty(colData) ? null : colData);
                    i++;
                }
                i = 1;
                hasFirst = false;
            }
            Row row = sheet.createRow(i);
            int j = 0;
            for (Object colData : data) {
                Cell cell = row.createCell(j, CellType.STRING);
                cell.setCellValue(StringUtils.isEmpty(colData) ? null : String.valueOf(colData));
                j++;
            }
            i++;
            ++index;
        }
        POIUtilMhy.outPutStreamExcel(workbook, exportPath);
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("start");
        long startTime = System.currentTimeMillis();

        LinkedList<Object[]> data = new LinkedList<>();
        String[] strings;
        for (int i = 0; i < 3500000; i++) {
            strings = new String[4];
            strings[0] = "test-" + (i + 1) + "-1";
            strings[1] = "test-" + (i + 1) + "-2";
            strings[2] = "test-" + (i + 1) + "-3";
            strings[3] = "test-" + (i + 1) + "-4";
            data.add(strings);
        }
        exportExcel("C:\\Users\\Administrator\\Desktop/MapTest.xlsx"
                , new String[]{"title-1", "title-2", "title-3", "title-4"}
                , data);
        // 导出350万条4列小字符串
        // LinkedList专用 耗时 31.742秒，没有提效，弃用

        System.out.println("over,用时:" + (System.currentTimeMillis() - startTime) + "毫秒");
    }
}
