import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class RobotBatchServer implements Runnable {
	public static int statusFrameCount=0;
	public void run()
	{
		String pgmId="RobotBatchServer";
		RobotMainServer.GetLastScanID();
		String mess="lastid:"+RobotMainServer.idscanG+" "+RobotMainServer.posXG+ " "+RobotMainServer.posYG;
		TraceLog Trace = new TraceLog();
		Trace.TraceLog(pgmId,mess);
//		System.out.println(pgmId+": "+"lastid:"+RobotMainServer.idscanG+" "+RobotMainServer.posXG+ " "+RobotMainServer.posYG);

		Fenetre ihm = new Fenetre();
		Fenetre2 ihm2 = new Fenetre2();
		FenetreGraphiqueSonar ihm3 = new FenetreGraphiqueSonar();
		ihm2.SetInitialPosition();
		ihm3.SetInitialPosition();
//		FenetreGraphique graph = new FenetreGraphique();
//		PanneauGraphique.point(posXG,posYG);
//		graph.repaint();
//		graph.SetInitialPosition();
			DatagramSocket serverSocket = null;
			try {
				serverSocket = new DatagramSocket(1830);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			//byte[] receiveData = new byte[1024];
			//byte[] sendData = new byte[1024];
			int ID=0; // station ID
			byte Type=0; // station type
			int InpLen=0; // input datalength - lue dans la trame UDP
			int trameNumber=0;
			byte lastTrameNumber=0;
			EchoRobot echo=new EchoRobot();
		//	echo.EchoRobot();
			echo.start();
			//	EchoRobot echo=new EchoRobot();
//			echo.EchoRobot();
				//echo.start();
//		      SendUDP snd = new SendUDP();
//		      snd.SendEcho();
			TraceEvents trace=new TraceEvents();
//			trace.TraceEvents();
			trace.start();
			while(true)
			{
			    ihm.RefreshStat();
				Connection conn = null;
				Statement stmtR = null;
				Statement stmtI = null;

//				ResultSet rs = null;
				byte[] receiveData = new byte[1024];
				byte[] sentence2=new byte[1024];
		//		byte[] sendData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(sentence2, sentence2.length);
				try {
					serverSocket.receive(receivePacket);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				String sentence = new String( receivePacket.getData());

				 sentence2 =  receivePacket.getData();
				 InpLen=(byte)sentence2[4];
				

				ID=sentence2[0];
//				Type=sentence2[2];

			 trameNumber=sentence2[7];
			 if (RobotMainServer.debugCnx==true)	
			 	{
				 	for (int i=0;i<50;i++)
				 		{
				 			RobotMainServer.hexaPrint(sentence2[i]);
				 		}
				 	System.out.println();
		 			RobotMainServer.hexaPrint(sentence2[8]);
				 	System.out.println();
			 	}
				int idx=Type;
				if(RobotMainServer.runningStatus<=0)
				{
					RobotMainServer.runningStatus=1;
				}
			if (sentence2[6] == 0x01) { // means need a ack
					InetAddress IPAddress = receivePacket.getAddress();
				    DatagramSocket clientSocket = null;
					try {
						clientSocket = new DatagramSocket();				
						} 
					catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						}
				    byte[] sendData = new byte[4];
				    sendData[0]=0x63;
				    sendData[1]=SendUDP.countUdp;
				    sendData[2]=0x61;
				    sendData[3]=sentence2[7];
				    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
				    try {
						clientSocket.send(sendPacket);
				    	} 
				    catch (IOException e1) {
						e1.printStackTrace();
						}
					EchoRobot.pendingEcho=0;

					try {
						Thread.sleep(300);
				 		} 
					catch (InterruptedException e1) {
						e1.printStackTrace();
				 		}
					clientSocket.close();
					if (sentence2[8]==0x46){       // scan data
				    	 int i2=9;
				    	 int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consider� sign�
				    	 int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
				    	 int distFront=256*oct0+oct1;
				    	 i2=12;
				    	 oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consider� sign�
				    	 oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
				    	 int distBack=256*oct0+oct1;
				    	 i2=15;
				    	 oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consider� sign�
				    	 oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
				    	 int angle=256*oct0+oct1;
				    	 i2=19;
				    	 oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consider� sign�
				    	 oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
				    	 RobotMainServer.northOrientation=256*oct0+oct1;
				    	 RobotMainServer.RefreshHardPositionOnScreen();
				    	 if (RobotMainServer.scanStepCount!=0)
				    	 	{
				    		 	RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][0]=angle;
				    		 	RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][1]=distFront;
				    		 	RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][2]=distBack;
				    		 	RobotMainServer.scanStepCount++;
				    	 	}
				    	 if (lastTrameNumber!=trameNumber)
				    	 	{
				    		 try {
				    			 Class.forName("com.mysql.jdbc.Driver").newInstance();
				    			 String connectionUrl = "jdbc:mysql://jserver:3306/robot";
				    			 String connectionUser = "jean";
				    			 String connectionPassword = "manu7890";
				    			 conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
				    			 stmtR = conn.createStatement();
				    			 stmtI = conn.createStatement();
				    			 int idscan=Fenetre.ids();
				    			 RobotMainServer.idscanG=Fenetre.idsString();
				    			 String idsG=RobotMainServer.idscanG;
				    			 RobotMainServer.GetCurrentPosition(idsG);
			//	    			 int angl=RobotMainServer.orientG;
				    			 String sql="INSERT INTO scanResult VALUES ("+idscan+",now(),"+RobotMainServer.posX+","+RobotMainServer.posY+","+angle+","+distFront+","+distBack+","+RobotMainServer.northOrientation+","+RobotMainServer.idCarto+")";
				    			 //System.out.println("ind id "+IndIdS+", pos " + IndPos + ", len: " + IndLen+" value"+IndValue);
				    			Trace.TraceLog(pgmId,sql);
				    //			 System.out.println(sql);
				    			 stmtI.executeUpdate(sql);
				    		 	}
				    		 	catch (Exception e) {
				    		 		e.printStackTrace();
				    		 	} finally {
				    		 		try { if (stmtR != null) stmtR.close(); } catch (SQLException e) { e.printStackTrace(); }
				    		 		try { if (stmtI != null) stmtI.close(); } catch (SQLException e) { e.printStackTrace(); }
				    		 		try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				    		 	}
				    		 	lastTrameNumber=sentence2[15];
				    	 	}
				    	 PanneaugraphiqueSonar.point(RobotMainServer.posXG,RobotMainServer.posYG,RobotMainServer.northOrientation,distFront,distBack,angle);
				    	 ihm3.repaint();
					}
					
					
					if (sentence2[8]==0x01){       // end of action need ack
				    	int sbnb=(byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80);
						StringBuffer sbb4 = new StringBuffer(2);
						sbb4.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
						sbb4.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
						ihm.MajActionRetcode(" 0x"+sbb4);
						int retCode=sentence2[10];
				    	if (sentence2[9]==0x66){                    // scan en cours
							EchoRobot.pendingEcho=0;
			//				System.out.println("scan running");
							ihm.MajRobotStat("scan running");
							RobotMainServer.runningStatus=1;
							}
						if (sentence2[9]==0x67){                    // 
							EchoRobot.pendingEcho=0;
//							System.out.println("scan ended");
							int i2=10;
							int eventType=(byte)(sentence2[9]&0x7F)-(byte)(sentence2[9]&0x80);
//							System.out.println("event type:"+eventType);
							mess="event type:"+eventType;
							Trace.TraceLog(pgmId,mess);
							int actionRetcode=(byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80);
							int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consider� sign�
							int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
							RobotMainServer.northOrientation=256*oct0+oct1;
							UpdateScanRefOrientation(RobotMainServer.northOrientation);
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
//							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,RobotMainServer.eventArduino);  // reqCode,retCode,source, dest
							ihm.MajRobotStat("scan ended");
							mess="scan ended:"+retCode;
							Trace.TraceLog(pgmId,mess);
						    RobotMainServer.scanStepCount=0;
							RobotMainServer.runningStatus=2;
							}
						if (sentence2[9]==0x6b){                    // 
							EchoRobot.pendingEcho=0;
//							System.out.println("align ended");
							int eventType=(byte)(sentence2[9]&0x7F)-(byte)(sentence2[9]&0x80);
							mess="event type:"+eventType;
							Trace.TraceLog(pgmId,mess);
							int actionRetcode=(byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80);
							int i2=10;
							int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consider� sign�
							int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
							RobotMainServer.northOrientation=256*oct0+oct1;
							oct0=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80); // manip car byte consider� sign�
							oct1=(byte)(sentence2[25]&0x7F)-(byte)(sentence2[25]&0x80);
							RobotMainServer.deltaNORotation=256*oct0+oct1;
							if (sentence2[23]==0x2d)
							{
								RobotMainServer.deltaNORotation=-RobotMainServer.deltaNORotation;
							}
//							EventManagement.UpdateEvent(eventType,actionRetcode,1,2);  // reqCode,retCode,source, dest
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
							ihm.MajRobotStat("align ended:"+retCode);
							mess="robot align ended"+" deltaNORot:"+RobotMainServer.deltaNORotation;
							Trace.TraceLog(pgmId,mess);
							RobotMainServer.runningStatus=2;
							}
						
						if (sentence2[9]==0x6c){                    // 
							EchoRobot.pendingEcho=0;
//							System.out.println("servo align ended");
							int eventType=(byte)(sentence2[9]&0x7F)-(byte)(sentence2[9]&0x80);
							mess="event type:"+eventType;
							Trace.TraceLog(pgmId,mess);
							int actionRetcode=(byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80);
							int i2=10;
							int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consider� sign�
							int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
							RobotMainServer.northOrientation=256*oct0+oct1;
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
							ihm.MajRobotStat("servo align ended");
							mess="servo align ended:"+retCode;
							Trace.TraceLog(pgmId,mess);
							RobotMainServer.runningStatus=2;
							}
						if (sentence2[9]==0x6d){                    // 
							EchoRobot.pendingEcho=0;
//							System.out.println("servo align ended");
							int eventType=(byte)(sentence2[9]&0x7F)-(byte)(sentence2[9]&0x80);
							mess="event type:"+eventType;
							Trace.TraceLog(pgmId,mess);
							int actionRetcode=(byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80);
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
//							EventManagement.UpdateEvent(eventType,actionRetcode,1,2);  // reqCode,retCode,source, dest
							ihm.MajRobotStat("pingFB ended");
							mess="pingFG ended:"+retCode;
							Trace.TraceLog(pgmId,mess);
							RobotMainServer.runningStatus=2;
							}
						if (sentence2[9]==0x68){                    //
							EchoRobot.pendingEcho=0;
//							System.out.println("moving");
							ihm.MajRobotStat("moving");
							int ang=ihm.ang();
							int mov=ihm.mov();
							Fenetre2.PosActualise(ang,mov);
							RobotMainServer.runningStatus=3;
							RobotMainServer.actStat=0x02;   // move en cours
							}
						if (sentence2[9]==0x69){                    // 
							EchoRobot.pendingEcho=0;
	//						int ang=ihm.ang();
	//						int mov=ihm.mov();
	//						Fenetre2.PosActualise(ang,mov);
							RobotMainServer.actStat=0x02; 
							int eventType=(byte)(sentence2[9]&0x7F)-(byte)(sentence2[9]&0x80);
							mess="event type:"+eventType;
							Trace.TraceLog(pgmId,mess);
							int actionRetcode=(byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80);
							ihm.MajRobotStat("move ended");
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
//							EventManagement.UpdateEvent(eventType,actionRetcode,1,2);  // reqCode,retCode,source, dest
							RobotMainServer.runningStatus=4;
							int oct0=(byte)(sentence2[15]&0x7F)-(byte)(sentence2[15]&0x80); // manip car byte consider� sign�
							int oct1=(byte)(sentence2[16]&0x7F)-(byte)(sentence2[16]&0x80);
							RobotMainServer.hardPosX=256*oct0+oct1;
							if (sentence2[14]==0x2d)
							{
								RobotMainServer.hardPosX=-RobotMainServer.hardPosX;
							}
							oct0=(byte)(sentence2[18]&0x7F)-(byte)(sentence2[18]&0x80); // manip car byte consider� sign�
							oct1=(byte)(sentence2[19]&0x7F)-(byte)(sentence2[19]&0x80);
							RobotMainServer.hardPosY=256*oct0+oct1;
							if (sentence2[17]==0x2d)
							{
								RobotMainServer.hardPosY=-RobotMainServer.hardPosY;
							}
							oct0=(byte)(sentence2[21]&0x7F)-(byte)(sentence2[21]&0x80); // manip car byte consider� sign�
							oct1=(byte)(sentence2[22]&0x7F)-(byte)(sentence2[22]&0x80);
							RobotMainServer.hardAlpha=256*oct0+oct1;
							if (sentence2[20]==0x2d)
							{
								RobotMainServer.hardAlpha=-RobotMainServer.hardAlpha;
							}
							oct0=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80); // manip car byte consider� sign�
							oct1=(byte)(sentence2[25]&0x7F)-(byte)(sentence2[25]&0x80);
							RobotMainServer.deltaNORotation=256*oct0+oct1;
							if (sentence2[23]==0x2d)
							{
								RobotMainServer.deltaNORotation=-RobotMainServer.deltaNORotation;
							}
							oct0=(byte)(sentence2[27]&0x7F)-(byte)(sentence2[27]&0x80); // manip car byte consider� sign�
							oct1=(byte)(sentence2[28]&0x7F)-(byte)(sentence2[28]&0x80);
							RobotMainServer.deltaNOMoving=256*oct0+oct1;
							if (sentence2[26]==0x2d)
							{
								RobotMainServer.deltaNOMoving=-RobotMainServer.deltaNOMoving;
							}
							oct0=(byte)(sentence2[12]&0x7F)-(byte)(sentence2[12]&0x80); // manip car byte consider� sign�
							oct1=(byte)(sentence2[13]&0x7F)-(byte)(sentence2[13]&0x80);
							RobotMainServer.northOrientation=256*oct0+oct1;
							oct0=(byte)(sentence2[30]&0x7F)-(byte)(sentence2[30]&0x80); // manip car byte consider� sign�
							oct1=(byte)(sentence2[31]&0x7F)-(byte)(sentence2[31]&0x80);
							RobotMainServer.gyroHeading=256*oct0+oct1;
							if (sentence2[29]==0x2d)
							{
								RobotMainServer.gyroHeading=-RobotMainServer.gyroHeading;
							}
							/*
							oct0=(byte)(sentence2[33]&0x7F)-(byte)(sentence2[33]&0x80); // manip car byte consider� sign�
							oct1=(byte)(sentence2[34]&0x7F)-(byte)(sentence2[34]&0x80);			
							RobotMainServer.cumulativeRightHoles=256*oct0+oct1;
							*/
							String XString=Integer.toString(RobotMainServer.hardPosX);
							String YString=Integer.toString(RobotMainServer.hardPosY);
							String HString=Integer.toString(RobotMainServer.hardAlpha);
							mess="move ended X:"+XString+" Y:"+YString+" Heading:"+HString+ " NO:"+RobotMainServer.northOrientation+" deltaNORot:"+RobotMainServer.deltaNORotation+" deltaNOMov:"+RobotMainServer.deltaNOMoving+" GyroHeading:"+RobotMainServer.gyroHeading+" retCode:"+retCode;
							Trace.TraceLog(pgmId,mess);
	//						System.out.println("refresh hard on screen");
							
							RobotMainServer.RefreshHardPositionOnScreen();
							if (RobotMainServer.actStat==0x02){ 
								Fenetre2.ValidePosition(RobotMainServer.posX,RobotMainServer.posY,RobotMainServer.alpha);
								
				//				PanneauGraphique.point(200+posXG/10,200+posYG/10);
				//				graph.repaint();
								}
						RobotMainServer.actStat=0x03;
							}	
						if (sentence2[9]==0x7b){                    //
							EchoRobot.pendingEcho=0;
							int eventType=(byte)(sentence2[9]&0x7F)-(byte)(sentence2[9]&0x80);
							mess="event type:"+eventType;
							Trace.TraceLog(pgmId,mess);
							int actionRetcode=(byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80);
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest							
							if (actionRetcode==0)
							{
								int i2=10;
								int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consider� sign�
								int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
								RobotMainServer.northOrientation=256*oct0+oct1;
								RobotMainServer.RefreshHardPositionOnScreen();
							}

							}
			     	}
					if(sentence2[8]==0x76)
					{
						int i=0;
//						int idx=8;
		//				(byte)(sentence2[7]&0x7F)-(byte)(sentence2[7]&0x80)
						RobotMainServer.BNOLocFlag=(byte) ((byte)(sentence2[9]&0x7F)-(byte)(sentence2[9]&0x80));
						int oct0=(byte)(sentence2[11]&0x7F)-(byte)(sentence2[11]&0x80); // manip car byte consider� sign�
						int oct1=(byte)(sentence2[12]&0x7F)-(byte)(sentence2[12]&0x80);
						int pos=256*oct0+oct1;
						if (sentence2[10]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNOLeftPosX=pos;
						 oct0=(byte)(sentence2[14]&0x7F)-(byte)(sentence2[14]&0x80); // manip car byte consider� sign�
						 oct1=(byte)(sentence2[15]&0x7F)-(byte)(sentence2[15]&0x80);
						 pos=256*oct0+oct1;
						if (sentence2[13]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNOLeftPosY=pos;
						 oct0=(byte)(sentence2[17]&0x7F)-(byte)(sentence2[17]&0x80); // manip car byte consider� sign�
						 oct1=(byte)(sentence2[18]&0x7F)-(byte)(sentence2[18]&0x80);
						 pos=256*oct0+oct1;
						if (sentence2[16]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNORightPosX=pos;
						 oct0=(byte)(sentence2[20]&0x7F)-(byte)(sentence2[20]&0x80); // manip car byte consider� sign�
						 oct1=(byte)(sentence2[21]&0x7F)-(byte)(sentence2[21]&0x80);
						 pos=256*oct0+oct1;
						if (sentence2[19]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNORightPosY=pos;
						 oct0=(byte)(sentence2[23]&0x7F)-(byte)(sentence2[23]&0x80); // manip car byte consider� sign�
						 oct1=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80);
						 pos=256*oct0+oct1;
						if (sentence2[22]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNOLocHeading=pos;
					mess=" BNO loc flag:"+RobotMainServer.BNOLocFlag+" leftX:"+RobotMainServer.BNOLeftPosX+" leftY:"+RobotMainServer.BNOLeftPosY+" rightX:"+RobotMainServer.BNORightPosX+" rightY:"+RobotMainServer.BNORightPosY+ " Heading:"+RobotMainServer.BNOLocHeading;
					Trace.TraceLog(pgmId,mess);
//					Fenetre2.RefreshBNO();
					}
				}
				if (sentence2[6]==0x65){                    // e echo status info
					EchoRobot.pendingEcho=0;
					if (RobotMainServer.javaRequestStatusPending==true)
					{
 // reqCode,retCode,source, dest
						EventManagement.UpdateEvent(RobotMainServer.robotInfoUpdated,0,RobotMainServer.eventOctave,RobotMainServer.eventArduino);  // reqCode,retCode,source, dest
						EventManagement.UpdateEvent(RobotMainServer.robotUpdatedEnd,0,RobotMainServer.eventOctave,RobotMainServer.eventArduino);  // reqCode,retCode,source, dest
					}
					if (RobotMainServer.pendingAcqUdp=false)
					{
						if (sentence2[28]==SendUDP.countUdp)
						{
							RobotMainServer.pendingAcqUdp=false;
							statusFrameCount=0;
						}
						else
						{
							statusFrameCount++;
							if (sentence2[28]>SendUDP.countUdp || statusFrameCount >3)
							{
								SendUDP.ResendLastFrame();
							}
						}
					}
	//			    RobotMainServer.javaRequestStatusPending=false;
	//				System.out.print("echo response: ");
					if (RobotMainServer.runningStatus==0)
					{
						RobotMainServer.runningStatus=1;
					}
					ihm.MajRobotStat("connected");
					if (sentence2[8]==0x66){
	//					System.out.print("scan running ");
						if (RobotMainServer.runningStatus==2)  // scan required
						{
							RobotMainServer.runningStatus=102;
						}

						ihm.MajRobotStat("echo:scan running");
						mess="echo:scan running";
						Trace.TraceLog(pgmId,mess);
					}
					if (sentence2[8]==0x67){
	//					System.out.print("scan ended ");

						if (RobotMainServer.runningStatus==2 || RobotMainServer.runningStatus==102)
						{
							RobotMainServer.runningStatus=103;
						}
						ihm.MajRobotStat("scan ended");
						mess="echo:scan ended";
						Trace.TraceLog(pgmId,mess);
						/*
						if (Integer.parseInt(Fenetre.idscan.getText())==0){  // scan id 0 means for localization
							String    CShell="localization.bat "+Fenetre.idscan.getText();
							try {
								Process   p = Runtime.getRuntime().exec(CShell);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			
						}
*/
						if (RobotMainServer.countScan>0)                                     // scan for learning is running
						{
							RobotMainServer.countScan=RobotMainServer.countScan-1;
							    int newIdScan=Integer.parseInt(Fenetre.idscan.getText())+1;
							      RobotMainServer.idscanG= Integer.toString(newIdScan);
							      Fenetre.idscan.setText(RobotMainServer.idscanG);
							      Fenetre.label.setText("Demarrage du scan");   
	//						      System.out.println(RobotMainServer.idscanG);
							      SendUDP snd = new SendUDP();
							      snd.SendUDPScan();
							      Fenetre.go();

						}

					}
					if (sentence2[8]==0x68){
//						System.out.print("moving");
						if (RobotMainServer.runningStatus==4)  // scan required
						{
							RobotMainServer.runningStatus=104;
						}
						ihm.MajRobotStat("moving");
						mess="echo:moving";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
			//			PanneauGraphique.point(150+posXG,150+posYG);
			//			graph.repaint();
					}
					if (sentence2[8]==0x69){
						if (RobotMainServer.actStat==0x01){   // on a rate le statut moving
							long ang=ihm.ang();
							long mov=ihm.mov();
							ihm2.PosActualise(ang,mov);
							RobotMainServer.actStat=0x02; 
						}
						if (RobotMainServer.actStat==0x02){ 
//							ihm2.ValidePosition();
		//					PanneauGraphique.point(200+posXG/10,200+posYG/10);
		//					graph.repaint();
						}
						if (RobotMainServer.runningStatus==4 || RobotMainServer.runningStatus==104)
						{
							RobotMainServer.runningStatus=105;
						}

						ihm.MajRobotStat("move ended");

						EventManagement.UpdateEvent(4,0,1,2);  // reqCode,retCode,source, dest
//						System.out.println("echo:move ended");
						RobotMainServer.actStat=0x03;
					}
					if (sentence2[8]==0x6a){
						if (RobotMainServer.runningStatus==6 )
						{
							RobotMainServer.runningStatus=106;
						}
						ihm.MajRobotStat("aligning");
						mess="echo:aligning";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
			//			PanneauGraphique.point(150+posXG,150+posYG);
			//			graph.repaint();
					}
					if (sentence2[8]==0x6b){

						if (RobotMainServer.octavePendingRequest==6 && RobotMainServer.runningStatus!=107);
						{
							RobotMainServer.octaveRequestPending=false;
						}
						RobotMainServer.runningStatus=107;
						ihm.MajRobotStat("aligning ended");
						mess="echo:robot align ended";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
			//			PanneauGraphique.point(150+posXG,150+posYG);
			//			graph.repaint();
					}
					String sb ;

					int sbn=sentence2[9];
					RobotMainServer.powerDiag=sbn;
					byte sbnb=(byte)sbn;
					StringBuffer sbb1 = new StringBuffer(2);
					sbb1.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb1.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					sbn=sentence2[11];
					sbnb=(byte)sbn;
					RobotMainServer.motorDiag=sbn;
					StringBuffer sbb2 = new StringBuffer(2);
					sbb2.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb2.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					sbn=sentence2[12];
					sbnb=(byte)sbn;
					RobotMainServer.connectionDiag=sbn;
					StringBuffer sbb3 = new StringBuffer(2);
					sbb3.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb3.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					StringBuffer sbb = new StringBuffer(15);
					sbn=sentence2[13];
					if (sbn==255)   // robot just reboot
					{
						RobotMainServer.currentLocProb=0;
						RobotMainServer.hardJustReboot=true;
					}
					sbnb=(byte)sbn;
					RobotMainServer.robotDiag=sbn;
					StringBuffer sbb4 = new StringBuffer(2);
					sbb4.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb4.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					ihm.MajRobotDiag("0x"+sbb1+" 0x"+sbb2+" 0x"+sbb4+" 0x"+sbb3);
	//				System.out.println(" diagPower:0x"+sbb1+"  Motors:0x"+sbb2+"  Connections:0x"+sbb3+"  Robot:0x"+sbb4);
					mess=" diagPower:0x"+sbb1+"  Motors:0x"+sbb2+"  Robot:0x"+sbb4+"  I2C:0x"+sbb3;
					Trace.TraceLog(pgmId,mess);
					//
					if (RobotMainServer.simulation==0)
					{
					int oct0=(byte)(sentence2[15]&0x7F)-(byte)(sentence2[15]&0x80); // manip car byte consider� sign�
					int oct1=(byte)(sentence2[16]&0x7F)-(byte)(sentence2[16]&0x80);
					RobotMainServer.hardPosX=256*oct0+oct1;
					if (sentence2[14]==0x2d)
					{
						RobotMainServer.hardPosX=-RobotMainServer.hardPosX;
					}
					oct0=(byte)(sentence2[18]&0x7F)-(byte)(sentence2[18]&0x80); // manip car byte consider� sign�
					oct1=(byte)(sentence2[19]&0x7F)-(byte)(sentence2[19]&0x80);
					RobotMainServer.hardPosY=256*oct0+oct1;
					if (sentence2[17]==0x2d)
					{
						RobotMainServer.hardPosY=-RobotMainServer.hardPosY;
					}
					oct0=(byte)(sentence2[21]&0x7F)-(byte)(sentence2[21]&0x80); // manip car byte consider� sign�
					oct1=(byte)(sentence2[22]&0x7F)-(byte)(sentence2[22]&0x80);
					RobotMainServer.hardAlpha=256*oct0+oct1;
					if (sentence2[20]==0x2d)
					{
						RobotMainServer.hardAlpha=-RobotMainServer.hardAlpha;
					}
					}
					int oct0=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80); // manip car byte consider� sign�
					int oct1=(byte)(sentence2[25]&0x7F)-(byte)(sentence2[25]&0x80);
					
					if (sentence2[23]==0x09)
					{
						RobotMainServer.northOrientation=256*oct0+oct1;
						RobotMainServer.absoluteOrientation=-1;	
					}
					else if (sentence2[23]==0x0c)
					{
						RobotMainServer.absoluteOrientation=256*oct0+oct1;
						RobotMainServer.northOrientation=-1;	
					}
					else
					{
						RobotMainServer.northOrientation=-1;		
						RobotMainServer.absoluteOrientation=-1;	
					}
					RobotMainServer.RefreshHardPositionOnScreen();
					if ( RobotMainServer.hardJustReboot==false && RobotMainServer.serverJustRestarted==true)
					{
						oct0=(byte)(sentence2[26]&0x7F)-(byte)(sentence2[27]&0x80);
						RobotMainServer.currentLocProb=oct0;
						RobotMainServer.serverJustRestarted=false;
					}
	//			ihm2.ValidePosition(RobotMainServer.hardPosX,RobotMainServer.hardPosY,RobotMainServer.hardAlpha);
					mess="posX:"+RobotMainServer.hardPosX+ " posY:"+RobotMainServer.hardPosY+" angle:"+ RobotMainServer.hardAlpha;
					Trace.TraceLog(pgmId,mess);
				if 	(RobotMainServer.octaveRequestPending==true && RobotMainServer.octavePendingRequest==1)
				{
					RobotMainServer.octavePendingRequest=0;
					RobotMainServer.octaveRequestPending=false;
				}
						
				}
	// end of status info
				
				
				if (sentence2[6]==0x70){  // power info
					EchoRobot.pendingEcho=0;
				int power1=sentence2[7]*10;
				if (power1<0)
				{
					power1=256-power1;
				}
				int power2=sentence2[8]*10;
				if (power2<0)
				{
					power2=256-power2;
				}
				RobotMainServer.power9V=power1;
				RobotMainServer.power5V=power2;
//				System.out.println("power1: "+power1+"cV power2: "+power2+"cV power3: "+power3+"cV");
				ihm.MajRobotPower(Integer.toString(power1)+ "cV "+Integer.toString(power2)+ "cV ");
				}
				
				if (sentence2[6]==0x71 || sentence2[6]==0x72|| sentence2[6]==0x73 || sentence2[6]==0x74){  // encoder & motor values
				EchoRobot.pendingEcho=0;
				int nbValue=(byte)(sentence2[7]&0x7F)-(byte)(sentence2[7]&0x80);
				int ix=8;
				if(sentence2[6]==0x71)
				{
					mess="encoder leftHigh leftLow RightHigh RightLow LefPWM rightPWM Ratio ";
//					Trace.TraceLog(pgmId,mess);
//					System.out.print("encoder leftHigh leftLow RightHigh RightLow LefPWM right PWM Ratio ");
				}
				if(sentence2[6]==0x72)
				{
					mess="encoder leftHigh leftMaxLevel leftLow leftMinLevel rightHigh rightMaxLevel rightLow rightMinLevel ";
//					System.out.print("encoder leftHigh leftMaxLevel leftLow leftMinLevel rightHigh rightMaxLevel rightLow rightMinLevel ");
				}
				if(sentence2[6]==0x73)
				{
					mess="leftMotorPWM RightMotorPWM ";
//					System.out.print("leftMotorPWM RightMotorPWM ");
				}
				if(sentence2[6]==0x74)
				{
					mess="leftHoles RightHoles ";
				}
				for (int i=1;i<=nbValue;i++)
				{
					int oct0=(byte)(sentence2[ix]&0x7F)-(byte)(sentence2[ix]&0x80); // manip car byte consider� sign�
					int oct1=(byte)(sentence2[ix+1]&0x7F)-(byte)(sentence2[ix+1]&0x80);
					int value=256*oct0+oct1;
					mess=mess+" "+value;
	//				System.out.print("-"+value+ "-");
					ix=ix+3;
					if(sentence2[6]==0x71 )
					{						
					switch(i){
						case (1):
						{
					RobotMainServer.leftHighThreshold=value;
					break;
					}
						case (2):
						{
					RobotMainServer.leftLowThreshold=value;
					break;
					}
						case (3):
						{
					RobotMainServer.rightHighThreshold=value;
					break;
					}
						case (4):
						{
					RobotMainServer.rightLowThreshold=value;
					break;
					}
						case (5):
						{
					RobotMainServer.leftPWM=value;
					break;
					}
						case (6):
						{
					RobotMainServer.rightPWM=value;
					break;
					}
						case (7):
						{
					RobotMainServer.PWMRatio=value;
					break;
					}
					}
					}
					if(sentence2[6]==0x72 )
					{						
					switch(i){
						case (1):
						{
					RobotMainServer.leftHighThreshold=value;
					break;
					}
						case (2):
						{
					RobotMainServer.leftMaxLevel=value;
					break;
					}
						case (3):
						{
					RobotMainServer.leftLowThreshold=value;
					break;
					}						
						case (4):
					{
							RobotMainServer.leftMinLevel=value;
							break;
					}
						case (5):
						{
					RobotMainServer.rightHighThreshold=value;
					break;
					}
						case (6):
						{
					RobotMainServer.rightMaxLevel=value;
					break;
					}
						case (7):
						{
					RobotMainServer.rightLowThreshold=value;
					break;
					}

						case (8):
						{
					RobotMainServer.rightMinLevel=value;
					break;
					}
					}
					}
					if(sentence2[6]==0x73 )
					{						
					switch(i){
						case (1):
						{
					RobotMainServer.leftPWM=value;
					break;
					}
						case (2):
						{
					RobotMainServer.rightPWM=value;
					break;
					}

					}
					}


					/*
					 * 
					 */
					if(sentence2[6]==0x74 )
					{
						switch(i){
						case (1):{
							RobotMainServer.cumulativeLeftHoles=value;
							break;
						}
						case (2):{
							RobotMainServer.cumulativeRightHoles=value;
							break;
						}

						}

						

					}

			//	System.out.println();

					}
				Trace.TraceLog(pgmId,mess);
				if (sentence2[6]==0x74 && (RobotMainServer.cumulativeLeftHoles != RobotMainServer.prevCumulativeLeftHoles || RobotMainServer.cumulativeRightHoles!= RobotMainServer.prevCumulativeRightHoles))
				{
					String sql="INSERT INTO encodersStatistics VALUES (now(), 1,"
							+ ""+RobotMainServer.cumulativeLeftHoles+","+RobotMainServer.cumulativeRightHoles+","+RobotMainServer.leftPWM+","+RobotMainServer.rightPWM+","+RobotMainServer.PWMRatio+","
									+ ""+RobotMainServer.leftMaxLevel+","+RobotMainServer.leftMinLevel+","+RobotMainServer.rightMaxLevel+","+RobotMainServer.rightMinLevel+""
							+ ","+RobotMainServer.leftLowThreshold+","+RobotMainServer.leftHighThreshold+","+RobotMainServer.rightLowThreshold+","+RobotMainServer.rightHighThreshold+")";
					RobotMainServer.prevCumulativeLeftHoles=RobotMainServer.cumulativeLeftHoles ;
					RobotMainServer.prevCumulativeRightHoles=RobotMainServer.cumulativeRightHoles ;
					InsertSqlData(sql);
				}
				}
				if(sentence2[6]==0x75)
				{

					int i=0;
//					int idx=8;
	//				(byte)(sentence2[7]&0x7F)-(byte)(sentence2[7]&0x80)
					RobotMainServer.BNOMode=(byte) ((byte)(sentence2[7]&0x7F)-(byte)(sentence2[7]&0x80));
					RobotMainServer.BNOCalStat=(byte) ((byte)(sentence2[8]&0x7F)-(byte)(sentence2[8]&0x80));
					RobotMainServer.BNOSysStat=(byte) ((byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80));
					RobotMainServer.BNOSysError=(byte) ((byte)(sentence2[11]&0x7F)-(byte)(sentence2[11]&0x80));
	//					int oct0=(byte)(sentence2[ix+3*i]&0x7F)-(byte)(sentence2[ix+3*i]&0x80); // manip car byte consider� sign�
	//					int oct1=(byte)(sentence2[ix+1+3*i]&0x7F)-(byte)(sentence2[ix+1+3*i]&0x80);
				mess=" BNO status mode 0x:"+byteToHex(RobotMainServer.BNOMode)+" calibration status 0x:"+byteToHex(RobotMainServer.BNOCalStat)+" system status 0x:"+byteToHex(RobotMainServer.BNOSysStat)+" system error 0x:"+byteToHex(RobotMainServer.BNOSysError);
				Trace.TraceLog(pgmId,mess);
				Fenetre2.RefreshBNO();
				}

				if(sentence2[6]==0x80)
				{
					System.out.print("subsystem registers ");
					int i=0;
//					int idx=8;
					for (i=0;i<sentence2[7];i++)
					{
	//					int oct0=(byte)(sentence2[ix+3*i]&0x7F)-(byte)(sentence2[ix+3*i]&0x80); // manip car byte consider� sign�
	//					int oct1=(byte)(sentence2[ix+1+3*i]&0x7F)-(byte)(sentence2[ix+1+3*i]&0x80);
						System.out.println(" register: 0x"+byteToHex(sentence2[8+3*i])+" value:00x"+byteToHex(sentence2[9+3*i]));
					}
				}
			}
	}

	void UpdateScanRefOrientation(int northOrientation)
	{
		int idscan=Fenetre.ids();
		if (idscan!=0)
			{
			Connection conn2 = null;
			Statement stmt = null;
			try{
	
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				String connectionUrl = "jdbc:mysql://jserver:3306/robot";
				String connectionUser = "jean";
				String connectionPassword = "manu7890";
				conn2 = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
				stmt = conn2.createStatement();
				String sql="INSERT INTO scanDesc VALUES ("+idscan+","+RobotMainServer.idCarto+","+northOrientation+",now())";
	
	//		    String sql = "UPDATE scanDesc " +
	//	                "SET northOrientation = "+northOrientation+"  WHERE scanId = "+idscan+"";
			    stmt.executeUpdate(sql);
			    	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (conn2 != null) conn2.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
	}
	  public static String byteToHex(byte b) {
		    int i = b & 0xFF;
		    return Integer.toHexString(i);
		  }
	  public void InsertSqlData(String sql)
	  {
			Connection conn = null;
			Statement stmtI = null;
			String pgmId="RobotBatchServer/Sql";
			TraceLog Trace = new TraceLog();
		 try {
			 Class.forName("com.mysql.jdbc.Driver").newInstance();
			 String connectionUrl = "jdbc:mysql://jserver:3306/robot";
			 String connectionUser = "jean";
			 String connectionPassword = "manu7890";
			 conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
				 stmtI = conn.createStatement();
			 int idscan=Fenetre.ids();
			 RobotMainServer.idscanG=Fenetre.idsString();
			 String idsG=RobotMainServer.idscanG;
			 RobotMainServer.GetCurrentPosition(idsG);
			 int angl=RobotMainServer.orientG;
//			 String sql="INSERT INTO scanResult VALUES ("+idscan+",now(),"+RobotMainServer.posX+","+RobotMainServer.posY+","+angle+","+distFront+","+distBack+","+angl+","+RobotMainServer.idCarto+")";
			 //System.out.println("ind id "+IndIdS+", pos " + IndPos + ", len: " + IndLen+" value"+IndValue);
			Trace.TraceLog(pgmId,sql);
//			 System.out.println(sql);
			 stmtI.executeUpdate(sql);
		 	}
		 	catch (Exception e) {
		 		e.printStackTrace();
		 	} finally {
		 		try { if (stmtI != null) stmtI.close(); } catch (SQLException e) { e.printStackTrace(); }
		 		try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		 	}
}
}