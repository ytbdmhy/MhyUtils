package com.ytbdmhy.utils;

import com.ytbdmhy.pojo.PoiInvokeTest;
import com.ytbdmhy.pojo.poi.POIEntity;
import com.ytbdmhy.pojo.poi.annotation.PoiTableHeader;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class POIInvokeUtilMhy extends POIUtilMhy {

    public static void export(POIEntity poiEntity) {
        if (CollectionUtils.isEmpty(poiEntity.getDataList())
                || StringUtils.isEmpty(poiEntity.getTitle())
                || StringUtils.isEmpty(poiEntity.getExportPath()))
            return;
        Class<?> clazz = poiEntity.getDataList().get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        boolean hasTableHeader = false;
        Field[] hasHFields = new Field[fields.length];
        int i = 0;
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations != null && fieldAnnotations.length > 0) {
                for (Annotation annotation : fieldAnnotations) {
                    if (annotation.annotationType().equals(PoiTableHeader.class)) {
                        hasTableHeader = true;
                        hasHFields[i] = field;
                        ++i;
                    }
                }
            }
        }
        if (!hasTableHeader)
            return;


        List<String[]> excelData = new ArrayList<>();
        System.out.println("over");
    }

    public static void main(String[] args) {
        POIEntity poiEntity = new POIEntity();
        poiEntity.setExportPath("C:\\Users\\Administrator\\Desktop\\poiInvokeTest.xlsx");
        List<PoiInvokeTest> poiInvokeTestList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            PoiInvokeTest poiTest = new PoiInvokeTest();
            poiTest.setName("test-name-" + i);
            poiTest.setAge(String.valueOf((int) (Math.random() * 100) + 1));
            poiTest.setIndex(String.valueOf(i));
            poiInvokeTestList.add(poiTest);
        }
        poiEntity.setDataList(poiInvokeTestList);
        poiEntity.setTitle("poiInvokeTest");
        export(poiEntity);
    }
}
