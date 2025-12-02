//package Project;
//
//import java.io.Serializable;
//import java.util.Random;
//
//public class UserBean implements Serializable {
//    private String fname;
//    private String lname;
//    private String uname;
//    private String gender;
//    private String pword;
//    private long accNo;
//    private int balance;
//    private int pinNo;
//    private int cid;
//    private int cvv;
//    private long cardno;
//
//    // ✅ Correct constructor
//    
//
//        // Generate random account details
//    
//        /**
//	 * @param fname
//	 * @param lname
//	 * @param uname
//	 * @param gender
//	 * @param accNo
//	 */
//	public UserBean(String fname, String lname, String uname, String gender, long accNo) {
//		super();
//		this.fname = fname;
//		this.lname = lname;
//		this.uname = uname;
//		this.gender = gender;
//		this.pword = pword;
//		this.accNo = accNo;
//		this.balance = balance;
//		this.pinNo = pinNo;
//		this.cid = cid;
//		this.cvv = cvv;
//		this.cardno = cardno;
//	     generateAccNo();
//		generatePinNo();
//        generateCid();
//        generateCardNo();
//        generateCvv();
//    }
//
// 
////
////	public UserBean(String fname2, String lname2, String uname2, String gender2, String pword2, int balance2) {
////		this.fname=fname2;
////		this.lname=lname2;
////		this.uname=uname2;
////		this.gender=gender2;
////		this.pword=pword2;
////		this.balance=balance2;
////	}
//
//	public UserBean(String string, String string2, String string3, long long1, int int1, String string4,
//			String string5) {
//		// TODO Auto-generated constructor stub
//	}
//
//
//
//	// Random Generators
//    private void generateAccNo() {
//        Random r = new Random();
//        this.accNo = r.nextLong(1000000000L, 9999999999L);
//    }
//
//    private void generatePinNo() {
//        Random r = new Random();
//        this.pinNo = r.nextInt(1000, 9999);
//    }
//
//    private void generateCardNo() {
//        Random r = new Random();
//        this.cardno = r.nextLong(1000000000000000L, 9999999999999999L);
//    }
//
//    private void generateCid() {
//        Random r = new Random();
//        this.cid = r.nextInt(1000, 9999);
//    }
//
//    private void generateCvv() {
//        Random r = new Random();
//        this.cvv = r.nextInt(100, 999);
//    }
//
//    // Getters and Setters
//    public String getFname() { return fname; }
//    public String getLname() { return lname; }
//    public String getUname() { return uname; }
//    public String getGender() { return gender; }
//    public String getPword() { return pword; }
//    public long getAccNo() { return accNo; }
//    public int getBalance() { return balance; }
//    public int getPinNo() { return pinNo; }
//    public int getCid() { return cid; }
//    public int getCvv() { return cvv; }
//    public long getCardno() { return cardno; }
//
//    @Override
//    public String toString() {
//        return "UserBean [fname=" + fname + ", lname=" + lname + ", uname=" + uname + ", gender=" + gender +
//                ", pword=" + pword + ", accNo=" + accNo + ", balance=" + balance + ", pinNo=" + pinNo + "]";
//    }
//}
package Project;

import java.io.Serializable;
import java.util.Random;

public class UserBean implements Serializable {
    private String fname;
    private String lname;
    private String uname;
    private String aahno;
    private String gender;
    private String pword;
    private long accNo;
    private int balance;
    private int pinNo;
    private int cid;
    private int cvv;
    private long cardno;
    boolean userFound=false;
    // ✅ Constructor for registration
    public UserBean(String fname, String lname, String uname, String aahno,String pword, String gender, int balance) {
        this.fname = fname;
        this.lname = lname;
        this.uname = uname;
        this.aahno=aahno;
        this.pword = pword;
        this.gender = gender;
        this.balance = balance;

        generateAccNo();
        generatePinNo();
        generateCid();
        generateCardNo();
        generateCvv();
    }
    public UserBean(String fname, String lname, String uname,String aahno, String pword, String gender, int balance,long accNo) {
        this.fname = fname;
        this.lname = lname;
        this.uname = uname;
        this.aahno=aahno;
        this.pword = pword;
        this.gender = gender;
        this.balance = balance;
        this.accNo=accNo;
        this.userFound=true;
    }

    // ✅ Random Generators (Java 8 Compatible)
    private void generateAccNo() {
        Random r = new Random();
        this.accNo = 1000000000L + (long)(r.nextDouble() * 8999999999L); // 10 digits
    }

    private void generatePinNo() {
        Random r = new Random();
        this.pinNo = 1000 + r.nextInt(9000);
    }

    private void generateCardNo() {
        Random r = new Random();
        this.cardno = 4000000000000000L + (long)(r.nextDouble() * 999999999999999L); // 16 digits
    }

    private void generateCid() {
        Random r = new Random();
        this.cid = 100000 + r.nextInt(900000); // 6 digits
    }

    private void generateCvv() {
        Random r = new Random();
        this.cvv = 100 + r.nextInt(900); // 3 digits
    }
    

    // ✅ Getters
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getUname() { return uname; }
    public String getaahno() { return aahno; }
    
    public String getGender() { return gender; }
    public String getPword() { return pword; }
    public long getAccNo() { return accNo; }
    public int getBalance() { return balance; }
    public int getPinNo() { return pinNo; }
    public int getCid() { return cid; }
    public int getCvv() { return cvv; }
    public long getCardno() { return cardno; }
	@Override
	public String toString() {
		return "UserBean [fname=" + fname + ", lname=" + lname + ", uname=" + uname + ", aahno=" + aahno + ", gender="
				+ gender + ", pword=" + pword + ", accNo=" + accNo + ", balance=" + balance + ", pinNo=" + pinNo
				+ ", cid=" + cid + ", cvv=" + cvv + ", cardno=" + cardno + ", userFound=" + userFound + "]";
	}
    

 
}
