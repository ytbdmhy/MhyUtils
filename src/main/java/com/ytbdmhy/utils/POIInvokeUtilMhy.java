package com.ytbdmhy.utils;

import com.ytbdmhy.pojo.PoiInvokeTest;
import com.ytbdmhy.pojo.poi.POIEntity;
import com.ytbdmhy.pojo.poi.POIHeaderIndex;
import com.ytbdmhy.pojo.poi.annotation.PoiTableHeader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class POIInvokeUtilMhy extends POIUtilMhy {

    public static void invokeExport(POIEntity poiEntity) {
        if (CollectionUtils.isEmpty(poiEntity.getDataList())
                || StringUtils.isEmpty(poiEntity.getTitle())
                || StringUtils.isEmpty(poiEntity.getExportPath()))
            return;
        Class<?> clazz = poiEntity.getDataList().get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        LinkedHashMap<String, Integer> firstRow = new LinkedHashMap<>();
        List<POIHeaderIndex> headerIndices = new ArrayList<>();
        int i = 0;
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations != null && fieldAnnotations.length > 0) {
                for (Annotation annotation : fieldAnnotations) {
                    if (annotation.annotationType().equals(PoiTableHeader.class)) {
                        POIHeaderIndex headerIndex = new POIHeaderIndex();
                        PoiTableHeader header = (PoiTableHeader) annotation;
                        headerIndex.setHeader(header);
                        headerIndex.setIndex(header.index());
                        headerIndex.setField(field);
                        headerIndices.add(headerIndex);
                        ++i;
                    }
                }
            }
        }
        Field[] sortFields = new Field[headerIndices.size()];
        if (headerIndices.size() == 0) {
            return;
        } else {
            i = 0;
            // TODO 应该可优化:处理tempHeaders的排序
            POIHeaderIndex[] tempHeaders = new POIHeaderIndex[headerIndices.size()];
            for (POIHeaderIndex headerIndex : headerIndices) {
                if (i == 0) {
                    tempHeaders[0] = headerIndex;
                } else {
                    for (int j = i - 1; j > -1; j--) {
                        if (headerIndex.compare(tempHeaders[j]) > -1) {
                            if (j == i - 1) {
                                tempHeaders[i] = headerIndex;
                            } else {
                                for (int k = i; k > j + 1; k --) {
                                    tempHeaders[k] = tempHeaders[k - 1];
                                }
                                tempHeaders[j + 1] = headerIndex;
                            }
                            break;
                        }
                    }
                }
                ++i;
            }
            i = 0;
            for (POIHeaderIndex headerIndex : tempHeaders) {
                sortFields[i] = headerIndex.getField();
                firstRow.put(headerIndex.getHeader().value(), headerIndex.getHeader().width() * 264);
                ++i;
            }
        }
        // poiEntity的dataList根据methods或sortFields转换成excelData
        List<Object[]> excelData = new ArrayList<>();
        for (Object object : poiEntity.getDataList()) {
            String[] strings = new String[sortFields.length];
            int l = 0;
            for (Field field : sortFields) {
                Object row = ReflectionUtil.getFieldValue(object, field.getName());
                strings[l] = row == null ? null : String.valueOf(row);
                ++l;
            }
            excelData.add(strings);
        }
        POIUtilMhy.formatExportExcel(poiEntity.getExportPath(), poiEntity.getTitle(), firstRow , excelData, poiEntity.isNeedMergeTitle());
    }

    public static void main(String[] args) {
        POIEntity poiEntity = new POIEntity();
        poiEntity.setExportPath("C:\\Users\\Administrator\\Desktop\\poiInvokeTest.xlsx");
        LinkedList<PoiInvokeTest> poiInvokeTestList = new LinkedList<>();
        for (int i = 0; i < 350; i++) {
            PoiInvokeTest poiTest = new PoiInvokeTest();
            poiTest.setName("test-name-" + i);
            poiTest.setAge(String.valueOf((int) (Math.random() * 100) + 1));
            poiTest.setIndex(String.valueOf(i));
            poiTest.setMarry((int) (Math.random() * 2) + 1 == 1);
            poiInvokeTestList.add(poiTest);
        }
        poiEntity.setDataList(poiInvokeTestList);
        poiEntity.setTitle("poiInvokeTest");
        poiEntity.setNeedMergeTitle(true);
        long start = System.currentTimeMillis();
        invokeExport(poiEntity);
        System.out.println("1耗时:" + (System.currentTimeMillis() - start));
        // 导出350W条3列小字符串
        // ArrayList 耗时 27.321秒
        // LinkedList 耗时 26.591秒
    }
}
