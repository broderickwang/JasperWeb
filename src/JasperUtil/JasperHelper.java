package JasperUtil;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.HtmlExporterConfiguration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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


    public static void prepareReport(JasperReport jasperReport, PrintType type) {
            /*
             * 如果导出的是excel，则需要去掉周围的margin
             */
            switch (type){
                case EXCEL_TYPE:
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
                    break;
            }

    }

    /**
     * 导出excel
     */
    public static void exportExcel(JasperPrint jasperPrint, String defaultFilename,
                                    HttpServletResponse response) throws IOException, JRException {

        String defaultname=null;
        if(defaultFilename.trim()!=null&&defaultFilename!=null){
            defaultname=defaultFilename+".xls";
        }else{
            defaultname="export.xls";
        }
        String fileName = new String(defaultname.getBytes("gbk"), "utf-8");
        System.out.println("1"); // only this line is printed in console not others
        JRXlsExporter exporter1=null;
        try{
            exporter1 = new  JRXlsExporter();
            System.out.println("2");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("3");
        response.setContentType("application/xls");
        System.out.println("4");
        response.setHeader("Content-Disposition", "inline; filename=\""+fileName+"\"");
        System.out.println("5");
        exporter1.setParameter(JRExporterParameter.JASPER_PRINT,  jasperPrint);
        System.out.println("6");
        exporter1.setParameter(JRExporterParameter.OUTPUT_STREAM,  response.getOutputStream());
        System.out.println("7");
        exporter1.exportReport();
    }

    /**
     * 导出pdf文件
     * 注意此处中文问题，

     * 这里应该详细说：主要在studio里变下就行了。

     * 下边的设置就在点字段的属性后出现。
     * pdf font name ：STSong-Light ，pdf encoding ：UniGB-UCS2-H
     */
    private static void exportPdfFile(JasperPrint jasperPrint, String defaultFilename,
                                       HttpServletResponse response) throws IOException, JRException {
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
     * 导出PDF，直接显示在servlet web页面
     * @param jasperPrint
     * @param response
     * @throws IOException
     */
    private static void exprotPdfIO(JasperPrint jasperPrint,HttpServletResponse response)throws IOException{
        FileBufferedOutputStream fbos = new FileBufferedOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        try {
            exporter.exportReport();
            fbos.close();
            if (fbos.size() > 0) {
                //response.setContentType("application/pdf;charset=utf-8");
                response.setContentType("application/pdf");
                response.setContentLength(fbos.size());
                ServletOutputStream ouputStream = response.getOutputStream();
                try {
                    fbos.writeData(ouputStream);
                    fbos.dispose();
                    ouputStream.flush();
                } finally {
                    if (null != ouputStream) {
                        ouputStream.close();
                    }
                }
            }
        } catch (JRException e1) {
            e1.printStackTrace();
        }finally{
            if(null !=fbos){
                fbos.close();
                fbos.dispose();
            }
        }
    }

    /**
     * 导出html
     */
    private static void exportHtml(JasperPrint jasperPrint,String defaultFilename,
                                    HttpServletResponse response) throws IOException, JRException {
        response.setContentType("text/html");
        /*ServletOutputStream ouputStream = response.getOutputStream();
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
        ouputStream.close();*/
        //强制设置分页，不再使用过时的方法
        DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
        context.setProperty(HtmlExporterConfiguration.PROPERTY_BETWEEN_PAGES_HTML,"<DIV STYLE='page-break-before:always;'></DIV>");

        jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);

        JasperExportManager.getInstance(context).exportReportToHtmlFile(jasperPrint, defaultFilename);

        String[] names = defaultFilename.split("\\/");

        System.out.print(names);

        response.sendRedirect(names[names.length-1]);
    }

    /**
     * 导出word
     */
    private static void exportWord(JasperPrint jasperPrint,String defaultFilename,
                                    HttpServletResponse response)
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
     * 以数据连接的方式，按照类型导出不同格式文件
     * @param type
     * @param defaultFilename
     * @param file
     * @param parameters
     * @param conn
     * @param response
     */
    public static void export(PrintType type, String defaultFilename, File file,
                               Map parameters,Connection conn, HttpServletResponse response) {

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
            prepareReport(jasperReport, type);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            switch (type){
                case PDF_TYPE:
                    exportPdfFile(jasperPrint,defaultFilename, response);
                    break;
                case PDF_IO_TYPE:
                    exprotPdfIO(jasperPrint,response);
                    break;
                case HTML_TYPE:
                    exportHtml(jasperPrint,defaultFilename, response);
                    break;
                case EXCEL_TYPE:
                    exportExcel(jasperPrint,defaultFilename, response);
                    break;
                case WORD_TYPE:
                    exportWord(jasperPrint,defaultFilename, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以JRDataSource的方式，按照类型导出不同格式文件
     * @param datas
     * @param type
     * @param defaultFilename
     * @param file
     * @param parameters
     * @param response
     */
    public static void export(Collection datas, PrintType type, String defaultFilename, File file,
                               Map parameters, HttpServletResponse response) {

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
            prepareReport(jasperReport, type);
            JRDataSource ds = new JRBeanCollectionDataSource(datas, false);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);

            switch (type){
                case PDF_TYPE:
                    exportPdfFile(jasperPrint,defaultFilename, response);
                    break;
                case PDF_IO_TYPE:
                    exprotPdfIO(jasperPrint,response);
                    break;
                case HTML_TYPE:
                    exportHtml(jasperPrint,defaultFilename, response);
                    break;
                case EXCEL_TYPE:
                    exportExcel(jasperPrint,defaultFilename, response);
                    break;
                case WORD_TYPE:
                    exportWord(jasperPrint,defaultFilename, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以JRBeanCollectionDataSource的方式(JavaBean数组)，按照类型导出不同格式文件
     *  暂时未用到，已合并到上一个export方法中
     * @param datas
     * @param type
     * @param defaultFilename
     * @param fileName
     * @param parameters
     * @param response
     */
    private static void export2(ArrayList datas, PrintType type, String defaultFilename, String fileName,
                               Map parameters, HttpServletResponse response) {

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(fileName);
            prepareReport(jasperReport, type);
            JRBeanCollectionDataSource beanColDataSource =
                    new JRBeanCollectionDataSource(datas);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

            switch (type){
                case PDF_TYPE:
                    exportPdfFile(jasperPrint,defaultFilename, response);
                    break;
                case PDF_IO_TYPE:
                    exprotPdfIO(jasperPrint,response);
                    break;
                case HTML_TYPE:
                    exportHtml(jasperPrint,defaultFilename, response);
                    break;
                case EXCEL_TYPE:
                    exportExcel(jasperPrint,defaultFilename, response);
                    break;
                case WORD_TYPE:
                    exportWord(jasperPrint,defaultFilename, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mian(String[] args){

    }
}
