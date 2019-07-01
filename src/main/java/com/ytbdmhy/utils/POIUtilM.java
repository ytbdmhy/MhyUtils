package com.ytbdmhy.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class POIUtilM {

    /**
     * 大名县导出excel
     * @param filePath 导出的文件路径
     * @param dataList 文件内容
     * @throws Exception
     */
    public static void dmxExportExcel(String filePath, List<String[]> dataList) {
        // 创建工作簿
        Workbook workbook;
        if (dataList.size() > 60000) {
            workbook = new SXSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        // 创建工作表
        Sheet sheet = workbook.createSheet();

        // 合并单元格
        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(0, 0, 0, 8);
        setRegionBorderNone(cellRangeAddress1, sheet);
        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(1, 1, 4, 8);
        setRegionBorderNone(cellRangeAddress2, sheet);
        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(2, 3, 0, 0);
        setRegionBorder(cellRangeAddress3, sheet);
        CellRangeAddress cellRangeAddress4 = new CellRangeAddress(2, 2, 1, 3);
        setRegionBorder(cellRangeAddress4, sheet);
        CellRangeAddress cellRangeAddress5 = new CellRangeAddress(2, 3, 4, 4);
        setRegionBorder(cellRangeAddress5, sheet);
        CellRangeAddress cellRangeAddress6 = new CellRangeAddress(2, 3, 5, 5);
        setRegionBorder(cellRangeAddress6, sheet);
        CellRangeAddress cellRangeAddress7 = new CellRangeAddress(2, 3, 6, 6);
        setRegionBorder(cellRangeAddress7, sheet);
        CellRangeAddress cellRangeAddress8 = new CellRangeAddress(2, 3, 7, 7);
        setRegionBorder(cellRangeAddress8, sheet);
        CellRangeAddress cellRangeAddress9 = new CellRangeAddress(2, 3, 8, 8);
        setRegionBorder(cellRangeAddress9, sheet);

        // 给A2至I2筛选功能
//        CellRangeAddress screen = CellRangeAddress.valueOf("A2:I2");
//        sheet.setAutoFilter(screen);

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
        // 设置列宽
        sheet.setColumnWidth(0, 12 * 256);
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(2, 12 * 256);
        sheet.setColumnWidth(3, 12 * 256);
        sheet.setColumnWidth(4, 12 * 256);
        sheet.setColumnWidth(5, 12 * 256);
        sheet.setColumnWidth(6, 12 * 256);
        sheet.setColumnWidth(7, 12 * 256);
        sheet.setColumnWidth(8, 12 * 256);

        // 第一行
        Row firstRow = sheet.getRow(0);
        // 设置行高
        firstRow.setHeight((short) (20 * 40));
        Cell firstRowCell = firstRow.createCell(0, CellType.STRING);
        CellStyle firstCS = workbook.createCellStyle();
        XSSFFont titleFont = (XSSFFont) workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleFont.setFontName("Calibri");
        firstCS.setFont(titleFont);
        firstCS.setVerticalAlignment(VerticalAlignment.CENTER);
        firstCS.setAlignment(HorizontalAlignment.CENTER);
        firstRowCell.setCellStyle(firstCS);
        firstRowCell.setCellValue("大名县2019年各乡镇社保卡信息采集进度表");

        // 第二行
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
        Row secondRow = sheet.getRow(1);
        secondRow.setHeight((short) (20 * 20));
        Cell dateTime = secondRow.createCell(4, CellType.STRING);
        CellStyle secondCS = workbook.createCellStyle();
        setCellBorderNone(secondCS);
        secondCS.setFont(font);
        secondCS.setVerticalAlignment(VerticalAlignment.CENTER);
        secondCS.setAlignment(HorizontalAlignment.CENTER);
        dateTime.setCellStyle(secondCS);
        dateTime.setCellValue(dateFormat.format(new Date()));

        // 第三行
        Row thirdRow = sheet.getRow(2);
        thirdRow.setHeight((short) (20 * 20));
        CellStyle thirdCS = workbook.createCellStyle();
        XSSFFont thirdFont = (XSSFFont) workbook.createFont();
        thirdFont.setFontHeightInPoints((short) 11);
        thirdFont.setBold(true);
        thirdFont.setFontName("Calibri");
        thirdCS.setFont(thirdFont);
        setCellBorder(thirdCS);
        thirdCS.setVerticalAlignment(VerticalAlignment.CENTER);
        thirdCS.setAlignment(HorizontalAlignment.CENTER);
        setCell(thirdRow, thirdCS, 0, "乡    镇");
        setCell(thirdRow, thirdCS, 1, "2019年采集任务");
        setCell(thirdRow, thirdCS, 4, "新增采集");
        setCell(thirdRow, thirdCS, 5, "总采集数");
        setCell(thirdRow, thirdCS, 6, "采集率");
        setCell(thirdRow, thirdCS, 7, "推送数量");
        // 第三行第八列
        CellStyle thirdEightCS = workbook.createCellStyle();
        XSSFFont thirdEightFont = (XSSFFont) workbook.createFont();
        thirdEightFont.setFontHeightInPoints((short) 10);
        thirdEightFont.setBold(true);
        thirdEightFont.setFontName("Calibri");
        thirdEightCS.setFont(thirdEightFont);
        thirdEightCS.setWrapText(true);
        setCellBorder(thirdEightCS);
        thirdEightCS.setVerticalAlignment(VerticalAlignment.CENTER);
        thirdEightCS.setAlignment(HorizontalAlignment.CENTER);
        setCell(thirdRow, thirdEightCS, 8, "有过采集记录总数(含做过推送的)");

        // 第四行
        Row fourthRow = sheet.getRow(3);
        fourthRow.setHeight((short) (20 * 30));
        setCell(fourthRow, thirdCS, 1, "采集任务");
        setCell(fourthRow, thirdCS, 2, "已采集");
        setCell(fourthRow, thirdCS, 3, "任务完成率");

        // 循环输入dataList内容
        int i = 4;
        Row row;
        int rowLength = dataList.get(0).length;
        for (String[] rowData : dataList) {
            row = sheet.createRow(i);
            for (int j = 0; j < 9; j++) {
                if (j < rowLength) {
                    setCell(row, cellStyle, j, rowData[j]);
                } else {
                    setCell(row, cellStyle, j, "");
                }
            }
            ++i;
        }

        if (filePath.indexOf(".xls") < 0)
            filePath += ".xlsx";
        if (workbook != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                workbook.write(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private static void setCellBorderNone(CellStyle cellStyle) {
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
    }

    private static void setRegionBorder(CellRangeAddress cellRangeAddress, Sheet sheet) {
        sheet.addMergedRegion(cellRangeAddress);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet); // 上边框
    }

    private static void setRegionBorderNone(CellRangeAddress cellRangeAddress, Sheet sheet) {
        sheet.addMergedRegion(cellRangeAddress);
        RegionUtil.setBorderBottom(BorderStyle.NONE, cellRangeAddress, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.NONE, cellRangeAddress, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.NONE, cellRangeAddress, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.NONE, cellRangeAddress, sheet); // 上边框
    }

    public static void main(String[] args) {
        List<String[]> data = new ArrayList();
        String[] strings = new String[]{"test1", "test2", "test3"};
        for (int i=0;i<100000;i++) {
            data.add(strings);
        }
        dmxExportExcel("D:/WorkFile/dmx.xlsx", data);
    }
}
