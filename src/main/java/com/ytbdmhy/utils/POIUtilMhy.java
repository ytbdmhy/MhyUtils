package com.ytbdmhy.utils;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Copyright:
 * @Description: POI工具类
 * @author: miaohaoyun
 * @since:
 * @history: created in 10:02 2019-06-11 created by miaohaoyun
 * @Remarks: poi 3.17; poi-ooxml 3.17; poi-scratchpad 3.17; xlsx-streamer 2.1.0
 */
public class POIUtilMhy {

    // 导出excel的每sheet的最大行数限制
    private static final int sheetSize = 800000;

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
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firstRow;
    }

    public static List readExcel(String filePath) {
        return StringUtils.isEmpty(filePath) ? null : readExcel(new File(filePath));
    }

    public static List readExcel(File file) {
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
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void exportExcel(String exportPath, List<Object[]> dataList) {
        exportExcel(exportPath, new String[]{}, dataList);
    }

    public static void exportExcel(String exportPath, String[] firstRow, List<Object[]> dataList) {
        boolean isLinkedList = dataList.getClass() == LinkedList.class;
        if (dataList == null || dataList.size() == 0)
//            throw new NullPointerException("将要导出excel的数据为空");
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

        for (int index = 0,size = dataList.size() / sheetSize + 1; index < size; index++) {
            // 创建工作表
            Sheet sheet = workbook.createSheet();
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
            int i = 0;
            if (firstRow.length > 0) {
                Row row = sheet.createRow(0);
                // 设置行高
                row.setHeight((short) (20 * 15));
                for (String colData : firstRow) {
                    // 创建对应的单元格
                    Cell cell = row.createCell(i);
                    // 设置单元格的数据类型为文本
                    cell.setCellType(CellType.STRING);
                    // 设置单元格的数值
                    cell.setCellValue(StringUtils.isEmpty(colData) ? null : colData);
                    i++;
                }
                i = 1;
            }

            if (size == 1) {
                createSheetData(dataList, sheet, i);
            } else {
                int x = i;
                if (isLinkedList) {
                    LinkedList<Object[]> indexList = new LinkedList<>();
                    indexList.addAll(dataList.subList(index * sheetSize
                            , (index * sheetSize + sheetSize - 1) > dataList.size() ? dataList.size() : index * sheetSize + sheetSize));
                    createSheetData(indexList, sheet, x);
                } else {
                    for (int y = 0, z = (index == size-1 ? dataList.size() + sheetSize - (sheetSize * size) : sheetSize); y < z; y++) {
                        Row row = sheet.createRow(x);
                        int j = 0;
                        for (Object colData : dataList.get(z + (index * sheetSize))) {
                            Cell cell = row.createCell(j, CellType.STRING);
                            cell.setCellValue(StringUtils.isEmpty(colData) ? null : String.valueOf(colData));
                            j++;
                        }
                        x++;
                    }
                }
            }
        }
        outPutStreamExcel(workbook, exportPath);
    }

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

    public static void outPutStreamExcel(Workbook workbook, String exportPath) {
        if (workbook != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(exportPath);
                workbook.write(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void createSheetData(List<Object[]> dataList, Sheet sheet, int i) {
        for (Object[] rowData : dataList) {
            Row row = sheet.createRow(i);
            int j = 0;
            for (Object colData : rowData) {
                Cell cell = row.createCell(j, CellType.STRING);
                cell.setCellValue(StringUtils.isEmpty(colData) ? null : String.valueOf(colData));
                j++;
            }
            i++;
        }
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

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("start");
        long startTime = System.currentTimeMillis();

        List<Object[]> data = new LinkedList<>();
        String[] strings;
        for (int i = 0; i < 3500000; i++) {
            strings = new String[4];
            strings[0] = i + 1 + "-1";
            strings[1] = i + 1 + "-2";
            strings[2] = i + 1 + "-3";
            strings[3] = i + 1 + "-4";
            data.add(strings);
        }
        exportExcel("C:\\Users\\Administrator\\Desktop/MapTest.xlsx"
                , new String[]{"title-1", "title-2", "title-3", "title-4"}
                , data);
        // 导出350万条4列小字符串
        // ArrayList 耗时 33.445秒
        // LinkedList 耗时 30.928秒

        System.out.println("over,用时:" + (System.currentTimeMillis() - startTime) + "毫秒");
    }
}
