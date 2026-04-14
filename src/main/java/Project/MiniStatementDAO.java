package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MiniStatementDAO {

    private Connection con = null;
    private ArrayList<TranstrationBean> trans = null;

    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:ORCL", "system", "Yash1438");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TranstrationBean> getMiniStatement(long accno) {
        try {
            trans = new ArrayList<TranstrationBean>();

            PreparedStatement pstmt = con.prepareStatement(
                "SELECT ACCNO, TRANTYPE, AMOUNT, TRANDATE " +
                "FROM MINISTATEMENT WHERE ACCNO = ? ORDER BY TRANDATE DESC"
            );

            pstmt.setString(1, String.valueOf(accno));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                trans.add(new TranstrationBean(
                    rs.getString(1),  // ACCNO
                    rs.getString(2),  // TRANTYPE
                    rs.getString(3),  // AMOUNT
                    rs.getString(4),  // TRANDATE
                    null
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return trans;
    }

    public int deleteByDate(String dateTime) {
        int rows = 0;
        try {
            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM MINISTATEMENT WHERE TRANDATE = ?");
            ps.setString(1, dateTime);
            rows = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
}