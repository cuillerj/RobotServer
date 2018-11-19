import java.io.IOException;



public class StartTensorFlowPrediction {
	static String    CShell="C:/Users/jean/Documents/Donnees/octave/robot/StartTensorFlowPrediction.bat";


/**
 * 
 */

public static Process runProcess(){
	Process rc = null;
	try {
			rc = Runtime.getRuntime().exec(CShell);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return rc;
	
}
}