package Project;

import java.io.Serializable;
import java.util.Random;

public class UserBean implements Serializable {
    private String fname, lname, uname, aahno, pword, gender, email;
    private long accNo, cardno;
    private int balance, pinNo, cid, cvv;

    // Constructor for Registration
    public UserBean(String fname, String lname, String uname, String aahno, String pword, String gender, int balance, String email) {
        this.fname = fname; this.lname = lname; this.uname = uname; this.aahno = aahno; this.pword = pword; this.gender = gender; this.balance = balance; this.email = email;
        Random r = new Random();
        this.accNo = 1000000000L + (long)(r.nextDouble() * 8999999999L);
        this.pinNo = 1000 + r.nextInt(9000);
        this.cardno = 4000000000000000L + (long)(r.nextDouble() * 999999999999999L);
        this.cid = 100000 + r.nextInt(900000);
        this.cvv = 100 + r.nextInt(900);
    }

    // Constructor for Login (9 parameters)
    public UserBean(String fname, String lname, String uname, String aahno, String pword, String gender, int balance, long accNo, String email) {
        this.fname = fname; this.lname = lname; this.uname = uname; this.aahno = aahno; this.pword = pword; this.gender = gender; this.balance = balance; this.accNo = accNo; this.email = email;
    }

    // Getters
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getUname() { return uname; }
    public String getaahno() { return aahno; }
    public String getPword() { return pword; }
    public String getGender() { return gender; }
    public String getEmail() { return email; }
    public long getAccNo() { return accNo; }
    public int getBalance() { return balance; }
    public int getPinNo() { return pinNo; }
    public int getCid() { return cid; }
    public int getCvv() { return cvv; }
    public long getCardno() { return cardno; }
}