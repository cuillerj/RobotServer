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
	public static int deltaNORotation;
	public static int deltaNOMoving;
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
	public static boolean serverJustRestarted=false;
	public static boolean debugCnx=false;
	public static boolean javaRequestStatusPending=false;
	public static boolean octaveRequestPending=false;
	public static int octavePendingRequest=0;
	public static boolean interactive=false;
	public static int simulation=0;
	public static String fname="robotJavaTrace.txt";
	public static final int robotInfoUpdated=1;
	public static final int robotUpdatedEnd=8;
	public static final int scanning =102;   // 0x66
	public static final int moving =104;     // 0x68
	public static final int scanEnd=103;
	public static final int moveEnd=105;
	public static final int northAlignEnd=107;
	public static final int servoAlignEnd=108;
	public static final int pingFBEnd=109;
	public static final int eventJava=0;
	public static final int eventOctave=10;
	public static final int eventArduino=20;
	public static int[][] actionSimulable=new int[200][2];    // row 1 (simulable 0 ou 1) row 2 shift dest si simu
	public static int[][] eventTimeoutTable=new int[200][3]; // row 1 normal mode and up to 2 simulator mode 3 reserved
	public static final byte moveRetcodeEncoderLeftLowLevel =1;
	public static final byte moveRetcodeEncoderRightLowLevel= 2;
	public static final byte moveRetcodeEncoderLeftHighLevel =3;
	public static final byte moveRetcodeEncoderRightHighLevel =4;
	public static final byte moveUnderLimitation =5;
	public static final byte moveKoDueToSpeedInconsistancy =10;
	public static final byte moveKoDueToObstacle =7;
	public static final byte diagMotorPbLeft= 0;
	public static final byte diagMotorPbRight =1;
	public static final byte diagMotorPbSynchro =2;
	public static final byte diagMotorPbEncoder =3;
	public static final byte diagRobotPause =0;
	public static final byte diagRobotObstacle= 1;
	public static final int stepSize =10;
	public static final byte resetMotor =0;
	public static final byte resetObstacle =1;
	public static final byte resetPause =2;
	public static int cumulativeLeftHoles=0;
	public static int cumulativeRightHoles=0;
	public static long lastSentTime= 0;

//	public static String ipRobot="aprobot";  // 138 ou 133
	static char[] TAB_BYTE_HEX = { '0', '1', '2', '3', '4', '5', '6','7',
            '8', '9', 'A', 'B', 'C', 'D', 'E','F' };
	
public static void main(String args[]) throws Exception
			{
	String pgmId="Mainserver";
	String mess="start robot main server";
    TraceLog Trace = new TraceLog();
    Trace.TraceLog(pgmId,mess);
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
	initEventTable();
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
public static void LaunchSimu()
{
	SetSimulationMode(1);
	initEventTable();
	StartTimeoutManagement();
	FenetreGraphiqueSonar ihm3 = new FenetreGraphiqueSonar();
//	ihm2.SetInitialPosition();
	ihm3.SetInitialPosition();  // au moins une fenetre active avant de detacher le batch
			//InitRobot();
//	RobotBatchServer batch = new RobotBatchServer();
//	Thread myThread = new Thread(batch);
//	myThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
//	myThread.start(); 
	}
public static void initEventTable()
{                                                // duration expressed in 1/10 of second
	eventTimeoutTable[robotInfoUpdated][0]=100; // normal mode
	eventTimeoutTable[robotInfoUpdated][1]=20;  // simulation mode
	eventTimeoutTable[robotUpdatedEnd][0]=100; // normal mode
	eventTimeoutTable[robotUpdatedEnd][1]=20;  // simulation mode
	eventTimeoutTable[scanEnd][0]=1200; // normal mode
	eventTimeoutTable[scanEnd][1]=1200;  // simulation mode
	eventTimeoutTable[moveEnd][0]=300; // normal mode
	eventTimeoutTable[moveEnd][1]=30;  // simulation mode
	eventTimeoutTable[northAlignEnd][0]=900; // normal mode
	eventTimeoutTable[northAlignEnd][1]=30;  // simulation mode
	eventTimeoutTable[servoAlignEnd][0]=100; // normal mode
	eventTimeoutTable[servoAlignEnd][1]=10;  // simulation mode
	eventTimeoutTable[pingFBEnd][0]=100; // normal mode
	eventTimeoutTable[pingFBEnd][1]=10;  // simulation mode
	
	actionSimulable[robotInfoUpdated][0]=1;   // simulation enable
	actionSimulable[robotInfoUpdated][1]=1;  // simulation shift value
	actionSimulable[robotUpdatedEnd][0]=1; 
	actionSimulable[robotUpdatedEnd][1]=1; 
	actionSimulable[moveEnd][0]=1;       // simulation enable
	actionSimulable[moveEnd][1]=1;       // simulation shift value
	actionSimulable[northAlignEnd][0]=1;
	actionSimulable[northAlignEnd][1]=1;
	actionSimulable[pingFBEnd][0]=1;
	actionSimulable[pingFBEnd][1]=1;
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
public static int GetEventTimeout(int idx)
{
	return eventTimeoutTable[idx][simulation];
}
public static void Scan360()
{
    int newIdScan=0;
	octaveRequestPending=true;
	RobotMainServer.runningStatus=2;
    RobotMainServer.idscanG= Integer.toString(newIdScan);
    RobotMainServer.scanStepCount=1;
    Fenetre.idscan.setText(RobotMainServer.idscanG);
    Fenetre.label.setText("Scan 360 requested");   
 //   System.out.println(RobotMainServer.idscanG);
	int action=scanEnd;
	int timeout=eventTimeoutTable[action][simulation];
	EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
	if(simulation*actionSimulable[action][0]==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendUDPScan();
	}
}
public static void Move(long ang,long mov)
{
//	System.out.println("Move requested");
//    RobotMainServer.idscanG= Fenetre.idscan.getText();
	octaveRequestPending=true;
	int action=moveEnd;
    Fenetre.label.setText("Move requested");   
	RobotMainServer.runningStatus=4;
	if (simulation!=0)
	{
		ArduinoSimulator.SaveMoveRequest(ang,mov);
		ArduinoSimulator.SaveNorthAlignRequest((northOrientation+ang)%360);
	}
	int timeout=eventTimeoutTable[action][simulation];
//	System.out.println("timeout:"+timeout);
	EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
    if ((mov!=0 || ang!=0) && simulation*actionSimulable[action][0]==0)
    {
    SendUDP snd = new SendUDP();
    snd.SendUDPMove((long)ang,(long) mov);
    Fenetre2.PosActualise(ang,mov);
    }
    RobotMainServer.actStat=0x01;  //demande mov
}
/*
public static void GoTo(long gotoX,long gotoY)
{
	octaveRequestPending=true;
    Fenetre.label.setText("GoTo");   
	RobotMainServer.runningStatus=4;
	int timeout=eventTimeoutTable[4][simulation];
	EventManagement.AddPendingEvent(4,timeout,1,2);
    if (posX!=gotoX|| posY!=gotoY)
    {
    SendUDP snd = new SendUDP();
    snd.SendUDPGoto ((long)gotoX,(long)gotoY);
    }
    RobotMainServer.actStat=0x01;  //demande mov
}
*/
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
public static int GetHeading()
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
public static int GetHardHeading()
{
//	EventManagement.AddPendingEvent(1,20,1,2);
return hardAlpha;
}
public static int GetNorthOrientation()
{
	int action=robotInfoUpdated;
	int timeout=eventTimeoutTable[action][simulation];
	EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
//	RobotMainServer.octavePendingRequest=1;    // request info uptodate
//	RobotMainServer.octaveRequestPending=true;
	if(simulation*actionSimulable[action][0]==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendEcho();
	}

//	while(RobotMainServer.javaRequestStatusPending==true)
	
return northOrientation;
}
public static int GetDeltaNORotation()
{
//	while(RobotMainServer.javaRequestStatusPending==true)
	
return	deltaNORotation;
}
public static int GetDeltaNOMoving()
{
//	while(RobotMainServer.javaRequestStatusPending==true)
	
return	deltaNOMoving;
}
public static int GetCurrentLocProb()
{
return currentLocProb;
}
public static int GetEventArduinoDest(int reqType)
{
return (eventArduino+simulation*actionSimulable[reqType][0]*actionSimulable[reqType][1]);
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
public static void SetHeading(int value)
{
alpha=value;
Fenetre2.ValideOrientation(value);
}
public static void SetDebugCnxOn (boolean value)
{
debugCnx=value;
}
public static void SetSimulationMode (int value)
{
	if (value<3)
	{
		simulation=value;
		TraceLog Trace = new TraceLog();
		String mess="Set Simulation Mode:"+value;
		Trace.TraceLog("RobotMainServer",mess);
	}
}
public static void ValidHardPosition()
{
SetPosX(hardPosX);
SetPosY(hardPosY);
SetHeading(hardAlpha);
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
int action=robotUpdatedEnd;
int timeout=eventTimeoutTable[action][simulation*actionSimulable[action][0]];
EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
octaveRequestPending=true;
if(simulation*actionSimulable[action][0]==0)
{
	SendUDP snd = new SendUDP();
	snd.SendUDPInit(posX,posY,alpha,currentLocProb);

}
Fenetre2.ValidePosition(posX, posY, alpha);
	}
public static void UpdateRobotStatus()
{
int timeout=eventTimeoutTable[robotInfoUpdated][simulation];
EventManagement.AddPendingEvent(robotInfoUpdated,timeout,eventOctave,eventArduino+simulation*actionSimulable[robotInfoUpdated][0]*actionSimulable[robotInfoUpdated][1]);
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendEcho();
	}
public static void ResetRobotStatus()
{
int timeout=eventTimeoutTable[robotInfoUpdated][simulation];
EventManagement.AddPendingEvent(robotInfoUpdated,timeout,eventOctave,eventArduino+simulation*actionSimulable[robotInfoUpdated][0]*actionSimulable[robotInfoUpdated][1]);
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendUDPReset();
	}
public static void RobotAlignServo(int value)
{             // request servomotor alignment from 0 to 180�
int timeout=eventTimeoutTable[servoAlignEnd][simulation];
EventManagement.AddPendingEvent(servoAlignEnd,timeout,eventOctave,eventArduino+simulation*actionSimulable[robotInfoUpdated][0]*actionSimulable[robotInfoUpdated][1]);
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendUDPServoAlign(value);
	}
public static void RobotNorthRotate(int value)
{             // request servomotor alignment from 0 to 180�
int action=northAlignEnd;
int timeout=eventTimeoutTable[action][simulation];
if (simulation!=0)
{
	ArduinoSimulator.SaveMoveRequest(value,0);
	ArduinoSimulator.SaveNorthAlignRequest((northOrientation+value)%360);
}
EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
octaveRequestPending=true;
if(simulation*actionSimulable[action][0]==0)
	{
	SendUDP snd = new SendUDP();
	snd.SendUDPNorthRotate(value);
	}
}
public static void PingEchoFrontBack()
{             // 
int action=pingFBEnd;
int timeout=eventTimeoutTable[action][simulation];
EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
octaveRequestPending=true;
RobotMainServer.scanStepCount=1;  // use robot.GetScanDist...(0) to get front and echo distance
if ( simulation*actionSimulable[action][0]==0)
	{
	SendUDP snd = new SendUDP();
	snd.SendUDPPingEchoFrontBack();
	}
}
public static void NorthAlign(int northShift)
{
	int action=northAlignEnd;
	int timeout=eventTimeoutTable[action][simulation];
	ArduinoSimulator.SaveMoveRequest((northShift)%360,0);
	ArduinoSimulator.SaveNorthAlignRequest((northShift)%360);
	EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
//	RobotMainServer.octaveRequestPending=true;
	octaveRequestPending=true;
	if(simulation*actionSimulable[action][0]==0)
		{
		SendUDP snd = new SendUDP();
		snd.NorthAlignRobot(northShift);
		}
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
public static void Horn(int duration)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SendUDPHorn(duration);
}
public static void SetShifPulse(int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SendUDPShiftPulse(value);
}
public static void SetObstacleDetection(boolean value)
{             // 0 off 1 on
	SendUDP snd = new SendUDP();
	snd.SendUDPObstacleDetection(value);
}
public static int GetParametersNumValue(int ID)
{
	int value=ParametersSetting.GetParametersNumValue(ID);
	
return	value;
}

}