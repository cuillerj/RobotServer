
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class PanneaugraphiqueSonar extends JPanel { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int ptXFront=0;
	public static int ptYFront=0;
	public static int ptXBack=0;
	public static int ptYBack=0;
//	public static int refAngleSonar=(int) Math.toRadians(65); // le sonar est aligne avec le robot a 75
	public static int zeroX=0;
	public static int zeroY=0;
	public static int maxEchoLen=450;
	public static int graphSmallScale=50;
	public static int graphBigScale=100;
	public static int [][] frontArray = new int [RobotMainServer.scanArraySize][2];
	public static int [][] backArray = new int [RobotMainServer.scanArraySize][2];	
	public static int arrayIdx=-1;
	public void paintComponent(Graphics g){
    //Vous verrez cette phrase chaque fois que la méthode sera invoquée

    int center[]={this.getWidth()/2,this.getHeight()/2};
    zeroX=center[0];
    zeroY=center[1];
 //   System.out.println("center:"+center[0]+" "+center[1]);
    float scale=Math.min(zeroX,zeroY);
    scale=scale/maxEchoLen;
    float ptXrefFront=ptXFront*scale+zeroX;
    float ptYrefFront=ptYFront*scale+zeroY;
    float ptXrefBack=ptXBack*scale+zeroX;
    float ptYrefBack=ptYBack*scale+zeroY;
//    int ptYrefBack=ptYBack+this.getHeight()/2;
    int dimpt=10;  // taille di point a dessiner
//    System.out.println("panneau sonar Je suis exécutéex: front X"+ptXrefFront+" Y;"+ptYrefFront+" back X"+ptXrefBack+" Y;"+ptYrefBack+ " scale:"+scale+" idx:"+arrayIdx); 

    for (int i=0;i<=arrayIdx;i++)
    {
        g.setColor(Color.green);

        g.fillOval(frontArray[i][0]-dimpt/2,frontArray[i][1]-dimpt/2,dimpt,dimpt);
        g.setColor(Color.blue);

        g.fillOval(backArray[i][0]-dimpt/2,backArray[i][1]-dimpt/2,dimpt,dimpt);   	
    }
    arrayIdx++;
    arrayIdx=(arrayIdx)%(RobotMainServer.scanArraySize);
    g.setColor(Color.orange);
    int XrefFront=Math.round(ptXrefFront);
    int YrefFront=Math.round(ptYrefFront);
    g.fillOval(XrefFront-dimpt/2,YrefFront-dimpt/2,dimpt,dimpt);
    g.setColor(Color.red);
    int XrefBack=Math.round(ptXrefBack);
    int YrefBack=Math.round(ptYrefBack);
    g.fillOval(XrefBack-dimpt/2,YrefBack-dimpt/2,dimpt,dimpt);
    frontArray[arrayIdx][0]=XrefFront;
    frontArray[arrayIdx][1]=YrefFront;
    backArray[arrayIdx][0]=XrefBack;
    backArray[arrayIdx][1]=YrefBack;


    g.setColor(Color.black);
    float graph1=Math.min(this.getWidth(),this.getHeight());
    float graph2=graph1;
    graph1=graph1*graphBigScale/(2*maxEchoLen);
    graph2=graph2*graphSmallScale/(2*maxEchoLen);
    g.drawLine(this.getWidth()/2,0,this.getWidth()/2,this.getHeight());
    g.drawLine(0,this.getHeight()/2,this.getWidth(),this.getHeight()/2);
    g.setColor(Color.blue);
    int j=(2*maxEchoLen)/graphBigScale;
    j=(2*maxEchoLen)/graphSmallScale;
    for (int i=1;i<=j;i++)
    {
    	if (i%2==0)
    	{
    	    g.setColor(Color.black);
    	}
    	else
    	{
    	    g.setColor(Color.white);
    	}
        g.drawLine(this.getWidth()/2+i*(int) graph2,0,this.getWidth()/2+i*(int) graph2,this.getHeight());
        g.drawLine(this.getWidth()/2-i*(int) graph2,0,this.getWidth()/2-i*(int) graph2,this.getHeight());    	
    }
    for (int i=1;i<=j;i++)
    {
    	if (i%2==0)
    	{
    	    g.setColor(Color.black);
    	}
    	else
    	{
    	    g.setColor(Color.white);
    	}
        g.drawLine(0,this.getHeight()/2+i*(int) graph2,this.getWidth(),this.getHeight()/2+i*(int) graph2);
        g.drawLine(0,this.getHeight()/2-i*(int) graph2,this.getWidth(),this.getHeight()/2-i*(int) graph2);       	
    }


        Font font = new Font("Courier", Font.BOLD, 30);
        g.setFont(font);
        g.drawString("^",zeroX-8,zeroY+5);

      }               
    

  public static void point(int posXi,int posYi, int orient, int distFront,int distBack, int angle){
	    //Vous verrez cette phrase chaque fois que la méthode sera invoquée
//	    System.out.println("point Je suis appele angle:"+ angle+" distfront:"+distFront+ " distback:"+distBack);
	    angle=-angle;
	    ptXFront=(int) (distFront*Math.cos(Math.toRadians(angle)));
	    ptYFront=(int) (distFront*Math.sin(Math.toRadians(angle)));
	    ptXBack=(int) (distBack*Math.cos(Math.toRadians(angle)+Math.PI));
	    ptYBack=(int) (distBack*Math.sin(Math.toRadians(angle)+Math.PI));
//	    System.out.println("point xF:"+ ptXFront+" yF:"+ptYFront+ " xB:"+ptXBack+ " yB:"+ptYBack);
	//   pointComponent(null);
	  }
  public static void razPoints()
  {
	  for (int i=0;i<RobotMainServer.scanArraySize;i++)
	  {
		  frontArray[i][0]=0;
		  frontArray[i][1]=0;
		  backArray[i][0]=0;
		  backArray[i][1]=0;

	  }
	  ptXFront=0;
	  ptYFront=0;
	  ptXBack=0;
	  ptYBack=0;
	  arrayIdx=-1;
  }
 
}
