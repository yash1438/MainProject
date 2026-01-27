package Project;

import java.sql.*;

public class HDFCDAOServlet {
    private Connection con;

    public HDFCDAOServlet() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yaswanth", "143812");
            con.setAutoCommit(false);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public int registerUser(UserBean ub) {
        try {
            PreparedStatement ps1 = con.prepareStatement("INSERT INTO HDFC (FNAME, LNAME, GENDER, ACCNO, BALANCE, PWORD, UNAME, AADHARNO, EMAIL) VALUES (?,?,?,?,?,?,?,?,?)");
            ps1.setString(1, ub.getFname());
            ps1.setString(2, ub.getLname());
            ps1.setString(3, ub.getGender());
            ps1.setLong(4, ub.getAccNo());
            ps1.setInt(5, ub.getBalance());
            ps1.setString(6, ub.getPword());
            ps1.setString(7, ub.getUname());
            ps1.setString(8, ub.getaahno());
            ps1.setString(9, ub.getEmail());
            int k1 = ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement("INSERT INTO HDFC_USER_DETAILS (CID, ACCOUNTNO, CARDNO, PINNO, CVV) VALUES (?,?,?,?,?)");
            ps2.setInt(1, ub.getCid());
            ps2.setLong(2, ub.getAccNo());
            ps2.setLong(3, ub.getCardno());
            ps2.setInt(4, ub.getPinNo());
            ps2.setInt(5, ub.getCvv());
            int k2 = ps2.executeUpdate();

            if (k1 > 0 && k2 > 0) { con.commit(); return 1; }
            else { con.rollback(); }
        } catch (SQLException e) { try{con.rollback();}catch(Exception ex){} e.printStackTrace(); }
        return 0;
    }

    public UserBean loginUser(String uname, String pword) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM HDFC WHERE UNAME=? AND PWORD=?");
            ps.setString(1, uname);
            ps.setString(2, pword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UserBean(rs.getString("FNAME"), rs.getString("LNAME"), rs.getString("UNAME"), rs.getString("AADHARNO"), rs.getString("PWORD"), rs.getString("GENDER"), rs.getInt("BALANCE"), rs.getLong("ACCNO"), rs.getString("EMAIL"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}