import java.sql.Date;

/*
 *Created by Broderick
 * User: Broderick
 * Date: 2017/6/16
 * Time: 09:51
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
**/
public class JiaofeiBean {
    private String xiangmumingcheng;
    private String shuliang;
    private String biaojia;
    private String yueshu;
    private float jine;
    private Date jiezhiriqi;

    public JiaofeiBean() {
    }

    public JiaofeiBean(String xiangmumingcheng, String shuliang, String biaojia, String yueshu, float jine, Date jiezhiriqi) {
        this.xiangmumingcheng = xiangmumingcheng;
        this.shuliang = shuliang;
        this.biaojia = biaojia;
        this.yueshu = yueshu;
        this.jine = jine;
        this.jiezhiriqi = jiezhiriqi;
    }

    public String getXiangmumingcheng() {
        return xiangmumingcheng;
    }

    public void setXiangmumingcheng(String xiangmumingcheng) {
        this.xiangmumingcheng = xiangmumingcheng;
    }

    public String getShuliang() {
        return shuliang;
    }

    public void setShuliang(String shuliang) {
        this.shuliang = shuliang;
    }

    public String getBiaojia() {
        return biaojia;
    }

    public void setBiaojia(String biaojia) {
        this.biaojia = biaojia;
    }

    public String getYueshu() {
        return yueshu;
    }

    public void setYueshu(String yueshu) {
        this.yueshu = yueshu;
    }

    public float getJine() {
        return jine;
    }

    public void setJine(float jine) {
        this.jine = jine;
    }

    public Date getJiezhiriqi() {
        return jiezhiriqi;
    }

    public void setJiezhiriqi(Date jiezhiriqi) {
        this.jiezhiriqi = jiezhiriqi;
    }
}
