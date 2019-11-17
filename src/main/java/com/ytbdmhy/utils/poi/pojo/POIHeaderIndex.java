package com.ytbdmhy.utils.poi.pojo;

import com.ytbdmhy.utils.poi.annotation.PoiTableHeader;

import java.lang.reflect.Field;

public class POIHeaderIndex {

    private PoiTableHeader header;

    private Field field;

    private int index;

    public PoiTableHeader getHeader() {
        return header;
    }

    public void setHeader(PoiTableHeader header) {
        this.header = header;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int compare(POIHeaderIndex second) {
        return Integer.compare(this.index, second.index);
    }
}
