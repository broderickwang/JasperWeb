/*
 *Created by Broderick
 * User: Broderick
 * Date: 2017/6/12
 * Time: 08:55
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
**/
public class Custom {
    private String CUSTOMERID;
    private String LOGINNAME;
    private String NICKNAME;

    public Custom(String cUSTOMERID, String lOGINNAME, String nICKNAME) {
        super();
        CUSTOMERID = cUSTOMERID;
        LOGINNAME = lOGINNAME;
        NICKNAME = nICKNAME;
    }
    public Custom() {
    }
    public String getCUSTOMERID() {
        return CUSTOMERID;
    }
    public void setCUSTOMERID(String cUSTOMERID) {
        CUSTOMERID = cUSTOMERID;
    }
    public String getLOGINNAME() {
        return LOGINNAME;
    }
    public void setLOGINNAME(String lOGINNAME) {
        LOGINNAME = lOGINNAME;
    }
    public String getNICKNAME() {
        return NICKNAME;
    }
    public void setNICKNAME(String nICKNAME) {
        NICKNAME = nICKNAME;
    }
}
