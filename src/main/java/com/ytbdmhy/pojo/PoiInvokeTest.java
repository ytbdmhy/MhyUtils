package com.ytbdmhy.pojo;

import com.ytbdmhy.pojo.poi.annotation.PoiTableHeader;

public class PoiInvokeTest {

    @PoiTableHeader(value = "序号", index = 1, width = 5)
    private String index;

    @PoiTableHeader(value = "姓名", index = 2, width = 20)
    private String name;

    @PoiTableHeader(value = "年龄", index = 3, width = 10)
    private String age;

    @PoiTableHeader(value = "生日", index = 4, width = 20)
    private String birthday;

    private String remark;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
