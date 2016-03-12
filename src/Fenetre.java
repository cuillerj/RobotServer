import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Fenetre extends JFrame{
 
/**
	 * 
	 */
  private static final long serialVersionUID = 1L;
//  private Panneau pan = new Panneau();
  private JButton boutonStart = new JButton("Start");
  private JButton boutonStop = new JButton("Stop");
  private JButton boutonScan = new JButton("ScanOnce");
  private JButton boutonScanSeq = new JButton("ScanSeq");
  private JButton boutonMove = new JButton("Move");
  private JButton boutonGoto = new JButton("Goto");
  private JButton boutonInit = new JButton("Init");
  private JButton boutonRefresh = new JButton("Refresh");
  public JButton boutonAffEcho = new JButton("Aff Echo");
  private JButton boutonCalibrate = new JButton("W Calibr");
  private JPanel container = new JPanel();
  static JLabel label = new JLabel("Angle ° - Deplact en mn - Id2 du scan ");
  private JLabel label2 = new JLabel("Action >> ");
  private JLabel label3 = new JLabel("Angle Distance  posX  posY scanId ");
  static JFormattedTextField  angle = new JFormattedTextField(NumberFormat.getIntegerInstance());
  static JFormattedTextField  move = new JFormattedTextField(NumberFormat.getIntegerInstance());
  private JFormattedTextField  orient= new JFormattedTextField(NumberFormat.getIntegerInstance());
  static JFormattedTextField  idscan = new JFormattedTextField(NumberFormat.getIntegerInstance());
  private JFormattedTextField  init_X = new JFormattedTextField(NumberFormat.getIntegerInstance());
  private JFormattedTextField  init_Y = new JFormattedTextField(NumberFormat.getIntegerInstance());

//  private JPanel container2 = new JPanel();
 // private JTextField move = new JTextField("0");
//  private int compteur = 0;
//  private JTextField textf = new JTextField(20);

  public int movIHM;
// 

//public int ang;
//public int mov;
//public int ids;
  public static String robotStat=" ?";
  public static String robotPower= " ?";
  public static String robotDiag;
//  public static int idCarto=1;    // a rendre modifiable
  public Fenetre(){
    this.setTitle("Fields: move (angle, distance)  init (orientation posX posY) goto (posX posY)  scan Id");
    this.setSize(800, 150);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    container.setBackground(Color.white);
    container.setLayout(new BorderLayout());
  //  container.add(pan, BorderLayout.CENTER);
//    container2.setBackground(Color.blue);
//    container2.setLayout(new BorderLayout());

    boutonInit.addActionListener(new BoutonInitListener()); 
    boutonStart.addActionListener(new BoutonStartListener());
    boutonStop.addActionListener(new BoutonStopListener());
    boutonScan.addActionListener(new BoutonScanListener()); 
    boutonScanSeq.addActionListener(new BoutonScanSeqListener()); 
    boutonMove.addActionListener(new BoutonMoveListener()); 
    boutonGoto.addActionListener(new BoutonGotoListener()); 
    boutonRefresh.addActionListener(new BoutonRefreshListener()); 
    boutonAffEcho.addActionListener(new BoutonAffEchoListener()); 
    boutonCalibrate.addActionListener(new BoutonCalibrateListener()); 
//  boutonInit.setBackground(Color.black);
//    boutonInit.setForeground(Color.white);
    JPanel south = new JPanel();
    JPanel top = new JPanel();
 //   top.setBackground(Color.green);
    south.setBackground(Color.gray);
 //   top.setLocation(20, 20);
  //  top.setSize(400, 200);
    Font police = new Font("Tahoma", Font.BOLD, 16);
    angle.setFont(police);
    angle.setPreferredSize(new Dimension(50, 30));
    angle.setForeground(Color.GREEN);
 //   angle.setLocation(200, 200);
    init_X.setText("0");
    init_Y.setText("0");
    angle.setText("0");
    move.setFont(police);
    move.setPreferredSize(new Dimension(50, 30));
    move.setForeground(Color.BLUE);
    move.setText("0");
    idscan.setFont(police);
   idscan.setPreferredSize(new Dimension(50, 30));
//    idscan.setForeground(Color.BLUE);
    idscan.setForeground(Color.RED);
    idscan.setBackground(Color.LIGHT_GRAY);
//    idscan.setBackground(Color.black);
    idscan.setText(RobotMainServer.idscanG);
    orient.setFont(police);
    orient.setPreferredSize(new Dimension(50, 30));
    orient.setForeground(Color.GREEN);
    orient.setBackground(Color.LIGHT_GRAY);
    init_X.setFont(police);
    init_X.setPreferredSize(new Dimension(50, 30));
    init_X.setForeground(Color.DARK_GRAY);
    init_X.setBackground(Color.LIGHT_GRAY);
    init_Y.setFont(police);
    init_Y.setPreferredSize(new Dimension(50, 30));
    init_Y.setForeground(Color.BLACK);
    init_Y.setBackground(Color.LIGHT_GRAY);
//    orient.setBackground(Color.black);
 //   String orientGS=""+RobotMainServer.orientG;
 //   orient.setText(orientGS);
    orient.setText("0");
 //   container.add(label);
  top.add(label);
  south.add(label2);
  label2.setHorizontalAlignment(JLabel.CENTER);
    boutonInit.setForeground(Color.RED);
    boutonInit.setBackground(Color.LIGHT_GRAY);
  //  top.add(label2);
    top.add(angle);
    top.add(move);
    top.add(orient);
    top.add(init_X);
    top.add(init_Y);
    top.add(idscan,BorderLayout.PAGE_END);



 //   label2.setHorizontalAlignment(JLabel.CENTER);
    south.add(label2);
    south.add(boutonStart);
    south.add(boutonStop);
    south.add(boutonRefresh);
    south.add(boutonScan);
    south.add(boutonScanSeq);
    south.add(boutonMove);
    south.add(boutonGoto);
    south.add(boutonAffEcho);
    south.add(boutonInit);
    south.add(boutonCalibrate);

    label.setFont(police);
    label.setForeground(Color.blue);
    label.setHorizontalAlignment(JLabel.CENTER);
    container.add(top, BorderLayout.CENTER);
    container.add(south, BorderLayout.SOUTH);
    container.add(label, BorderLayout.NORTH);

 //   container2.add(top, BorderLayout.CENTER);
 //   container2.add(south, BorderLayout.SOUTH);
 //   container2.add(label, BorderLayout.NORTH);
    this.setContentPane(container);
//    this.setContentPane(container2);
    this.setVisible(true);

//    ang = Integer.parseInt(angle.getText());
 //   mov = Integer.parseInt(move.getText());
  //  ids = Integer.parseInt(idscan.getText());
    go();
  }
      
  static void go(){
      label.setText(RobotMainServer.stationStatus+" "+robotStat+"-"+robotPower+ "-"+robotDiag);   
     RobotMainServer.idscanG= idscan.getText();
    //Cette méthode ne change pas
  }
 public class BoutonAffEchoListener implements ActionListener{
	    //Redéfinition de la méthode actionPerformed()
	    public void actionPerformed(ActionEvent arg0) {
			FenetreEchoLocation ihm4 = new FenetreEchoLocation();
			ihm4.SetInitialPosition();
			ihm4.repaint();
	      go();
	    }
	  }
  class BoutonRefreshListener implements ActionListener{
	    //Redéfinition de la méthode actionPerformed()
	    public void actionPerformed(ActionEvent arg0) {
	    	if (EchoRobot.pendingEcho>1)
	    	{
	    		robotStat="timout";
	    	}
	      go();
	    }
	  }

  class BoutonStartListener implements ActionListener{
    //Redéfinition de la méthode actionPerformed()
    public void actionPerformed(ActionEvent arg0) {
        RobotMainServer.idscanG= idscan.getText();
      label.setText("Demarrage du robot");   
      SendUDP snd = new SendUDP();
      snd.SendUDPStart();
      go();
    }
  }
      
  //Classe écoutant notre second bouton
  class BoutonStopListener implements ActionListener{

    public void actionPerformed(ActionEvent e) {
        RobotMainServer.idscanG= idscan.getText();
      label.setText("Arret du robot");
      SendUDP snd = new SendUDP();
      snd.SendUDPStop();
      go();
    }
  } 
  class BoutonCalibrateListener implements ActionListener{

	    public void actionPerformed(ActionEvent e) {
	        RobotMainServer.idscanG= idscan.getText();
	      label.setText("Calibrate");
	      SendUDP snd = new SendUDP();
	      snd.SendUDPCalibrate();
	      go();
	    }
	  } 
  class BoutonScanListener implements ActionListener{
	    //Redéfinition de la méthode actionPerformed()
	    public void actionPerformed(ActionEvent arg0) {
    	
	    	RobotMainServer.Scan360();
	    	/*
	    int newIdScan=0;
	      RobotMainServer.idscanG= Integer.toString(newIdScan);
	      RobotMainServer.scanStepCount=1;
	      idscan.setText(RobotMainServer.idscanG);
	      label.setText("Demarrage du scan");   
	      System.out.println(RobotMainServer.idscanG);
	      SendUDP snd = new SendUDP();
	      snd.SendUDPScan();
	      */
	      go();
	    }
	  }
  class BoutonScanSeqListener implements ActionListener{
	    //Redéfinition de la méthode actionPerformed()
	    public void actionPerformed(ActionEvent arg0) {
	    RobotMainServer.countScan=20;
	    int newIdScan=Integer.parseInt(idscan.getText())+1;
	      RobotMainServer.idscanG= Integer.toString(newIdScan);
	      idscan.setText(RobotMainServer.idscanG);
	      label.setText("Demarrage du scan serie");   
	      System.out.println(RobotMainServer.idscanG);
	     SendUDP snd = new SendUDP();
	      snd.SendUDPScan();
	      go();
	    }
	  }
  class BoutonMoveListener implements ActionListener{
	    //Redéfinition de la méthode actionPerformed()
	    public void actionPerformed(ActionEvent arg0) {
	        RobotMainServer.idscanG= idscan.getText();
	      label.setText("Move");   
	      
//	      System.out.println("angle " + angle.getText());
//	      System.out.println("move " + move.getText());
//	      int ang = Integer.parseInt(angle.getText());
//	      String moveT=move.getText();
//	      Object mov = move.getValue();
	      Object ang = angle.getValue();
	      Object mov = move.getValue();
//	      System.out.println("angle2 " + ang);
	      if (!mov.equals(null) || !ang.equals(null))
	      {
	      SendUDP snd = new SendUDP();
	      snd.SendUDPMove((long)ang,(long) mov);
	      }
//	      Fenetre2 f2 = new Fenetre2();
//	      f2.SetcurrentInd(idscan.getText());
//	      f2.SetInitialPosition();
//	      f2.SetMove(move.getText(), angle.getText());
	      RobotMainServer.actStat=0x01;  //demande mov
	      go();

	    }
	  }
  class BoutonGotoListener implements ActionListener{
	    //Redéfinition de la méthode actionPerformed()
	    public void actionPerformed(ActionEvent arg0) {
	        RobotMainServer.idscanG= idscan.getText();
	      label.setText("GoTo");   
//	      System.out.println("angle " + angle.getText());
//	      System.out.println("move " + move.getText());
//	      int ang = Integer.parseInt(angle.getText());
//	      String moveT=move.getText();
//	      Object mov = move.getValue();
	      Object posX = init_X.getValue();
	      Object posY = init_Y.getValue();
//	      System.out.println("angle2 " + ang);
	      if (!posX.equals(null) || !posY.equals(null))
	      {
	      SendUDP snd = new SendUDP();
	      snd.SendUDPGoto((long)posX,(long) posY);
	      }
//	      Fenetre2 f2 = new Fenetre2();
//	      f2.SetcurrentInd(idscan.getText());
//	      f2.SetInitialPosition();
//	      f2.SetMove(move.getText(), angle.getText());
	      RobotMainServer.actStat=0x01;  //demande mov
	      go();

	    }
	  }
  class BoutonInitListener implements ActionListener{
	    //Redéfinition de la méthode actionPerformed()
	    public void actionPerformed(ActionEvent arg0) {
	        RobotMainServer.idscanG= idscan.getText();
	      label.setText("Init posX, posY, orientation");   
	      System.out.println("posX " + angle.getText());
//	      System.out.println("posY " + move.getText());
//	      System.out.println("idscan " + idscan.getText());
	      int posX = Integer.parseInt(init_X.getText());
	      int posY= Integer.parseInt(init_Y.getText());
	      int ids= Integer.parseInt(idscan.getText());
	      int orien= Integer.parseInt(orient.getText());
	      RobotMainServer.posX=posX;
	      RobotMainServer.posY=posY;
	      RobotMainServer.alpha=orien;
	      InitPos initRobot = new InitPos();
	      initRobot.InitRobot(posX, posY, orien, ids);
	      SendUDP snd = new SendUDP();
	      snd.SendUDPInit(posX,posY,orien,RobotMainServer.currentLocProb);
	      Fenetre2.ValidePosition(posX, posY, orien);
	      go();
	    }
	  }
public int ang() {
	// TODO Auto-generated method stub
    int ang = Integer.parseInt(angle.getText());
	return ang;
}
public static int ids() {
	// TODO Auto-generated method stub
    int ids = Integer.parseInt(idscan.getText());
	return ids;
}
public int mov() {
	// TODO Auto-generated method stub
	String nbS=move.getText();
	/*
	byte[] nbB = null;
	try {
		nbB = nbS.getBytes(nbS);
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	int i=0;
	for (i=0;i<3;i++)
	{
	 RobotMainServer.hexaPrint(nbB[i]);
	}
	*/
	String nb=nbS.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]","");

	 int mov=0;
	try{
     mov = Integer.parseInt(nb);
	}
	catch (Exception e) {
		e.printStackTrace();
		} 
	finally
	{
		
	}
	return mov;
}
public static String idsString() {
	// TODO Auto-generated method stub
    String ids = idscan.getText();
	return ids;
}
public void MajRobotStat(String mess) {
	// TODO Auto-generated method stub
	robotStat=mess;
    go();

}
public void MajRobotPower(String power1) {
	// TODO Auto-generated method stub
	robotPower=power1;
    go();

}
public void MajRobotDiag(String string) {
	// TODO Auto-generated method stub
	robotDiag=string;
    go();

}
public static void RefreshStat() {
	// TODO Auto-generated method stub
	if (EchoRobot.pendingEcho>2)
	{
		robotStat="timout";
		RobotMainServer.runningStatus=-1;
	}
	
    go();
}
}
