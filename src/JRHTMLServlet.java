import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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

public class JRHTMLServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        ServletContext context = this.getServletConfig().getServletContext();
        File jasperFile = new File(context.getRealPath("jasper/jiaofeidan.jasper"));
        Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("year", "2013");
//		parameters.put("unit_mc", "ces");
        File image = new File(this.getServletContext().getRealPath("/jasper/cherry.jpg"));
        parameters.put("imgs",new FileInputStream(image));
        JasperPrint jasperPrint = null;
        //response.setCharacterEncodin("utf-8");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:" + "thin:@192.168.9.202:1521:orcl";
            String user = "sde";
            String password = "sde";
            Connection conn = DriverManager.getConnection(url, user, password);
            JasperReport jasperReport = (JasperReport) JRLoader
                    .loadObject(jasperFile);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                   conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null != jasperPrint){
            JRHtmlExporter exporter = new JRHtmlExporter();
            request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,jasperPrint);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());
            exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,"image?image=");
            exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "<DIV STYLE='page-break-before:always;'></DIV>");
            try {
                exporter.exportReport();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
