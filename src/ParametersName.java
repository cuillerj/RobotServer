import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ParametersName {
	public static String  GetParameterName (int ID)
	{
		String pgmId="ParametersName";
		String mess="Start";
		TraceLog Trace = new TraceLog();
		Trace.TraceLog(pgmId,mess);
			while(true)
			{
				Connection conn = null;
				Statement stmtI = null;
				ResultSet rs = null;
				    		 try {
				    			 Class.forName("com.mysql.jdbc.Driver").newInstance();
				    			 String connectionUrl = "jdbc:mysql://jserver:3306/robot";
				    			 String connectionUser = "jean";
				    			 String connectionPassword = "manu7890";
				    			 conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
				    			 stmtI = conn.createStatement();
				    			 String sql="SELECT * from parameters where idparameter = "+ID+" limit 1";
				    			 Trace.TraceLog(pgmId,sql);
				    			 rs=stmtI.executeQuery(sql);
				    			 String name="";
				 				while (rs.next()) {	 					
				 					name=rs.getString("name");
					 				 mess="found parameter "+ID+" name:"+name;
					 				Trace.TraceLog(pgmId,mess);
				 				}
				 				mess="End";
				 				Trace.TraceLog(pgmId,mess);
					    		return(name);
				    		 	}
				    		 	catch (Exception e) {
					 				mess="Error";
					 				Trace.TraceLog(pgmId,mess);
				    		 		e.printStackTrace();
				    		 	} finally {
				    		 		try { if (stmtI != null) stmtI.close(); } catch (SQLException e) { e.printStackTrace(); }

				    		 		try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				    		 	}
				    	 	}
	}

}