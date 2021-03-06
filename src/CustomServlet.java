import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.engine.util.JRLoader;

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

public class CustomServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        File jasperFile = new File(this.getServletContext()
                .getRealPath("/jasper/custom.jasper"));
        File image = new File(this.getServletContext().getRealPath("/jasper/cherry.jpg"));
        Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("year", "2013");
        parameters.put("imgs", new FileInputStream(image));
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
                    parameters,new CustomDataSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != jasperPrint) {
            /*try {
                exportPdf(response, jasperPrint, "test1");
            } catch (JRException e) {
                e.printStackTrace();
            }*/
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
