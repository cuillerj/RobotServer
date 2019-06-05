import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DecodeUDPFrame {
//	static byte supportedFrameFlagsArray [][]= new byte[256][256]; // first position inside the frame second flag value third 0 not supported 
//	final static String fileName="C:/Users/jean/Documents/Donnees/octave/robot/DecodeFrame.txt";
	
	static boolean appendFlag=false;
	public static int flagPosition=6;
	public static class OutputStream{
	}
	
	public static void PrintUDPFrame(byte[] input, int dataLenght) throws IOException
	{

		if(RobotMainServer.frameDecodeLevel==0)
		{
			return;
		}
		 java.util.Date date= new java.util.Date();
		try {
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			FileWriter fileName = new FileWriter("C:/Users/jean/Documents/Donnees/octave/robot/DecodeFrame.txt", appendFlag); //Set true for append mode
			PrintWriter outFile = new PrintWriter(fileName);
			appendFlag=true;
			if(RobotMainServer.frameDecodeLevel>=2)
				outFile.println();
				{
				outFile.print(timeStamp+" 0x: ");
				for (int i=0;i<dataLenght;i++)
				{
					outFile.print(RobotBatchServer.byteToHex(input[i]));
					outFile.print(" ");
				}
				outFile.println();

				}
			if(RobotMainServer.frameDecodeLevel>=2)
			{
				switch(input[flagPosition])
				{
					case (byte) 0x65:
					{
						outFile.println("*** status:"+RobotBatchServer.byteToHex(input[flagPosition]));
						outFile.print("\tappStat:"+RobotBatchServer.byteToHex(input[flagPosition+1]));
						outFile.print(" actStat:"+RobotBatchServer.byteToHex(input[flagPosition+2]));
						outFile.println(DecodeAction(input[flagPosition+2]));
						outFile.print("\tdiag power:"+RobotBatchServer.byteToHex(input[flagPosition+3]));
						outFile.print(DecodeDiagPower(input[flagPosition+3]));
						outFile.print(" *motor:"+RobotBatchServer.byteToHex(input[flagPosition+5]));
						outFile.print(DecodeDiagMotors(input[flagPosition+5]));
						outFile.print(" *robot:"+RobotBatchServer.byteToHex(input[flagPosition+7]));
						outFile.println(DecodeDiagRobot(input[flagPosition+7]));
						outFile.print("\tsubsystem status:"+RobotBatchServer.byteToHex(input[flagPosition+6]));
						outFile.print(DecodeSubSystemStatus(input[flagPosition+6]));
						outFile.print(" *BNO Mode:"+RobotBatchServer.byteToHex(input[flagPosition+17]));
						outFile.println(DecodeBNOMode(input[flagPosition+17]));
						outFile.print("\ttoDo:"+RobotBatchServer.byteToHex(input[flagPosition+23]));
						outFile.println(DecodeToDo(input[flagPosition+23]));
						outFile.print("\tpending action:"+RobotBatchServer.byteToHex(input[flagPosition+25]));
						outFile.println(DecodePendingAction(input[flagPosition+25]));
						outFile.print("\twait flag:"+RobotBatchServer.byteToHex(input[flagPosition+27]));
						outFile.println(" last received frame:"+RobotBatchServer.byteToHex(input[flagPosition+28]));
						break;
					}
					case (byte) 0x75:
					{
						outFile.println("*** BNO status:"+RobotBatchServer.byteToHex(input[flagPosition]));
						outFile.print("\tMode:"+RobotBatchServer.byteToHex(input[flagPosition+1]));
						outFile.println(DecodeBNOMode(input[flagPosition+1]));
						outFile.print("\tcalibration status:"+RobotBatchServer.byteToHex(input[flagPosition+2]));
						outFile.println(DecodeCalibrationStatus(input[flagPosition+2]));				
						outFile.print("\tsystem status:"+RobotBatchServer.byteToHex(input[flagPosition+4]));
						outFile.print(DecodeSystemStatus(input[flagPosition+4]));				
						outFile.println(" system error:"+RobotBatchServer.byteToHex(input[flagPosition+5]));
						break;
					}
					case (byte) 0x97:
						outFile.println("*** internal flag:"+RobotBatchServer.byteToHex(input[flagPosition]));
						outFile.print("\ttoDo:"+RobotBatchServer.byteToHex(input[flagPosition+2]));
						outFile.print(" "+DecodeAction(input[flagPosition+2]));
						outFile.print(" detail:"+RobotBatchServer.byteToHex(input[flagPosition+3]));
						outFile.println(" "+DecodeToDoDetail(input[flagPosition+3]));			
						outFile.print("\tdiag connection:"+RobotBatchServer.byteToHex(input[flagPosition+5]));
						outFile.println(" "+DecodeDiagConnection(input[flagPosition+5]));
						outFile.print("\tIR detection:"+RobotBatchServer.byteToHex(input[flagPosition+6]));
						outFile.println(" "+DecodeIRDetection(input[flagPosition+6]));					
						outFile.print("\twaitFlag:"+RobotBatchServer.byteToHex(input[flagPosition+8]));
						outFile.println(" "+DecodeWaitFlag(input[flagPosition+8]));	
						outFile.print("\tcurrent move:"+RobotBatchServer.byteToHex(input[flagPosition+9]));
						outFile.println(" "+DecodeAction(input[flagPosition+9]));
						outFile.print("\tpending action:"+RobotBatchServer.byteToHex(input[flagPosition+11]));
						outFile.println(" "+DecodeAction(input[flagPosition+11]));
						outFile.print("\tpending polling response:"+RobotBatchServer.byteToHex(input[flagPosition+12]));
						outFile.println(" "+DecodePendingPollingResp(input[flagPosition+12]));
						outFile.print("\tgyroscope upToDate:"+RobotBatchServer.byteToHex(input[flagPosition+14]));
						outFile.println(" "+DecodeGyroUpToDate(input[flagPosition+14]));
						outFile.print("\tend move to send:"+RobotBatchServer.byteToHex(input[flagPosition+15]));
						outFile.println(" "+DecodeEndMoveToSend(input[flagPosition+15]));
						outFile.print("\tnorth aligned:"+RobotBatchServer.byteToHex(input[flagPosition+17]));
						outFile.println(" "+DecodeNorthAligned(input[flagPosition+17]));
						outFile.print("\tBNO init location:"+RobotBatchServer.byteToHex(input[flagPosition+18]));
						outFile.println(" "+DecodeStepInitBNOLocation(input[flagPosition+18]));
						outFile.print("\tget BNO location:"+RobotBatchServer.byteToHex(input[flagPosition+20]));
						outFile.println(" "+DecodeGetBNOLocation(input[flagPosition+20]));
						outFile.print("\tBNO Mode:"+RobotBatchServer.byteToHex(input[flagPosition+21]));
						outFile.println(DecodeBNOMode(input[flagPosition+21]));
						outFile.print("\tPID Mode:"+RobotBatchServer.byteToHex(input[flagPosition+23]));					
						outFile.println(DecodePIDMode(input[flagPosition+23]));
						outFile.print("\treboot phasee:"+RobotBatchServer.byteToHex(input[flagPosition+24]));
						outFile.println(DecodeStepRebootPhase(input[flagPosition+24]));
						outFile.print("\tretry Count:"+RobotBatchServer.byteToHex(input[flagPosition+26]));
						outFile.print(" last ack frame:"+RobotBatchServer.byteToHex(input[flagPosition+27]));
						outFile.println(" last received frame:"+RobotBatchServer.byteToHex(input[flagPosition+28]));
						break;

				}
			}

			outFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void InitTable()
	{
		//OutputStream.Println("Decode UDP frame started");
	//	OutputStream.Flush();

		System.out.println("start decode UDP");

	}
	public static String DecodeToDo(byte action)
	{
		final byte toDoScan = 0b00000001;    // toDo bit for scan request
		final byte toDoMove = 0b00000010;     // some move  to do
		final byte toDoRotation = 0b00000100;   // rotation to do
		final byte toDoStraight = 0b00001000;  // straight move to do
		final byte toDoBackward = 0b00010000; // straight move to do is backward
		final byte toDoClockwise = 0b00100000;// rotate clockwise
		final byte toDoAlign = 0b01000000;     // north aligning
		final byte toDoPingFB = (byte) 0b10000000;    // echo ping front back
		String action_S=">>";

			if((action & toDoScan)==toDoScan){
				action_S=action_S+" echoScan";				
			}
			if((action & toDoPingFB)==toDoPingFB){
				action_S=action_S+" ping_front_back";				
			}
			if((action & toDoAlign)==toDoAlign){
				action_S=action_S+" north_align";			
			}
			if((action & toDoMove)==toDoMove){
				action_S=action_S+" move";			
			}
			if((action & toDoRotation)==toDoRotation){
				action_S=action_S+" rotate";			
			}
			if((action & toDoBackward)==toDoBackward){
				action_S=action_S+" backward";			
			}
			if((action & toDoStraight)==toDoStraight){
				action_S=action_S+" straight";			
			}
			if((action & toDoClockwise)==toDoClockwise){
				action_S=action_S+" clockwise";			
			}			
	
		return action_S;
	}
	public static String DecodeAction(byte action)
	{

		String action_S=">>"+RobotMainServer.actionList[action & 0x000000ff];	
	
		return action_S;
	}
	public static String DecodePendingAction(byte action)
	{
		final byte leftLMotor = 0b00000001;    // left motor running
		final byte rightMotor = 0b00000010;     // right motor running

		String action_S=">>";

			if((action & leftLMotor)==leftLMotor){
				action_S=action_S+" left_motor_running";				
			}
			if((action & rightMotor)==rightMotor){
				action_S=action_S+" right_motor_running";				
			}		
		return action_S;
	}
	public static String DecodeBNOMode(byte BNOMode)
	{
		final byte IMUPLUS = 0x08;    // 
		final byte COMPASS = 0x09;     // 
		final byte NDOF = 0x0c;   //
		String action_S=">>";
		switch (BNOMode)
		{
			case (IMUPLUS):
				action_S=action_S+" IMUPLUS";
				break;
			case (COMPASS):
				action_S=action_S+" COMPASS";
				break;
			case (NDOF):
				action_S=action_S+" NDOF";
				break;
			
		}	
		return action_S;
	}
	public static String DecodeSubSystemStatus(byte status)
	{
		final byte monitGyroStatusBit = 0x01;    // 
		final byte monitMagnetoStatusBit = 0x02;     // 
		final byte I2CConnectionBit = 0x04;   //
		String status_S=">>";
		if ((status&monitGyroStatusBit)==0x01)
		{
			status_S=status_S+" gyroscope";
		}
		if ((status&monitMagnetoStatusBit)==0x02)
		{
			status_S=status_S+" magneto";
		}
		if ((status&I2CConnectionBit)==0x04)
		{
			status_S=status_S+" I2C_connected";
		}
		else{
			status_S=status_S+" I2C_not_connected";
		}
		return status_S;
	}
	public static String DecodeToDoDetail(byte status)
	{
		final byte toDoGyroRotation = 0x01;    // 
		final byte toDoMoveAcrossPass = 0x02;     // 
		final byte toDoAlignRotate = 0x04;   //
		final byte toDoAlignUpdateNO = 0x08;   //
		String status_S=">>";
		if ((status&toDoGyroRotation)==0x00)
		{
			status_S=status_S+" none";
		}
		if ((status&toDoGyroRotation)==0x01)
		{
			status_S=status_S+" gyro_rotation";
		}
		if ((status&toDoMoveAcrossPass)==0x02)
		{
			status_S=status_S+" move_across_pass";
		}
		if ((status&toDoAlignRotate)==0x04)
		{
			status_S=status_S+" align_rotate";
		}
		if ((status&toDoAlignUpdateNO)==0x08)
		{
			status_S=status_S+" align_update";
		}
		return status_S;
	}
	public static String DecodeWheelInterruption(byte status)
	{
		String status_S=">> interruption ";
		if (status == -1)
		{
			status_S=status_S+"completed";
		}
		else{
			status_S=status_S+"pending:"+status;
		}
		return status_S;
	}
	public static String DecodeIRDetection(byte status)
	{
		String status_S=">> ";
		if (status==0x00)
		{
			status_S=status_S+"inactive";	
		}
		else{
			status_S=status_S+"active";		
		}
		return status_S;
	}
	public static String DecodeWaitFlag(byte status)
	{
		String status_S=">> waiting_for ";
		if (status==0x00)
		{
			status_S=status_S+"none";	
		}
		else{
			if ((status&0x01)==0x01)
			{
				status_S=status_S+" start_moving";
			}
			if ((status&0x01)==0x02)
			{
				status_S=status_S+" restart_moving";
			}
			if ((status&0x01)==0x02)
			{
				status_S=status_S+" pause_end";
			}
		}
		return status_S;
	}
		public static String DecodePendingPollingResp(byte status)
		{
			String status_S=">> ";
			if (status==0x00)
			{
				status_S=status_S+"none";	
			}
			else{
				status_S=status_S+"yes";		
			}
			return status_S;
		}		
		public static String DecodeGyroUpToDate(byte status)
		{
			String status_S=">> gyroscope ";
			if (status==0x00)
			{
				status_S=status_S+"upToDate";	
			}
			else{
				status_S=status_S+"pending_step:"+status;		
			}
			return status_S;
		}
		public static String DecodeEndMoveToSend(byte status)
		{
			String status_S=">> ";
			if (status==0x00)
			{
				status_S=status_S+"none";	
			}
			else{
				status_S=status_S+"yes";		
			}
			return status_S;
		}	
		
		public static String DecodeNorthAligned(byte status)
		{
			String status_S=">> ";
			if (status==0x00)
			{
				status_S=status_S+"no";	
			}
			else{
				status_S=status_S+"yes";		
			}
			return status_S;
		}
		public static String DecodeStepInitBNOLocation(byte status)
		{
			String status_S=">> ";
			if (status==0x00)
			{
				status_S=status_S+"completed";	
			}
			else{
				status_S=status_S+" step:"+status;		
			}
			return status_S;
		}
		
		public static String DecodeGetBNOLocation(byte status)
		{
			String status_S=">> ";
			if (status==0x00)
			{
				status_S=status_S+"completed";	
			}
			else{
				if (status==-1)
				{
					status_S=status_S+"none";	
				}
				else{
					status_S=status_S+" step:"+status;					
				}
				status_S=status_S+" step:"+status;		
			}
			return status_S;
		}
		public static String DecodePIDMode(byte status)
		{
			String status_S=">> ";
			if (status==0x00)
			{
				status_S=status_S+"On";	
			}
			else{
				status_S=status_S+"Off";		
			}
			return status_S;
		}
		public static String DecodeStepRebootPhase(byte status)
		{
			String status_S=">> ";
			if (status==0x00)
			{
				status_S=status_S+"completed";	
			}
			else{
				status_S=status_S+" step:"+status;		
			}
			return status_S;
		}
		public static String DecodeCalibrationStatus(byte status)
		{
			String status_S=">> ";
			int value = ((byte) (((byte) 0b11000000 )& status)>>6)& 0x00000003; 
			status_S=status_S+"system:"+value;	
			value = ((byte) (((byte) 0b00110000 )& status)>>4)& 0x00000003; 
			status_S=status_S+" gyroscope:"+value;
			value = ((byte) (((byte) 0b00001100 )& status)>>2)& 0x00000003; 
			status_S=status_S+" accelerometer:"+value; 
			value = ((byte) (((byte) 0b00000011 )& status))& 0x00000003; 
			status_S=status_S+" magnetometer:"+value;	
			return status_S;
		}
		public static String DecodeSystemStatus(byte status)
		{
			String status_S=">>";
			byte value = (byte) (((byte) 0b01000000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+ "without_fusion";					
			}
			value = (byte) (((byte) 0b00100000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" fusion_running";
			}
			value = (byte) (((byte) 0b00010000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" executing_self_test";	
			}
			value = (byte) (((byte) 0b00001000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" system_initialization";
			}
			value = (byte) (((byte) 0b00000100 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" initialization_periphericals";
			}
			value = (byte) (((byte) 0b00000010 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" system_error";
			}
			value = (byte) (((byte) 0b00000001 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" iddle";
			}
			return status_S;
		}
		
		public static String DecodeDiagPower(byte status)
		{
			String status_S=">> issue:";
			if (status==0x00)
			{
				status_S=status_S+ "none";	
			}
			byte value = (byte) (((byte) 0b00000001 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+ " input_AtMega";					
			}
			value = (byte) (((byte) 0b00000010 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" input_Esp8266";
			}
			value = (byte) (((byte) 0b00000100 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" output_to_electronic";	
			}
			value = (byte) (((byte) 0b00001000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" input_servo_motor";
			}
			value = (byte) (((byte) 0b00010000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" ouput_to_motors";
			}
			value = (byte) (((byte) 0b00100000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" input_motord";
			}
			return status_S;
		}
		public static String DecodeDiagMotors(byte status)
		{
			byte diagMotorPbLeft=(byte) 0b00000001;
			byte diagMotorPbRight=(byte) 0b00000010;
			byte diagMotorPbSynchro=(byte)0b00000100;
			byte diagMotorPbEncoder=(byte)0b00001000;;
			String status_S=">> issue:";
			if (status==0x00)
			{
				status_S=status_S+ "none";	
			}
			byte value = (byte) (((byte) 0b00000001 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+ " left_motor";					
			}
			value = (byte) (((byte) 0b00000010 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" right_motor";
			}
			value = (byte) (((byte) 0b00000100 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" synchronization";	
			}
			value = (byte) (((byte) 0b00001000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" encoder";
			}
			return status_S;
		}
		public static String DecodeDiagConnection(byte status)
		{
			byte diagConnectionIP=(byte) 0b00000001;
			byte diagConnectionI2C=(byte) 0b00000010;
			byte diagConnectionLostInputFrame=(byte)0b00000100;
		    byte diagConnectionDuplicateInputFrame=(byte)0b00001000;
			String status_S=">> issue:";
			if (status==0x00)
			{
				status_S=status_S+ "none";	
			}
			byte value = (byte) (((byte) 0b00000001 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+ " connection-IP";					
			}
			value = (byte) (((byte) 0b00000010 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" connection-I2C";
			}
			value = (byte) (((byte) 0b00000100 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" lost-input-frame";
			}
			value = (byte) (((byte) 0b00001000 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" duplicate-frame";
			}
			return status_S;

		}
		public static String DecodeDiagRobot(byte status)
		{
			String status_S=">> issue:";
			if (status==0x00)
			{
				status_S=status_S+ "none";	
			}
			byte value = (byte) (((byte) 0b00000001 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+ " pause";					
			}
			value = (byte) (((byte) 0b00000010 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" obstacle";
			}
			value = (byte) (((byte) 0b00000100 )& status); 
			if (value!=0x00)
			{
				status_S=status_S+" gyro_rotation";	
			}
			return status_S;
		}
	}



