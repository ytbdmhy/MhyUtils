package com.ytbdmhy.pojo.poi.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PoiTableHeader {

    String value() default "";

    int index() default 0;

    int width() default 10;
}
