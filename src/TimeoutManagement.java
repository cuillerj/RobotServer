
public class TimeoutManagement extends Thread{

	Thread t;

	// [reqId,code,timeout,source,dest,retcode,countTimer]
	// source dest 0 java, 1 octave 2 arduino
	public void run(){
	while(true)
		{
		try{
			int i=0;
//			System.out.print(".");
			for ( i=0;i<EventManagement.sizeTable;i++)
			{

				if (EventManagement.pendingRequestTable[i][2]!=0)
				{
					EventManagement.pendingRequestTable[i][6]++;
//					System.out.print("+");

					if (EventManagement.pendingRequestTable[i][6]>=EventManagement.pendingRequestTable[i][2])
					{
//						System.out.println("-");
						EventManagement.pendingRequestTable[i][5]=-1;  // timeout
						EventManagement.pendingRequestTable[i][2]=0;
						RobotMainServer.octaveRequestPending=false;
						RobotMainServer.octavePendingRequest=EventManagement.pendingRequestTable[i][1];
						if (EventManagement.pendingRequestTable[i][1]==1)  // waiting for status info
						{
					    RobotMainServer.javaRequestStatusPending=false;
						}
//						return;
					}
				}
			}

				Thread.sleep(100);
			} catch (Exception e) {
//				System.out.print("e");
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		   finally{}
		}
}
}