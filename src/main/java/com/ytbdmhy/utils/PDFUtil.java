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

public class PDFUtil {

    public static void ftlToPdf(String ftlName, String ftlTemplateContent, HashMap<String, Object> ftlParams, String exportPath) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(exportPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        outResponsePdf(outputStream, getHtmlContent(ftlName, ftlTemplateContent, ftlParams));
    }

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

    private static void outResponsePdf(OutputStream outputStream, String htmlContent) {
        if (outputStream == null) return;
        try {
            // 关闭日志,忽略openhtmltopdf和xstream的xerces包冲突
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
