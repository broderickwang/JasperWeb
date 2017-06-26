import bean.DataBean;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import util.DataBeanList;
import JasperUtil.JasperHelper;
import JasperUtil.PrintType;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemplateServlet extends HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            File reportFile = new File(this.getServletContext()
                    .getRealPath("jasper/template.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile);

            Map parameters = new HashMap();

            DataBeanList DataBeanList = new DataBeanList();
            ArrayList<DataBean> dataList = DataBeanList.getDataBeanList();

            JasperHelper.export(dataList, PrintType.HTML_TYPE,
                    this.getServletContext().getRealPath("/")+"5est2.html",reportFile,parameters,response);

            /*JRDataSource ds = new JRBeanCollectionDataSource(dataList, false);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);

            DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
            context.setProperty(HtmlExporterConfiguration.PROPERTY_BETWEEN_PAGES_HTML,
                    "<DIV STYLE='page-break-before:always;'></DIV>");

            jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);

            JasperExportManager.getInstance(context).exportReportToHtmlFile(jasperPrint,
                    this.getServletContext().getRealPath("/")+"5est.html");

            response.sendRedirect("5est.html");*/

            /*JRBeanCollectionDataSource beanColDataSource =
                    new JRBeanCollectionDataSource(dataList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, beanColDataSource);


            //强制设置分页，不再使用过时的方法
            DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
            context.setProperty(HtmlExporterConfiguration.PROPERTY_BETWEEN_PAGES_HTML,"<DIV STYLE='page-break-before:always;'></DIV>");

            jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);

            JasperExportManager.getInstance(context).exportReportToHtmlFile(jasperPrint, this.getServletContext().getRealPath("/")+"test.html");

            response.sendRedirect("test.html");*/
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
