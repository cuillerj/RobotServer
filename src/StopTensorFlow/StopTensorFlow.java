package StopTensorFlow;

import java.io.IOException;

public class StopTensorFlow {
	static String    CShell="C:/Users/jean/Documents/Donnees/octave/robot/StopTensorFlow.bat";
	public static Process runProcess(){
		Process rc = null;
		try {
				rc = Runtime.getRuntime().exec(CShell);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	System.exit(0);
		return rc;
		
	}
}
