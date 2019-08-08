package com.ytbdmhy.utils;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.util.XRLog;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;

/**
 * 导出PDF工具
 * Created by miaohaoyun on 2019/08/08
 * 依赖: spring-boot-starter-freemarker 2.1.4.RELEASE
 *      openhtmltopdf-core 1.0.0
 *      openhtmltopdf-pdfbox 1.0.0
 *      openhtmltopdf-slf4j 1.0.0
 */
public class PDFUtil {

    /**
     * FTL对象
     */
    public static class Ftl {
        private String ftlName;
        private String ftlTemplateContent;
        private HashMap<String, Object> ftlParams;

        public Ftl() {}

        public Ftl(String ftlName, String ftlTemplateContent, HashMap<String, Object> ftlParams) {
            this.ftlName = ftlName;
            this.ftlTemplateContent = ftlTemplateContent;
            this.ftlParams = ftlParams;
        }

        public void setFtlName(String ftlName) {
            this.ftlName = ftlName;
        }

        public void setFtlTemplateContent(String ftlTemplateContent) {
            this.ftlTemplateContent = ftlTemplateContent;
        }

        public void setFtlParams(HashMap<String, Object> ftlParams) {
            this.ftlParams = ftlParams;
        }

        public void ftlToPdf(String exportPath) {
            PDFUtil.ftlToPdf(this.ftlName, this.ftlTemplateContent, this.ftlParams, exportPath);
        }

        public void ftlToPdf(HttpServletResponse response) {
            PDFUtil.ftlToPdf(this.ftlName, this.ftlTemplateContent, this.ftlParams, response);
        }
    }

    /**
     * 根据ftl按路径导出PDF
     * @param ftlName ftl名称
     * @param ftlTemplateContent ftl内容
     * @param ftlParams ftl参数
     * @param exportPath 导出的路径
     */
    public static void ftlToPdf(String ftlName, String ftlTemplateContent, HashMap<String, Object> ftlParams, String exportPath) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(exportPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        outResponsePdf(outputStream, getHtmlContent(ftlName, ftlTemplateContent, ftlParams));
    }

    /**
     * 根据ftl用response导出PDF
     * @param ftlName ftl名称
     * @param ftlTemplateContent ftl内容
     * @param ftlParams ftl参数
     * @param response 响应流
     */
    public static void ftlToPdf(String ftlName, String ftlTemplateContent, HashMap<String, Object> ftlParams, HttpServletResponse response) {
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + (ftlName.endsWith(".pdf") ? ftlName : ftlName + ".pdf"));
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        outResponsePdf(outputStream, getHtmlContent(ftlName, ftlTemplateContent, ftlParams));
    }

    /**
     * 根据ftl获得html的字符串
     * @param ftlName ftl名称
     * @param ftlTemplateContent ftl内容
     * @param ftlParams ftl参数
     * @return html内容的字符串
     */
    private static String getHtmlContent(String ftlName, String ftlTemplateContent, HashMap<String, Object> ftlParams) {
        String htmlContent = null;
        Writer writer = null;
        try{
            writer = new StringWriter();
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
            configuration.setClassicCompatible(true);
            configuration.setEncoding(Locale.SIMPLIFIED_CHINESE, "UTF-8");
            StringTemplateLoader templateLoader = new StringTemplateLoader();
            templateLoader.putTemplate(ftlName, ftlTemplateContent);
            configuration.setTemplateLoader(templateLoader);
            Template template = configuration.getTemplate(ftlName);
            template.setEncoding("UTF-8");
            template.process(ftlParams, writer);
            htmlContent = writer.toString();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlContent;
    }

    /**
     * 导出PDF
     * @param outputStream 输出流
     * @param htmlContent html内容的字符串
     */
    private static void outResponsePdf(OutputStream outputStream, String htmlContent) {
        if (outputStream == null) return;
        try {
            // 关闭日志,忽略openhtmltopdf和xstream的xerces包冲突。开发环境可打开日志查看html的CSS状态
            XRLog.setLoggingEnabled(false);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            // 设置ftl中对应使用的simsun(宋体),否则无法显示中文。字体文件在resources/static/font中!
            builder.useFont(() -> PDFUtil.class.getResourceAsStream("/static/font/simsun.ttf"), "simsun", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.withHtmlContent(htmlContent, "");
            builder.toStream(outputStream);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}