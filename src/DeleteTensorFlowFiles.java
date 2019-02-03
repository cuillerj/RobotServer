import java.io.IOException;



public class DeleteTensorFlowFiles {
	static String    CShell="C:/Users/jean/Documents/Donnees/octave/robot/DeleteTensorFlowFiles.bat";


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