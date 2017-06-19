import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 *Created by Broderick
 * User: Broderick
 * Date: 2017/6/12
 * Time: 08:56
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
**/
public class JiaofeiService {
    public List<JiaofeiBean> getAllJiaofei() {
        JiaofeiBean p=null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<JiaofeiBean> perList = new ArrayList<JiaofeiBean>();
        try {
            conn = getOracleConn();
            String sql="select xiangmumingcheng,shuliang,biaojia,yueshu,jine,jiezhiriqi from jiaofeibiao";
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                p=new JiaofeiBean();
                p.setXiangmumingcheng(rs.getString("xiangmumingcheng"));
                p.setBiaojia(rs.getString("biaojia"));
                p.setJiezhiriqi(rs.getDate("jiezhiriqi"));
                p.setJine(rs.getFloat("jine"));
                p.setYueshu(rs.getString("yueshu"));
                perList.add(p);
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
                System.out.println("DB Closed!");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return perList;
    }

    private Connection getOracleConn(){
        Connection conn=null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("beggin connect to the DB!");
            String url = "jdbc:oracle:" + "thin:@192.168.9.202:1521:orcl";
            String user = "sde";
            String password = "sde";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("DB connectted!");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return conn;
    }
}
