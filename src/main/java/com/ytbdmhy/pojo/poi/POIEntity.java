package com.ytbdmhy.pojo.poi;

import java.util.List;

public class POIEntity {

    private String exportPath;

    private String title;

    private List dataList;

    private boolean needMergeTitle;

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public boolean isNeedMergeTitle() {
        return needMergeTitle;
    }

    public void setNeedMergeTitle(boolean needMergeTitle) {
        this.needMergeTitle = needMergeTitle;
    }
}
