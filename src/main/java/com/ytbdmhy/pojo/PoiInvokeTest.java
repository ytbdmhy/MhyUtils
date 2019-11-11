package com.ytbdmhy.pojo;

import com.ytbdmhy.pojo.poi.annotation.PoiTableHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoiInvokeTest {

    @PoiTableHeader(index = 1, width = 5)
    private String index;

    @PoiTableHeader(index = 2, width = 20)
    private String name;

    @PoiTableHeader(index = 3, width = 10)
    private String age;

    @PoiTableHeader(index = 4, width = 20)
    private String birthday;

    private String remark;
}
