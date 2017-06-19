<%--
  Created by Broderick
  User: Broderick
  Date: 2017/6/19
  Time: 09:15
  Version: 1.0
  Description:
  Email:wangchengda1990@gmail.com
--%>
<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="net.sf.jasperreports.engine.util.*" %>
<%@ page import="net.sf.jasperreports.engine.export.*" %>
<%@ page import="net.sf.jasperreports.j2ee.servlets.*" %>
<%@ page import="java.io.*" %>
<%@ page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%
    JasperPrint jasperPrint = (JasperPrint)session.getAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
    if (request.getParameter("reload") != null || jasperPrint == null)
    {
        File reportFile = new File(application.getRealPath("/jasper/jiaofeidan.jasper"));
        if (!reportFile.exists())
            throw new JRRuntimeException("File preson.jasper not found. The report design must be compiled first.");
//        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile.getPath());
        Map parameters = new HashMap();
//        File image = new File(this.getServletContext().getRealPath("/jasper/cherry.jpg"));
//        parameters.put("imgs", new FileInputStream(image));
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:" + "thin:@192.168.9.202:1521:orcl";
            String user = "sde";
            String password = "sde";
            Connection conn = DriverManager.getConnection(url, user, password);
            JasperReport jasperReport = (JasperReport) JRLoader
                    .loadObject(reportFile);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                    conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
    }
    JRHtmlExporter exporter = new JRHtmlExporter();
    int pageIndex = 0;
    int lastPageIndex = 0;
    if (jasperPrint.getPages() != null)
    {
        lastPageIndex = jasperPrint.getPages().size() - 1;
    }
    String pageStr = request.getParameter("page");
    try
    {
        pageIndex = Integer.parseInt(pageStr);
    }
    catch(Exception e)
    {
    }
    if (pageIndex < 0)
    {
        pageIndex = 0;
    }
    if (pageIndex > lastPageIndex)
    {
        pageIndex = lastPageIndex;
    }
    StringBuffer sbuffer = new StringBuffer();
    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
    exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "../image?image=");
    exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
    exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
    exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
    exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
    exporter.exportReport();
%>


<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
    <style type="text/css">
        a {text-decoration: none}
    </style>
</head>
<body text="#000000" link="#000000" alink="#000000" vlink="#000000">
<table width="100%" cellpadding="0" cellspacing="0" border="0">

    <tr>
        <td width="50%">&nbsp;</td>
        <td align="center">
            <%=sbuffer.toString()%>
        </td>
        <td width="50%">&nbsp;</td>
    </tr>
</table>
</body>
</html>