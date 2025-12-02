package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MiniStatementDAO {
			
	private Connection   con =null;
	private ArrayList<TranstrationBean> trans = null;
	{
		  try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			     con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yaswanth", "1438");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  ArrayList<TranstrationBean> getMiniStatement(long accno) {
		
		try {
			trans=new ArrayList<TranstrationBean>();
			 
	         PreparedStatement pstmt = con.prepareStatement(
	                 "SELECT * FROM    MINIStatement WHERE SENDERACCOUNTNO=? OR  RECEIVERACCOUNTNO=? ");
	         
	         pstmt.setString(1, String.valueOf(accno));
	         pstmt.setString(2, String.valueOf(accno));
	         
	         ResultSet rs= pstmt.executeQuery();
	         
	        while(rs.next()) {
	        	trans.add(new TranstrationBean(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5)));
	        	
	        }
	        
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return trans;
		
	}
//	public int   setMiniStatement(TranstrationBean tb){
//		 try {
//			  PreparedStatement pstmt = con.prepareStatement(
//		                 "Insert into table MINIStatement values(?,?,?,?,?)");
//		         
//		       pstmt.setString(1,tb.getSenderaccountNo());
//		       pstmt.setString(2, tb.getReceiveraccountNo());
//		       pstmt.setString(3, tb.getTranstrationtype());
//		       pstmt.setString(4, tb.getTranstrationamount());
//		      pstmt.setString(5, tb.getDate_time());
//		      return pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		
//		return 0;
//		
//	}
	public int deleteByDate(String dateTime) {
	    int rows = 0;
 try {
	         PreparedStatement ps = con.prepareStatement(
	                 "DELETE FROM MINIStatement WHERE DATE_TIME = ?");

	        ps.setString(1, dateTime);
	        rows = ps.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return rows;
	}

}
	
