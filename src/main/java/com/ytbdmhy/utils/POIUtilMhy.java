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
public class POIUtilMhy {

    public static String[] readExcelFirstRow(String filePath) {
        return readExcelFirstRow(new File(filePath));
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
        return readExcel(new File(filePath));
    }

    public static List readExcel(File file) {
        List<String[]> result = new LinkedList<>();
        Workbook workbook = getWorkbook(file);
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (file.getName().toLowerCase().endsWith(".xls")) {
//                    if (sheet == null)
//                        continue;
                    for (int rowNum = sheet.getFirstRowNum(); rowNum < sheet.getLastRowNum(); rowNum++) {
                        Row row = sheet.getRow(rowNum);
                        String[] rowValue = new String[row.getLastCellNum() - row.getFirstCellNum()];
//                        if (row == null)
//                            continue;
                        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
                            Cell cell = row.getCell(cellNum);
//                            if (cell == null)
//                                continue;
                            if (cell == null) {
                                rowValue[cellNum] = "";
                            } else if (cell.getCellType() == 0) {
                                rowValue[cellNum] = String.valueOf(cell.getNumericCellValue());
                            } else if (cell.getCellType() == 1) {
                                rowValue[cellNum] = cell.getStringCellValue();
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

        System.out.println("over,用时:" + (System.currentTimeMillis() - startTime) + "毫秒");
    }
}
