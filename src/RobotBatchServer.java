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
	public void run()
	{
		RobotMainServer.GetLastScanID();
		System.out.println("lastid:"+RobotMainServer.idscanG+" "+RobotMainServer.posXG+ " "+RobotMainServer.posYG);

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
}
		
//				System.out.print(" ID " + ID+ " len "+InpLen+" ");
		//			System.out.println("RECEIVED length: " + sentence2.length);
//				System.out.println(" RECEIVED: " +sentence2[7]);

				int idx=Type;
				if (sentence2[6]==0x01){ // scan -test InpLen contournement bug && ( InpLen==10)

//					System.out.print( " n°:"+trameNumber);
					EchoRobot.pendingEcho=0;
//				String FNam=FNamD+idx+".txt";  // modifie le nom du fichier log en ajoutant le type de la station emettrice
			    java.util.Date today = new java.util.Date();
			    //System.out.println(" - time:"+new java.sql.Timestamp(today.getTime()));
//			    System.out.println(" - time: "+today);
				 try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				InetAddress IPAddress = receivePacket.getAddress();
			      DatagramSocket clientSocket = null;
				try {
					clientSocket = new DatagramSocket();
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//			      InetAddress IPAddress = InetAddress.getByName("192.168.1.99");
			      byte[] sendData = new byte[4];
			      sendData[0]=0x63;
			      sendData[1]=0x34;
			      sendData[2]=0x61;
			      sendData[3]=sentence2[7];
//				      String startCmde="c4a";
//			      sendData = startCmde.getBytes();
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
			      try {
					clientSocket.send(sendPacket);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			     clientSocket.close();
				int port = receivePacket.getPort();
				int i2=9;
				int oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consideré signé
				int oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
				int distFront=256*oct0+oct1;
				i2=12;
				 oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consideré signé
				 oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
				int distBack=256*oct0+oct1;
				i2=15;
				 oct0=(byte)(sentence2[i2]&0x7F)-(byte)(sentence2[i2]&0x80); // manip car byte consideré signé
				 oct1=(byte)(sentence2[i2+1]&0x7F)-(byte)(sentence2[i2+1]&0x80);
				int angle=256*oct0+oct1;
				if (RobotMainServer.scanStepCount!=0)
				{
					RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][0]=angle;
					RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][1]=distFront;
					RobotMainServer.scanArray[(RobotMainServer.scanStepCount-1)%15][2]=distBack;
					RobotMainServer.scanStepCount++;
				}
//				PanneauGraphique.point(22,22);
//				ihm2.setAlwaysOnTop(true);
//				ihm2.setLocation(20,20);
		//		ihm.setTitle("robot v0 - time: "+today);
//				ihm2.setSize("pour voir");
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

		//			int posX=posXG;
		//			int posY=posYG;
					int angl=RobotMainServer.orientG;
	//				System.out.println("pos:"+RobotMainServer.posXG+ " "+RobotMainServer.posYG);
					String sql="INSERT INTO scanResult VALUES ("+idscan+",now(),"+RobotMainServer.posX+","+RobotMainServer.posY+","+angle+","+distFront+","+distBack+","+angl+","+RobotMainServer.idCarto+")";
					//System.out.println("ind id "+IndIdS+", pos " + IndPos + ", len: " + IndLen+" value"+IndValue);
					System.out.println(sql);
					stmtI.executeUpdate(sql);

						
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try { if (stmtR != null) stmtR.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if (stmtI != null) stmtI.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				}
				lastTrameNumber=sentence2[15];
	}
		  		PanneaugraphiqueSonar.point(RobotMainServer.posXG,RobotMainServer.posYG,RobotMainServer.orientG,distFront,distBack,angle);
	  		ihm3.repaint();
				}
				if (sentence2[6]==0x65){                    // e echo
					EchoRobot.pendingEcho=0;
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

						ihm.MajRobotStat("scan running");

					}
					if (sentence2[8]==0x67){
	//					System.out.print("scan ended ");
						if (RobotMainServer.runningStatus==2 || RobotMainServer.runningStatus==102)
						{
							RobotMainServer.runningStatus=103;
						}
						ihm.MajRobotStat("scan ended");
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
					    RobotMainServer.scanStepCount=0;
//						System.out.print("move ended");
						RobotMainServer.actStat=0x03;
					}
					if (sentence2[8]==0x6a){

						if (RobotMainServer.runningStatus==6 )
						{
							RobotMainServer.runningStatus=106;
						}
						ihm.MajRobotStat("aligning");
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
					ihm.MajRobotDiag("0x"+sbb1+" 0x"+sbb2+" 0x"+sbb3+" 0x"+sbb4);
	//				System.out.println(" diagPower:0x"+sbb1+"  Motors:0x"+sbb2+"  Connections:0x"+sbb3+"  Robot:0x"+sbb4);
					
					//
					int oct0=(byte)(sentence2[15]&0x7F)-(byte)(sentence2[15]&0x80); // manip car byte consideré signé
					int oct1=(byte)(sentence2[16]&0x7F)-(byte)(sentence2[16]&0x80);
					RobotMainServer.hardPosX=256*oct0+oct1;
					if (sentence2[14]==0x2d)
					{
						RobotMainServer.hardPosX=-RobotMainServer.hardPosX;
					}
					oct0=(byte)(sentence2[18]&0x7F)-(byte)(sentence2[18]&0x80); // manip car byte consideré signé
					oct1=(byte)(sentence2[19]&0x7F)-(byte)(sentence2[19]&0x80);
					RobotMainServer.hardPosY=256*oct0+oct1;
					if (sentence2[17]==0x2d)
					{
						RobotMainServer.hardPosY=-RobotMainServer.hardPosY;
					}
					oct0=(byte)(sentence2[21]&0x7F)-(byte)(sentence2[21]&0x80); // manip car byte consideré signé
					oct1=(byte)(sentence2[22]&0x7F)-(byte)(sentence2[22]&0x80);
					RobotMainServer.hardAlpha=256*oct0+oct1;
					if (sentence2[20]==0x2d)
					{
						RobotMainServer.hardAlpha=-RobotMainServer.hardAlpha;
					}
					oct0=(byte)(sentence2[24]&0x7F)-(byte)(sentence2[24]&0x80); // manip car byte consideré signé
					oct1=(byte)(sentence2[25]&0x7F)-(byte)(sentence2[25]&0x80);
					RobotMainServer.northOrientation=256*oct0+oct1;
					if ( RobotMainServer.hardJustReboot==false && RobotMainServer.serverJustRestarted==true)
					{
						oct0=(byte)(sentence2[26]&0x7F)-(byte)(sentence2[27]&0x80);
						RobotMainServer.currentLocProb=oct0;
						RobotMainServer.serverJustRestarted=false;
					}
	//			ihm2.ValidePosition(RobotMainServer.hardPosX,RobotMainServer.hardPosY,RobotMainServer.hardAlpha);
//					System.out.println("posX:"+RobotMainServer.posX+ " posY:"+RobotMainServer.posY+" angle:"+ RobotMainServer.alpha);
				if 	(RobotMainServer.octaveRequestPending==true && RobotMainServer.octavePendingRequest==1)
				{
					RobotMainServer.octavePendingRequest=0;
					RobotMainServer.octaveRequestPending=false;
				}
						
				}
				if (sentence2[6]==0x66){                    // scan en cours
					EchoRobot.pendingEcho=0;
	//				System.out.println("scan running");
					ihm.MajRobotStat("scan running");
					RobotMainServer.runningStatus=1;
				}
				if (sentence2[6]==0x67){                    // scan en cours
					EchoRobot.pendingEcho=0;
//					System.out.println("scan ended");
					ihm.MajRobotStat("scan ended");
					RobotMainServer.runningStatus=2;
				}
				if (sentence2[6]==0x68){                    // scan en cours
					EchoRobot.pendingEcho=0;
//					System.out.println("moving");
					ihm.MajRobotStat("moving");
					int ang=ihm.ang();
					int mov=ihm.mov();
					ihm2.PosActualise(ang,mov);
					RobotMainServer.runningStatus=3;
					RobotMainServer.actStat=0x02;   // move en cours
				}
				if (sentence2[6]==0x69){                    // scan en cours
					EchoRobot.pendingEcho=0;
					if (RobotMainServer.actStat==0x01){   // on a rate le statut moving
						int ang=ihm.ang();
						int mov=ihm.mov();
						ihm2.PosActualise(ang,mov);
						RobotMainServer.actStat=0x02; 
					}
//					System.out.println("move ended");
					ihm.MajRobotStat("move ended");
					RobotMainServer.runningStatus=4;
					if (RobotMainServer.actStat==0x02){ 
						ihm2.ValidePosition(RobotMainServer.posX,RobotMainServer.posY,RobotMainServer.alpha);
						
		//				PanneauGraphique.point(200+posXG/10,200+posYG/10);
		//				graph.repaint();
					}
					RobotMainServer.actStat=0x03;
				}
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
			   }
	}
	
}
