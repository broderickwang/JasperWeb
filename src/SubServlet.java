import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class SubServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        File jasperFile = new File(this.getServletContext()
                .getRealPath("/jasper/Blank_A4.jasper"));
        File image = new File(this.getServletContext().getRealPath("/jasper/Blank_A4_Landscape.jasper"));
        Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("year", "2013");
        parameters.put("subreport", new FileInputStream(image));
        parameters.put("imageURL","/Users/ttb/JaspersoftWorkspace/MyReports/cherry.jpg");
//		parameters.put("unit_mc", "ceshi");
        JasperPrint jasperPrint = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:" + "thin:@192.168.9.202:1521:orcl";
            String user = "sde";
            String password = "sde";
            Connection conn = DriverManager.getConnection(url, user, password);
            JasperReport jasperReport = (JasperReport) JRLoader
                    .loadObjectFromFile(jasperFile.getPath());
            jasperPrint = JasperFillManager.fillReport(jasperReport,
                    parameters,conn );
            if (null != jasperPrint) {
                jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);

                SimpleHtmlExporterConfiguration simpleHtmlExporterConfiguration = new SimpleHtmlExporterConfiguration();
                simpleHtmlExporterConfiguration.setBetweenPagesHtml("<DIV STYLE='page-break-before:always;'></DIV>");
                JasperExportManager.exportReportToHtmlFile(jasperPrint, this.getServletContext().getRealPath("/")+"subservlet.html");
                conn.close();
                

                response.sendRedirect("subservlet.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * pdf导出
     */
    private void exportPdf(HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream();

        try {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

            response.setContentType("application/pdf;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".pdf");
            exporter.exportReport();
            ouputStream.flush();
        } finally {
            try {
                ouputStream.close();
            } catch (Exception e) {
            }
        }
    }
}
