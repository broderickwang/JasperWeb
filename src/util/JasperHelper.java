package util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/*
 *Created by Broderick
 * User: Broderick
 * Date: 2017/6/23
 * Time: 15:36
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
**/
public class JasperHelper {
    public static final String PRINT_TYPE = "print";
    public static final String PDF_TYPE = "pdf";
    public static final String EXCEL_TYPE = "excel";
    public static final String HTML_TYPE = "html";
    public static final String WORD_TYPE = "word";
    public static void prepareReport(JasperReport jasperReport, String type) {
            /*
             * 如果导出的是excel，则需要去掉周围的margin
             */
        if ("excel".equals(type))
            try {
                Field margin = JRBaseReport.class
                        .getDeclaredField("leftMargin");
                margin.setAccessible(true);
                margin.setInt(jasperReport, 0);
                margin = JRBaseReport.class.getDeclaredField("topMargin");
                margin.setAccessible(true);
                margin.setInt(jasperReport, 0);
                margin = JRBaseReport.class.getDeclaredField("bottomMargin");
                margin.setAccessible(true);
                margin.setInt(jasperReport, 0);
                Field pageHeight = JRBaseReport.class
                        .getDeclaredField("pageHeight");
                pageHeight.setAccessible(true);
                pageHeight.setInt(jasperReport, 2147483647);
            } catch (Exception exception) {
            }
    }

    /**
     * 导出excel
     */
    public static void exportExcel(JasperPrint jasperPrint, String defaultFilename,
                                   HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {

          /*
              * 设置头信息
              */
        response.setContentType("application/vnd.ms-excel");
        String defaultname=null;
        if(defaultFilename.trim()!=null&&defaultFilename!=null){
            defaultname=defaultFilename+".xls";
        }else{
            defaultname="export.xls";
        }
        String fileName = new String(defaultname.getBytes("gbk"), "utf-8");
        response.setHeader("Content-disposition", "attachment; filename="
                + fileName);
        ServletOutputStream ouputStream = response.getOutputStream();
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,ouputStream);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
        exporter.exportReport();
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 导出pdf，注意此处中文问题，

     * 这里应该详细说：主要在ireport里变下就行了。看图

     * 1）在ireport的classpath中加入iTextAsian.jar
     * 2）在ireport画jrxml时，看ireport最左边有个属性栏。

     * 下边的设置就在点字段的属性后出现。
     * pdf font name ：STSong-Light ，pdf encoding ：UniGB-UCS2-H
     */
    private static void exportPdf(JasperPrint jasperPrint,String defaultFilename,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
        response.setContentType("application/pdf");
        String defaultname=null;
        if(defaultFilename.trim()!=null&&defaultFilename!=null){
            defaultname=defaultFilename+".pdf";
        }else{
            defaultname="export.pdf";
        }
        String fileName = new String(defaultname.getBytes("GBK"), "ISO8859_1");
        response.setHeader("Content-disposition", "attachment; filename="
                + fileName);
        ServletOutputStream ouputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint,
                ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 导出html
     */
    private static void exportHtml(JasperPrint jasperPrint,String defaultFilename,
                                   HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
        response.setContentType("text/html");
        ServletOutputStream ouputStream = response.getOutputStream();
        JRHtmlExporter exporter = new JRHtmlExporter();
        exporter.setParameter(
                JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
                Boolean.FALSE);
        exporter
                .setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
                "UTF-8");
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                ouputStream);

        exporter.exportReport();

        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 导出word
     */
    private static void exportWord(JasperPrint jasperPrint,String defaultFilename,
                                   HttpServletRequest request, HttpServletResponse response)
            throws JRException, IOException {
        response.setContentType("application/msword;charset=utf-8");
        String defaultname=null;
        if(defaultFilename.trim()!=null&&defaultFilename!=null){
            defaultname=defaultFilename+".doc";
        }else{
            defaultname="export.doc";
        }
        String fileName = new String(defaultname.getBytes("GBK"), "utf-8");
        response.setHeader("Content-disposition", "attachment; filename="
                + fileName);
        JRExporter exporter = new JRRtfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response
                .getOutputStream());

        exporter.exportReport();
    }
    /**
     * 按照类型导出不同格式文件
     *
     * @param datas
     *            数据
     * @param type
     *            文件类型
     * @param is
     *            jasper文件的来源
     * @param request
     * @param response
     * @param defaultFilename   默认的导出文件的名称
     */
    private static void export(Collection datas, String type, String defaultFilename, InputStream is,
                               HttpServletRequest request, HttpServletResponse response) {

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
            prepareReport(jasperReport, type);
            JRDataSource ds = new JRBeanCollectionDataSource(datas, false);
            Map parameters = new HashMap();
            // parameters.put("wheresql", " and status='3'");
            /* parameters.put("wheresql", "");
               String diver = "oracle.jdbc.driver.OracleDriver";
                String url = "jdbc:oracle:thin:@192.168.1.156:1521:orcl";
                String username = "qqwcrm0625";
                String password = "qqwcrm";
                ReportDataSource datasource = new ReportDataSource();
                datasource.setDiver(diver);
                datasource.setUrl(url);
                datasource.setUsername(username);
                datasource.setPassword(password);
                Connection con=getConnection(datasource);*/
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);

            if (EXCEL_TYPE.equals(type)) {
                exportExcel(jasperPrint,defaultFilename, request, response);
            } else if (PDF_TYPE.equals(type)) {
                exportPdf(jasperPrint,defaultFilename, request, response);
            } else if (HTML_TYPE.equals(type)) {
                exportHtml(jasperPrint,defaultFilename, request, response);
            } else if (WORD_TYPE.equals(type)) {
                exportWord(jasperPrint,defaultFilename, request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
