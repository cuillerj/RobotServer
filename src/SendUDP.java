import java.io.*;
import java.net.*;
import java.sql.Timestamp;

public class SendUDP extends Thread{
	Thread t;
	String pgmId="SendUDP";

	public static byte countUdp=0x00;
	public static byte [] copySentData;
 

//	public String ipRobot="192.168.1.133";  // 138 ou 133
	public SendUDP() {
		try{
	//	  System.out.println("argument: " + args[0]);
		  CheckTimer();
//		  String mess="sendUDP";
//		  TraceLog Trace = new TraceLog();
//		  Trace.TraceLog(pgmId,mess);
	//      DatagramSocket clientSocket = new DatagramSocket();
	//      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
//	      byte[] sendData = new byte[3];
//	      String startCmde="c4x";
//	      sendData = startCmde.getBytes();
//	      sendData=SecurSendUdp(sendData);
	//      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	//      clientSocket.send(sendPacket);
	//      clientSocket.close();
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPStop() {
		try{
	  String mess="stop";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
		RobotMainServer.runningStatus=-1; // pending stop
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
	      String startCmde="c4s";
	      sendData = startCmde.getBytes();
	      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();
	//   System.exit(0);
	  
	  	 
	   
	   
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPReset() {
		try{
	  String mess="reset";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
		RobotMainServer.runningStatus=2004; // pending reset
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[4];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x72;   // r reset
			 sendData[3]=(byte) 0xff;   // 0xff reset all
		   sendData=SecurSendUdp(sendData);

//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	
	public void SendUDPStart() {
		// TODO Auto-generated method stub
		try{
			  String mess="start";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
				RobotMainServer.runningStatus=2000; // pending start
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[3];
			      String startCmde="c4x";
			      sendData = startCmde.getBytes();
			      sendData=SecurSendUdp(sendData);
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
			//   System.exit(0);
					   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendUDPCalibrate() {
		// TODO Auto-generated method stub
		try{
			  String mess="calibrate";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
				RobotMainServer.runningStatus=2003; // pending calibrate
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[3];
			      String startCmde="c4w";
			      sendData = startCmde.getBytes();
			      sendData=SecurSendUdp(sendData);
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
			//   System.exit(0);
			  
			  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendUDPScan() {
		// TODO Auto-generated method stub
		try{
			  String mess="scan";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
				RobotMainServer.runningStatus=1001; // pending scan
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[3];
			      String startCmde="c4+";
			      sendData = startCmde.getBytes();
			      sendData=SecurSendUdp(sendData);
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
			  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	
	public void SendUDPServoAlign(int value) {
		try{
	  String mess="align servo";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
		RobotMainServer.runningStatus=2005; // pending reset
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[4];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x53;   // S align servomotor
			 sendData[3]=(byte) value;   // value in degre between 0 to 180 
		      sendData=SecurSendUdp(sendData);
	//      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	//      clientSocket.send(sendPacket);
	//      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}

	
	public void SendUDPNorthRotate(int value) {
		try{
	  String mess="rotate VS NO:"+value;
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
		RobotMainServer.runningStatus=2005; // pending reset
///	      DatagramSocket clientSocket = new DatagramSocket();
///	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[5];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x6e;   // e rotation based on north orientation
			 sendData[3]=(byte) (value/256);   // value in degre between 0 to 360
			 sendData[4]=(byte) (value);   //
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPGyroRotate(long ang) {
		try{
	  String mess="rotate using gyro:"+ang;
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
		RobotMainServer.runningStatus=2005; // pending reset
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[5];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x6f;   // rotation based on gyroscope
			 sendData[3]=(byte) (Math.abs(ang)/256);   // value in degre between 0 to 360
			 sendData[4]=(byte) (Math.abs(ang));   //
			 if (ang<0)
			 {
				 sendData[3]=(byte) (sendData[3]|0b10000000);  // upper bit to one means negative rotation
			 }
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	
	public void SendUDPPingEchoFrontBack() {
		try{
	  String mess="echo ping FB";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
		RobotMainServer.runningStatus=2005; // pending reset
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=RobotMainServer.requestPingFrontBack;   // p 
		     sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPRequestNarrowaPathMesurments() {
		try{
	  String mess="RequestNarrowaPathMesurments";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
	  RobotMainServer.runningStatus=2005; // pending reset
      byte[] sendData = new byte[3];
      sendData[0]=0x63;  // c
      sendData[1]=0x34;  // 4
      sendData[2]=RobotMainServer.requestNarrowPathMesurments;   // p 
      sendData=SecurSendUdp(sendData);
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPRequestNarrowPathEchos() {
		try{
	  String mess="requestNarrowPathEchos";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
	  RobotMainServer.runningStatus=2005; // pending reset
      byte[] sendData = new byte[3];
      sendData[0]=0x63;  // c
      sendData[1]=0x34;  // 4
      sendData[2]=RobotMainServer.requestNarrowPathEchos;   // p 
      sendData=SecurSendUdp(sendData);
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}	
	public void SendUDPPowerOnOffEncoder(byte encoderOn) {
		try{
	  String mess="encode OnOff";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[4];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x3a;   // : 
			 sendData[3]=encoderOn;   // 
		     sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPTrace(byte value) {
		try{
			  String mess="trace OnOff";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
			  byte[] sendData = new byte[4];
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=RobotMainServer.requestTrace;   // : 
			 sendData[3]=value;   // 
		     sendData=SecurSendUdp(sendData);
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPTraceNO(byte value) {
		try{
			  String mess="traceNO OnOff";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
			  byte[] sendData = new byte[4];
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=RobotMainServer.requestTraceNO;   // : 
			 sendData[3]=value;   // 
		     sendData=SecurSendUdp(sendData);
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPSleep(boolean OnOff) {
		try{
			  String mess="sleep request ";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
	//	      DatagramSocket clientSocket = new DatagramSocket();
		//      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			  byte[] sendData = new byte[4];
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=RobotMainServer.requestSleep;   // : 
			 if (OnOff)
			 {
				 sendData[3]=0x01;   // 		
			     sendData=SendUdp(sendData);
			 }
			 else
			 {
				 sendData[3]=0x00;   // 	
			     sendData=SecurSendUdp(sendData);
			 }				 
//		      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	//	      clientSocket.send(sendPacket);
		//      clientSocket.close();  

		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SetEncoderThreshold(boolean Left,int lowValue, int highValue) {
		try{
	  String mess="set encoder thresholds";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[10];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x3c;   // : 
			 if (Left)
			 {
				 sendData[3]=0x4c;   // L
			 }
			 else{
				 sendData[3]=0x52;   // R
			 }
			 sendData[4]=0x6c;   // 
			 sendData[5]=(byte) (Math.abs(lowValue/256));   // 
			 sendData[6]=(byte) lowValue;
			 sendData[7]=0x68;   // 
			 sendData[8]=(byte) (Math.abs(highValue/256));   // 
			 sendData[9]=(byte) highValue;
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SetPWMMotor(boolean Left,int value) {
		try{
	  String mess="set PWM motor";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	/      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[7];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x3f;   // : 
			 if (Left)
			 {
				 sendData[3]=0x4c;   // L
			 }
			 else{
				 sendData[3]=0x52;   // R
			 }
			 sendData[4]=0x00;   // 
			 sendData[5]=(byte) (Math.abs(value/256));   // 
			 sendData[6]=(byte) value;
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
///	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SetSlowPWMRatio(int value) {
		try{
	  String mess="set SetSlowPWMRatio";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	/      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[7];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x3f;   // : 
			 sendData[3]=0x72;   //  r
			 sendData[4]=0x00;   // 
			 sendData[5]=(byte) (Math.abs(value/256));   // 
			 sendData[6]=(byte) value;
		     sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
///	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SetGyroSelectedRange(int value) {
		if (value==00||value==1||value==2)
		{
		try{
	  String mess="set gyro selected range";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[4];
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x41;   // : 
			 sendData[3]=(byte) (((byte) value)&0x0f);   // 
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
		}
	public void SetGyroODR(int value) {
		if (value>=0 && value<=15)
		{        // 0 100 mgHz 4 200 8 400 12 800
		try{
	  String mess="set gyro selected range";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[4];
	      byte valueB=(byte) (((byte) value)&0x0f);
	      valueB=(byte) (valueB<<4&0xf0);
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x42;   // : 
			 sendData[3]=valueB;   // 
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	}

	public void SetGyroBiasMicrosec(int value) {
		if (value>=0 && value<=255)
		{        // 0 100 mgHz 4 200 8 400 12 800
		try{
	  String mess="set SetGyroBiasMicrosec";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[6];
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x44;   // : 
			 sendData[3]=0x01;   // one register
			 sendData[4]=0x18;   // register 18
			 sendData[5]=(byte) value;   // 
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	}

	public void GetSubsytemRegisters( int register) {
	try{
	  String mess="get gyro registers";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte registerB=(byte) register;
	      byte[] sendData = new byte[9];
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x43;   // : 
			 sendData[3]=0x05;   //
			 for (int i=0;i<5;i++)
			 {
				 sendData[4+i]=(byte) (registerB+i);
			 }
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
	}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	
	public void QueryEncodersValues() {
		try{
	  String mess="query encoders values";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x3e;   // : 
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendRequestBNOData() {
		try{
	  String mess="SendRequestBNOData";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x7c;   // : 
		     sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}	
	
	
	
	public void GetSubsytemLocation() {
		try{
	  String mess="GetSubsytemLocation";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x00;  // 
			 sendData[2]=0x7a;   // : 
		     sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void QueryMotorsPWM() {
		try{
	  String mess="query motor PWM";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x40;   // : 
		      sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}

		public void SendUDPRequestSensors() {
		try{
	  String mess="query IR Sensors";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=RobotMainServer.requestIRsensors;   // 
		     sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SetMotorsRatio(int value) {
		try{
	  String mess="set motors ratio";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[5];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x3d;   // : 
			 sendData[3]=(byte) (Math.abs(value/256));   // 
			 sendData[4]=(byte) value;
		     sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SetMotorPulseLenght(int value) {
		try{
	  String mess="set motors pulse lenght";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[5];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x50;   // : 
			 sendData[3]=(byte) (Math.abs(value/256));   // 
			 sendData[4]=(byte) value;
		     sendData=SecurSendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}

	public void SendUDPInit(int posX,int posY,int orient,int locProb) {
		// TODO Auto-generated method stub
		try{
//			  System.out.println("init2" );
			String mess="send init";
			TraceLog Trace = new TraceLog();
			Trace.TraceLog(pgmId,mess);
			RobotMainServer.runningStatus=2000; // pending init
			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      
			      byte[] sendData = new byte[25];
//			      String startCmde="c4I";
//			      sendData = startCmde.getBytes();
    			 byte[] DataToSend = new byte[14];
    			 DataToSend[0]=0x63;  // c
    			 DataToSend[1]=0x34;  // 4
    			 DataToSend[2]=0x49;   // I
    			 if (posX<0)
    			 {
        			 DataToSend[3]=0x2d;  // posX signe
    			 }
    			 else
    			 {
        			 DataToSend[3]=0x2b;  // posX signe
    			 }
    			 if (posY<0)
    			 {
        			 DataToSend[6]=0x2d;  // posYsigne
    			 }
    			 else
    			 {
        			 DataToSend[6]=0x2b;  // posY signe
    			 }
    			 if (orient<0)
    			 {
        			 DataToSend[9]=0x2d;  // orient  negative
    			 }
    			 else
    			 {
        			 DataToSend[9]=0x2b;  //orient  positive
    			 }
    			 DataToSend[5]=(byte)Math.abs(posX);  // posX reste /256
    			 posX=Math.abs(posX)/256;
    			 DataToSend[4]=(byte)posX;  // posX/256
    			 DataToSend[8]=(byte)Math.abs(posY);  // posY reste /256
       			 posY=Math.abs(posY)/256;
         		 DataToSend[7]=(byte)posY;  // posY/256
    			 DataToSend[11]=(byte)Math.abs(orient);  // orientation /256
       			 orient=Math.abs(orient)/256;
    			 DataToSend[10]=(byte)orient;  // orientation /256
    			 DataToSend[12]=0x00; 
    			 DataToSend[13]=(byte)Math.abs(locProb); 
 /*   			
    			 for (int i=0;i<13;i++)
    			 {
    				 RobotMainServer.hexaPrint(DataToSend[i]);

    			 }
    			 System.out.println();
 */   			
				  sendData = DataToSend;
			      sendData=SecurSendUdp(sendData);
	//		      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	//		      clientSocket.send(sendPacket);
	//		      clientSocket.close();
			  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendEcho() {
		try{
			String mess="send echo";
			TraceLog Trace = new TraceLog();
			Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
	      String startCmde="c4e";
	      sendData = startCmde.getBytes();
	//      sendData=SecurSendUdp(sendData);
	      sendData=SendUdp(sendData);
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();
  	 	   
	   
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void RefreshNorthOrientation() {
		try{
			String mess="get NO";
			TraceLog Trace = new TraceLog();
			Trace.TraceLog(pgmId,mess);
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
		     byte[] sendData = new byte[25];
			 byte[] DataToSend = new byte[3];
			 DataToSend[0]=0x63;  // c
			 DataToSend[1]=0x34;  // 4
			 DataToSend[2]=0x7b;   // I
			 sendData = DataToSend;
	      sendData=SecurSendUdp(sendData);
	   
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPMove(long angle,long move) {
		// TODO Auto-generated method stub
		try{
			RobotMainServer.runningStatus=1003; // pending move
			  String mess="move angle:"+angle+ " move:"+move;
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
			      DatagramSocket clientSocket = new DatagramSocket();
	//		      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] cmde = new byte[15];
			      byte[] sendData = new byte[15];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=0x6d;     //m
			      if (angle>=0){
				      cmde[3]=0x2b;
			      }
			      else {
				      cmde[3]=0x2d;
				      angle=-angle;
			      }
			      cmde[4]=(byte)(angle/256);
			      cmde[5]=(byte)(angle);
			      if (move>=0){
				      cmde[6]=0x2b;
			      }
			      else {
				      cmde[6]=0x2d;
				      move=-move;
			      }
			      cmde[7]=(byte)(move/256);
			      cmde[8]=(byte)(move);
	//		      int i;
//			      for (i=9;i<20;i++)
//			      	{
//			    	  cmde[i]=0x00;
//			      	}
//			      String startCmde="c4m";
		      sendData = cmde;
		      sendData=SecurSendUdp(sendData);
//		      System.out.println(byteArrayToHex(sendData));
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
			//   System.exit(0);
			  		  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendUDPGoto(long posX,long posY) {
		// TODO Auto-generated method stub
		try{
			RobotMainServer.runningStatus=1003; // pending move
			  String mess="posX:"+posX+ " posY:"+posY;
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] cmde = new byte[15];
			      byte[] sendData = new byte[15];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=0x67;  // "g"
			      if (posX>=0){
				      cmde[3]=0x2b;
			      }
			      else {
				      cmde[3]=0x2d;
				      posX=-posX;
			      }
			      cmde[4]=(byte)(posX/256);
			      cmde[5]=(byte)(posX);
			      if (posY>=0){
				      cmde[6]=0x2b;
			      }
			      else {
				      cmde[6]=0x2d;
				      posY=-posY;
			      }
			      cmde[7]=(byte)(posY/256);
			      cmde[8]=(byte)(posY);
	//		      int i;
//			      for (i=9;i<20;i++)
//			      	{
//			    	  cmde[i]=0x00;
//			      	}
//			      String startCmde="c4m";
		      sendData = cmde;
		      sendData=SecurSendUdp(sendData);
		      System.out.println(byteArrayToHex(sendData));
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
			//   System.exit(0);
			  		  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public static String byteArrayToHex(byte[] a) {
		   StringBuilder sb = new StringBuilder(a.length * 2);
		   for(byte b: a)
		      sb.append(String.format("%02x", b & 0xff));
		   return sb.toString();
		}
	public void NorthAlignRobot(int angle) {
		// TODO Auto-generated method stub
		try{
			  String mess="align to:"+angle;
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] cmde = new byte[15];
			      byte[] sendData = new byte[15];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.northAlignRequest;  // N command
			      cmde[3]=(byte)(angle/256);
			      cmde[4]=(byte)(angle);

		      sendData = cmde;
		      sendData=SecurSendUdp(sendData);
//		      System.out.println(byteArrayToHex(sendData));
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
			//   System.exit(0);
			  		  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendUDPHorn(int duration) {
		// TODO Auto-generated method stub
		try{
			  String mess="horn";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[4];
			      byte[] cmde = new byte[4];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=0x68;  // h command
			      cmde[3]=(byte)(duration);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendRequestVersion() {
		// TODO Auto-generated method stub
		try{
			  String mess="request for arduino version";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[3];
			      byte[] cmde = new byte[3];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.requestVersion;  // 
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendRequestPID() {
		// TODO Auto-generated method stub
		try{
			  String mess="request PID parameters";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[3];
			      byte[] cmde = new byte[3];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.requestPID;  // 
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendPIDKp(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set PID Kp";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[7];
			      byte[] cmde = new byte[7];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x00;  // Kx
			      cmde[4]=0x00;  // Kp
			      cmde[5]=(byte)(value/256);
			      cmde[6]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendPIDKi(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set PID Ki";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[7];
			      byte[] cmde = new byte[7];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x00;  // Kx
			      cmde[4]=0x01;  // Ki
			      cmde[5]=(byte)(value/256);
			      cmde[6]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendPIDKd(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set PID Kd";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[7];
			      byte[] cmde = new byte[7];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x00;  // Kx
			      cmde[4]=0x02;  // Kd
			      cmde[5]=(byte)(value/256);
			      cmde[6]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	
	public void SendLeftSetpoint(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set LeftSetpoint";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[7];
			      byte[] cmde = new byte[7];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x02;  // Setpoint
			      cmde[4]=0x00;  // left
			      cmde[5]=(byte)(value/256);
			      cmde[6]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendRightSetpoint(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set RightSetpoint";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[7];
			      byte[] cmde = new byte[7];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x02;  // Setpoint
			      cmde[4]=0x01;  // left
			      cmde[5]=(byte)(value/256);
			      cmde[6]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	//  {leftMinOut,rightMinOut,leftMaxOut,rightMaxOut, leftStartOut, rightStartOut}
	public void SendLeftMinLimit(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set leftMinLimit";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[6];
			      byte[] cmde = new byte[6];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x01;  // limit
			      cmde[4]=0x00;  // leftMinLimit
			      cmde[5]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}			   
			   finally{}
				}
	public void SendRightMinLimit(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set RighttMinLimit";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[6];
			      byte[] cmde = new byte[6];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x01;  // limit
			      cmde[4]=0x01;  // RighttMinLimit
			      cmde[5]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}			   
			   finally{}
				}
	public void SendLeftMaxLimit(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set leftMaxLimit";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[6];
			      byte[] cmde = new byte[6];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x01;  // limit
			      cmde[4]=0x02;  // leftMaxLimit
			      cmde[5]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}			   
			   finally{}
				}
	public void SendRightMaxLimit(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set RighttMaxLimit";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[6];
			      byte[] cmde = new byte[6];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x01;  // limit
			      cmde[4]=0x03;  // RighttMaxLimit
			      cmde[5]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}			   
			   finally{}
				}

	public void SendLeftStartLimit(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set LeftStartLimit";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[6];
			      byte[] cmde = new byte[6];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x01;  // limit
			      cmde[4]=0x04;  // LeftStartMinLimit
			      cmde[5]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}			   
			   finally{}
				}
	public void SendRightStartLimit(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set RightStartLimit";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[6];
			      byte[] cmde = new byte[6];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=RobotMainServer.setPID;  // 
			      cmde[3]=0x01;  // limit
			      cmde[4]=0x05;  // RightStartMinLimit
			      cmde[5]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}			   
			   finally{}
				}
	public void SendUDPShiftPulse(int value) {
		// TODO Auto-generated method stub
		try{
			  String mess="shift pulse";
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[4];
			      byte[] cmde = new byte[4];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=0x69;  // i command
			      cmde[3]=(byte)(value);
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendUDPObstacleDetection(boolean value) {
		// TODO Auto-generated method stub
		try{
			  String mess="detection obstacle:"+value;
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
//			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[4];
			      byte[] cmde = new byte[4];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=0x4f;  // i command
			      if (value)
			    	  cmde[3]=0x01;
			      else
			    	  cmde[3]=0x00;
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SetBNOMode(byte value) {
		// TODO Auto-generated method stub
		try{
			  String mess="set BNOMode:"+value;
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
			      byte[] sendData = new byte[4];
			      byte[] cmde = new byte[4];
			      cmde[0]=0x63;
			      cmde[1]=0x00;
			      cmde[2]=0x79;
			      cmde[3]=value;  // 
			      sendData = cmde;
			      sendData=SecurSendUdp(sendData);
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}

	public void CheckTimer() {
		try{
	//	  System.out.println("argument: " + args[0]);
		 	java.util.Date date= new java.util.Date();
		 	date =new Timestamp(date.getTime());		 	
		 	long millis=date.getTime();
			String mess="wait for:"+ millis+ "ms to send";
			TraceLog Trace = new TraceLog();
			millis=millis - RobotMainServer.lastSentTime;  
		 	if (millis<500 && RobotMainServer.lastSentTime!=0)
		 	{
			   Thread.sleep(500-millis);         // to let arduino time for treatment
			   Trace.TraceLog(pgmId,mess);
		 	}
		 	RobotMainServer.lastSentTime=millis;

		}
	   catch(Exception e)
	   {}
	   
	   finally{}

		}
	byte[] SecurSendUdp(byte[] sendData)
	{
		while (CheckLastFrame());				;
//		System.out.println(" in: 0x"+byteToHex(sendData[0])+" - "+byteToHex(sendData[1])+" - "+byteToHex(sendData[2]));
	      DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
	      InetAddress IPAddress = null;
		try {
			IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
//		System.out.println(" count:"+countUdp);
		countUdp++;
//		System.out.println(" count:"+countUdp);
		sendData[1]=countUdp;
//		System.out.println(" out: 0x"+byteToHex(sendData[0])+" - "+byteToHex(sendData[1])+" - "+byteToHex(sendData[2]));
		copySentData=sendData;
		RobotMainServer.pendingAcqUdp=true;
		RobotBatchServer.statusFrameCount=0;
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
		 DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, RobotMainServer.ipRobot, 8888);
	      try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      clientSocket.close();
		return sendData;
	}
	byte[] SendUdp(byte[] sendData)
	{
		while (CheckLastFrame());				;
//		System.out.println(" in: 0x"+byteToHex(sendData[0])+" - "+byteToHex(sendData[1])+" - "+byteToHex(sendData[2]));
	      DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
	      InetAddress IPAddress = null;
		try {
			IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
//		System.out.println(" count:"+countUdp);
		countUdp++;
//		System.out.println(" count:"+countUdp);
		sendData[1]=countUdp;
//		System.out.println(" out: 0x"+byteToHex(sendData[0])+" - "+byteToHex(sendData[1])+" - "+byteToHex(sendData[2]));
		copySentData=sendData;
		RobotMainServer.pendingAcqUdp=false;
		RobotBatchServer.statusFrameCount=0;
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, RobotMainServer.ipRobot, 8888);
	      try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      clientSocket.close();
		return sendData;
	}
	static void ResendLastFrame()
	{
		String pgmId="SendUDP";
	     DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
	      InetAddress IPAddress = null;
		try {
			IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		  String mess=" resend: 0x"+byteToHex(copySentData[0])+" - "+byteToHex(copySentData[1])+" command:"+byteToHex(copySentData[2]);
		  TraceLog Trace = new TraceLog();
		  Trace.TraceLog(pgmId,mess);
//		System.out.println(" resend: 0x"+byteToHex(copySentData[0])+" - "+byteToHex(copySentData[1])+" - "+byteToHex(copySentData[2]));
//		RobotMainServer.pendingAcqUdp=false;
		RobotBatchServer.statusFrameCount=0;
	  //    DatagramPacket sendPacket = new DatagramPacket(copySentData, copySentData.length, IPAddress, 8888);
	      DatagramPacket sendPacket = new DatagramPacket(copySentData, copySentData.length, RobotMainServer.ipRobot, 8888);
	      try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      clientSocket.close();

	}
	static boolean CheckLastFrame()
	{
		if(RobotMainServer.simulation==1)
		{
			RobotMainServer.pendingAcqUdp=false;
			return false;
		}
			
		if (RobotMainServer.pendingAcqUdp==true)
		{
			try {
				Thread.sleep(2000);
				  String mess="sleep";
				  TraceLog Trace = new TraceLog();
				  Trace.TraceLog("SendUDP/CheckLastFrames",mess);
				if (RobotMainServer.pendingAcqUdp==true)  
				{
					ResendLastFrame();	
					Thread.sleep(1000);
					RobotMainServer.pendingAcqUdp=false; // just one retry
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
			
		}
		else{
			return false;
		}
	}
	  public static String byteToHex(byte b) {
		    int i = b & 0xFF;
		    return Integer.toHexString(i);
		  }
	public void SendMoveAcrossNarrowPass(int distance, int witdh, int length,int startToEntryDistance,int northOrientation,
			int lenToDo ,int echoToGet) {
		try{
			RobotMainServer.runningStatus=1003; // pending move
			  String mess="passDistance:"+distance+" passWitdh:"+witdh+" passLength:"+length+ " startToEntryDistance:"+startToEntryDistance+" northOrientation:"+northOrientation+" move:"+lenToDo+" echoToGet:"+echoToGet;
			  TraceLog Trace = new TraceLog();
			  Trace.TraceLog(pgmId,mess);
			      DatagramSocket clientSocket = new DatagramSocket();
//			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] cmde = new byte[20];
			      byte[] sendData = new byte[20];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=(byte)RobotMainServer.moveAcrossPass;     
			      if (distance>=0){
				      cmde[3]=0x2b;
			      }
			      else {
				      cmde[3]=0x2d;
				      distance=-distance;
			      }
			      cmde[4]=(byte)(distance);
			      cmde[6]=(byte)(witdh);
			      cmde[8]=(byte)(length);
			      if (lenToDo>=0){
				      cmde[9]=0x2b;
			      }
			      else {
				      cmde[9]=0x2d;
				      lenToDo=-lenToDo;
			      }
			      cmde[10]=(byte)(lenToDo/256);
			      cmde[11]=(byte)(lenToDo);
			      cmde[13]=(byte)(echoToGet/256);
			      cmde[14]=(byte)(echoToGet);
			      cmde[16]=(byte)(northOrientation/256);
			      cmde[17]=(byte)(northOrientation);	
			      cmde[19]=(byte)(startToEntryDistance);	
	//		      int i;
//			      for (i=9;i<20;i++)
//			      	{
//			    	  cmde[i]=0x00;
//			      	}
//			      String startCmde="c4m";
		      sendData = cmde;
		      sendData=SecurSendUdp(sendData);
//		      System.out.println(byteArrayToHex(sendData));
//			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//			      clientSocket.send(sendPacket);
//			      clientSocket.close();
			//   System.exit(0);
			  		  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
		// TODO Auto-generated method stub
		
	}
	
	


