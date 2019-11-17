package com.ytbdmhy.pojo.poi;

import com.ytbdmhy.utils.poi.POIInvokeUtilMhy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class POIEntity {

    private String exportPath;

    private HttpServletResponse response;

    private String title;

    private List dataList;

    private boolean needMergeTitle;

    public POIEntity() {}

    public POIEntity(HttpServletResponse response, String title, List dataList, boolean needMergeTitle) {
        this.response = response;
        this.title = title;
        this.dataList = dataList;
        this.needMergeTitle = needMergeTitle;
    }

    public POIEntity(String exportPath, String title, List dataList, boolean needMergeTitle) {
        this.exportPath = exportPath;
        this.title = title;
        this.dataList = dataList;
        this.needMergeTitle = needMergeTitle;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
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

    public void export() {
        if (StringUtils.isEmpty(this.exportPath)
                || this.response == null
                || CollectionUtils.isEmpty(this.dataList))
            return;
        if (StringUtils.isEmpty(this.title))
            this.title = String.valueOf(System.currentTimeMillis());
        POIInvokeUtilMhy.invokeExport(this);
    }
}
