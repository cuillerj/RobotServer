import java.net.DatagramPacket;

//import javax.swing.*;

//import javax.swing.JPanel;
//import java.awt.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.*; 
import java.net.*;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;
public class RobotMainServer 
	{
	public static int posXG=0;
	public static int posYG=0;
	public static int orientG=0;
	public static String idscanG="0";
	public static int countScan=0;
	public static int posX;
	public static int posY;
	public static int alpha;
	public static byte actStat=0x00;
	public static String stationStatus="";
	public static String ipRobot="192.168.1.133";  // 138 ou 133
	public static int[][] scanArray = new int [15][3];
	public static int scanStepCount=0;
	public static int power9V;
	public static int power5V;
	public static int powerDiag;
	public static int motorDiag;
	public static int connectionDiag;
	public static int robotDiag;
	public static int runningStatus=0;

//	public static String ipRobot="aprobot";  // 138 ou 133
	static char[] TAB_BYTE_HEX = { '0', '1', '2', '3', '4', '5', '6','7',
            '8', '9', 'A', 'B', 'C', 'D', 'E','F' };
public static void main(String args[]) throws Exception
			{ 
//	Fenetre ihm = new Fenetre();
//	Fenetre2 ihm2 = new Fenetre2();
//	FenetreGraphiqueSonar ihm3 = new FenetreGraphiqueSonar();
//	ihm2.SetInitialPosition();
//	ihm3.SetInitialPosition();
	LanchBatch();
//	RobotBatchServer batch = new RobotBatchServer();
//	Thread myThread = new Thread(batch);
//	myThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
//	myThread.start(); 
///	RobotBatchServer.StartBatch();
			}
			
		//   System.exit(0);
		 

			

public static void GetCurrentPosition(String ids) {
	Connection conn = null;
	Statement stmtR1 = null;
	Statement stmtI1 = null;
//		Fenetre ihm = new Fenetre();
//		JFormattedTextField ids=indScan;
//	String ids =indScan.getText();
	int indscan=Integer.parseInt(ids);

//	  System.out.println("actualise position:"+indscan);
try {

Class.forName("com.mysql.jdbc.Driver").newInstance();
String connectionUrl = "jdbc:mysql://jserver:3306/robot";
String connectionUser = "jean";
String connectionPassword = "manu7890";
conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
//conn.setAutoCommit(false);
stmtR1 = conn.createStatement();
stmtI1 = conn.createStatement();
ResultSet rs = null;

rs = stmtR1.executeQuery("SELECT * FROM scanResult WHERE idscan = "+indscan+"  ORDER by time desc limit 1"); 
while (rs.next()) {
  posXG = rs.getInt("posX");
  posYG = rs.getInt("posY");
  orientG = rs.getInt("orientation");
//  System.out.println("x:"+posXG+" Y:"+posYG+" orient:"+orientG);
  Fenetre2.ValidePosition(posXG, posYG, orientG);

}
rs.close();

} catch (Exception e) {
e.printStackTrace();
} 
finally {
try { if (stmtR1 != null) stmtR1.close(); } catch (SQLException e) { e.printStackTrace(); }
try { if (stmtI1 != null) stmtI1.close(); } catch (SQLException e) { e.printStackTrace(); }
try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }}
}
public static void  GetLastScanID() {
	Connection conn = null;
	Statement stmtR1 = null;
	Statement stmtI1 = null;
//		Fenetre ihm = new Fenetre();
//		JFormattedTextField ids=indScan;
//	String ids =indScan.getText();
//	int indscan=Integer.parseInt(ids);

//	  System.out.println("actualise position:"+indscan);
try {

Class.forName("com.mysql.jdbc.Driver").newInstance();
String connectionUrl = "jdbc:mysql://jserver:3306/robot";
String connectionUser = "jean";
String connectionPassword = "manu7890";
conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
//conn.setAutoCommit(false);
stmtR1 = conn.createStatement();
stmtI1 = conn.createStatement();
ResultSet rs = null;
rs = stmtR1.executeQuery("SELECT * FROM scanResult  ORDER by time desc limit 1"); 
while (rs.next()) {
  idscanG = rs.getString("idscan");
  posXG = rs.getInt("posX");
  posYG = rs.getInt("posY");
  orientG = rs.getInt("orientation");
//  System.out.println("x:"+posXG+" Y:"+posYG+" orient:"+orientG);
}
rs.close();

} catch (Exception e) {
e.printStackTrace();
} 
finally {
try { if (stmtR1 != null) stmtR1.close(); } catch (SQLException e) { e.printStackTrace(); }
try { if (stmtI1 != null) stmtI1.close(); } catch (SQLException e) { e.printStackTrace(); }
try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }}

}
/*public void RefreshAff() {
	// TODO Auto-generated method stub
    ihm.RefreshStat();
	}
	}
//   System.exit(0);
  */
public static void  UpdateGraphRobotLocation() {

	}
public static Object  TestOctave() {
	System.out.println("test octave 2");
    int[] A = {1,2};
    return  A;
}
public static void hexaPrint(byte y)
{

		System.out.print("-" + String.format("%x", y));
	}
public static void LanchBatch()
{
	FenetreGraphiqueSonar ihm3 = new FenetreGraphiqueSonar();
//	ihm2.SetInitialPosition();
	ihm3.SetInitialPosition();  // au moins une fenetre active avant de detacher le batch
			//InitRobot();
	RobotBatchServer batch = new RobotBatchServer();
	Thread myThread = new Thread(batch);
	myThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
	myThread.start(); 
	}
public static int GetScanAngle(int idx)
{
	return scanArray[idx][0];

}
public static int GetScanDistFront(int idx)
{
	return scanArray[idx][1];

}
public static int GetScanDistBack(int idx)
{
	return scanArray[idx][2];

}
public static void Scan360()
{
    int newIdScan=0;
    RobotMainServer.idscanG= Integer.toString(newIdScan);
    RobotMainServer.scanStepCount=1;
    Fenetre.idscan.setText(RobotMainServer.idscanG);
    Fenetre.label.setText("Demarrage du scan");   
    System.out.println(RobotMainServer.idscanG);
    SendUDP snd = new SendUDP();
    snd.SendUDPScan();

}
public static void Move(long ang,long mov)
{
//    RobotMainServer.idscanG= Fenetre.idscan.getText();
    Fenetre.label.setText("Move");   
    if (mov!=0 || ang!=0)
    {
    SendUDP snd = new SendUDP();
    snd.SendUDPMove((long)ang,(long) mov);
    }

    RobotMainServer.actStat=0x01;  //demande mov

}
public static void GoTo(long posX,long posY)
{
    Fenetre.label.setText("GoTo");   

    if (posX!=0|| posY!=0)
    {
    SendUDP snd = new SendUDP();
    snd.SendUDPGoto((long)posX,(long) posY);
    }

    RobotMainServer.actStat=0x01;  //demande mov
}
public static int RunningStatus()
{
return runningStatus;
}
	}
