package Project;

public class TranstrationBean {
private String senderaccountNo;
private String receiveraccountNo;
private String transtrationtype;
private String transtrationamount;
private String date_time;
/**
 * @param senderaccountNo
 * @param receiveraccountNo
 * @param transtrationtype
 * @param transtrationamount
 * @param date_time
 */
public TranstrationBean(String senderaccountNo, String receiveraccountNo, String transtrationtype,
		String transtrationamount, String date_time) {
	super();
	this.senderaccountNo = senderaccountNo;
	this.receiveraccountNo = receiveraccountNo;
	this.transtrationtype = transtrationtype;
	this.transtrationamount = transtrationamount;
	this.date_time = date_time;
}

public String getSenderaccountNo() {
	return senderaccountNo;
}

public void setSenderaccountNo(String senderaccountNo) {
	this.senderaccountNo = senderaccountNo;
}

public String getReceiveraccountNo() {
	return receiveraccountNo;
}

public void setReceiveraccountNo(String receiveraccountNo) {
	this.receiveraccountNo = receiveraccountNo;
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
	return "TranstrationBean [senderaccountNo=" + senderaccountNo + ", receiveraccountNo=" + receiveraccountNo
			+ ", transtrationtype=" + transtrationtype + ", transtrationamount=" + transtrationamount + ", date_time="
			+ date_time + "]";
}

}
