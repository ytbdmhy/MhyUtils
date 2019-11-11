package com.ytbdmhy.pojo;

import com.ytbdmhy.pojo.annotation.AnnotationTest;
import com.ytbdmhy.pojo.poi.annotation.PoiTableHeader;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AnnotationTest
public class InvokeTest {

    private String name;

    @PoiTableHeader(index = 1, width = 20)
    private Integer age;

    private String address;

    private String nation;

    private Date birthday;
}
