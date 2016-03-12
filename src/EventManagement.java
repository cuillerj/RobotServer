
public class EventManagement{
	Thread t;
	static int sizeTable=10;
	public static int[][] pendingRequestTable=new int[10][7];
	static int reqId=0;
	// [reqId,code,timeout,source,dest,retcode,countTimer]
	// source dest 0 java, 1 octave 2 arduino

	public static void AddPendingEvent(int reqCode,int reqTimeout,int reqSource,int reqDest)
	{
		ClearEventTable (reqCode,reqSource,reqDest);
		RobotMainServer.octaveRequestPending=true;
		RobotMainServer.octavePendingRequest=reqCode;
		pendingRequestTable[reqId%sizeTable][0]=reqId;
		pendingRequestTable[reqId%sizeTable][1]=reqCode;
		pendingRequestTable[reqId%sizeTable][2]=reqTimeout;
		pendingRequestTable[reqId%sizeTable][3]=reqSource;
		pendingRequestTable[reqId%sizeTable][4]=reqDest;
		pendingRequestTable[reqId%sizeTable][5]=9;
		pendingRequestTable[reqId%sizeTable][6]=0;
		reqId++;
		if (reqCode==1 || reqCode==8)
		{
	    RobotMainServer.javaRequestStatusPending=true;
		}
	}
	public static void ClearEventTable (int reqCode,int reqSource,int reqDest)
	{
		int i=0;
		for ( i=0;i<EventManagement.sizeTable;i++)
		{
			if (pendingRequestTable[i][1]==reqCode && pendingRequestTable[i][3]==reqSource 
					&& pendingRequestTable[i][4]==reqDest)
			{
				int j=0;
				for (j=0;j<7;j++)
				{
					pendingRequestTable[i][j]=0;
				}
			}
		}
	}
	public static void UpdateEvent(int reqCode,int retCode,int reqSource,int reqDest) {
		// TODO Auto-generated method stub
		int i=0;
		int reqPendingNumber=0;
		for ( i=0;i<EventManagement.sizeTable;i++)
		{
			
			if (pendingRequestTable[i][1]==reqCode && pendingRequestTable[i][3]==reqSource 
					&& pendingRequestTable[i][4]==reqDest)
			{
					EventManagement.pendingRequestTable[i][5]=retCode;  // timeout
					EventManagement.pendingRequestTable[i][2]=0;
	//				RobotMainServer.octaveRequestPending=false;
					RobotMainServer.octavePendingRequest=EventManagement.pendingRequestTable[i][1];

			}
			if (EventManagement.pendingRequestTable[i][2]!=0)
			{
				reqPendingNumber++;
			}
		}
		if (reqPendingNumber==0)
		{
			RobotMainServer.octaveRequestPending=false;
		}
		if (reqCode==1 || reqCode==8)
		{
	    RobotMainServer.javaRequestStatusPending=false;
		}
	}
}