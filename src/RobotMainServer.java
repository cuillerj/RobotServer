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

import StopTensorFlow.StopTensorFlow;

public class RobotMainServer 
	{
	public static float version=(float) 2.4;
	public static int posXG=0;
	public static int posYG=0;
	public static int orientG=0;
	public static String idscanG="0";
	public static int countScan=0;
	public static int scanSeqLen=20;
	public static int posX;
	public static int posY;
	public static int alpha;
	public static int hardPosX;
	public static int hardPosY;
	public static int hardAlpha;
	public static int northOrientation;
	public static int absoluteOrientation;
	public static int deltaNORotation;
	public static int deltaNOMoving;
	public static int gyroHeading=0;
	public static byte actStat=0x00;
	public static String stationStatus="";
//	public static String ipRobot="192.168.1.35";  //
	public static InetAddress ipRobot=null;  // 
	public static int shiftparameterid=8;
	public static double shiftEchoVsRotationCenter = 0;  // to be read in DB
	public static int scanArraySize = 15;
	public static int[][] scanArray = new int [scanArraySize][4];
	public static int[] pingArray = new int [4];
	public static int[] pathDistances = new int [5];
	public static int[] pathEchos = new int [20];
	public static int scanStepCount=0;
	public static int scanReceiveCount=0;
	public static int lastEchoFront=0;
	public static int lastEchoBack=0;	
	public static int power9V;
	public static int power5V;
	public static int powerDiag;
	public static int motorDiag;
	public static int connectionDiag;
	public static int robotDiag;
	public static int runningStatus=0;
	public static int idCarto=1;                   // a rendre modifiable
	public static int trainBatch=0;
	public static int currentLocProb=0;
	public static boolean hardJustReboot=false;
	public static boolean serverJustRestarted=false;
	public static boolean debugCnx=false;
	public static boolean javaRequestStatusPending=false;
	public static boolean octaveRequestPending=false;
	public static int octavePendingRequest=0;
	public static boolean interactive=false;
	public static int frameDecodeLevel=2;
	public static int simulation=0;
	public static int batchTypeFlag=0;  // 0:no batch 1: real mode running  2 simulation mode running
	public static int simulatedHardX=0;     // used in simulation corresponding to hard location to guess
	public static int simulatedHardY=0;
	public static int simulatedHardH=0;
	public static String fname="robotJavaTrace.txt";
	public static Process TfProcess=null;
	public static int actionRetcode=0;
	public static final int robotInfoUpdated=1;
	public static final int robotUpdatedEnd=8;
	public static final int scanning =102;   // 0x66
	public static final int moving =104;     // 0x68
	public static final int scanEnd=103;
	public static final int moveEnd=105;
	public static final int northAlignEnd=107;
	public static final int servoAlignEnd=108;
	public static final int pingFBEnd=109;
	public static final int gyroRotating=110;  // 0x6e
	public static final int gyroRotateEnd=111;  // 0x6f
	public static final int moveAcrossPass=128;
	public static final int moveAcrossPassEnded=129;
	public static final int requestBNOEnd=118;
	public static final int robotNOUpdated=123;
	public static final int eventJava=0;
	public static final int eventOctave=10;
	public static final int eventArduino=20;
	public static int[][] actionSimulable=new int[200][2];    // row 1 (simulable 0 ou 1) row 2 shift dest si simu
	public static int[][] eventTimeoutTable=new int[200][3]; // row 1 normal mode and up to 2 simulator mode 3 reserved
	public static final byte moveRetcodeEncoderLeftLowLevel =0x01;
	public static final byte moveRetcodeEncoderRightLowLevel= 0x02;
	public static final byte moveRetcodeEncoderLeftHighLevel =0x03;
	public static final byte moveRetcodeEncoderRightHighLevel =0x04;
	public static final byte moveWheelSpeedInconsistancy=0x10;
	public static final byte moveUnderLimitation =0x05;
	public static final byte moveKoDueToWheelStopped =0x0a;
	public static final byte moveKoDueToObstacle =0x07;
	public static final byte moveKoDueToNotEnoughSpace = 0x0b;
	public static final byte moveAcrossPathKoDueToWheelStopped = 0x6e;
	public static final byte moveAcrossPathKoDueToObstacle = 0x6b;
	public static final byte moveAcrossPathKoDueToNotEnoughSpace = 0x6f;
	public static final byte moveAcrossPathKoDueToNotFindingStart = 0x70;
	public static final byte moveAcrossPathKoDueToNotFindingEntry = 0x71; // 0x71
	public static final byte moveAcrossPathKoDueToNotFindingExit = 0x72;
	public static final String [] moveRetcodeList= new String[20];
	public static final String [][] retcodeList= new String[256][256]; // [action,retCode]
	public static final String [] actionList= new String[256]; // 
	public static final byte rotationKoToManyRetry =(byte) 0xfe;
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
	public static int prevCumulativeLeftHoles=0;
	public static int cumulativeRightHoles=0;
	public static int prevCumulativeRightHoles=0;
	public static long lastSentTime= 0;
	public static int leftHighThreshold=0;
	public static int leftLowThreshold=0;
	public static int rightHighThreshold=0;
	public static int rightLowThreshold=0;
	public static int leftPWM=0;
	public static int rightPWM=0;
	public static int PWMRatio=0;
	public static int SlowPWMRatio=0;
	public static int leftMaxLevel=0;
	public static int leftMinLevel=0;
	public static int rightMaxLevel=0;
	public static int rightMinLevel=0;
	public static int echoClosestRefX=0;
	public static int echoClosestRefY=0;
	public static int echoClosestRefServoHeading=0;
	public static int echoClosestRefDistFront=0;
	public static int echoClosestRefDistBack=0;
	public static float echoClosestStdFront=0;
	public static float echoClosestStdBack=0;
	public static int echoClosestCount=0;
	public static int echoClosestDistance=0;
	public static float noiseLevel=0;         // level of noise on rotation & move 
	public static float scanNoiseLevel=0;         // level of noise on rotation & move 	
	public static boolean noiseRetCode=false;  // is there noise on move retCode
	public static boolean noisePath=false;      // simulation noise going across path
	public static int noiseRetValue=1;   // if noise on move retcode define random limit (higher lower the noise)
	public static byte BNOMode=0x00;
	public static byte BNOCalStat=0x00;
	public static byte BNOSysStat=0x00;
	public static byte BNOSysError=0x00;
	public static boolean pendingAcqUdp=false;
	public static int BNOLeftPosX=0;
	public static int BNOLeftPosY=0;
	public static int BNORightPosX=0;
	public static int BNORightPosY=0;
	public static int BNOLocFlag=0;
	public static int BNOLocHeading=0;
	public static int voltage1=0;
	public static int voltage2=0;
	public static int voltage3=0;
	public static int voltage4=0;
	public static int voltage5=0;
	public static int voltage6=0;
	public static int voltage7=0;
	public static int arduinoVersion=0;
	public static int arduinoSubVersion=0;
	public static byte requestPingFrontBack=0x70;
	public static byte requestUpdateNO=0x7b;
	public static byte requestBNOData=0x7c;
	public static byte requestNarrowPathMesurments=0x7d;
	public static byte requestNarrowPathEchos =0x7e;
	public static byte respBNOSubsytemStatus=0x75;
	public static byte respBNOLocation=0x76;
	public static byte respNarrowPathMesurments=0x77;
	public static byte respNarrowPathEchos=0x78;
	public static byte requestTrace = (byte) 0x90;
	public static byte requestTraceNO = (byte) 0x91;
	public static byte respTrace=(byte) 0x90;
	public static byte respTraceNO=(byte) 0x91;
	public static byte requestSleep=(byte) 0x92;
	public static byte requestVersion=(byte) 0x93;
	public static byte respVersion=(byte) 0x93;
	public static byte northAlignRequest=(byte) 0x45;
	public static byte requestPID = (byte)0x94;
	public static byte respPID = (byte)0x94;
	public static byte setPID = (byte)0x95;
	public static byte requestIRsensors = (byte)0x96;
	public static int respIRsensors = (byte)0x96 & 0x000000ff;;
	public static byte requestInternalFlags = (byte)0x97;
	public static int respInternalFlags = (byte)0x97 & 0x000000ff;;
	public static int obstacleHeading=0;
	public static byte IRMap=0x00;
	public static boolean pendingNarrowPathEchos=false;
	public static boolean pendingNarrowPathMesurments=false;
//	public static String ipRobot="aprobot";  // 138 ou 133
	static char[] TAB_BYTE_HEX = { '0', '1', '2', '3', '4', '5', '6','7',
            '8', '9', 'A', 'B', 'C', 'D', 'E','F' };
	public static PrintStream stdout = System.out;
	public static int retCodeDetail=0;
	public static String pgmId="Mainserver";
	
public static void main(String args[]) throws Exception
			{

	System.out.println("start robot main server version: "+version);

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
    ids=ids.replaceAll("\\W", ""); 
	int indscan=Integer.parseInt(ids);

//	  System.out.println("actualise position:"+indscan);
try {

Class.forName("com.mysql.jdbc.Driver").newInstance();
//String connectionUrl = "jdbc:mysql://jserver:3306/robot";
//String connectionUser = "jean";
//String connectionPassword = "manu7890";
String connectionUrl = GetSqlConnection.GetRobotDB();
String connectionUser = GetSqlConnection.GetUser();
String connectionPassword = GetSqlConnection.GetPass();
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
public static int  GetMaxScanID() {
	Connection conn = null;
	Statement stmtR1 = null;
	Statement stmtI1 = null;
	int maxScanId=-1;
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
rs = stmtR1.executeQuery("SELECT max(idscan) as max FROM scanResult"); 

while (rs.next()) {
	maxScanId= rs.getInt("max");
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
return maxScanId;
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
public static int  LaunchBatch()
{
	if (batchTypeFlag==1)
	{
		return 0;		
	}
	if (batchTypeFlag==0)
	{	
	initEventTable();
	DecodeUDPFrame.InitTable();
	StartTimeoutManagement();
//enetreGraphiqueSonar ihm3 = new FenetreGraphiqueSonar();
//	ihm2.SetInitialPosition();
//	ihm3.SetInitialPosition();  // au moins une fenetre active avant de detacher le batch
			//InitRobot();
	RobotBatchServer batch = new RobotBatchServer();
	Thread myThread = new Thread(batch);
	myThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
	myThread.start(); 
	MonitorUDPLink monitUDP=new MonitorUDPLink();
	Thread udpThread = new Thread(monitUDP);
	udpThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
	udpThread.start(); 
	batchTypeFlag=1;
	return 0;	
	}
	else{
		return -1;  // can not switch between real and simulation mode
	}
	}
public static int  LaunchSimu()
{
	if (batchTypeFlag==2)
	{
		return 0;		
	}
	if (batchTypeFlag==0)
	{	
	SetSimulationMode(1);
	initEventTable();
	DecodeUDPFrame.InitTable();
	StartTimeoutManagement();
//	Fenetre ihm = new Fenetre();
//	Fenetre2 ihm2 = new Fenetre2();
	FenetreGraphiqueSonar ihm3 = new FenetreGraphiqueSonar();
//	ihm2.SetInitialPosition();
	ihm3.SetInitialPosition();
	TraceEvents trace=new TraceEvents();
//	trace.TraceEvents();
	RobotBatchServer batch = new RobotBatchServer();
	Thread myThread = new Thread(batch);
	myThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
	trace.start();
	batchTypeFlag=2;
			//InitRobot();
//	RobotBatchServer batch = new RobotBatchServer();
//	Thread myThread = new Thread(batch);
//	myThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
//	myThread.start();
	return 0;	
	}
	else{
		return -1; // can not switch between real and simulation mode
	}
	}
public static void initEventTable()
{                                                // duration expressed in 1/10 of second
	eventTimeoutTable[robotInfoUpdated][0]=100; // normal mode
	eventTimeoutTable[robotInfoUpdated][1]=20;  // simulation mode
	eventTimeoutTable[robotUpdatedEnd][0]=100; // normal mode
	eventTimeoutTable[robotUpdatedEnd][1]=20;  // simulation mode
	eventTimeoutTable[scanEnd][0]=1200; // normal mode
	eventTimeoutTable[scanEnd][1]=100;  // simulation mode
	eventTimeoutTable[moveEnd][0]=600; // normal mode
	eventTimeoutTable[moveEnd][1]=30;  // simulation mode
	eventTimeoutTable[northAlignEnd][0]=3000; // normal mode
	eventTimeoutTable[northAlignEnd][1]=30;  // simulation mode
	eventTimeoutTable[servoAlignEnd][0]=100; // normal mode
	eventTimeoutTable[servoAlignEnd][1]=10;  // simulation mode
	eventTimeoutTable[pingFBEnd][0]=100; // normal mode
	eventTimeoutTable[pingFBEnd][1]=10;  // simulation mode
	eventTimeoutTable[robotNOUpdated][0]=500; // normal mode
	eventTimeoutTable[robotNOUpdated][1]=20;  // simulation mode
	eventTimeoutTable[moveAcrossPassEnded][0]=1200; // normal mode
	eventTimeoutTable[moveAcrossPassEnded][1]=60;  // simulation mode
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
	actionSimulable[scanEnd][0]=1;
	actionSimulable[scanEnd][1]=1;
	actionSimulable[moveAcrossPassEnded][0]=1; // normal mode
	actionSimulable[moveAcrossPassEnded][1]=1;  // simulation mode

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
public static void ClearScanArray()
{

	for (int i=0;i<scanArraySize;i++)
	{
		scanArray[i][0]=0;
		scanArray[i][1]=0;
		scanArray[i][2]=0;
		scanArray[i][3]=0;
	}
}
public static int GetScanNOOrientation()
{
	int i;
	int average=0;
	for (i=0;i<scanArraySize;i++)
	{
		average=average+scanArray[i][3];
	}
	average=average/scanArraySize;
	return average;
}
public static int SetScanAngle(int idx,int value)
{
	scanArray[idx][0]=value;
	return scanArray[idx][0];

}
public static int SetScanDistFront(int idx,int value)
{
	scanArray[idx][1]=value;
	return scanArray[idx][1];

}
public static int SetScanDistBack(int idx, int value)
{
	scanArray[idx][2]=value;
	return scanArray[idx][2];
}
public static void SetScanId(int value)
{
    RobotMainServer.idscanG= Integer.toString(value);
    Fenetre.idscan.setText(RobotMainServer.idscanG);
    Fenetre.label.setText("Init scanID");  
}
public static void SetTrainBatch(int value)
{
    RobotMainServer.trainBatch= value;
}
public static int GetScanNorthOrientation(int idx)
{
	return scanArray[idx][3];
}
public static int GetEventTimeout(int idx)
{
	return eventTimeoutTable[idx][simulation];
}
public static void Scan360()
{
    int newIdScan=0;
    ClearScanArray();
    PanneaugraphiqueSonar.razPoints();
    scanReceiveCount=0;
	octaveRequestPending=true;
	RobotMainServer.runningStatus=2;
    RobotMainServer.idscanG= Integer.toString(newIdScan);
    RobotMainServer.scanStepCount=1;
    RobotMainServer.countScan=0;
    Fenetre.idscan.setText(RobotMainServer.idscanG);
    Fenetre.label.setText("Scan 360 requested");   
	int action=scanEnd;
	int timeout=eventTimeoutTable[action][simulation];
	EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
	if(simulation*actionSimulable[action][0]==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendUDPScan();
	}
	else{
		GetScanRawData(simulatedHardX,simulatedHardY);
	}
}
public static void Scan360Id (int value)
{
	octaveRequestPending=true;
    scanReceiveCount=0;
    ClearScanArray();
    PanneaugraphiqueSonar.razPoints();
	RobotMainServer.runningStatus=2;
    RobotMainServer.idscanG= Integer.toString(value);
    RobotMainServer.scanStepCount=1;
    RobotMainServer.countScan=0;
    Fenetre.idscan.setText(RobotMainServer.idscanG);
    Fenetre.label.setText("ScanId 360  requested");   
	int action=scanEnd;
	int timeout=eventTimeoutTable[action][simulation];
	EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
	if(simulation*actionSimulable[action][0]==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendUDPScan();
	}
	else{
		GetScanRawData(simulatedHardX,simulatedHardY);
	}
}

public static int GetScanRawData(int inX,int inY)
{
    int retCode=GetSqlData.GetScanRawData(inX,inY);
    return retCode;
}
public static int GetPathDistances(int idx)
{
    return pathDistances[idx];
}
public static int GetPathEchosRight(int idx)
{
    return pathEchos[2*idx];
}
public static int GetPathEchosLeft(int idx)
{
    return pathEchos[2*idx+1];
}
public static void Move(long ang,long mov)
{
//	System.out.println("Move requested");
//    RobotMainServer.idscanG= Fenetre.idscan.getText();
	BNOLocFlag=255;
	octaveRequestPending=true;
	int action=moveEnd;
    Fenetre.label.setText("Move requested");   
	RobotMainServer.runningStatus=4;
	if (simulation!=0)
	{
		ArduinoSimulator.SaveNorthAlignRequest((northOrientation+ang)%360);
		ArduinoSimulator.SaveMoveRequest(ang,mov);
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


public static void MoveAcrossNarrowPass(int distance,int witdh, int length,int startToEntryDistance,int northOrientation, int lenToDo,int echoToGet)
{
//	System.out.println("Move requested");
//    RobotMainServer.idscanG= Fenetre.idscan.getText();
	BNOLocFlag=255;
	octaveRequestPending=true;
	int action=moveAcrossPassEnded;
    Fenetre.label.setText("Move APath requested");   
	RobotMainServer.runningStatus=4;
	if (simulation!=0)
	{
		ArduinoSimulator.accrossDistance=distance;
		ArduinoSimulator.accrossWidth=witdh;
		ArduinoSimulator.accrossLength=length;
		ArduinoSimulator.accrossStartToEntryDistance=startToEntryDistance;
		ArduinoSimulator.accrossnNorthOrientation=northOrientation;
		ArduinoSimulator.accrossLenToDo=lenToDo;
		ArduinoSimulator.accrossEchoToGet=echoToGet;
	}
	int timeout=eventTimeoutTable[action][simulation];
//	System.out.println("timeout:"+timeout);
	EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
    if ((lenToDo!=0 || distance!=0) && simulation*actionSimulable[action][0]==0)
    {
    SendUDP snd = new SendUDP();
    snd.SendMoveAcrossNarrowPass(distance, witdh, length,startToEntryDistance,northOrientation, lenToDo,echoToGet);
    Fenetre2.PosActualise(0,lenToDo);
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
public static int GetGyroHeading()
{
return gyroHeading;
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
public static int RefreshNorthOrientation()
{
	int action=robotNOUpdated;
	int prevNorthOrientation=northOrientation;
	northOrientation=999;
	int timeout=eventTimeoutTable[action][simulation];
	EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
//	RobotMainServer.octavePendingRequest=1;    // request info uptodate
//	RobotMainServer.octaveRequestPending=true;
	if(simulation*actionSimulable[action][0]==0)
	{
		SendUDP snd = new SendUDP();
		snd.RefreshNorthOrientation();
	}

//	while(RobotMainServer.javaRequestStatusPending==true)
	
return prevNorthOrientation;
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
	reqType=reqType & 0x000000ff;
	return (eventArduino+simulation*actionSimulable[reqType][0]*actionSimulable[reqType][1])& 0x000000ff;
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

	if (value==true)
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
		System.out.println(pgmId+" version: "+version+" > create trace");
	}
	else
	{
		System.out.println("switch to stdout");
		System.setOut(stdout);  
	}

}
public static boolean GetHardJustReboot ()
{
return hardJustReboot;
}
public static boolean GetOctaveRequestPending ()
{
return octaveRequestPending;
}
public static int GetRetcodeDetail ()
{
return retCodeDetail & 0x000000ff;
}
public static void SetCurrentLocProb (int value)
{
currentLocProb=value;
Fenetre2.SetcurrentLocProb();
}
public static void UpdateHardRobotLocation()
{
BNOLocFlag=255;
int timeout=eventTimeoutTable[robotInfoUpdated][simulation];
EventManagement.AddPendingEvent(robotInfoUpdated,timeout,eventOctave,eventArduino+simulation*actionSimulable[robotInfoUpdated][0]*actionSimulable[robotInfoUpdated][1]);
octaveRequestPending=true;
if(simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendUDPInit(posX,posY,alpha,currentLocProb);
	}
else
	{
		hardPosX=posX;
		hardPosY=posY;
		hardAlpha=alpha;
	}
/*
int action=robotUpdatedEnd;
int timeout=eventTimeoutTable[action][simulation*actionSimulable[action][0]];
EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
octaveRequestPending=true;
if(simulation*actionSimulable[action][0]==0)
{
	SendUDP snd = new SendUDP();
	snd.SendUDPInit(posX,posY,alpha,currentLocProb);

}
*/
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
public static void requestBNOData()
{
int timeout=eventTimeoutTable[robotInfoUpdated][simulation];
EventManagement.AddPendingEvent(robotInfoUpdated,timeout,eventOctave,eventArduino+simulation*actionSimulable[robotInfoUpdated][0]*actionSimulable[robotInfoUpdated][1]);
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendRequestBNOData();
	}
public static void ResetRobotStatus()
{
int timeout=eventTimeoutTable[robotInfoUpdated][simulation];
EventManagement.AddPendingEvent(robotInfoUpdated,timeout,eventOctave,eventArduino+simulation*actionSimulable[robotInfoUpdated][0]*actionSimulable[robotInfoUpdated][1]);
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendUDPReset();
	}
public static void RequestVersion()
{
int timeout=eventTimeoutTable[robotInfoUpdated][simulation];
EventManagement.AddPendingEvent(robotInfoUpdated,timeout,eventOctave,eventArduino+simulation*actionSimulable[robotInfoUpdated][0]*actionSimulable[robotInfoUpdated][1]);
octaveRequestPending=true;
SendUDP snd = new SendUDP();
snd.SendRequestVersion();
	}
public static void RequestInternalFlags()
{
SendUDP snd = new SendUDP();
snd.RequestInternalFlags();
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
{             
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
public static void RobotGyroRotate(int value)
{             
	TraceLog Trace = new TraceLog();
	String mess="RobotGyroRotate:"+value;
	Trace.TraceLog("RobotMainServer",mess);
int action=moveEnd;
int timeout=eventTimeoutTable[action][simulation];
if (simulation!=0)
{
	ArduinoSimulator.SaveGyroRequest(value);
	ArduinoSimulator.SaveNorthAlignRequest((northOrientation+value)%360);
}
EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
octaveRequestPending=true;
if(simulation*actionSimulable[action][0]==0)
	{
	SendUDP snd = new SendUDP();
	snd.SendUDPGyroRotate(value);
	}
}
public static void PingEchoFrontBack()
{             // 
int action=pingFBEnd;
int timeout=eventTimeoutTable[action][simulation];
for (int i=0;i<4;i++)
{
	RobotMainServer.pingArray[i]=0;
}
RobotMainServer.idscanG= Integer.toString(0);
Fenetre.idscan.setText(RobotMainServer.idscanG);
Fenetre.label.setText("Init scanID"); 
EventManagement.AddPendingEvent(action,timeout,eventOctave,eventArduino+simulation*actionSimulable[action][0]*actionSimulable[action][1]);
octaveRequestPending=true;
RobotMainServer.scanStepCount=0;  // use robot.GetScanDist...(0) to get front and echo distance
if ( simulation*actionSimulable[action][0]==0)
	{
	SendUDP snd = new SendUDP();
	snd.SendUDPPingEchoFrontBack();
	}
}

public static int GetPingAngle()
{
	return pingArray[0];
}
public static int GetPingFront()
{
	return pingArray[1];
}
public static int GetPingBack()
{
	return pingArray[2];
}
public static void NorthAlign(int northShift)
{
	int action=northAlignEnd;
	int timeout=eventTimeoutTable[action][simulation];
	if (simulation!=0)
	{
		ArduinoSimulator.SaveMoveRequest((northShift)%360,0);
		ArduinoSimulator.SaveNorthAlignRequest((northShift)%360);
	}
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
	StopTensorFlow();

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
	return retCode & 0x000000ff;
}
public static void Horn(int duration)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendUDPHorn(duration);
	}	
}
public static void SetShifPulse(int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SendUDPShiftPulse(value);
}
public static void SetEncoderThreshold(boolean Left,int lowValue, int highValue)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SetEncoderThreshold(Left,lowValue, highValue);
}
public static void SetPWMMotor(boolean Left,int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SetPWMMotor(Left,value);
}
public static void SetSlowPWMRatio(int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SetSlowPWMRatio(value);
}
public static void SetMotorPulseLenght(int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SetMotorPulseLenght(value);
}
public static void QueryEncodersValues()
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.QueryEncodersValues();
}
public static void SetPowerEncoder(boolean on)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	if (on)
	{
		snd.SendUDPPowerOnOffEncoder((byte) 0x01);	
	}
	else{
		snd.SendUDPPowerOnOffEncoder((byte) 0x00);			
	}

}
public static void QueryMotorsPWM()
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.QueryMotorsPWM();
}

public static void SetMotorsRatio(int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SetMotorsRatio(value);
}
public static void SetObstacleDetection(boolean value)
{             // 0 off 1 on
	SendUDP snd = new SendUDP();
	snd.SendUDPObstacleDetection(value);
}
public static void SetUdpTrace(boolean value)
{             // 0 off 1 on
	SendUDP snd = new SendUDP();
	if(value)
	{
		snd.SendUDPTrace((byte) 0x01);		
	}
	else{
		snd.SendUDPTrace((byte) 0x00);				
	}
}
public static void SetUdpTraceNO(boolean value)
{             // 0 off 1 on
	SendUDP snd = new SendUDP();
	if(value)
	{
		snd.SendUDPTraceNO((byte) 0x01);		
	}
	else{
		snd.SendUDPTraceNO((byte) 0x00);				
	}
}

public static void SetGyroSelectedRange(int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SetGyroSelectedRange(value);
}
public static void SetGyroODR(int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SetGyroODR(value);
}
public static void SetGyroBiasMicrosec(int value)
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.SetGyroBiasMicrosec(value);
}
public static void GetSubsytemRegisters(int register )
{             // duration in seconds up to 254
	SendUDP snd = new SendUDP();
	snd.GetSubsytemRegisters(register);
}
public static void GetSubsystemLocation()
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		BNOLocFlag=255;
		snd.GetSubsytemLocation();
	}
}
public static int GetParameterNumValue(int ID)
{
	int value=ParametersSetting.GetParametersNumValue(ID);
	
return	value;
}
public static int GetParametersNumbers()
{
	int value=ParametersSetting.GetParametersNumbers();
	
return	value;
}

public static String GetParameterName(int number)
{             
	String ParameterName=ParametersName.GetParameterName(number);
	return ParameterName;
}
public static int GetClosestReferenceEcho(int inX,int inY,int servoHeading,int tileSize)
{
	String pgmId="GetClosestReferenceEcho";
	String mess="inX:"+inX+" inY:"+inY+" servoH:"+servoHeading+" size:"+tileSize;
    TraceLog Trace = new TraceLog();
    Trace.TraceLog(pgmId,mess);
	int RC=GetSqlData.GetClosestEchoGetClosestReferenceEcho(inX, inY, servoHeading, tileSize);
return	RC;
}
public static void setBNOMode(int mode)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SetBNOMode((byte) mode);
	}
	else
	{
		BNOMode=(byte)mode;
	}
}
public static void setSleepMode(boolean OnOff)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		EchoRobot.pendingEcho=-10;
		SendUDP snd = new SendUDP();
		snd.SendUDPSleep(OnOff);
	}
	else
	{

	}
}
public static void RequestNarrowPathMesurments()
{             // duration in seconds up to 254
	if (simulation==0)
	{
		pendingNarrowPathMesurments=true;
		SendUDP snd = new SendUDP();
		snd.SendUDPRequestNarrowaPathMesurments();
	}
}
public static void RequestNarrowPathEchos()
{             // duration in seconds up to 254
	if (simulation==0)
	{
		pendingNarrowPathEchos=true;
		SendUDP snd = new SendUDP();
		snd.SendUDPRequestNarrowPathEchos();
	}
}
public static void GetIRSensors()
{
	if(simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendUDPRequestSensors();
	}
}
public static void RequestPID()
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendRequestPID();
	}
}
public static void setPIDKp(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendPIDKp(value);
	}
}
public static void setPIDKi(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendPIDKi(value);
	}
}
public static void setPIDKd(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendPIDKd(value);
	}
}
public static void setLeftSetpoint(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendLeftSetpoint(value);
	}
}
public static void setRightSetpoint(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendRightSetpoint(value);
	}
}
public static void leftMinLimit(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendLeftMinLimit(value);
	}
}
public static void rightMinLimit(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendRightMinLimit(value);
	}
}
public static void leftMaxLimit(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendLeftMaxLimit(value);
	}
}
public static void rightMaxLimit(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendRightMaxLimit(value);
	}
}
public static void leftStartLimit(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendLeftStartLimit(value);
	}
}
public static void rightStartLimit(int value)
{             // duration in seconds up to 254
	if (simulation==0)
	{
		SendUDP snd = new SendUDP();
		snd.SendRightStartLimit(value);
	}
}
public static int StartTensorFlowPrediction()
{             // duration in seconds up to 254
	int retCode=-1;

	if (TfProcess==null){
		Process rc=StartTensorFlowPrediction.runProcess();
		String mess="Start TensorFlow prediction:"+rc;;
	    TraceLog Trace = new TraceLog();
		Trace.TraceLog(pgmId,mess);
		TfProcess=rc;
		retCode=0;
	}
	else{
		String mess="TensorFlow prediction aleardy active";
	    TraceLog Trace = new TraceLog();
		Trace.TraceLog(pgmId,mess);
		DeleteTensorFlowFiles();
	}
	return retCode;
}
public static int DeleteTensorFlowFiles()
{             // duration in seconds up to 254
	int retCode=-1;

		Process rc=DeleteTensorFlowFiles.runProcess();
		String mess="Delete tensor flow files:"+rc;;
	    TraceLog Trace = new TraceLog();
		Trace.TraceLog(pgmId,mess);
		TfProcess=rc;
		retCode=0;

	return retCode;
}
public static int StopTensorFlow()
{             // duration in seconds up to 254
	int retCode=-1;
		Process rc=StopTensorFlow.runProcess();
		String mess="Stop tensor flow:"+rc;;
	    TraceLog Trace = new TraceLog();
		Trace.TraceLog(pgmId,mess);
		TfProcess=rc;
		retCode=0;

	return retCode;
}
@SuppressWarnings("null")
public static int UpdateCurrentShiftNorthOrientation(int value)
{
	Connection conn = null;
	Statement stmtS = null;
	Statement stmtU = null;
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
	//	 String connectionUrl = "jdbc:mysql://jserver:3306/robot";
//		 String connectionUser = "jean";
//		 String connectionPassword = "manu7890";
		String connectionUrl = GetSqlConnection.GetRobotDB();
		String connectionUser = GetSqlConnection.GetUser();
		String connectionPassword = GetSqlConnection.GetPass();
		conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
		String pgmId="Mainserver";
	    TraceLog Trace = new TraceLog();
		int prevValue=0;
		int idparameter=0;
		String sql="SELECT * FROM parameters where name='currentShiftNorthOrientation' limit 1";
	    Trace.TraceLog(pgmId,sql);
		stmtS = conn.createStatement();
		rs = stmtS.executeQuery(sql);
		while (rs.next()) {
		  prevValue = rs.getInt("numValue");
		  idparameter = rs.getInt("idparameter");
		}
		rs.close();
		sql="UPDATE parameters set numValue="+value+" where idparameter="+idparameter+"";
	    Trace.TraceLog(pgmId,sql);
		String mess=" update shift NO prev value:"+prevValue+ " new value:"+value+"";
	    Trace.TraceLog(pgmId,mess);
		stmtU = conn.createStatement();
		stmtU.executeUpdate(sql); 
	
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally {

 		try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
 	}
return value;
}
public static void InitRobotValues()
{
	BNOLeftPosX=0;
	BNOLeftPosY=0;
	BNORightPosX=0;
	BNORightPosY=0;
	BNOLocFlag=0;
	BNOLocHeading=0;
	BNOMode=0x00;
	BNOCalStat=0x00;
	BNOSysStat=0x00;
	BNOSysError=0x00;
	posX=0;
	posY=0;
	alpha=0;
	hardPosX=0;
	hardPosY=0;
	hardAlpha=0;
	northOrientation=0;
	absoluteOrientation=0;
	scanStepCount=0;
	power9V=0;
	power5V=0;
	powerDiag=0;
	motorDiag=0;
	connectionDiag=0;
	robotDiag=0;
	runningStatus=0;
	currentLocProb=0;
	voltage1=0;
	voltage2=0;
	voltage3=0;
	voltage4=0;
	voltage5=0;
	voltage6=0;
	voltage7=0;
	cumulativeLeftHoles=0;
	prevCumulativeLeftHoles=0;
	cumulativeRightHoles=0;
	prevCumulativeRightHoles=0;
	lastSentTime= 0;
	leftHighThreshold=0;
	leftLowThreshold=0;
	rightHighThreshold=0;
	rightLowThreshold=0;
	leftPWM=0;
	rightPWM=0;
	PWMRatio=0;
	SlowPWMRatio=0;
	leftMaxLevel=0;
	leftMinLevel=0;
	rightMaxLevel=0;
	rightMinLevel=0;
	echoClosestRefX=0;
	echoClosestRefY=0;
	echoClosestRefServoHeading=0;
	echoClosestRefDistFront=0;
	echoClosestRefDistBack=0;
	echoClosestStdFront=0;
	echoClosestStdBack=0;
	echoClosestCount=0;
	echoClosestDistance=0;
	moveRetcodeList[0]="ok";
	moveRetcodeList[1]="moveRetcodeEncoderLeftLowLevel";
	moveRetcodeList[2]="moveRetcodeEncoderRightLowLevel";
	moveRetcodeList[3]="moveRetcodeEncoderLeftHighLevel";
	moveRetcodeList[4]="moveRetcodeEncoderRightHighLevel";
	moveRetcodeList[5]="moveUnderLimitation";
	moveRetcodeList[7]="moveKoDueToObstacle";
	moveRetcodeList[10]="moveKoDueToWheelStopped";
	moveRetcodeList[11]="moveKoDueToNotEnoughSpace";
	moveRetcodeList[16]="moveWheelSpeedInconsistancy";
	for (int i=0;i<=256;i++)
	{
		
	}
	retcodeList[moveEnd][0]="ok";
	retcodeList[moveEnd][1]="moveRetcodeEncoderLeftLowLevel";
	retcodeList[moveEnd][2]="moveRetcodeEncoderRightLowLevel";
	retcodeList[moveEnd][3]="moveRetcodeEncoderLeftHighLevel";
	retcodeList[moveEnd][4]="moveRetcodeEncoderRightHighLevel";
	retcodeList[moveEnd][5]="moveUnderLimitation";
	retcodeList[moveEnd][7]="moveKoDueToObstacle";
	retcodeList[moveEnd][10]="moveKoDueToWheelStopped";
	retcodeList[moveEnd][11]="moveKoDueToNotEnoughSpace";
	retcodeList[moveEnd][16]="moveWheelSpeedInconsistancy";
	retcodeList[robotInfoUpdated][0]="ok";
	retcodeList[pingFBEnd][0]="ok";
	retcodeList[robotNOUpdated][0]="ok";
	retcodeList[northAlignEnd][0]="ok";
	retcodeList[northAlignEnd][1]="ko!";
	retcodeList[northAlignEnd][11]="moveKoDueToNotEnoughSpace";
	retcodeList[servoAlignEnd][0]="ok";
	retcodeList[robotUpdatedEnd][0]="ok";
	for (int i=0;i<256;i++)
	{
		retcodeList[i][255]="timeout";
		retcodeList[i][255]="timeout";
	}
	actionList[scanning]="scanning";
	actionList[moving]="moving";
	actionList[moveEnd]="moveEnd";
	actionList[scanEnd]="scanEnd";
	actionList[robotUpdatedEnd]="robotUpdatedEnd";
	actionList[northAlignEnd]="northAlignEnd";
	actionList[scanning]="scanning";
	actionList[servoAlignEnd]="servoAlignEnd";
	actionList[pingFBEnd]="pingFBEnd";
	actionList[gyroRotating]="gyroRotating";
	actionList[gyroRotateEnd]="gyroRotateEnd";	
	actionList[moveAcrossPass]="moveAcrossPass";
	actionList[moveAcrossPassEnded]="moveAcrossPassEnded";
	actionList[requestBNOEnd]="requestBNOEnd";
	actionList[robotNOUpdated]="robotNOUpdated";
	actionList[respIRsensors]="respIRsensors";	

	shiftEchoVsRotationCenter=ParametersSetting.GetParametersNumValue(shiftparameterid)/10;
	ArduinoSimulator.InitSimulatorValues();
	}
public static int byteToUnsignedInt(byte b) {
	int retValue=b & 0x000000ff;
    return retValue ;
  }
}

