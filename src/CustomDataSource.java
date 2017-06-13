import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.sql.Connection;
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
public class CustomDataSource implements  JRDataSource {
    private int index = -1;

    private List<Custom> datas = new CustomerService().getAllCustomer();

    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;
        String fieldName = field.getName();
        if ("customerid".equalsIgnoreCase(fieldName)) {
            value = datas.get(index).getCUSTOMERID();
        } else if ("loginname".equalsIgnoreCase(fieldName)) {
            value = datas.get(index).getLOGINNAME();
        } else if ("nickname".equalsIgnoreCase(fieldName)) {
            value = datas.get(index).getNICKNAME();
        }
        return value;
    }

    public boolean next() throws JRException {
        index++;
        return index < datas.size();
    }
}
