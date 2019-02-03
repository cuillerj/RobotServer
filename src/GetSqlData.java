import java.io.IOException;
import java.util.Random;
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
import java.util.Random;
public class GetSqlData {
	@SuppressWarnings("resource")
	public static int GetClosestEchoGetClosestReferenceEcho(int inX,int inY,int servoHeading,int tileSize) 
	{
		String pgmId="GetSqlData";
		String mess="Start GetClosestEchoGetClosestReferenceEcho";
		TraceLog Trace = new TraceLog();

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

	    			String connectionUrl = GetSqlConnection.GetRobotDB();
	    			String connectionUser = GetSqlConnection.GetUser();
	    			String connectionPassword = GetSqlConnection.GetPass();
	    			 conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
					cStmt = conn.prepareCall("{call apeRobotGetClosestEcho(?, ?, ?, ?,?,?,?,?,?,?,?,?,?)}");
	    			 cStmt.registerOutParameter(5, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(6, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(7, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(8, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(9, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(10, java.sql.Types.FLOAT);
	    			 cStmt.registerOutParameter(11, java.sql.Types.FLOAT);
	    			 cStmt.registerOutParameter(12, java.sql.Types.INTEGER);
	    			 cStmt.registerOutParameter(13, java.sql.Types.INTEGER);

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
				    			 boolean hadResults = cStmt.execute();
				    			 mess="after exe";
//				    				Trace.TraceLog(pgmId,mess);
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
	@SuppressWarnings("null")
	public static int GetScanRawData(int inX,int inY) 
	{
		String pgmId="GetSqlData";
		String mess="Start GetScanRawData.";
		TraceLog Trace = new TraceLog();
		Trace.TraceLog(pgmId,mess);

		String connectionUrl = GetSqlConnection.GetRobotDB();
		String connectionUser = GetSqlConnection.GetUser();
		String connectionPassword = GetSqlConnection.GetPass();

		int[] idscanArray=new int[100];
				Connection conn = null;
				Statement stmt1 = null;
				ResultSet rs = null;
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
	    			 try {
						conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
		    			stmt1 = conn.createStatement();
						mess=connectionUrl;
						Trace.TraceLog(pgmId,mess);
					} catch (SQLException e) {
						mess="pb db connecxion";
						Trace.TraceLog(pgmId,mess);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			try {
						String sql="SELECT idscan FROM scanResult WHERE posX = "+inX+" AND posY = "+inY+" AND idscan !=0 group by idscan order by time desc limit 100";
//						Trace.TraceLog(pgmId,sql);
						rs = stmt1.executeQuery(sql);

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			int idx=0;
	    			try {
						while (rs.next()) {
							idscanArray[idx] = rs.getInt("idscan");
							idx++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		    Random randomGenerator = new Random();
	    		    int randomScanId = randomGenerator.nextInt(idx-1);
					mess="randomly choose scanId:"+idscanArray[randomScanId];
					Trace.TraceLog(pgmId,mess);
	    			try {
					rs = stmt1.executeQuery("SELECT distFront,distBack,angle FROM scanResult WHERE idscan ="+idscanArray[randomScanId]+" order by angle limit 15" );
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			idx=0;
	    		    RandomGaussian gaussian = new RandomGaussian();
	    		    double varianceEcho = RobotMainServer.scanNoiseLevel;
	    			try {
						while (rs.next()) {
							RobotMainServer.scanArray[idx][1] = Math.max((int)(gaussian.getGaussian(rs.getInt("distFront"),varianceEcho)),0);
							RobotMainServer.scanArray[idx][2] = Math.max((int)(gaussian.getGaussian(rs.getInt("distBack"),varianceEcho)),0);
							RobotMainServer.scanArray[idx][0] = rs.getInt("angle");
							idx++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				finally {

    		 		try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    		 	}
		return(0);		    	 	
	}
}