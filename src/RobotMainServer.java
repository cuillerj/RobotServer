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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

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
	public static boolean javaRequestStatusPending=false;
	public static boolean octaveRequestPending=false;
	public static int octavePendingRequest=0;
	public static boolean interactive=false;
	public static String fname="robotJavaTrace.txt";
//	public static String ipRobot="aprobot";  // 138 ou 133
	static char[] TAB_BYTE_HEX = { '0', '1', '2', '3', '4', '5', '6','7',
            '8', '9', 'A', 'B', 'C', 'D', 'E','F' };
public static void main(String args[]) throws Exception
			{
	System.out.println("start robot main server");
	PrintStream console = System.out;
//	File file = new File(fname);
//	FileOutputStream fos = new FileOutputStream(file);
//	PrintStream ps = new PrintStream(fos);
//	System.setOut(ps);
//	System.out.println("create trace");
//	System.setOut(console);
//	System.out.println("print console");
	try{
	int args0_len=0;
	 args0_len=args[0].length();
		if (args[0].equals("i"))     // interactive mode
		{
			interactive=true;
			System.setOut(console);
			System.out.println("trace sur console");
			LaunchBatch();
		}
	}
	  catch (Exception e) { 
//			System.setOut(ps);
//			System.out.println("trace fichier");
	  }

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
	StartTimeoutManagement();
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
    Fenetre.label.setText("Scan requested");   
 //   System.out.println(RobotMainServer.idscanG);
	EventManagement.AddPendingEvent(2,1200,1,2);
    SendUDP snd = new SendUDP();
    snd.SendUDPScan();
}
public static void Move(long ang,long mov)
{
	System.out.println("Move requested");
//    RobotMainServer.idscanG= Fenetre.idscan.getText();
	octaveRequestPending=true;
    Fenetre.label.setText("Move requested");   
	RobotMainServer.runningStatus=4;
	EventManagement.AddPendingEvent(4,600,1,2);
    if (mov!=0 || ang!=0)
    {
    SendUDP snd = new SendUDP();
    snd.SendUDPMove((long)ang,(long) mov);
    Fenetre2.PosActualise(ang,mov);
    }
    RobotMainServer.actStat=0x01;  //demande mov
}
public static void GoTo(long gotoX,long gotoY)
{
	octaveRequestPending=true;
    Fenetre.label.setText("GoTo");   
	RobotMainServer.runningStatus=4;
	EventManagement.AddPendingEvent(4,600,1,2);
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
//	EventManagement.AddPendingEvent(1,20,1,2);
return hardPosX;
}
public static int GetHardPosY()
{
//	EventManagement.AddPendingEvent(1,20,1,2);
return hardPosY;
}
public static int GetHardAngle()
{
//	EventManagement.AddPendingEvent(1,20,1,2);
return hardAlpha;
}
public static int GetNorthOrientation()
{
	EventManagement.AddPendingEvent(1,100,1,2);
//	RobotMainServer.octavePendingRequest=1;    // request info uptodate
//	RobotMainServer.octaveRequestPending=true;
	SendUDP snd = new SendUDP();
	snd.SendEcho();
//	while(RobotMainServer.javaRequestStatusPending==true)
	
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
public static void ValidHardPosition()
{
SetPosX(hardPosX);
SetPosY(hardPosY);
SetAlpha(hardAlpha);
}
public static void SetTraceFileOn (boolean value)
{

	File file = new File(fname);
	FileOutputStream fos = null;
	try {
		fos = new FileOutputStream(file);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	PrintStream ps = new PrintStream(fos);
	System.setOut(ps);
	System.out.println("create trace");

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
EventManagement.AddPendingEvent(8,120,1,2);
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendUDPInit(posX,posY,alpha,currentLocProb);
Fenetre2.ValidePosition(posX, posY, alpha);
	}
public static void UpdateRobotStatus()
{
EventManagement.AddPendingEvent(1,2,1,2);
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendEcho();
	}
public static void NorthAlign(int northShift)
{
	EventManagement.AddPendingEvent(6,900,1,2);
//	RobotMainServer.octaveRequestPending=true;
	SendUDP snd = new SendUDP();
	snd.NorthAlignRobot(northShift);
}
public static void RefreshHardPositionOnScreen()
{
Fenetre2.RefreshHardPosition();
	}
public static void StopRobotServer()
{
System.exit(0);
	}
public static void StartTimeoutManagement()
{
	TimeoutManagement timeout=new TimeoutManagement();
	timeout.start();
}
public static int GetRetcode(int reqCode,int reqSource,int reqDest)
{
	int i=0;
	int retCode=0;
	for ( i=0;i<EventManagement.sizeTable;i++)
	{
//		System.out.println	(EventManagement.pendingRequestTable[i][1]+" "+EventManagement.pendingRequestTable[i][5]+" "+EventManagement.pendingRequestTable[i][3]+" "+EventManagement.pendingRequestTable[i][4]);

		if (EventManagement.pendingRequestTable[i][1]==reqCode && EventManagement.pendingRequestTable[i][3]==reqSource  && EventManagement.pendingRequestTable[i][4]==reqDest)
		{
			retCode= EventManagement.pendingRequestTable[i][5];
		}
	}
	return retCode;
}

}