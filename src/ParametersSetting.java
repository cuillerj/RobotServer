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


public class ParametersSetting {
	public static int GetParametersNumValue (int ID)
	{
		String pgmId="ParametersSetting";
		String mess="Start";
		TraceLog Trace = new TraceLog();
	//	Trace.TraceLog(pgmId,mess);

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
	//			    			 Trace.TraceLog(pgmId,sql);
				    			 rs=stmtI.executeQuery(sql);
				    			 int numValue=0;
				 				while (rs.next()) {		 					
				 					numValue=rs.getInt("numValue");
					 				 mess="found parameter "+ID+" value:"+numValue;
					 				Trace.TraceLog(pgmId,mess);
	//								name=rs.getString("name");
	//								charValue=rs.getString("charValue");
				 				}
				 				mess="End";
			//	 				Trace.TraceLog(pgmId,mess);
					    		 return(numValue);
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
	public static int GetParametersNumbers ()
	{
		String pgmId="ParametersNumbers";
		String mess="Start";
		TraceLog Trace = new TraceLog();
	//	Trace.TraceLog(pgmId,mess);

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
				    			 String sql="SELECT count(*) as number from parameters ";
//				    			 Trace.TraceLog(pgmId,sql);
				    			 rs=stmtI.executeQuery(sql);
				    			 int numValue=0;
				 				while (rs.next()) {		 					
				 					numValue=rs.getInt("number");
					 				 mess=" count:"+numValue;
					 				Trace.TraceLog(pgmId,mess);
	//								name=rs.getString("name");
	//								charValue=rs.getString("charValue");
				 				}
				 				mess="End";
			//	 				Trace.TraceLog(pgmId,mess);
					    		 return(numValue);
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