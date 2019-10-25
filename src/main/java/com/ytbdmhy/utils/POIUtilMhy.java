package com.ytbdmhy.utils;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * POI工具类
 * Created by miaohaoyun on 2019/06/xx.
 * poi 3.17; poi-ooxml 3.17; poi-scratchpad 3.17; xlsx-streamer 2.1.0
 */
public class POIUtilMhy {

    // 导出excel的每sheet的最大行数限制
    private static final int sheetSize = 1000000;

    // 默认列宽
    private static final int defaultWeight = 10 * 256;

    public static String[] readExcelFirstRow(String filePath) {
        return StringUtils.isEmpty(filePath) ? null : readExcelFirstRow(new File(filePath));
    }

    public static String[] readExcelFirstRow(File file) {
        Workbook workbook = getWorkbook(file);
        String[] firstRow = null;
        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                firstRow = new String[row.getPhysicalNumberOfCells()];
                int i = 0;
                for (Cell cell : row) {
                    firstRow[i] = cell.getStringCellValue();
                    i++;
                }
                break;
            }
        }
        try {
            if (workbook != null)
                workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firstRow;
    }

    public static List<String[]> readExcel(String filePath) {
        return StringUtils.isEmpty(filePath) ? null : readExcel(new File(filePath));
    }

    public static List<String[]> readExcel(File file) {
        List<String[]> result = new LinkedList<>();
        Workbook workbook = getWorkbook(file);
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (file.getName().toLowerCase().endsWith(".xls")) {
                    for (int rowNum = sheet.getFirstRowNum(); rowNum < sheet.getLastRowNum(); rowNum++) {
                        Row row = sheet.getRow(rowNum);
                        String[] rowValue = new String[row.getLastCellNum() - row.getFirstCellNum()];
                        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
                            Cell cell = row.getCell(cellNum);
                            if (cell == null) {
                                rowValue[cellNum] = "";
                            } else if (cell.getCellType() == 0) {
                                rowValue[cellNum] = String.valueOf(cell.getNumericCellValue());
//                            } else if (cell.getCellType() == 1) {
//                                rowValue[cellNum] = cell.getStringCellValue();
                            } else {
                                rowValue[cellNum] = cell.getStringCellValue();
                            }
                        }
                        result.add(rowValue);
                    }
                } else {
                    for (Row row : sheet) {
                        String[] rowValue = new String[row.getPhysicalNumberOfCells()];
                        int i = 0;
                        for (Cell cell : row) {
                            rowValue[i] = cell.getStringCellValue();
                            i++;
                        }
                        result.add(rowValue);
                    }
                }
            }
        }
        try {
            if (workbook != null)
                workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//    public static void exportExcel(String exportPath, List<Object[]> dataList) {
//        exportExcel(exportPath, new String[]{}, dataList);
//    }
//
//    public static void exportExcel(String exportPath, String[] firstRow, List<Object[]> dataList) {
//        if (dataList == null || dataList.size() == 0)
////            throw new NullPointerException("将要导出excel的数据为空");
//            return;
//        outPutToExportPath(complementExportPath(exportPath), getWorkbook(null, stringArrayToMap(firstRow), dataList));
//    }

    public static void formatExportExcel(HttpServletResponse response, String title, String[] firstRowMap, List<Object[]> dataList) {
        formatExportExcel(response, title, firstRowMap, dataList, false);
    }

    public static void formatExportExcel(HttpServletResponse response, String title, LinkedHashMap<String, Integer> firstRowMap, List<Object[]> dataList) {
        formatExportExcel(response, title, firstRowMap, dataList, false);
    }

    public static void formatExportExcel(HttpServletResponse response, String title, String[] firstRowMap, List<Object[]> dataList, boolean needMergeTitle) {
        formatExportExcel(response, title, stringArrayToMap(firstRowMap), dataList, needMergeTitle);
    }

    public static void formatExportExcel(HttpServletResponse response, String title, LinkedHashMap<String, Integer> firstRowMap, List<Object[]> dataList, boolean needMergeTitle) {
        outPutToResponse(response, getWorkbook(title, firstRowMap, dataList, needMergeTitle), title);
    }

    public static void formatExportExcel(String exportPath, String title, String[] firstRowMap, List<Object[]> dataList) {
        formatExportExcel(exportPath, title, stringArrayToMap(firstRowMap), dataList, false);
    }

    public static void formatExportExcel(String exportPath, String title, String[] firstRowMap, List<Object[]> dataList, boolean needMergeTitle) {
        formatExportExcel(exportPath, title, stringArrayToMap(firstRowMap), dataList, needMergeTitle);
    }

    public static void formatExportExcel(String exportPath, String title, LinkedHashMap<String, Integer> firstRowMap, List<Object[]> dataList) {
        outPutToExportPath(exportPath, getWorkbook(title, firstRowMap, dataList, false));
    }

    public static void formatExportExcel(String exportPath, String title, LinkedHashMap<String, Integer> firstRowMap, List<Object[]> dataList, boolean needMergeTitle) {
        outPutToExportPath(exportPath, getWorkbook(title, firstRowMap, dataList, needMergeTitle));
    }

    private static Workbook getWorkbook(String title, LinkedHashMap<String, Integer> firstRowMap, List<Object[]> dataList) {
        return getWorkbook(title, firstRowMap, dataList, false);
    }

    private static Workbook getWorkbook(String title, LinkedHashMap<String, Integer> firstRowMap, List<Object[]> dataList, boolean needMergeTitle) {
        boolean isLinkedList = dataList.getClass() == LinkedList.class;
        // 创建工作簿
        Workbook workbook;
        if (dataList.size() > 60000) {
            workbook = new SXSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        // 创建单元格格式
        CellStyle cellStyle = getCellStyleOfWorkbook(workbook);
        for (int index = 0,size = dataList.size() / sheetSize + 1; index < size; index++) {
            // 创建工作表
            Sheet sheet = workbook.createSheet();
            int dataFirstRow = 0;

            // 如有表头且需要合并,合并单元格
            if (needMergeTitle && !StringUtils.isEmpty(title)) {
                if (firstRowMap != null && firstRowMap.size() > 1) {
                    mergeTitle(workbook, sheet, title, firstRowMap.size(), dataFirstRow);
                    ++dataFirstRow;
                } else if (!CollectionUtils.isEmpty(dataList) && dataList.get(0) != null && dataList.get(0).length > 1) {
                    mergeTitle(workbook, sheet, title, dataList.get(0).length, dataFirstRow);
                    ++dataFirstRow;
                }
            }

            if (firstRowMap != null) {
                // 表头首行
                Row firstRow = sheet.createRow(dataFirstRow);
                firstRow.setHeight((short) (20 * 20));
                CellStyle firstCS = workbook.createCellStyle();
                XSSFFont firstFont = (XSSFFont) workbook.createFont();
                firstFont.setFontHeightInPoints((short) 11);
                firstFont.setFontName("Calibri");
//                firstFont.setColor(Font.COLOR_RED);
                firstCS.setFont(firstFont);
                setCellBorder(firstCS);
                firstCS.setVerticalAlignment(VerticalAlignment.CENTER);
                firstCS.setAlignment(HorizontalAlignment.CENTER);
                // 设置列宽
                int temp = 0;
                for (Map.Entry<String, Integer> entry : firstRowMap.entrySet()) {
                    sheet.setColumnWidth(temp, entry.getValue());
                    setCell(firstRow, firstCS, temp, entry.getKey());
                    ++temp;
                }
                ++dataFirstRow;
            }

            if (size == 1) {
                createSheetDataUseEnhance(dataList, sheet, dataFirstRow, cellStyle);
            } else {
                if (isLinkedList) {
                    LinkedList<Object[]> indexList = new LinkedList<>(dataList.subList(index * sheetSize, (index * sheetSize + sheetSize - 1) > dataList.size() ? dataList.size() : index * sheetSize + sheetSize));
                    createSheetDataUseEnhance(indexList, sheet, dataFirstRow, cellStyle);
                } else {
                    createSheetDataUseNormal(dataList, sheet, dataFirstRow, index, size, cellStyle);
                }
            }
        }
        return workbook;
    }

    private static Workbook getWorkbook(File file) {
        if (file == null)
//            throw new NullPointerException("excel不存在");
            return null;
        String fileName = file.getName();
        if (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx"))
//            throw new NullPointerException("读取的文件不是excel");
            return null;
        Workbook workbook = null;
        try {
            // 获取文件的IO流
            InputStream inputStream = new FileInputStream(file);
            if (fileName.toLowerCase().endsWith(".xls")) {
                workbook = WorkbookFactory.create(inputStream);
            } else if (fileName.toLowerCase().endsWith(".xlsx")) {
                workbook = StreamingReader.builder()
                        .rowCacheSize(100)
                        .bufferSize(4096)
                        .open(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    private static void mergeTitle(Workbook workbook, Sheet sheet, String title, int lastCol, int dataFirstRow) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, lastCol - 1);
        setRegionBorderNone(cellRangeAddress, sheet);
        // 第一行
        Row titleRow = sheet.getRow(dataFirstRow);
        // 设置行高
        titleRow.setHeight((short) (10 * 40));
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
    }

    private static void outPutToExportPath(String exportPath, Workbook workbook) {
        if (workbook != null) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(exportPath);
                workbook.write(fileOutputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeOutputStreamAndWorkbook(fileOutputStream, workbook);
            }
        }
    }

    private static void outPutToResponse(HttpServletResponse response, Workbook workbook, String title) {
        if (workbook != null) {
            OutputStream outputStream = null;
            try {
                response.setContentType("application/force-download");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(title + System.currentTimeMillis() + ".xlsx", "UTF-8"));
                outputStream = response.getOutputStream();
                workbook.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeOutputStreamAndWorkbook(outputStream, workbook);
            }
        }
    }

    private static void createSheetDataUseEnhance(List<Object[]> dataList, Sheet sheet, int i, CellStyle cellStyle) {
        if (CollectionUtils.isEmpty(dataList) || sheet == null)
            return;
        for (Object[] rowData : dataList) {
            Row row = sheet.createRow(i);
            int j = 0;
            for (Object colData : rowData) {
                setCell(row, cellStyle, j, StringUtils.isEmpty(colData) ? null : String.valueOf(colData));
                j++;
            }
            i++;
        }
    }

    private static void createSheetDataUseNormal(List<Object[]> dataList, Sheet sheet, int x, int index, int size, CellStyle cellStyle) {
        if (CollectionUtils.isEmpty(dataList) || sheet == null)
            return;
        for (int y = 0, z = (index == size - 1 ? dataList.size() + sheetSize - (sheetSize * size) : sheetSize); y < z; y++) {
            Row row = sheet.createRow(x);
            int j = 0;
            for (Object colData : dataList.get(y + (index * sheetSize))) {
                setCell(row, cellStyle, j, StringUtils.isEmpty(colData) ? null : String.valueOf(colData));
                j++;
            }
            x++;
        }
    }

    private static CellStyle getCellStyleOfWorkbook(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        setCellFontStyle(workbook, cellStyle);
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
        return cellStyle;
    }

    private static void setCellFontStyle(Workbook workbook, CellStyle cellStyle) {
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
    }

    private static void setCell(Row row, CellStyle cellStyle, int cellNum, String cellValue) {
        Cell cell = row.createCell(cellNum, CellType.STRING);
        if (cellStyle != null)
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

    private static String complementExportPath(String exportPath) {
        return !StringUtils.isEmpty(exportPath) && !exportPath.toLowerCase().endsWith(".xls") && !exportPath.toLowerCase().endsWith(".xlsx") ? exportPath + ".xlsx" : exportPath;
    }

    private static void closeOutputStreamAndWorkbook(OutputStream outputStream, Workbook workbook) {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            if (workbook != null)
                workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LinkedHashMap<String, Integer> stringArrayToMap(String[] firstRow) {
        if (firstRow == null || firstRow.length == 0)
            return null;
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        for (String string : firstRow) {
            map.put(string, defaultWeight);
        }
        return map;
    }

    public static void main(String[] args) {
        System.out.println("start");

        long startTime = System.currentTimeMillis();
        LinkedList<Object[]> data = new LinkedList<>();
        String[] strings;
        for (int i = 0; i < 3500000; i++) {
            strings = new String[4];
            strings[0] = i + 1 + "-1";
            strings[1] = i + 1 + "-2";
            strings[2] = i + 1 + "-3";
            strings[3] = i + 1 + "-4";
            data.add(strings);
        }
        long a1 = System.currentTimeMillis();
        System.out.println("生成dataList用时:" + (a1 - startTime) + "毫秒");

        formatExportExcel("C:\\Users\\Administrator\\Desktop\\MapTest.xlsx", "test", new String[]{}, data, true);
        // 导出350万条4列小字符串
        // ArrayList 耗时 33.445秒
        // LinkedList 耗时 30.928秒
        long a2 = System.currentTimeMillis();
        System.out.println("生成excel用时:" + (a2 - a1) + "毫秒");
    }
}
