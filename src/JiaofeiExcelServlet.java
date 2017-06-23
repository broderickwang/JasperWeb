import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.engine.util.JRLoader;
import util.JasperHelper;
import util.Utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JiaofeiExcelServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /* try {
            response.setCharacterEncoding("UTF-8");
            ServletContext servletContext=this.getServletConfig().getServletContext();
            //得到jasper文件
            File jasperFile=new File(servletContext.getRealPath("/jasper/jiaofeidan.jasper"));
            JasperReport jasperReport= (JasperReport)JRLoader.loadObject(jasperFile);
            JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport,null, Utils.getConnection());
            JRXlsExporter exporter=new JRXlsExporter();
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

            response.setHeader("Content-Disposition", "attachment;filename=first.xls");
            response.setContentType("application/vnd_ms-excel");
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e){

        }*/
        ServletContext servletContext=this.getServletConfig().getServletContext();
        File jasperFile=new File(servletContext.getRealPath("/jasper/jiaofeidan.jasper"));
        JasperReport jasperReport= null;
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
            JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport,null, Utils.getConnection());


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
            response.setHeader("Content-Disposition", "inline; filename=\"report.xls\"");
            System.out.println("5");
            exporter1.setParameter(JRExporterParameter.JASPER_PRINT,  jasperPrint);
            System.out.println("6");
            exporter1.setParameter(JRExporterParameter.OUTPUT_STREAM,  response.getOutputStream());
            System.out.println("7");
            exporter1.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
