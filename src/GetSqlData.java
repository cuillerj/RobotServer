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
import java.sql.CallableStatement;

public class GetSqlData {
	@SuppressWarnings("resource")
	public static int GetClosestEchoGetClosestReferenceEcho(int inX,int inY,int servoHeading,int tileSize) 
	{
		String pgmId="GetSqlData";
		String mess="Start";
		TraceLog Trace = new TraceLog();
//		Trace.TraceLog(pgmId,mess);

				Connection conn = null;
				Statement stmtI = null;
				ResultSet rs = null;
				CallableStatement cStmt = null;
				try {
	    			 try {
						Class.forName("com.mysql.jdbc.Driver").newInstance();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			 String connectionUrl = "jdbc:mysql://jserver:3306/robot";
	    			 String connectionUser = "jean";
	    			 String connectionPassword = "manu7890";
	    			 conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
	    			 mess="BD connected";
  //  				Trace.TraceLog(pgmId,mess);
					cStmt = conn.prepareCall("{call apeRobotGetClosestEcho(?, ?, ?, ?,?,?,?,?,?,?,?,?,?)}");
	    			 mess="call prepared";
	    			 Trace.TraceLog(pgmId,mess);
	    			 cStmt.registerOutParameter(5, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(6, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(7, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(8, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(9, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(10, java.sql.Types.FLOAT);
	    			 cStmt.registerOutParameter(11, java.sql.Types.FLOAT);
	    			 cStmt.registerOutParameter(12, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(13, java.sql.Types.INTEGER);
	    			 mess="out param";
//   				Trace.TraceLog(pgmId,mess);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					cStmt.setInt(1, inX);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					cStmt.setInt(2, inY);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					cStmt.setInt(3,servoHeading);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					cStmt.setInt(4,tileSize);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				    		 try {
				    			 mess="before exe";
	//			    				Trace.TraceLog(pgmId,mess);
	//		    			    boolean hadResults = cStmt.execute();
	//			    			 stmtI = conn.createStatement();
//				    			 String sql="SELECT * from parameters where idparameter = "+ID+" limit 1";
	//			    			 Trace.TraceLog(pgmId,sql);
				    			 boolean hadResults = cStmt.execute();
				    			 mess="after exe";
				    				Trace.TraceLog(pgmId,mess);
				    				 RobotMainServer.echoClosestRefX=cStmt.getInt(5);
				    				 mess="found x: "+RobotMainServer.echoClosestRefX;
				    				 RobotMainServer.echoClosestRefY=cStmt.getInt(6);
				    				 mess=mess+" y: "+RobotMainServer.echoClosestRefY;
				    				 RobotMainServer.echoClosestRefServoHeading=cStmt.getInt(7);
				    				 mess=mess+" angle: "+RobotMainServer.echoClosestRefServoHeading;
				    				 RobotMainServer.echoClosestRefDistFront=cStmt.getInt(8);
				    				 mess=mess+" distF: "+RobotMainServer.echoClosestRefDistFront;
				    				 RobotMainServer.echoClosestRefDistBack=cStmt.getInt(9);
				    				 mess=mess+" distB: "+RobotMainServer.echoClosestRefDistBack;
				    				 RobotMainServer.echoClosestStdFront=cStmt.getFloat(10);
				    				 mess=mess+" stdF: "+RobotMainServer.echoClosestStdFront;
				    				 RobotMainServer.echoClosestStdBack=cStmt.getFloat(11);
				    				 mess=mess+" stdB: "+RobotMainServer.echoClosestStdBack;
				    				 RobotMainServer.echoClosestCount=cStmt.getInt(12);
				    				 mess=mess+" count: "+RobotMainServer.echoClosestCount;
				    				 RobotMainServer.echoClosestDistance=cStmt.getInt(13);
				    				 mess=mess+" dist: "+RobotMainServer.echoClosestDistance;
				    				 Trace.TraceLog(pgmId,mess);
				    				 mess="End";

			//	 				Trace.TraceLog(pgmId,mess);
					    		 return(RobotMainServer.echoClosestDistance);
				    		 	}
				    		 	catch (Exception e) {
					 				mess="Error";
					 				Trace.TraceLog(pgmId,mess);
				    		 		e.printStackTrace();
				    		 		
				    		 	} finally {

				    		 		try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				    		 	}
		return(0);		    	 	
	}
	
}