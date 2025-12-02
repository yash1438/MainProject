//package Project;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Date;
//
//
//public class HDFCDAOServlet {
//
//    private Connection con;
//
//    public HDFCDAOServlet() {
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yaswanth", "1438");
//            con.setAutoCommit(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int  registerUser(UserBean ub) {
//        int status = 0;
//        try {
//            PreparedStatement pstmt = con.prepareStatement("INSERT INTO hdfc VALUES (?, ?, ?, ?, ?, ?,?)");
// 
//            pstmt.setString(1, ub.getFname());
//            pstmt.setString(2, ub.getLname());
//            pstmt.setString(3, ub.getGender());
//            pstmt.setLong(4, ub.getAccNo());
//            pstmt.setInt(5, ub.getBalance());
//            pstmt.setString(6, ub.getPword());
//            pstmt.setString(7, ub.getUname());
//      
//         
//         
//            
//          
//            status = pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.out.println("HDFC RETURN"+status);
//        return createUserAccountDetails(status,ub);
//    }
//    private String getFname() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	private int createUserAccountDetails(int status, UserBean user) {
//        if (status > 0) {
//            try {
//                String sql = "INSERT INTO HDFC_USER_DETAILS (CID, ACCOUNTNO, CARDNO, PINNO, VALIDITY, CVV) VALUES (?, ?, ?, ?, ?, ?)";
//                PreparedStatement ps = con.prepareStatement(sql);
//System.out.println("IS IT HERE");
//                // Generate random unique values for each field
//                java.util.Random r = new java.util.Random();
//
//                int cid = r.nextInt(100000, 999999); // 6-digit customer ID
//                long accountNo = r.nextLong(100000000000L, 999999999999L); // 12-digit account number
//                long cardNo = r.nextLong(4000000000000000L, 4999999999999999L); // 16-digit card number (like Visa)
//                int pinNo = r.nextInt(1000, 9999); // 4-digit PIN
//                java.sql.Date validity = new java.sql.Date(System.currentTimeMillis() + (3L * 365 * 24 * 60 * 60 * 1000)); // 3 years from now
//                int cvv = r.nextInt(100, 999); // 3-digit CVV
//
//                // Set values
//                ps.setInt(1, cid);  
//                ps.setLong(2, accountNo);
//                ps.setLong(3, cardNo);
//                ps.setInt(4, pinNo);
//                ps.setDate(5, validity);
//                ps.setInt(6, cvv);
//
//                System.out.println("Generated Details:");
//                System.out.println("CID: " + cid);
//                System.out.println("Account No: " + accountNo);
//                System.out.println("Card No: " + cardNo);
//                System.out.println("PIN: " + pinNo);
//                System.out.println("Validity: " + validity);
//                System.out.println("CVV: " + cvv);
//
//                int k = ps.executeUpdate();
//                System.out.println("K="+k);
//                if (k > 0) con.commit();
//                System.out.println("hDFC uSER");
//                return k;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return 0;
//    }
//
//
//
//    public UserBean loginUser(String uname, String pword) {
//        try {
//        	System.out.println("username=: "+uname);
//        	System.out.println("pass: "+pword);
//            PreparedStatement pstmt = con.prepareStatement(
//                "SELECT * FROM HDFC WHERE UNAME = ? AND PWORD = ?"
//            );
//            pstmt.setString(1, uname);
//            pstmt.setString(2, pword);
//
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//            	UserBean ub = new UserBean(
//            		    rs.getString("FNAME"),
//            		    rs.getString("LNAME"),
//            		    rs.getString("PWORD"),
//            		    rs.getString("GENDER"),
//            		    rs.getInt("BALANCE")   
//            		    
//            		);
//
//                
//                return ub;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//}
package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HDFCDAOServlet {

    private Connection con;

    public HDFCDAOServlet() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yaswanth", "1438");
            con.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Register new user
    public int registerUser(UserBean ub) {
        int status = 0;
        try {
            PreparedStatement pstmt = con.prepareStatement(
                "INSERT INTO HDFC (FNAME, LNAME, GENDER, ACCNO, BALANCE, PWORD, UNAME,AADHARNO) VALUES (?, ?, ?, ?, ?, ?, ?,?)"
            );

            pstmt.setString(1, ub.getFname());
            pstmt.setString(2, ub.getLname());
            pstmt.setString(3, ub.getGender());
            pstmt.setLong(4, ub.getAccNo());
            pstmt.setInt(5, ub.getBalance());
            pstmt.setString(6, ub.getPword());
            pstmt.setString(7, ub.getUname());
            pstmt.setString(8, ub.getaahno());

            status = pstmt.executeUpdate();
            System.out.println("HDFC Main Table Insert: " + status);

            if (status > 0) {
                int k = createUserAccountDetails(ub);
                if (k > 0) {
                    con.commit();
                    System.out.println("Both Inserts Successful — Committed");
                    return 1;
                } else {
                    con.rollback();
                    System.out.println("Second Insert Failed — Rolled Back");
                    return 0;
                }
            }

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Insert into secondary table (HDFC_USER_DETAILS)
    private int createUserAccountDetails(UserBean user) {
        try {
            String sql = "INSERT INTO HDFC_USER_DETAILS (CID, ACCOUNTNO, CARDNO, PINNO, VALIDITY, CVV) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            java.sql.Date validity = new java.sql.Date(System.currentTimeMillis() + (3L * 365 * 24 * 60 * 60 * 1000)); // +3 years

            ps.setInt(1, user.getCid());
            ps.setLong(2, user.getAccNo());
            ps.setLong(3, user.getCardno());
            ps.setInt(4, user.getPinNo());
            ps.setDate(5, validity);
            ps.setInt(6, user.getCvv());

            int k = ps.executeUpdate();
            System.out.println("Inserted into HDFC_USER_DETAILS: " + k);
            return k;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Login validation
    public UserBean loginUser(String uname, String pword) {
        try {
            PreparedStatement pstmt = con.prepareStatement(
                "SELECT * FROM HDFC WHERE UNAME = ? AND PWORD = ?"
            );
            pstmt.setString(1, uname);
            pstmt.setString(2, pword);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserBean(
                    rs.getString("FNAME"),
                    rs.getString("LNAME"),
                    rs.getString("UNAME"),
                    rs.getString("AADHARNO"),
                    rs.getString("PWORD"),
                    rs.getString("GENDER"),
                    rs.getInt("BALANCE"),
                    rs.getLong("ACCNO")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
