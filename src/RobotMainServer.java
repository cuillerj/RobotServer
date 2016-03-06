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
	public static int hardPosX;
	public static int hardPosY;
	public static int hardAlpha;
	public static int northOrientation;
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
	public static int idCarto=1;                   // a rendre modifiable
	public static int currentLocProb=0;
	public static boolean hardJustReboot=false;
	public static boolean serverJustRestarted=true;
	public static boolean debugCnx=false;
	public static boolean octaveRequestPending=false;
	public static int octavePendingRequest=0;
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
public static void LaunchBatch()
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
	octaveRequestPending=true;
	RobotMainServer.runningStatus=2;
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
	octaveRequestPending=true;
    Fenetre.label.setText("Move");   
	RobotMainServer.runningStatus=4;
    if (mov!=0 || ang!=0)
    {
    SendUDP snd = new SendUDP();
    snd.SendUDPMove((long)ang,(long) mov);
    }

    RobotMainServer.actStat=0x01;  //demande mov

}
public static void GoTo(long gotoX,long gotoY)
{
	octaveRequestPending=true;
    Fenetre.label.setText("GoTo");   
	RobotMainServer.runningStatus=4;
    if (posX!=gotoX|| posY!=gotoY)
    {
    SendUDP snd = new SendUDP();
    snd.SendUDPGoto ((long)gotoX,(long)gotoY);
    }

    RobotMainServer.actStat=0x01;  //demande mov
}
public static int GetRunningStatus()
{
return runningStatus;
}
public static int GetPosX()
{
return posX;
}
public static int GetPosY()
{
return posY;
}
public static int GetPosAngle()
{
return alpha;
}
public static int GetHardPosX()
{
return hardPosX;
}
public static int GetHardPosY()
{
return hardPosY;
}
public static int GetHardAngle()
{
return hardAlpha;
}
public static int GetNorthOrientation()
{
	RobotMainServer.octavePendingRequest=1;    // request info uptodate
	RobotMainServer.octaveRequestPending=true;
	SendUDP snd = new SendUDP();
	snd.SendEcho();

return northOrientation;
}
public static int GetCurrentLocProb()
{
return currentLocProb;
}
public static void SetRunningStatus(int value)
{
runningStatus=value;
}
public static void SetPosX(int value)
{
posX=value;
Fenetre2.ValidePositionX(value);
}
public static void SetPosY(int value)
{
posY=value;
Fenetre2.ValidePositionY(value);
}
public static void SetAlpha(int value)
{
alpha=value;
Fenetre2.ValideOrientation(value);
}
public static void SetDebugCnxOn (boolean value)
{
debugCnx=value;
}
public static boolean GetHardJustReboot ()
{
return hardJustReboot;
}
public static boolean GetOctaveRequestPending ()
{
return octaveRequestPending;
}
public static void SetCurrentLocProb (int value)
{
currentLocProb=value;
Fenetre2.SetcurrentLocProb();
}
public static void UpdateHardRobotLocation()
{
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendUDPInit(posX,posY,alpha,currentLocProb);
Fenetre2.ValidePosition(posX, posY, alpha);
	}
public static void NorthAlignRobot(int northShift)
{
	RobotMainServer.octavePendingRequest=6;
	RobotMainServer.octaveRequestPending=true;
	SendUDP snd = new SendUDP();
	snd.NorthAlignRobot(northShift);
}
public static void StopRobotServer()
{
System.exit(0);
	}


}