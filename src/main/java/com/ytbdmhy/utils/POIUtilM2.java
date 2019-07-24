package com.ytbdmhy.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 合并单元格的POI demo
 */
public class POIUtilM2 {

    /**
     * 按照格式化导出excel
     * @param response
     * @param title 标题
     * @param firstRowMap 表头首行
     * @param dataList 文件内容
     * @throws Exception
     */
    public static void formatExportExcel(HttpServletResponse response, String title, TreeMap<String, Integer> firstRowMap, List<String[]> dataList) {
        // 创建工作簿
        Workbook workbook;
        if (dataList.size() > 60000) {
            workbook = new SXSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        // 创建工作表
        Sheet sheet = workbook.createSheet();

        // 如有表头,合并单元格
        if (firstRowMap != null && firstRowMap.size() > 1) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(1, 1, 0, firstRowMap.size());
            setRegionBorderNone(cellRangeAddress, sheet);
        }

        // 创建单元格格式
        CellStyle cellStyle = workbook.createCellStyle();
        // 创建字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 设置字体是否加粗
//        font.setBold(true);
        // 设置字体名称
        font.setFontName("Calibri");
        // 在样式应用设置的字体
        cellStyle.setFont(font);
        // 设置字体换行
//        cellStyle.setWrapText(true);
        // 设置边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 内容水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 内容垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        int dataFirstRow = 0;

        if (!StringUtils.isEmpty(title)) {
            // 第一行
            Row titleRow = sheet.getRow(dataFirstRow);
            // 设置行高
            titleRow.setHeight((short) (20 * 40));
            Cell titleRowCell = titleRow.createCell(0, CellType.STRING);
            CellStyle titleCS = workbook.createCellStyle();
            XSSFFont titleFont = (XSSFFont) workbook.createFont();
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            titleFont.setFontName("Calibri");
            titleCS.setFont(titleFont);
            titleCS.setVerticalAlignment(VerticalAlignment.CENTER);
            titleCS.setAlignment(HorizontalAlignment.CENTER);
            titleRowCell.setCellStyle(titleCS);
            titleRowCell.setCellValue(title);
            ++dataFirstRow;
        }

        if (firstRowMap != null) {
            // 表头首行
            Row firstRow = sheet.getRow(dataFirstRow);
            firstRow.setHeight((short) (20 * 20));
            CellStyle firstCS = workbook.createCellStyle();
            XSSFFont firstFont = (XSSFFont) workbook.createFont();
            firstFont.setFontHeightInPoints((short) 11);
            firstFont.setFontName("Calibri");
            firstCS.setFont(firstFont);
            setCellBorder(firstCS);
            firstCS.setVerticalAlignment(VerticalAlignment.CENTER);
            firstCS.setAlignment(HorizontalAlignment.CENTER);
            // 设置列宽
            AtomicInteger index = new AtomicInteger(0);
            firstRowMap.forEach((content, width) -> {
                sheet.setColumnWidth(index.get(), width);
                setCell(firstRow, firstCS, index.get(), content);
                index.incrementAndGet();
            });
            sheet.setColumnWidth(0, 12 * 256);
            ++dataFirstRow;
        }

        // 循环输入dataList内容
        int rowLength = dataList.get(0).length;
        for (String[] rowData : dataList) {
            Row row = sheet.createRow(dataFirstRow);
            for (int j = 0; j < 9; j++) {
                if (j < rowLength) {
                    setCell(row, cellStyle, j, rowData[j]);
                } else {
                    setCell(row, cellStyle, j, "");
                }
            }
            ++dataFirstRow;
        }

    }

    private static void setCell(Row row, CellStyle cellStyle, int cellNum, String cellValue) {
        Cell cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cellValue);
    }

    private static void setCellBorder(CellStyle cellStyle) {
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
    }

    private static void setRegionBorderNone(CellRangeAddress cellRangeAddress, Sheet sheet) {
        sheet.addMergedRegion(cellRangeAddress);
        RegionUtil.setBorderBottom(BorderStyle.NONE, cellRangeAddress, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.NONE, cellRangeAddress, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.NONE, cellRangeAddress, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.NONE, cellRangeAddress, sheet); // 上边框
    }
}
