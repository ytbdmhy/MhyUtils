package com.ytbdmhy.pojo.poi.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PoiTableHeader {

    int index() default 0;

    int width() default 10;
}
