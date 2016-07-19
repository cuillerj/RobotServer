import java.io.*;
import java.net.*;


public class SendUDP {
//	public String ipRobot="192.168.1.133";  // 138 ou 133
	public void SendUDP() {
		try{
	//	  System.out.println("argument: " + args[0]);
		  System.out.println("sendUDP" );
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
	      String startCmde="c4x";
	      sendData = startCmde.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	      clientSocket.send(sendPacket);
	      clientSocket.close();
	//   System.exit(0);
	   
	  	   
	   
	   
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPStop() {
		try{
	  System.out.println("stop" );
		RobotMainServer.runningStatus=2001; // pending stop
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
	      String startCmde="c4s";
	      sendData = startCmde.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	      clientSocket.send(sendPacket);
	      clientSocket.close();
	//   System.exit(0);
	  
	  	 
	   
	   
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPReset() {
		try{
	  System.out.println("reset" );
		RobotMainServer.runningStatus=2004; // pending reset
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[4];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x72;   // r reset
			 sendData[3]=(byte) 0xff;   // 0xff reset all
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	      clientSocket.send(sendPacket);
	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	
	public void SendUDPStart() {
		// TODO Auto-generated method stub
		try{
			  System.out.println("start" );
				RobotMainServer.runningStatus=2000; // pending start
			      DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[3];
			      String startCmde="c4x";
			      sendData = startCmde.getBytes();
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
			      clientSocket.send(sendPacket);
			      clientSocket.close();
			//   System.exit(0);
			  
			  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendUDPCalibrate() {
		// TODO Auto-generated method stub
		try{
			  System.out.println("calibrate" );
				RobotMainServer.runningStatus=2003; // pending calibrate
			      DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[3];
			      String startCmde="c4w";
			      sendData = startCmde.getBytes();
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
			      clientSocket.send(sendPacket);
			      clientSocket.close();
			//   System.exit(0);
			  
			  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendUDPScan() {
		// TODO Auto-generated method stub
		try{
			  System.out.println("scan" );
				RobotMainServer.runningStatus=1001; // pending scan
			      DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] sendData = new byte[3];
			      String startCmde="c4+";
			      sendData = startCmde.getBytes();
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
			      clientSocket.send(sendPacket);
			      clientSocket.close();
			  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendUDPServoAlign(int value) {
		try{
	  System.out.println("align servo" );
		RobotMainServer.runningStatus=2005; // pending reset
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[4];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x53;   // S align servomotor
			 sendData[3]=(byte) value;   // value in degre between 0 to 180 
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	      clientSocket.send(sendPacket);
	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPPingEchoFrontBack() {
		try{
	  System.out.println("echo ping FB" );
		RobotMainServer.runningStatus=2005; // pending reset
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
//	      String startCmde="c4r";
//	      sendData = startCmde.getBytes();
			 sendData[0]=0x63;  // c
			 sendData[1]=0x34;  // 4
			 sendData[2]=0x70;   // p 

	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	      clientSocket.send(sendPacket);
	      clientSocket.close();  
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	
	public void SendUDPInit(int posX,int posY,int orient,int locProb) {
		// TODO Auto-generated method stub
		try{
//			  System.out.println("init2" );
			RobotMainServer.runningStatus=2000; // pending init
			      DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
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
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
			      clientSocket.send(sendPacket);
			      clientSocket.close();
			  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	public void SendEcho() {
		try{
			System.out.println("send echo" );
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
	      byte[] sendData = new byte[3];
	      String startCmde="c4e";
	      sendData = startCmde.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	      clientSocket.send(sendPacket);
	      clientSocket.close();
  	 	   
	   
		}
	   catch(Exception e)
	   {}
	   
	   finally{}
		}
	public void SendUDPMove(long angle,long move) {
		// TODO Auto-generated method stub
		try{
			RobotMainServer.runningStatus=1003; // pending move
			  System.out.println("angle:"+angle+ " move:"+move );
			      DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] cmde = new byte[15];
			      byte[] sendData = new byte[15];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=0x6d;
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
//		      System.out.println(byteArrayToHex(sendData));
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
			      clientSocket.send(sendPacket);
			      clientSocket.close();
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
			  System.out.println("posX:"+posX+ " posY:"+posY );
			      DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
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
		      System.out.println(byteArrayToHex(sendData));
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
			      clientSocket.send(sendPacket);
			      clientSocket.close();
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
			  System.out.println("align to:"+angle );
			      DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);
			      byte[] cmde = new byte[15];
			      byte[] sendData = new byte[15];
			      cmde[0]=0x63;
			      cmde[1]=0x34;
			      cmde[2]=0x45;  // N command
			      cmde[3]=(byte)(angle/256);
			      cmde[4]=(byte)(angle);

		      sendData = cmde;
//		      System.out.println(byteArrayToHex(sendData));
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
			      clientSocket.send(sendPacket);
			      clientSocket.close();
			//   System.exit(0);
			  		  	 
			   
			   
				}
			   catch(Exception e)
			   {}
			   
			   finally{}
				}
	}
	


