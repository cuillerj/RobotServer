import java.io.*;
import java.net.*;
import java.sql.*;
public class EchoRobot extends Thread{
	Thread t;
	public static int pendingEcho=2;
	public static boolean sentEcho=false;
	public void run(){


while (true){

	try{
//		 System.out.println("pending:"+pendingEcho+ " "+sentEcho);
		if (RobotMainServer.ipRobot==null)
		{
			return;
		}
	      if ( pendingEcho>0 && sentEcho==false)
	      {
	    	  DatagramSocket clientSocket = new DatagramSocket();
	//    	  InetAddress IPAddress = InetAddress.getByName(RobotMainServer.ipRobot);

	    	  byte[] sendData = new byte[3];
	    	  String startCmde="c4e";
	    	  sendData = startCmde.getBytes();
	    	  sendData[1]=0x00;
	 //   	  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
	    	  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, RobotMainServer.ipRobot, 8888);
	    	  clientSocket.send(sendPacket);
	    	  pendingEcho=pendingEcho+1;
	    	  sentEcho=true;
	//      System.out.println("send echo:"+pendingEcho);
	    	  clientSocket.close();
	      }
	//	  System.out.println("statut du thread "   +this.getState());
	      if (pendingEcho==1 && sentEcho==true)
	      {
    	  sentEcho=false;
 //   	  pendingEcho++;
		   Thread.sleep(30000);
	      }
	      if (pendingEcho>1 && sentEcho==true)
	      {
	    	sentEcho=false;
//	    	pendingEcho++;
			Thread.sleep(5000);
	      }
	      else
	      {
	    	  pendingEcho++;
			Thread.sleep(15000);
	      }

		     Fenetre.RefreshStat();
//		  System.out.println("statut du thread "   +this.getState());
	   	   
		}
	   catch(Exception e)
	   {}
	   
	   finally{}

	   /*
	      Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			*/
	}
	
		}

	
	}

