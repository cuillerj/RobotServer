
import java.util.Random;
public class ArduinoSimulator{
	Thread t;
	public static long savedRotation=0;
	public static long savedDistance=0;
	public static long savedNorthOrientation=0;
	public static String pgmId="Simulator";

	// [reqId,code,timeout,source,dest,retcode,countTimer]
	// source dest 0 java, 1 octave 2 arduino 3 arduino simulator

	public static int  ActionEnd(int reqCode,int reqSource,int reqDest)
	{

		Random rand = new Random();
		int retCode=rand.nextInt(11); // generate a random return code for the action
		TraceLog Trace = new TraceLog();
//		String mess1="randDom retCode:"+retCode;
//		Trace.TraceLog(pgmId,mess1);

		if (reqCode==RobotMainServer.moveEnd )
		{
			if (retCode == RobotMainServer.moveKoDueToSpeedInconsistancy || retCode == RobotMainServer.moveKoDueToObstacle)
				{
				savedDistance=savedDistance/2;
				String mess2="randDom retCode Ko:"+retCode+" dist:"+savedDistance;
				Trace.TraceLog(pgmId,mess2);
				}
			else
			{
				retCode=0;	
			}
		    RandomGaussian gaussian = new RandomGaussian();
		    double meanRotation = savedRotation; 
		    double varianceRotation = 3.0f;
		    double meanDistance = savedDistance; 
		    double varianceDistance = 3.0f;
		    double meanNorthOrientation = savedNorthOrientation;
		    double varianceNO = 5.0f;
//			String mess="Arduino move :"+reqCode+ " "+ reqSource +" "+ reqDest;
//			Trace.TraceLog(pgmId,mess);
		    savedRotation=(long) ((gaussian.getGaussian(meanRotation, varianceRotation)));
		    savedDistance=(long) ((gaussian.getGaussian(meanDistance, varianceDistance)));
			long northOrientation=(long) ((gaussian.getGaussian(meanNorthOrientation, varianceNO)));
			long heading=(RobotMainServer.alpha+savedRotation)%360;
			
			double degrees=heading;
			String mess="noised move result rotation:"+savedRotation+" distance:"+savedDistance+" heading:"+heading+ " north orientation:"+northOrientation+ " retCode:"+retCode;
			Trace.TraceLog(pgmId,mess);
			double radians=Math.toRadians(degrees);

//		    System.out.println("random:"+gaussian.getGaussian(mean, variance));
			int posXnext=(int) (Math.cos(radians)*savedDistance);					
			int posYnext=(int) (Math.sin(radians)*savedDistance);
			RobotMainServer.hardPosX=posXnext+RobotMainServer.posX;
			RobotMainServer.hardPosY=posYnext+RobotMainServer.posY;
			RobotMainServer.hardAlpha=(int) (heading);
			RobotMainServer.northOrientation=(int) northOrientation;
		}
		if ( reqCode==RobotMainServer.northAlignEnd)
		{
			retCode=0;	
		    RandomGaussian gaussian = new RandomGaussian();
		    double meanRotation = savedRotation; 
		    double meanNorthOrientation = savedNorthOrientation;
		    double varianceRotation = 2.0f;
		    double varianceNO = 1.0f;

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
			String mess="Arduino robot updated posX:" + RobotMainServer.hardPosX + " posY:" + RobotMainServer.hardPosY+ " heading:" + RobotMainServer.hardAlpha+ " retCode:"+retCode;
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
		TraceLog Trace = new TraceLog();
		String mess="Arduino move simulation request:"+savedRotation+ " "+ savedDistance ;
		Trace.TraceLog(pgmId,mess);
	}
	public static void SaveNorthAlignRequest(long ang)
	{
		savedNorthOrientation=ang;
		TraceLog Trace = new TraceLog();
		String mess="Arduino North Align simulation request:"+savedNorthOrientation ;
		Trace.TraceLog(pgmId,mess);
	}
}