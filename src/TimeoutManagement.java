
public class TimeoutManagement extends Thread{

	Thread t;

	// [reqId,code,timeout,source,dest,retcode,countTimer]
	// source dest 0 java, 10 octave 20 arduino 20+1(21) arduino simulator
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

						EventManagement.pendingRequestTable[i][2]=0;
						RobotMainServer.octaveRequestPending=false;
						RobotMainServer.octavePendingRequest=EventManagement.pendingRequestTable[i][1];
						if (EventManagement.pendingRequestTable[i][1]==1)  // waiting for status info
						{
					    RobotMainServer.javaRequestStatusPending=false;
						}
						if (EventManagement.pendingRequestTable[i][4]==21)  // simulation mode
						{
							int retCode=ArduinoSimulator.ActionEnd(EventManagement.pendingRequestTable[i][1], EventManagement.pendingRequestTable[i][3], EventManagement.pendingRequestTable[i][4]);
							EventManagement.pendingRequestTable[i][2]=0;
							EventManagement.pendingRequestTable[i][5]=retCode;
						}
						else{
							EventManagement.pendingRequestTable[i][5]=-1;  // timeout
							EventManagement.pendingRequestTable[i][2]=0;
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