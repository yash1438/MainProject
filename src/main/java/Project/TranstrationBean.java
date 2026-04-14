package Project;

public class TranstrationBean {

    private String accNo;
    private String transtrationtype;
    private String transtrationamount;
    private String date_time;

    public TranstrationBean(String accNo, String transtrationtype,
            String transtrationamount, String date_time, String unused) {
        this.accNo = accNo;
        this.transtrationtype = transtrationtype;
        this.transtrationamount = transtrationamount;
        this.date_time = date_time;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getTranstrationtype() {
        return transtrationtype;
    }

    public void setTranstrationtype(String transtrationtype) {
        this.transtrationtype = transtrationtype;
    }

    public String getTranstrationamount() {
        return transtrationamount;
    }

    public void setTranstrationamount(String transtrationamount) {
        this.transtrationamount = transtrationamount;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    @Override
    public String toString() {
        return "TranstrationBean [accNo=" + accNo +
               ", transtrationtype=" + transtrationtype +
               ", transtrationamount=" + transtrationamount +
               ", date_time=" + date_time + "]";
    }
}