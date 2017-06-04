
import java.util.Random;

public class ArduinoSimulator{
	Thread t;
	public static long savedRotation=0;
	public static long savedDistance=0;
	public static long savedNorthOrientation=0;
	public static long savedGyro=0;
	public static String pgmId="Simulator";

	// [reqId,code,timeout,source,dest,retcode,countTimer]
	// source dest 0 java, 1 octave 2 arduino 3 arduino simulator

	public static int  ActionEnd(int reqCode,int reqSource,int reqDest)
	{

		Random rand = new Random();
		int retCode=0;
		if (RobotMainServer.noiseRetCode && reqCode==RobotMainServer.moveEnd)
		{
			retCode=rand.nextInt(RobotMainServer.noiseRetValue); // generate a random return code for the action
			
		}
		if (retCode==1)
		{
			retCode=RobotMainServer.moveWheelSpeedInconsistancy;
		}
		if (retCode==2)
		{
			retCode=RobotMainServer.moveUnderLimitation;
		}
		else if (retCode==3)
		{
			retCode=RobotMainServer.moveKoDueToWheelStopped;
		}
		else if (retCode==4)
		{
			retCode=RobotMainServer.moveKoDueToObstacle;
		}
		else if (retCode==5)
		{
			retCode=RobotMainServer.moveKoDueToNotEnoughSpace;
		}
		else
		{
			retCode=0;
		}
		TraceLog Trace = new TraceLog();
		String mess1="randDom retCode:"+retCode+" reqCode:"+reqCode;
		Trace.TraceLog(pgmId,mess1);

		if (reqCode==RobotMainServer.moveEnd )
		{
			if (retCode == RobotMainServer.moveKoDueToWheelStopped || retCode == RobotMainServer.moveKoDueToObstacle)
				{
				savedDistance=savedDistance/2;
				String mess2="randDom retCode Ko:"+retCode+" dist:"+savedDistance;
				Trace.TraceLog(pgmId,mess2);
				}
			else if (retCode == RobotMainServer.moveKoDueToNotEnoughSpace)
			{
				RobotMainServer.retCodeDetail=(int) (savedDistance*.75);
				savedDistance=0;
				savedRotation=0;
				savedGyro=0;

				String mess2="randDom retCode Ko:"+retCode+" dist:"+savedDistance+" rot:"+savedRotation;
				Trace.TraceLog(pgmId,mess2);			
			}
			else
			{
				retCode=0;	
			}
		    RandomGaussian gaussian = new RandomGaussian();
		    double meanRotation = savedRotation;
		    double meanGyro = savedGyro; 
		    double meanDistance = savedDistance; 
		    double meanNorthOrientation = savedNorthOrientation;
		    double varianceRotation = 3.0f*RobotMainServer.noiseLevel;
		    double varianceGyro = 2.5f*RobotMainServer.noiseLevel;
		    double varianceDistance = 3.0f*RobotMainServer.noiseLevel;
		    double varianceNO = 5.0f*RobotMainServer.noiseLevel;
//			String mess="Arduino move :"+reqCode+ " "+ reqSource +" "+ reqDest;
//			Trace.TraceLog(pgmId,mess);
		    savedRotation=(long) ((gaussian.getGaussian(meanRotation, varianceRotation)));
		    savedDistance=(long) ((gaussian.getGaussian(meanDistance, varianceDistance)));
	    	savedGyro =(long) ((gaussian.getGaussian(meanGyro, varianceGyro)));
			long northOrientation=(long) ((gaussian.getGaussian(meanNorthOrientation, varianceNO)));
			long heading=(RobotMainServer.alpha+savedRotation)%360;
			
			double degrees=heading;
			String mess="noised move result wheels rotation:"+savedRotation+" distance:"+savedDistance+" heading:"+heading+ " north orientation:"+northOrientation+ " gyro:"+savedGyro+" retCode:"+retCode;
			Trace.TraceLog(pgmId,mess);
			double radians=Math.toRadians(degrees);
			double currentH=Math.cos(Math.toRadians(RobotMainServer.hardAlpha));
//		    System.out.println("random:"+gaussian.getGaussian(mean, variance));
			double posCenterX= (RobotMainServer.posX-RobotMainServer.shiftEchoVsRotationCenter*Math.cos(Math.toRadians(currentH)));
			double posCenterY= (RobotMainServer.posY-RobotMainServer.shiftEchoVsRotationCenter*Math.sin(Math.toRadians(currentH)));
			double rotX=posCenterX+Math.cos(radians)*RobotMainServer.shiftEchoVsRotationCenter;
			double rotY=posCenterY+Math.sin(radians)*RobotMainServer.shiftEchoVsRotationCenter;
			int posXnext=(int)(rotX+(Math.cos(radians)*savedDistance));
			int posYnext=(int)(rotY+(Math.sin(radians)*savedDistance));
//			int posXnext=(int) (Math.cos(radians)*savedDistance);					
//			int posYnext=(int) (Math.sin(radians)*savedDistance);
			RobotMainServer.hardPosX=posXnext;
			RobotMainServer.hardPosY=posYnext;
			RobotMainServer.hardAlpha=(int) (heading);
			RobotMainServer.northOrientation=(int) northOrientation;
			RobotMainServer.gyroHeading= (int) savedGyro;
			savedGyro=0;
			savedRotation=0;
			savedDistance=0;
		}
		if ( reqCode==RobotMainServer.northAlignEnd)
		{
			retCode=0;	
		    RandomGaussian gaussian = new RandomGaussian();
		    double meanRotation = savedRotation; 
		    double meanNorthOrientation = savedNorthOrientation;
		    double varianceRotation = 2.0f*RobotMainServer.noiseLevel;
		    double varianceNO = 1.0f*RobotMainServer.noiseLevel;

//			String mess="Arduino move :"+reqCode+ " "+ reqSource +" "+ reqDest;
//			Trace.TraceLog(pgmId,mess);
		    savedRotation=(long) ((gaussian.getGaussian(meanRotation, varianceRotation)));
			long heading=(RobotMainServer.alpha+savedRotation)%360;
			long northOrientation=(long) ((gaussian.getGaussian(meanNorthOrientation, varianceNO)));
			String mess="noised NO result rotation:"+savedRotation+" heading:"+heading+ " north orientation:"+northOrientation;
			Trace.TraceLog(pgmId,mess);
			double degrees=heading;
			double radians=Math.toRadians(degrees);

//		    System.out.println("random:"+gaussian.getGaussian(mean, variance));
			int posXnext=(int) (Math.cos(radians)*savedDistance);					
			int posYnext=(int) (Math.sin(radians)*savedDistance);
			RobotMainServer.hardPosX=posXnext+RobotMainServer.posX;
			RobotMainServer.hardPosY=posYnext+RobotMainServer.posY;
			RobotMainServer.hardAlpha=(int) (heading);
			RobotMainServer.northOrientation=(int) northOrientation;
		}
		if (reqCode==RobotMainServer.robotUpdatedEnd)
		{
			retCode=0;	
			RobotMainServer.hardPosX=RobotMainServer.posX;
			RobotMainServer.hardPosY=RobotMainServer.posY;
			RobotMainServer.hardAlpha=RobotMainServer.alpha;
			String mess="Arduino robot updated posX:" + RobotMainServer.hardPosX + " posY:" + RobotMainServer.hardPosY+ " heading:" + RobotMainServer.hardAlpha+ " gyroHeading:" + RobotMainServer.gyroHeading+ " retCode:"+retCode + " prob:" + RobotMainServer.currentLocProb;
			Trace.TraceLog(pgmId,mess);
		}
		if (reqCode==RobotMainServer.robotInfoUpdated)
		{
			retCode=0;	
		}
		if (reqCode==RobotMainServer.pingFBEnd)
		{
			retCode=90;	
		}
		return retCode;
		
	}
	public static void SaveMoveRequest(long ang,long mov)
	{
		savedRotation=ang;
		savedDistance=mov;
//		savedGyro=0;
		TraceLog Trace = new TraceLog();
		String mess="Arduino move simulation request:"+savedRotation+ " "+ savedDistance ;
		Trace.TraceLog(pgmId,mess);
	}
	public static void SaveGyroRequest(long ang)
	{
//		savedRotation=0;
		savedRotation=ang;
		savedGyro=ang;
		TraceLog Trace = new TraceLog();
		String mess="Arduino Gyro simulation request:"+savedGyro ;
		Trace.TraceLog(pgmId,mess);
	}
	public static void SaveNorthAlignRequest(long ang)
	{
		savedNorthOrientation=(ang+savedNorthOrientation)%360;
//		savedGyro=0;
		TraceLog Trace = new TraceLog();
		String mess="Arduino North Align simulation request:"+savedNorthOrientation ;
		Trace.TraceLog(pgmId,mess);
	}
}