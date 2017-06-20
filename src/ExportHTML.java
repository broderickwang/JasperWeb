import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class ExportHTML extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            File reportFile = new File(this.getServletContext()
                    .getRealPath("jasper/jiaofeidan.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile);

            Map parameters = new HashMap();
            File image = new File(this.getServletContext().getRealPath("/jasper/cherry.jpg"));
            parameters.put("imgs", new FileInputStream(image));
            parameters.put("year", "2013");
            parameters.put("clientid","1");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:" + "thin:@192.168.9.202:1521:orcl";
            String user = "sde";
            String password = "sde";
            Connection conn = DriverManager.getConnection(url, user, password);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, conn);

            /*5.0之后已过时
             * JRHtmlExporter exporter = new JRHtmlExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());

            exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
            exporter.exportReport();*/

            //强制设置分页，不再使用过时的方法
            DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
            context.setProperty(HtmlExporterConfiguration.PROPERTY_BETWEEN_PAGES_HTML,"<DIV STYLE='page-break-before:always;'></DIV>");

            jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
            /*SimpleHtmlExporterConfiguration simpleHtmlExporterConfiguration = new SimpleHtmlExporterConfiguration();
            simpleHtmlExporterConfiguration.setBetweenPagesHtml("<DIV STYLE='page-break-before:always;'></DIV>");*/

            JasperExportManager.getInstance(context).exportReportToHtmlFile(jasperPrint, this.getServletContext().getRealPath("/")+"test.html");
            conn.close();

            response.sendRedirect("test.html");
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
