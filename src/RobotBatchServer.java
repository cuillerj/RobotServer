import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class RobotBatchServer implements Runnable {
	public static int statusFrameCount=0;
	public static int waitForEndOf=0;
	byte[] prevSentence=new byte[1024];
	public static int missedEventType=0;
	public static int missedEventRetcode=0;
	public void run()
	{
		String pgmId="RobotBatchServer";
		RobotMainServer.GetLastScanID();
		String mess="lastid:"+RobotMainServer.idscanG+" "+RobotMainServer.posXG+ " "+RobotMainServer.posYG;
		TraceLog Trace = new TraceLog();
		Trace.TraceLog(pgmId,mess);
		Fenetre ihm = new Fenetre();
		Fenetre2 ihm2 = new Fenetre2();
		FenetreGraphiqueSonar ihm3 = new FenetreGraphiqueSonar();
		ihm3.SetInitialPosition();

			DatagramSocket serverSocket = null;
			try {
				serverSocket = new DatagramSocket(1830);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			int ID=0; // station ID
			byte Type=0; // station type
			int InpLen=0; // input datalength - lue dans la trame UDP
			byte trameNumber=0;
			byte lastTrameNumber=0;
			EchoRobot echo=new EchoRobot();
			echo.start();
			TraceEvents trace=new TraceEvents();
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
					if (RobotMainServer.ipRobot==null)
					{
						RobotMainServer.ipRobot = receivePacket.getAddress();		
		    			Trace.TraceLog(pgmId,"Receive first IP frame:"+RobotMainServer.ipRobot.toString());
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				String sentence = new String( receivePacket.getData());

				 sentence2 =  receivePacket.getData();
			if(!Arrays.equals(sentence2, prevSentence)) // not a duplicated frame
			 {
				prevSentence=sentence2;					 
				EchoRobot.pendingEcho=0;
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
				if(RobotMainServer.runningStatus>=0)
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
					try {
						Thread.sleep(100);
				 		} 
					catch (InterruptedException e1) {
						e1.printStackTrace();
				 		}
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
				    	 int distFront=((sentence2[i2] << 8) & 0x0000ff00) | (sentence2[i2+1] & 0x000000ff);
				    	 i2=12;
				    	 int distBack=((sentence2[i2] << 8) & 0x0000ff00) | (sentence2[i2+1] & 0x000000ff);
				    	 i2=15;
				    	 int angle=((sentence2[i2] << 8) & 0x0000ff00) | (sentence2[i2+1] & 0x000000ff);;
				    	 i2=19;
				    	 RobotMainServer.northOrientation=((sentence2[i2] << 8) & 0x0000ff00) | (sentence2[i2+1] & 0x000000ff);
				    	 RobotMainServer.RefreshHardPositionOnScreen();
				    	 if (RobotMainServer.scanStepCount!=0)
				    	 	{
				    		 	RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][0]=angle;
				    		 	RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][1]=distFront;
				    		 	RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][2]=distBack;
				    		 	RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][3]=RobotMainServer.northOrientation;
				    		 	RobotMainServer.scanStepCount++;
				    	 	}
				    	 else{
				    		 RobotMainServer.pingArray[0]=angle;
				    		 RobotMainServer.pingArray[1]=distFront;
				    		 RobotMainServer.pingArray[2]=distBack;
				    		 RobotMainServer.pingArray[3]=RobotMainServer.northOrientation;
				    	 }
				    	 RobotMainServer.lastEchoFront=distFront;
				    	 RobotMainServer.lastEchoBack=distBack;
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
				    			 RobotMainServer.scanReceiveCount++;
				    			 String sql="INSERT INTO scanResult VALUES ("+idscan+",now(),"+RobotMainServer.posX+","+RobotMainServer.posY+","+angle+","+distFront+","+distBack+","+RobotMainServer.northOrientation+","+RobotMainServer.idCarto+","+RobotMainServer.trainBatch+")";
				    			 //System.out.println("ind id "+IndIdS+", pos " + IndPos + ", len: " + IndLen+" value"+IndValue);
				    //			Trace.TraceLog(pgmId,sql);
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
				    		 	lastTrameNumber=trameNumber;
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
						RobotMainServer.actionRetcode=(byte)(sentence2[10]);
						byte retCode=sentence2[10];
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
							int eventType=(sentence2[9] & 0x000000ff);
//							System.out.println("event type:"+eventType);
							mess="event type:"+eventType;
							Trace.TraceLog(pgmId,mess);
							int actionRetcode=(sentence2[10] & 0x000000ff);
	//						int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consideré signé
	//						int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
							RobotMainServer.northOrientation=((sentence2[i2] << 8) & 0x0000ff00) | (sentence2[i2+1] & 0x000000ff);
							UpdateScanRefOrientation(RobotMainServer.northOrientation);
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
//							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,RobotMainServer.eventArduino);  // reqCode,retCode,source, dest
							ihm.MajRobotStat("scan ended");
							mess="Received "+"event type:"+eventType+" scan ended:"+retCode;
							Trace.TraceLog(pgmId,mess);
						    RobotMainServer.scanStepCount=0;
							RobotMainServer.runningStatus=2;
							}
						if (sentence2[9]==0x6b){                    // 
							EchoRobot.pendingEcho=0;
//							System.out.println("align ended");
							int eventType=(sentence2[9] & 0x000000ff);
			//				mess="event type:"+eventType;
			//				Trace.TraceLog(pgmId,mess);
							int actionRetcode=(sentence2[10] & 0x000000ff);
							int i2=10;
	//						int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consideré signé
		//					int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
							RobotMainServer.northOrientation=((sentence2[i2] << 8) & 0x0000ff00) | (sentence2[i2+1] & 0x000000ff);
		//					oct0=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80); // manip car byte consideré signé
		//					oct1=(byte)(sentence2[25]&0x7F)-(byte)(sentence2[25]&0x80);
							RobotMainServer.deltaNORotation=((sentence2[24] << 8) & 0x0000ff00) | (sentence2[25] & 0x000000ff);;
							if (sentence2[23]==0x2d)
							{
								RobotMainServer.deltaNORotation=-RobotMainServer.deltaNORotation;
							}
//							EventManagement.UpdateEvent(eventType,actionRetcode,1,2);  // reqCode,retCode,source, dest
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
							ihm.MajRobotStat("align ended:"+retCode);
							mess="Received "+"event type:"+eventType+" robot align ended"+" deltaNORot:"+RobotMainServer.deltaNORotation+" retCode:"+retCode;
							Trace.TraceLog(pgmId,mess);
							RobotMainServer.runningStatus=2;
							}
						
						if (sentence2[9]==0x6c){                    // 
							EchoRobot.pendingEcho=0;
//							System.out.println("servo align ended");
							int eventType=(sentence2[9] & 0x000000ff);
			//				mess="event type:"+eventType;
				//			Trace.TraceLog(pgmId,mess);
							int actionRetcode=(sentence2[10] & 0x000000ff);
							int i2=10;
						//	int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consideré signé
						//	int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
							RobotMainServer.northOrientation=((sentence2[i2] << 8) & 0x0000ff00) | (sentence2[i2+1] & 0x000000ff);;
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
							ihm.MajRobotStat("servo align ended");
							mess="Received "+"event type:"+eventType+" servo align ended:"+retCode;
							Trace.TraceLog(pgmId,mess);
							RobotMainServer.runningStatus=2;
							}
						if (sentence2[9]==0x6d){                    // 
							EchoRobot.pendingEcho=0;
//							System.out.println("servo align ended");
							int eventType=(sentence2[9] & 0x000000ff);
				//			mess="event type:"+eventType;
					//		Trace.TraceLog(pgmId,mess);
							int actionRetcode=(sentence2[10] & 0x000000ff);
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
//							EventManagement.UpdateEvent(eventType,actionRetcode,1,2);  // reqCode,retCode,source, dest
							ihm.MajRobotStat("pingFB ended");
							mess="Received "+"event type:"+eventType+" pingFG ended:"+retCode;
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
							RobotMainServer.actStat=0x02; 
							int eventType=(sentence2[9] & 0x000000ff);
							int actionRetcode=(sentence2[10] & 0x000000ff);
							ihm.MajRobotStat("move ended");
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
							RobotMainServer.runningStatus=4;

							RobotMainServer.hardPosX=((sentence2[15] << 8) & 0x0000ff00) | (sentence2[16] & 0x000000ff);
							if (sentence2[14]==0x2d)
							{
								RobotMainServer.hardPosX=-RobotMainServer.hardPosX;
							}

							RobotMainServer.hardPosY=((sentence2[18] << 8) & 0x0000ff00) | (sentence2[19] & 0x000000ff);
							if (sentence2[17]==0x2d)
							{
								RobotMainServer.hardPosY=-RobotMainServer.hardPosY;
							}

							RobotMainServer.hardAlpha=((sentence2[21] << 8) & 0x0000ff00) | (sentence2[22] & 0x000000ff);
							if (sentence2[20]==0x2d)
							{
								RobotMainServer.hardAlpha=-RobotMainServer.hardAlpha;
							}

							RobotMainServer.deltaNORotation=((sentence2[24] << 8) & 0x0000ff00) | (sentence2[25] & 0x000000ff);
							if (sentence2[23]==0x2d)
							{
								RobotMainServer.deltaNORotation=-RobotMainServer.deltaNORotation;
							}

							RobotMainServer.deltaNOMoving=((sentence2[27] << 8) & 0x0000ff00) | (sentence2[28] & 0x000000ff);
							if (sentence2[26]==0x2d)
							{
								RobotMainServer.deltaNOMoving=-RobotMainServer.deltaNOMoving;
							}

							RobotMainServer.northOrientation=((sentence2[12] << 8) & 0x0000ff00) | (sentence2[13] & 0x000000ff);

							RobotMainServer.gyroHeading=((sentence2[30] << 8) & 0x0000ff00) | (sentence2[31] & 0x000000ff);
							if (sentence2[29]==0x2d)
							{
								RobotMainServer.gyroHeading=-RobotMainServer.gyroHeading;
							}
							RobotMainServer.retCodeDetail=((sentence2[34] << 8) & 0x0000ff00) | (sentence2[35] & 0x000000ff);
							if (sentence2[33]==0x2d)
							{
								RobotMainServer.retCodeDetail=-RobotMainServer.retCodeDetail;
							}
							String XString=Integer.toString(RobotMainServer.hardPosX);
							String YString=Integer.toString(RobotMainServer.hardPosY);
							String HString=Integer.toString(RobotMainServer.hardAlpha);
							mess="Received "+"event type:"+eventType+" move ended X:"+XString+" Y:"+YString+" Heading:"+HString+ " NO:"+RobotMainServer.northOrientation+" deltaNORot:"+RobotMainServer.deltaNORotation+" deltaNOMov:"+RobotMainServer.deltaNOMoving+" GyroHeading:"+RobotMainServer.gyroHeading+" retCode: 0x"+byteToHex(retCode)+" "+RobotMainServer.moveRetcodeList[retCode]+" detail:"+RobotMainServer.retCodeDetail;
							Trace.TraceLog(pgmId,mess);
	//						System.out.println("refresh hard on screen");
							AnalyseRetcode((byte)RobotMainServer.moveEnd, retCode,sentence2[33],sentence2[34],sentence2[35]);
							RobotMainServer.RefreshHardPositionOnScreen();
							if (RobotMainServer.actStat==0x02){ 
								Fenetre2.ValidePosition(RobotMainServer.posX,RobotMainServer.posY,RobotMainServer.alpha);
								
				//				PanneauGraphique.point(200+posXG/10,200+posYG/10);
				//				graph.repaint();
								}
						RobotMainServer.actStat=0x03;
							}
						if ((sentence2[9]&0x7f)==(((byte)RobotMainServer.moveAcrossPassEnded)&0x7f)){                    // 
							EchoRobot.pendingEcho=0;
	//						int ang=ihm.ang();
	//						int mov=ihm.mov();
	//						Fenetre2.PosActualise(ang,mov);
							RobotMainServer.actStat=0x02; 
							int eventType=(sentence2[9] & 0x000000ff);
			//				mess="event type:"+eventType;
			//				Trace.TraceLog(pgmId,mess);
							int actionRetcode=(sentence2[10] & 0x000000ff);
							ihm.MajRobotStat("move APath ended");
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest
//							EventManagement.UpdateEvent(eventType,actionRetcode,1,2);  // reqCode,retCode,source, dest
							RobotMainServer.runningStatus=4;
					//		int oct0=(byte)(sentence2[15]&0x7F)-(byte)(sentence2[15]&0x80); // manip car byte consideré signé
					//		int oct1=(byte)(sentence2[16]&0x7F)-(byte)(sentence2[16]&0x80);
							RobotMainServer.hardPosX=((sentence2[15] << 8) & 0x0000ff00) | (sentence2[16] & 0x000000ff);;
							if (sentence2[14]==0x2d)
							{
								RobotMainServer.hardPosX=-RobotMainServer.hardPosX;
							}
			//				oct0=(byte)(sentence2[18]&0x7F)-(byte)(sentence2[18]&0x80); // manip car byte consideré signé
			//				oct1=(byte)(sentence2[19]&0x7F)-(byte)(sentence2[19]&0x80);
							RobotMainServer.hardPosY=((sentence2[18] << 8) & 0x0000ff00) | (sentence2[19] & 0x000000ff);;
							if (sentence2[17]==0x2d)
							{
								RobotMainServer.hardPosY=-RobotMainServer.hardPosY;
							}
				//			oct0=(byte)(sentence2[21]&0x7F)-(byte)(sentence2[21]&0x80); // manip car byte consideré signé
				//			oct1=(byte)(sentence2[22]&0x7F)-(byte)(sentence2[22]&0x80);
							RobotMainServer.hardAlpha=((sentence2[21] << 8) & 0x0000ff00) | (sentence2[22] & 0x000000ff);
							if (sentence2[20]==0x2d)
							{
								RobotMainServer.hardAlpha=-RobotMainServer.hardAlpha;
							}
					//		oct0=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80); // manip car byte consideré signé
					//		oct1=(byte)(sentence2[25]&0x7F)-(byte)(sentence2[25]&0x80);
							RobotMainServer.deltaNORotation=((sentence2[24] << 8) & 0x0000ff00) | (sentence2[25] & 0x000000ff);
							if (sentence2[23]==0x2d)
							{
								RobotMainServer.deltaNORotation=-RobotMainServer.deltaNORotation;
							}
					//		oct0=(byte)(sentence2[27]&0x7F)-(byte)(sentence2[27]&0x80); // manip car byte consideré signé
					//		oct1=(byte)(sentence2[28]&0x7F)-(byte)(sentence2[28]&0x80);
							RobotMainServer.deltaNOMoving=((sentence2[27] << 8) & 0x0000ff00) | (sentence2[28] & 0x000000ff);
							if (sentence2[26]==0x2d)
							{
								RobotMainServer.deltaNOMoving=-RobotMainServer.deltaNOMoving;
							}
				//			oct0=(byte)(sentence2[12]&0x7F)-(byte)(sentence2[12]&0x80); // manip car byte consideré signé
				//			oct1=(byte)(sentence2[13]&0x7F)-(byte)(sentence2[13]&0x80);
							RobotMainServer.northOrientation=((sentence2[12] << 8) & 0x0000ff00) | (sentence2[1] & 0x000000ff);
				//			oct0=(byte)(sentence2[30]&0x7F)-(byte)(sentence2[30]&0x80); // manip car byte consideré signé
					//		oct1=(byte)(sentence2[31]&0x7F)-(byte)(sentence2[31]&0x80);
							RobotMainServer.gyroHeading=((sentence2[30] << 8) & 0x0000ff00) | (sentence2[31] & 0x000000ff);
							if (sentence2[29]==0x2d)
							{
								RobotMainServer.gyroHeading=-RobotMainServer.gyroHeading;
							}
					//		oct0=(byte)(sentence2[34]&0x7F)-(byte)(sentence2[34]&0x80); // manip car byte consideré signé
						//	oct1=(byte)(sentence2[35]&0x7F)-(byte)(sentence2[35]&0x80);
							RobotMainServer.retCodeDetail=((sentence2[34] << 8) & 0x0000ff00) | (sentence2[35] & 0x000000ff);
							if (sentence2[33]==0x2d)
							{
								RobotMainServer.retCodeDetail=-RobotMainServer.retCodeDetail;
							}
							/*
							oct0=(byte)(sentence2[33]&0x7F)-(byte)(sentence2[33]&0x80); // manip car byte consideré signé
							oct1=(byte)(sentence2[34]&0x7F)-(byte)(sentence2[34]&0x80);			
							RobotMainServer.cumulativeRightHoles=256*oct0+oct1;
							*/
							String XString=Integer.toString(RobotMainServer.hardPosX);
							String YString=Integer.toString(RobotMainServer.hardPosY);
							String HString=Integer.toString(RobotMainServer.hardAlpha);
							mess="Received "+"event type:"+eventType+" move across path ended X:"+XString+" Y:"+YString+" Heading:"+HString+ " NO:"+RobotMainServer.northOrientation+" deltaNORot:"+RobotMainServer.deltaNORotation+" deltaNOMov:"+RobotMainServer.deltaNOMoving+" GyroHeading:"+RobotMainServer.gyroHeading+" retCode: 0x"+byteToHex(retCode)+" detail:"+RobotMainServer.retCodeDetail;
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
							int eventType=(sentence2[9] & 0x000000ff);
							mess="Received "+"event type:"+eventType;
							Trace.TraceLog(pgmId,mess);
							int actionRetcode=(sentence2[10] & 0x000000ff);
							EventManagement.UpdateEvent(eventType,actionRetcode,RobotMainServer.eventOctave,
							RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest							
							if (actionRetcode==0)
							{
								int i2=12;
				//				int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consideré signé
				//				int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
								RobotMainServer.northOrientation=((sentence2[i2] << 8) & 0x0000ff00) | (sentence2[i2+1] & 0x000000ff);
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
						//int oct0=(byte)(sentence2[11]&0x7F)-(byte)(sentence2[11]&0x80); // manip car byte consideré signé
						//int oct1=(byte)(sentence2[12]&0x7F)-(byte)(sentence2[12]&0x80);
						int pos=((sentence2[11] << 8) & 0x0000ff00) | (sentence2[12] & 0x000000ff);
						if (sentence2[10]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNOLeftPosX=pos;
				//		 oct0=(byte)(sentence2[14]&0x7F)-(byte)(sentence2[14]&0x80); // manip car byte consideré signé
					//	 oct1=(byte)(sentence2[15]&0x7F)-(byte)(sentence2[15]&0x80);
						 pos=((sentence2[14] << 8) & 0x0000ff00) | (sentence2[15] & 0x000000ff);
						if (sentence2[13]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNOLeftPosY=pos;
						// oct0=(byte)(sentence2[17]&0x7F)-(byte)(sentence2[17]&0x80); // manip car byte consideré signé
						// oct1=(byte)(sentence2[18]&0x7F)-(byte)(sentence2[18]&0x80);
						 pos=((sentence2[17] << 8) & 0x0000ff00) | (sentence2[18] & 0x000000ff);
						if (sentence2[16]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNORightPosX=pos;
			//			 oct0=(byte)(sentence2[20]&0x7F)-(byte)(sentence2[20]&0x80); // manip car byte consideré signé
				//		 oct1=(byte)(sentence2[21]&0x7F)-(byte)(sentence2[21]&0x80);
						 pos=((sentence2[20] << 8) & 0x0000ff00) | (sentence2[21] & 0x000000ff);
						if (sentence2[19]==0x2d)
						{
							pos=-pos;
						}
						RobotMainServer.BNORightPosY=pos;
		//				 oct0=(byte)(sentence2[23]&0x7F)-(byte)(sentence2[23]&0x80); // manip car byte consideré signé
		//				 oct1=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80);
						 pos=((sentence2[23] << 8) & 0x0000ff00) | (sentence2[24] & 0x000000ff);
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
				if (sentence2[6]==0x65){                    // e  status info
					EchoRobot.pendingEcho=0;
					if (RobotMainServer.javaRequestStatusPending==true)
					{
 // reqCode,retCode,source, dest
						EventManagement.UpdateEvent(RobotMainServer.robotInfoUpdated,0,RobotMainServer.eventOctave,RobotMainServer.eventArduino);  // reqCode,retCode,source, dest
						EventManagement.UpdateEvent(RobotMainServer.robotUpdatedEnd,0,RobotMainServer.eventOctave,RobotMainServer.eventArduino);  // reqCode,retCode,source, dest
					}
					if (RobotMainServer.pendingAcqUdp==true)
					{
						if (sentence2[34]==SendUDP.countUdp)
						{
							RobotMainServer.pendingAcqUdp=false;
							statusFrameCount=0;
						}
						else
						{
							statusFrameCount++;
							if (statusFrameCount >3)
							{
								SendUDP.ResendLastFrame();
							}
						}
					if(RobotMainServer.octaveRequestPending==true && waitForEndOf==(sentence2[8] & 0x000000ff)) // end action missed

					{
						missedEventType=(sentence2[8] & 0x000000ff); 
						missedEventRetcode=sentence2[27];
						mess=" Status received:"+missedEventType+ " retcode:"+missedEventRetcode+" Wait for endAction";
						Trace.TraceLog(pgmId,mess);
		//				EventManagement.UpdateEvent(eventType,sentence2[27],RobotMainServer.eventOctave,
		//						RobotMainServer.eventArduino+RobotMainServer.simulation*RobotMainServer.actionSimulable[eventType][0]*RobotMainServer.actionSimulable[eventType][1]);  // reqCode,retCode,source, dest							
					}
					}
	//			    RobotMainServer.javaRequestStatusPending=false;
	//				System.out.print("echo response: ");

					ihm.MajRobotStat("connected");
					if (sentence2[8]==RobotMainServer.scanning){
	//					System.out.print("scan running ");
						if (RobotMainServer.runningStatus==2)  // scan required
						{
							RobotMainServer.runningStatus=102;
						}

						ihm.MajRobotStat("Scan running");
						mess="Scan running";
						Trace.TraceLog(pgmId,mess);
					}
					if (sentence2[8]==RobotMainServer.scanEnd){
	//					System.out.print("scan ended ");

						if (RobotMainServer.runningStatus==2 || RobotMainServer.runningStatus==102)
						{
							RobotMainServer.runningStatus=103;
						}
						ihm.MajRobotStat("scan ended");
						mess="Scan ended";
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
					if (sentence2[8]==RobotMainServer.moving){
//						System.out.print("moving");
						if (RobotMainServer.runningStatus==4)  // scan required
						{
							RobotMainServer.runningStatus=104;
						}
						ihm.MajRobotStat("moving");
						mess="Moving";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
			//			PanneauGraphique.point(150+posXG,150+posYG);
			//			graph.repaint();
					}
					if (sentence2[8]==RobotMainServer.moveEnd){
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

						ihm.MajRobotStat("Move ended");

						EventManagement.UpdateEvent(4,0,1,2);  // reqCode,retCode,source, dest
//						System.out.println("echo:move ended");
						RobotMainServer.actStat=0x03;
					}
					if (sentence2[8]==RobotMainServer.northAlignEnd){
						if (RobotMainServer.runningStatus==6 )
						{
							RobotMainServer.runningStatus=106;
						}
						ihm.MajRobotStat("aligning");
						mess="Aligning";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
			//			PanneauGraphique.point(150+posXG,150+posYG);
			//			graph.repaint();
					}
					if (sentence2[8]==RobotMainServer.northAlignEnd){

						if (RobotMainServer.octavePendingRequest==6 && RobotMainServer.runningStatus!=107);
						{
							RobotMainServer.octaveRequestPending=false;
						}
						RobotMainServer.runningStatus=107;
						ihm.MajRobotStat("aligning ended");
						mess="Align ended";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
			//			PanneauGraphique.point(150+posXG,150+posYG);
			//			graph.repaint();
					}
					if (sentence2[8]==RobotMainServer.gyroRotating){

						RobotMainServer.runningStatus=RobotMainServer.gyroRotating;
						ihm.MajRobotStat("gyroRotating");
						mess="gyroRotating";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
			//			PanneauGraphique.point(150+posXG,150+posYG);
			//			graph.repaint();
					}
					if (sentence2[8]==RobotMainServer.gyroRotateEnd){

						if (RobotMainServer.octavePendingRequest==6 && RobotMainServer.runningStatus!=RobotMainServer.gyroRotating);
						{
							RobotMainServer.octaveRequestPending=false;
						}
						RobotMainServer.runningStatus=RobotMainServer.gyroRotateEnd;
						ihm.MajRobotStat("gyroRotateEnd");
						mess="gyroRotateEnd";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
					}
					if ((sentence2[8]&0x7f)==(((byte)RobotMainServer.moveAcrossPassEnded)&0x7f)){

						if (RobotMainServer.octavePendingRequest==6 && RobotMainServer.runningStatus!=RobotMainServer.gyroRotating);
						{
							RobotMainServer.octaveRequestPending=false;
						}
						RobotMainServer.runningStatus=RobotMainServer.gyroRotateEnd;
						ihm.MajRobotStat("accrossPathEnd");
						mess="accrossPathEnd";
						Trace.TraceLog(pgmId,mess);
						int ang=ihm.ang();
						int mov=ihm.mov();
						RobotMainServer.actStat=0x02;  
						ihm2.PosActualise(ang,mov);
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
					//	RobotMainServer.currentLocProb=0;
						RobotMainServer.hardJustReboot=true;
					}
					sbnb=(byte)sbn;
					RobotMainServer.robotDiag=sbn;
					StringBuffer sbb4 = new StringBuffer(2);
					sbb4.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb4.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					ihm.MajRobotDiag("0x"+sbb1+" 0x"+sbb2+" 0x"+sbb4+" 0x"+sbb3);
	//				System.out.println(" diagPower:0x"+sbb1+"  Motors:0x"+sbb2+"  Connections:0x"+sbb3+"  Robot:0x"+sbb4);
					mess=" diagPower:0x"+sbb1+"  Motors:0x"+sbb2+"  Robot:0x"+sbb4+"  SubSystem:0x"+sbb3;
					Trace.TraceLog(pgmId,mess);
					StringBuffer sbb12 = new StringBuffer(2);
					StringBuffer sbb22 = new StringBuffer(2);
					StringBuffer sbb32 = new StringBuffer(2);
					StringBuffer sbb42 = new StringBuffer(2);
					sbn=sentence2[23];
					sbnb=(byte)sbn;
					sbb12.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb12.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					sbn=sentence2[27];
					sbnb=(byte)sbn;
					sbb22.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb22.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					sbn=sentence2[31];
					sbnb=(byte)sbn;
					sbb32.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb32.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					sbn=sentence2[33];
					sbnb=(byte)sbn;
					sbb42.append( RobotMainServer.TAB_BYTE_HEX[(sbnb>>4) & 0xf] );
					sbb42.append( RobotMainServer.TAB_BYTE_HEX[(sbnb) & 0x0f] );
					mess=" BNOMode:0x"+sbb12+"  LastRetCode:0x"+sbb22+"  PendingAction:0x"+sbb32+"  WaitFlag:0x"+sbb42;
					Trace.TraceLog(pgmId,mess);
					//
					if (RobotMainServer.simulation==0)
					{
	//				int oct0=(byte)(sentence2[15]&0x7F)-(byte)(sentence2[15]&0x80); // manip car byte consideré signé
		//			int oct1=(byte)(sentence2[16]&0x7F)-(byte)(sentence2[16]&0x80);
					RobotMainServer.hardPosX=((sentence2[15] << 8) & 0x0000ff00) | (sentence2[16] & 0x000000ff);;
					if (sentence2[14]==0x2d)
					{
						RobotMainServer.hardPosX=-RobotMainServer.hardPosX;
					}
			//		oct0=(byte)(sentence2[18]&0x7F)-(byte)(sentence2[18]&0x80); // manip car byte consideré signé
				//	oct1=(byte)(sentence2[19]&0x7F)-(byte)(sentence2[19]&0x80);
					RobotMainServer.hardPosY=((sentence2[18] << 8) & 0x0000ff00) | (sentence2[19] & 0x000000ff);
					if (sentence2[17]==0x2d)
					{
						RobotMainServer.hardPosY=-RobotMainServer.hardPosY;
					}
				//	oct0=(byte)(sentence2[21]&0x7F)-(byte)(sentence2[21]&0x80); // manip car byte consideré signé
				//	oct1=(byte)(sentence2[22]&0x7F)-(byte)(sentence2[22]&0x80);
					RobotMainServer.hardAlpha=((sentence2[21] << 8) & 0x0000ff00) | (sentence2[22] & 0x000000ff);
					if (sentence2[20]==0x2d)
					{
						RobotMainServer.hardAlpha=-RobotMainServer.hardAlpha;
					}
					}
			//		int oct0=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80); // manip car byte consideré signé
			//		int oct1=(byte)(sentence2[25]&0x7F)-(byte)(sentence2[25]&0x80);
					
					if (sentence2[23]==0x09)
					{
						RobotMainServer.northOrientation=((sentence2[24] << 8) & 0x0000ff00) | (sentence2[25] & 0x000000ff);
						RobotMainServer.absoluteOrientation=-1;	
					}
					else if (sentence2[23]==0x0c)
					{
						RobotMainServer.absoluteOrientation=((sentence2[24] << 8) & 0x0000ff00) | (sentence2[25] & 0x000000ff);
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
				//		oct0=(byte)(sentence2[26]&0x7F)-(byte)(sentence2[26]&0x80);
			//			RobotMainServer.currentLocProb=(sentence2[26] & 0x000000ff);
						RobotMainServer.serverJustRestarted=false;
					}
	//			ihm2.ValidePosition(RobotMainServer.hardPosX,RobotMainServer.hardPosY,RobotMainServer.hardAlpha);
					mess="posX:"+RobotMainServer.hardPosX+ " posY:"+RobotMainServer.hardPosY+" angle:"+ RobotMainServer.hardAlpha;
					Trace.TraceLog(pgmId,mess);
				if 	(RobotMainServer.octaveRequestPending==true && RobotMainServer.octavePendingRequest==1)
				{
					RobotMainServer.octavePendingRequest=0;
				}
				if (sentence2[7]==0xff)   // app status  equal not started or stopped
				{
					RobotMainServer.runningStatus=-1;
				}
				else{
				
					if (RobotMainServer.runningStatus==0 || RobotMainServer.runningStatus==-1 )
						{
							RobotMainServer.runningStatus=1;
						}
					}						
				}
	// end of status info
				
				
				if (sentence2[6]==0x70){  // power info

					int power1=((byte)(sentence2[7]&0x7F)-(byte)(sentence2[7]&0x80))*10;
					int power2=((byte)(sentence2[8]&0x7F)-(byte)(sentence2[8]&0x80))*10;
					int power3=((byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80))*10;
					int power4=((byte)(sentence2[11]&0x7F)-(byte)(sentence2[11]&0x80))*10;
					int power5=((byte)(sentence2[13]&0x7F)-(byte)(sentence2[13]&0x80))*10;
					int power6=((byte)(sentence2[14]&0x7F)-(byte)(sentence2[14]&0x80))*10;
					RobotMainServer.voltage1=power1;
					RobotMainServer.voltage2=power2;
					RobotMainServer.voltage3=power3;
					RobotMainServer.voltage4=power4;
					RobotMainServer.voltage5=power5;
					RobotMainServer.voltage6=power6;
//				System.out.println("power1: "+power1+"cV power2: "+power2+"cV power3: "+power3+"cV");
					ihm.MajRobotPower(Integer.toString(power1)+ "cV "+Integer.toString(power2)+ "cV ");
					String sql="INSERT INTO powerHistoric VALUES (now(), "
						+ ""+RobotMainServer.voltage1+","+RobotMainServer.voltage2+","+RobotMainServer.voltage3+","+RobotMainServer.voltage4+","+RobotMainServer.voltage5+","
								+ ""+RobotMainServer.voltage6+")";
					RobotMainServer.prevCumulativeLeftHoles=RobotMainServer.cumulativeLeftHoles ;
					RobotMainServer.prevCumulativeRightHoles=RobotMainServer.cumulativeRightHoles ;
					InsertSqlData(sql);				
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
				boolean changeEncoderValues=false;
				for (int i=1;i<=nbValue;i++)
				{
	//				int oct0=(byte)(sentence2[ix]&0x7F)-(byte)(sentence2[ix]&0x80); // manip car byte consideré signé
	//				int oct1=(byte)(sentence2[ix+1]&0x7F)-(byte)(sentence2[ix+1]&0x80);
					int value=((sentence2[ix] << 8) & 0x0000ff00) | (sentence2[ix+1] & 0x000000ff);;
					mess=mess+" "+value;
	//				System.out.print("-"+value+ "-");
					ix=ix+3;
					if(sentence2[6]==0x71 )
					{						
					switch(i){
						case (1):
						{
							if (RobotMainServer.leftHighThreshold!=value)
							{
								RobotMainServer.leftHighThreshold=value;
								changeEncoderValues=true;
							}
							break;
						}
						case (2):
						{
						if (RobotMainServer.leftLowThreshold!=value)
						{
							RobotMainServer.leftLowThreshold=value;
							changeEncoderValues=true;
						}
						break;
						}
						case (3):
						{
						if (RobotMainServer.rightHighThreshold!=value)
						{
							RobotMainServer.rightHighThreshold=value;
							changeEncoderValues=true;
						}
						break;
						}
						case (4):
						{
						if (RobotMainServer.rightLowThreshold!=value)
						{
							RobotMainServer.rightLowThreshold=value;
							changeEncoderValues=true;
						}
						break;
						}
						case (5):
						{
						if (RobotMainServer.leftPWM!=value)
						{
							RobotMainServer.leftPWM=value;
							changeEncoderValues=true;
						}
						break;
						}
						case (6):
						{
						RobotMainServer.rightPWM=value;
						if (RobotMainServer.rightPWM!=value)
						{
							RobotMainServer.rightPWM=value;
							changeEncoderValues=true;
						}
						break;
						}
						case (7):
						{
						if (RobotMainServer.PWMRatio!=value)
						{
							RobotMainServer.PWMRatio=value;
							changeEncoderValues=true;
						}
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
						case (3):
						{
					RobotMainServer.SlowPWMRatio=value;
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
			//	Trace.TraceLog(pgmId,mess);
				if (changeEncoderValues || RobotMainServer.cumulativeLeftHoles != RobotMainServer.prevCumulativeLeftHoles || RobotMainServer.cumulativeRightHoles!= RobotMainServer.prevCumulativeRightHoles)
				{
					String sql="INSERT INTO encodersStatistics VALUES (now(), 1,"
							+ ""+RobotMainServer.cumulativeLeftHoles+","+RobotMainServer.cumulativeRightHoles+","+RobotMainServer.leftPWM+","+RobotMainServer.rightPWM+","+RobotMainServer.SlowPWMRatio+","
									+ ""+RobotMainServer.leftMaxLevel+","+RobotMainServer.leftMinLevel+","+RobotMainServer.rightMaxLevel+","+RobotMainServer.rightMinLevel+""
							+ ","+RobotMainServer.leftLowThreshold+","+RobotMainServer.leftHighThreshold+","+RobotMainServer.rightLowThreshold+","+RobotMainServer.rightHighThreshold+")";
					RobotMainServer.prevCumulativeLeftHoles=RobotMainServer.cumulativeLeftHoles ;
					RobotMainServer.prevCumulativeRightHoles=RobotMainServer.cumulativeRightHoles ;
					InsertSqlData(sql);
				}
				}
				if(sentence2[6]==RobotMainServer.respBNOSubsytemStatus)
				{

					int i=0;
//					int idx=8;
	//				(byte)(sentence2[7]&0x7F)-(byte)(sentence2[7]&0x80)
					RobotMainServer.BNOMode=(byte) ((byte)(sentence2[7]&0x7F)-(byte)(sentence2[7]&0x80));
					RobotMainServer.BNOCalStat=(byte) ((byte)(sentence2[8]&0x7F)-(byte)(sentence2[8]&0x80));
					RobotMainServer.BNOSysStat=(byte) ((byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80));
					RobotMainServer.BNOSysError=(byte) ((byte)(sentence2[11]&0x7F)-(byte)(sentence2[11]&0x80));
	//					int oct0=(byte)(sentence2[ix+3*i]&0x7F)-(byte)(sentence2[ix+3*i]&0x80); // manip car byte consideré signé
	//					int oct1=(byte)(sentence2[ix+1+3*i]&0x7F)-(byte)(sentence2[ix+1+3*i]&0x80);
				mess=" BNO status mode 0x:"+byteToHex(RobotMainServer.BNOMode)+" calibration status 0x:"+byteToHex(RobotMainServer.BNOCalStat)+" system status 0x:"+byteToHex(RobotMainServer.BNOSysStat)+" system error 0x:"+byteToHex(RobotMainServer.BNOSysError);
				Trace.TraceLog(pgmId,mess);
				Fenetre2.RefreshBNO();
				}
				if(sentence2[6]==RobotMainServer.respNarrowPathMesurments)
				{
					mess=" distances:";
					RobotMainServer.pendingNarrowPathMesurments=false;
					for (int i=0;i<5;i++)
					{
						RobotMainServer.pathDistances[i]=(byte) ((byte)(sentence2[7+3*i]&0x7F))*256;
						RobotMainServer.pathDistances[i]=RobotMainServer.pathDistances[i]+(byte) ((byte)(sentence2[8+3*i]&0x7F));
						mess=mess+RobotMainServer.pathDistances[i]+"/";
					}
					byte lastStepId=(byte) ((byte)(sentence2[22]&0x7F));
					byte traceStepId=(byte) ((byte)(sentence2[24]&0x7F));
					byte interruptStepId=(byte) ((byte)(sentence2[25]&0x7F));
					mess=mess+" lastStepID: 0x"+byteToHex(lastStepId)+" traceStepId: 0x"+byteToHex(traceStepId)+" interruptStepId: 0x"+byteToHex(interruptStepId);
					Trace.TraceLog(pgmId,mess);
				}
				if(sentence2[6]==RobotMainServer.respNarrowPathEchos)
				{
					RobotMainServer.pendingNarrowPathEchos=false;
					for (int i=0;i<9;i++)
					{
						mess=" path echos:";
						RobotMainServer.pathEchos[2*i]=(byte) ((byte)(sentence2[7+3*i]&0x7F));
						RobotMainServer.pathEchos[2*i+1]=(byte) ((byte)(sentence2[7+3*i+1]&0x7F));
						mess=mess+RobotMainServer.pathEchos[2*i]+"/"+RobotMainServer.pathEchos[2*i+1];
						Trace.TraceLog(pgmId,mess);
					}

					Trace.TraceLog(pgmId,mess);
				}
				if(sentence2[6]==0x80)
				{
					System.out.print("subsystem registers ");
					int i=0;
//					int idx=8;
					for (i=0;i<sentence2[7];i++)
					{
	//					int oct0=(byte)(sentence2[ix+3*i]&0x7F)-(byte)(sentence2[ix+3*i]&0x80); // manip car byte consideré signé
	//					int oct1=(byte)(sentence2[ix+1+3*i]&0x7F)-(byte)(sentence2[ix+1+3*i]&0x80);
						System.out.println(" register: 0x"+byteToHex(sentence2[8+3*i])+" value:00x"+byteToHex(sentence2[9+3*i]));
					}
				}
				if(sentence2[6]==RobotMainServer.respTrace)
				{

					int power1=(sentence2[7] & 0x000000ff)*10;
					int power2=(sentence2[8] & 0x000000ff)*10;;
					int power3=(sentence2[10] & 0x000000ff)*10;
					int power4=(sentence2[11] & 0x000000ff)*10;
					int power5=(sentence2[13] & 0x000000ff)*10;
					int power6=(sentence2[14] & 0x000000ff)*10;
					RobotMainServer.voltage1=power1;
					RobotMainServer.voltage2=power2;
					RobotMainServer.voltage3=power3;
					RobotMainServer.voltage4=power4;
					RobotMainServer.voltage5=power5;
					RobotMainServer.voltage6=power6;
					int leftEncoder=((sentence2[16] << 8) & 0x0000ff00) | (sentence2[17] & 0x000000ff);
					int rightEncoder=((sentence2[19] << 8) & 0x0000ff00) | (sentence2[20] & 0x000000ff);
					mess="power 1:"+power1+" 2:"+power2+" 3:"+power3+" 4:"+power4+" 5:"+power5+" 6:"+power6+ " leftEncoder:"+leftEncoder+ " rightEncoder:"+rightEncoder;
			//		Trace.TraceLog(pgmId,mess);
				}
				if(sentence2[6]==RobotMainServer.respTraceNO)
				{
					int NO=((sentence2[7] << 8) & 0x0000ff00) | (sentence2[8] & 0x000000ff);
					RobotMainServer.northOrientation=NO;

					RobotMainServer.BNOMode=(byte) ((byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80));
					RobotMainServer.BNOCalStat=(byte) ((byte)(sentence2[11]&0x7F)-(byte)(sentence2[11]&0x80));
					RobotMainServer.BNOSysStat=(byte) ((byte)(sentence2[13]&0x7F)-(byte)(sentence2[13]&0x80));
					RobotMainServer.BNOSysError=(byte) ((byte)(sentence2[14]&0x7F)-(byte)(sentence2[14]&0x80));
					int AO=((sentence2[16] << 8) & 0x0000ff00) | (sentence2[17] & 0x000000ff);
					RobotMainServer.absoluteOrientation=AO;
					mess="North orientation:" + NO + " abs Orientation: "+RobotMainServer.absoluteOrientation+ " BNOMode:"+byteToHex(RobotMainServer.BNOMode) + " BNOCalStat:"+byteToHex(RobotMainServer.BNOCalStat) +" BNOSysStat:"+byteToHex(RobotMainServer.BNOSysStat) +" BNOSysError:"+byteToHex(RobotMainServer.BNOSysError);
					Trace.TraceLog(pgmId,mess);
					Fenetre2.RefreshBNO();
					Fenetre2.RefreshHardPosition();
				}
				if (sentence2[6]==RobotMainServer.respVersion){  // version info
					int version=(sentence2[7] & 0x000000ff);
					RobotMainServer.arduinoVersion=version;
					int subVersion=(sentence2[8] & 0x000000ff);
					RobotMainServer.arduinoSubVersion=subVersion;
					mess="Arduino version:" + version + "-"+subVersion;
					Trace.TraceLog(pgmId,mess);
				}
				if (sentence2[6]==RobotMainServer.respPID){  // version info
					float Kp=(byte) ((byte)(sentence2[7]&0x7F)-(byte)(sentence2[7]&0x80))*256;
					Kp=Kp+(byte) ((byte)(sentence2[8]&0x7F)-(byte)(sentence2[8]&0x80));
					float Ki=(byte) ((byte)(sentence2[10]&0x7F)-(byte)(sentence2[10]&0x80))*256;
					Ki=Ki+(byte) ((byte)(sentence2[11]&0x7F)-(byte)(sentence2[11]&0x80));
					float Kd=(byte) ((byte)(sentence2[13]&0x7F)-(byte)(sentence2[13]&0x80))*256;
					Kd=Kd+(byte) ((byte)(sentence2[14]&0x7F)-(byte)(sentence2[14]&0x80));
					mess="PID Kp:" + Kp/100 + " Ki:"+Ki/100+ " Kd:"+Kd/100;
					Trace.TraceLog(pgmId,mess);
					int leftMin=(byte) (byte)(sentence2[16]&0x7F)-(byte)(sentence2[16]&0x80);
					int rightMin=(byte) (byte)(sentence2[17]&0x7F)-(byte)(sentence2[17]&0x80);
					int leftMax=(byte) (byte)(sentence2[19]&0x7F)-(byte)(sentence2[19]&0x80);
					int rightMax=(byte) (byte)(sentence2[20]&0x7F)-(byte)(sentence2[20]&0x80);
					int leftStart=(byte) (byte)(sentence2[22]&0x7F)-(byte)(sentence2[22]&0x80);
					int rightStart=(byte) (byte)(sentence2[23]&0x7F)-(byte)(sentence2[23]&0x80);
					mess="Limit left min:" + leftMin + " max:"+leftMax+ " start:"+leftStart;
					Trace.TraceLog(pgmId,mess);
					mess="Limit right min:" + rightMin + " max:"+rightMax+ " start:"+rightStart;
					Trace.TraceLog(pgmId,mess);
					int leftSetpoint=((sentence2[25] << 8) & 0x00007f00) | (sentence2[26] & 0x000000ff);
					int rightSetpoint=((sentence2[28] << 8) & 0x00007f00) | (sentence2[29] & 0x000000ff);

					mess="setPoint left:" + leftSetpoint + " right:"+rightSetpoint;
					Trace.TraceLog(pgmId,mess);
				}
				if ((sentence2[6]&0x7f)==(((byte)RobotMainServer.requestIRsensors)&0x7f)){
					RobotMainServer.obstacleHeading=(int) ((byte)(sentence2[8]&0x7F)-(byte)(sentence2[8]&0x80))*256;
					RobotMainServer.obstacleHeading=RobotMainServer.obstacleHeading+(int) ((byte)(sentence2[9]&0x7F)-(byte)(sentence2[9]&0x80));
					RobotMainServer.IRMap=(byte)(sentence2[11]&0x7F);
					mess="IRSensors heading:"+RobotMainServer.obstacleHeading+" map: 0x"+byteToHex(RobotMainServer.IRMap);
					Trace.TraceLog(pgmId,mess);

				}
			}
			else
			{
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
	void AnalyseRetcode(byte action, byte retCode, byte retCodeDetail1, byte retCodeDetail2,byte retCodeDetail3)
	{
		TraceLog Trace = new TraceLog();
		String pgmid="RobotBatchServer/AnalyseRetCode";
		String mess=" 0x"+byteToHex(action)+" 0x"+byteToHex(retCode)+" 0x"+byteToHex(retCodeDetail1)+" 0x"+byteToHex(retCodeDetail2)+" 0x"+byteToHex(retCodeDetail3);
//		Trace.TraceLog(pgmid,mess);
		switch(action)
		{
		case(RobotMainServer.moveEnd):
			switch(retCode)
			{
				case(RobotMainServer.moveKoDueToObstacle):
				{
					switch(retCodeDetail1 & 0x80) 
					{
						case(0x80):
							 mess="echoSensors moveKoDueToObstacle ";
							Trace.TraceLog(pgmid,mess);
						case(0x00):
							RobotMainServer.obstacleHeading=(int) ((retCodeDetail1 & 0x7F)-(byte)(retCodeDetail1 & 0x80))*256;
							RobotMainServer.obstacleHeading=RobotMainServer.obstacleHeading+(int) ((byte)(retCodeDetail2 &0x7F)-(byte)(retCodeDetail2 &0x80));
							RobotMainServer.IRMap=(byte)(retCodeDetail3&0x7F);
							mess="IRSensors moveKoDueToObstacle heading:"+RobotMainServer.obstacleHeading+" map: 0x"+byteToHex(RobotMainServer.IRMap);
							Trace.TraceLog("RobotBatchServer/AnalyseRetCode",mess);
					}
				}
			
			}
		}
	}
	  public static String byteToHex(byte b) {
		    StringBuilder sb = new StringBuilder();
		    sb.append(Integer.toHexString(b));
		    if (sb.length() < 2) {
		        sb.insert(0, '0'); // pad with leading zero if needed
		    }
		    String hex = sb.toString();
		    return hex;
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
//			Trace.TraceLog(pgmId,sql);
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