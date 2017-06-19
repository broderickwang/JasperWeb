import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.List;

/*
 *Created by Broderick
 * User: Broderick
 * Date: 2017/6/12
 * Time: 08:55
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
**/
public class JiaofeiDataSource implements  JRDataSource {
    private int index = -1;

    private List<JiaofeiBean> datas = new JiaofeiService().getAllJiaofei();

    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;
        String fieldName = field.getName();
        if ("xiangmumingc".equalsIgnoreCase(fieldName)) {
            value = datas.get(index).getXiangmumingcheng();
        } else if ("shuliang".equalsIgnoreCase(fieldName)) {
            value = datas.get(index).getShuliang();
        } else if ("biaojia".equalsIgnoreCase(fieldName)) {
            value = datas.get(index).getBiaojia();
        } else if("yueshu".equalsIgnoreCase(fieldName)){
            value = datas.get(index).getYueshu();
        }else if("jine".equalsIgnoreCase(fieldName)){
            value = datas.get(index).getJine();
        }else if("jiezhiriqi".equalsIgnoreCase(fieldName)){
            value = datas.get(index).getJiezhiriqi();
        }
        return value;
    }

    public boolean next() throws JRException {
        index++;
        return index < datas.size();
    }
}
