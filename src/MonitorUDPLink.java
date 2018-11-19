
public class MonitorUDPLink implements Runnable {
	public void run(){
	String pgmId="MonitorUDPLink";
	  String mess="Start";
	  TraceLog Trace = new TraceLog();
	  Trace.TraceLog(pgmId,mess);
	while(true)
		{
		   try {
			Thread.sleep(5000);
			SendUDP.CheckLastFrame();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
}
