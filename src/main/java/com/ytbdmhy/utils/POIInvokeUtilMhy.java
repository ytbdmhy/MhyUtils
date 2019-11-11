package com.ytbdmhy.utils;

import com.ytbdmhy.pojo.PoiInvokeTest;
import com.ytbdmhy.pojo.poi.POIEntity;
import com.ytbdmhy.pojo.poi.annotation.PoiTableHeader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class POIInvokeUtilMhy extends POIUtilMhy {

    public static void export(POIEntity poiEntity) {
        if (CollectionUtils.isEmpty(poiEntity.getDataList())
                || StringUtils.isEmpty(poiEntity.getTitle())
                || StringUtils.isEmpty(poiEntity.getExportPath()))
            return;
        Class<?> clazz = poiEntity.getDataList().get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = new Method[fields.length];
        Field[] hasFields = new Field[fields.length];
        LinkedHashMap<String, Integer> firstRow = new LinkedHashMap<>();
        int i = 0;
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations != null && fieldAnnotations.length > 0) {
                for (Annotation annotation : fieldAnnotations) {
                    if (annotation.annotationType().equals(PoiTableHeader.class)) {
                        hasFields[i] = field;
                        methods[i] = ReflectionUtil.getMethod(clazz, "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), null);
                        ++i;
                    }
                }
            }
        }
        if (i == 0) {
            return;
        } else {
            i = 0;
            // TODO 处理methods的排序和去空
        }
        List<Object[]> excelData = new ArrayList<>();

        // TODO poiEntity的dataList根据hasHFields转换成excelData
        for (int j = 0; j < poiEntity.getDataList().size(); j++) {
            String[] strings = new String[methods.length];
            Object object = poiEntity.getDataList().get(j);
            int k = 0;
            for (Method method : methods) {
                try {
                    Object row = method.invoke(object);
                    strings[k] = row == null ? null : String.valueOf(row);
                } catch (Exception e) {
                    strings[k] = null;
                }
                ++k;
            }
            excelData.add(strings);
        }

        POIUtilMhy.formatExportExcel(poiEntity.getExportPath(), poiEntity.getTitle(), firstRow , excelData);
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
