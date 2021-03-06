import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Fenetre2 extends JFrame{
//	private Panneau pan = new Panneau();
//	  private JButton boutonActualise = new JButton("Calcul");
//	  private JButton boutonValide = new JButton("Valide");
//	  private JButton boutonInit = new JButton("Initialise position");
	 static FenetreGraphique graph = new FenetreGraphique();
	  private JPanel container = new JPanel();
	//  private JPanel container2 = new JPanel();
	  private static JLabel label = new JLabel("Position du robot ");
	  
	  private static JFormattedTextField  posX= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  posY = new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  orient= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  indScan= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  posXP= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  posYP= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  orientP= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  currentLocProb= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  posXHard= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  posYHard = new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  orientHard= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  northOrientation= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  absoluteOrientation= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  BNOMode= new JFormattedTextField(NumberFormat.getIntegerInstance());
	  private static JFormattedTextField  BNOCalibration= new JFormattedTextField(NumberFormat.getIntegerInstance());
	 // private JTextField move = new JTextField("0");
	  private int compteur = 0;
	//public int ang;
	//public int mov;
	//public int ids;
	  public int mov;
	  public int ang;
	  public Fenetre2(){
	    this.setTitle("Statut du robot");
	    this.setSize(250, 220);
//	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    this.setLocation(550,160);
	    container.setBackground(Color.white);
	    container.setLayout(new BorderLayout());
	  //  container.add(pan, BorderLayout.CENTER);
	        
	    //Ce sont maintenant nos classes internes qui �coutent nos boutons 
//	    boutonActualise.addActionListener(new BoutonActualise());
//	    boutonValide.addActionListener(new BoutonValide());
	    JPanel south = new JPanel();
	    JPanel top = new JPanel();
	    JPanel center = new JPanel();
	    Font police = new Font("Tahoma", Font.BOLD, 16);
	    posX.setFont(police);
	    posX.setPreferredSize(new Dimension(50, 30));
	    posX.setForeground(Color.DARK_GRAY);
	    posY.setFont(police);
	    posY.setPreferredSize(new Dimension(50, 30));
	    posY.setForeground(Color.BLACK);
	    orient.setFont(police);
	    orient.setPreferredSize(new Dimension(50, 30));
	    orient.setForeground(Color.GREEN);
	    indScan.setFont(police);
	    indScan.setPreferredSize(new Dimension(50, 30));
	    indScan.setForeground(Color.RED);
//	    indScan.setBackground(Color.black);
	    indScan.setText(RobotMainServer.idscanG);
	//    indScan.setLocation(300, 300);
	    posXP.setFont(police);
	    posXP.setPreferredSize(new Dimension(50, 30));
	    posXP.setForeground(Color.DARK_GRAY);
	    String posXPS=""+RobotMainServer.posXG;
	    posXP.setText(posXPS);
	    posYP.setFont(police);
	    posYP.setPreferredSize(new Dimension(50, 30));
	    posYP.setForeground(Color.BLACK);
	    String posYPS=""+RobotMainServer.posYG;
	    posYP.setText(posYPS);
	    orientP.setFont(police);
	    orientP.setPreferredSize(new Dimension(50, 30));
	    orientP.setForeground(Color.GREEN);
	    currentLocProb.setFont(police);
	    currentLocProb.setPreferredSize(new Dimension(50, 30));
	    currentLocProb.setForeground(Color.GRAY);
	    posXHard.setFont(police);
	    posXHard.setPreferredSize(new Dimension(50, 30));
	    posXHard.setForeground(Color.DARK_GRAY);
	    posYHard.setFont(police);
	    posYHard.setPreferredSize(new Dimension(50, 30));
	    posYHard.setForeground(Color.BLACK);
	    orientHard.setFont(police);
	    orientHard.setPreferredSize(new Dimension(50, 30));
	    orientHard.setForeground(Color.GREEN);
	    northOrientation.setFont(police);
	    northOrientation.setPreferredSize(new Dimension(50, 30));
	    northOrientation.setForeground(Color.RED);
	    absoluteOrientation.setFont(police);
	    absoluteOrientation.setPreferredSize(new Dimension(50, 30));
	    absoluteOrientation.setForeground(Color.RED);
	    BNOMode.setFont(police);
	    BNOMode.setPreferredSize(new Dimension(50, 30));
	    BNOMode.setForeground(Color.GRAY);
	    BNOCalibration.setFont(police);
	    BNOCalibration.setPreferredSize(new Dimension(50, 30));
	    BNOCalibration.setForeground(Color.GRAY);
	    String orientPS=""+RobotMainServer.orientG;
	    orientP.setText(orientPS);
	    container.add(label);
	    top.add(label);
	    top.add(posX);
	    top.add(posY);
	    top.add(orient);
	    top.add(indScan);
	    top.add(posXP);
	    top.add(posYP);
	    top.add(orientP);
	    top.add(currentLocProb);
	    top.add(posXHard);
	    top.add(posYHard);
	    top.add(orientHard);
	    top.add(northOrientation);
	    top.add(absoluteOrientation);
	    top.add(BNOMode);
	    top.add(BNOCalibration);
	    container.add(top, BorderLayout.CENTER);
//	    container.add(center, BorderLayout.CENTER);
	//    south.add(boutonActualise);
	//    south.add(boutonValide);

	    container.add(south, BorderLayout.SOUTH);

	    label.setFont(police);
	    label.setForeground(Color.blue);
	    label.setHorizontalAlignment(JLabel.CENTER);
	    container.add(label, BorderLayout.NORTH);
	    this.setContentPane(container);
	    this.setVisible(true);
	    int posX1= Integer.parseInt(posXP.getText());
	    int posY1= Integer.parseInt(posYP.getText());
	    int orient1= Integer.parseInt(orientP.getText());
  		PanneauGraphique.point(posX1,posY1,orient1);
  		graph.repaint();

	  }
	  /*
	
	  */
	  public int GetcurrentPosX() {
			// TODO Auto-generated method stub
		    int ang = Integer.parseInt(posX.getText());
			return ang;
		}
	  public int GetcurrentPosY() {
			// TODO Auto-generated method stub
		    int ang = Integer.parseInt(posY.getText());
			return ang;
		}
	  public int GetcurrentOrient() {
			// TODO Auto-generated method stub
		    int ang = Integer.parseInt(orient.getText());
			return ang;
		}

	
	public static void SetcurrentPosX(String text) {
		// TODO Auto-generated method stub
	    posXP.setText(text);	
	}
	public static void SetcurrentPosY(String text) {
		// TODO Auto-generated method stub
	    posYP.setText(text);	
	}
	public static void SetcurrentOrientation(String text) {
		// TODO Auto-generated method stub
	    orientP.setText(text);	
	}
	public static void SetnextPosX(String text) {
		// TODO Auto-generated method stub
	    posX.setText(text);	
	}
	public static void SetnextPosY(String text) {
		// TODO Auto-generated method stub
	    posY.setText(text);	
	}
	public static void SetnextOrientation(String text) {
		// TODO Auto-generated method stub
	    orient.setText(text);	
	}
	public void SetcurrentInd(String text) {
		// TODO Auto-generated method stub
	    indScan.setText(text);	
	}
	public static void SetcurrentLocProb() {
		// TODO Auto-generated method stub
	    currentLocProb.setText(Integer.toString(RobotMainServer.currentLocProb));	
	}
	public static void RefreshHardPosition() {
		// TODO Auto-generated method stub
	    posXHard.setText(Integer.toString(RobotMainServer.hardPosX));	
	    posYHard.setText(Integer.toString(RobotMainServer.hardPosY));	
	    orientHard.setText(Integer.toString(RobotMainServer.alpha));	
	    northOrientation.setText(Integer.toString(RobotMainServer.northOrientation));
	    absoluteOrientation.setText(Integer.toString(RobotMainServer.absoluteOrientation));	

	}
	public static void RefreshBNO() {
		// TODO Auto-generated method stub
	    BNOMode.setText(byteToHex(RobotMainServer.BNOMode));	
	   BNOCalibration.setText(byteToHex(RobotMainServer.BNOCalStat));	


	}
	public void SetMove(String move, String angle) {
		// TODO Auto-generated method stub
	    mov=Integer.parseInt(move);	
	    ang=Integer.parseInt(angle);	
	    System.out.println("move:"+mov+" "+ang);
	}
	public void SetInitialPosition() {
	Connection conn = null;
	Statement stmtR1 = null;
	Statement stmtI1 = null;
//		Fenetre ihm = new Fenetre();
//		JFormattedTextField ids=indScan;
//	String ids =indScan.getText();
//	int indscan=Integer.parseInt(ids);
    indScan.setText(RobotMainServer.idscanG);
    int indscan=Integer.parseInt(RobotMainServer.idscanG.replaceAll("[^a-zA-Z0-9\\s+]", ""));
	String posXP;
	String posYP;
	String orientP;
	System.out.println("actualise:"+indscan);
try {

	Class.forName("com.mysql.jdbc.Driver").newInstance();
	String connectionUrl = "jdbc:mysql://jserver:3306/robot";
	String connectionUser = "jean";
	String connectionPassword = "manu7890";
	conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
	//conn.setAutoCommit(false);
	stmtR1 = conn.createStatement();
	stmtI1 = conn.createStatement();
	ResultSet rs = null;
	SetcurrentLocProb();
	int ndscan=Integer.parseInt(RobotMainServer.idscanG);
	rs = stmtR1.executeQuery("SELECT * FROM scanResult WHERE idscan = "+indscan+"  ORDER by time desc limit 1"); 
		while (rs.next()) {
		  posXP = rs.getString("posX");
		  posYP = rs.getString("posY");
		  orientP = rs.getString("orientation");
		  SetcurrentPosX( posXP);
		  SetcurrentPosY( posYP);
		  SetcurrentOrientation( orientP);
		  System.out.println("x:"+posXP+" Y:"+posYP+" orient:"+orientP);
		}
	rs.close();

} catch (Exception e) {
e.printStackTrace();
} 
finally {
try { if (stmtR1 != null) stmtR1.close(); } catch (SQLException e) { e.printStackTrace(); }
try { if (stmtI1 != null) stmtI1.close(); } catch (SQLException e) { e.printStackTrace(); }
try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }}
}
	public static void TargetLocation(long ang2,long mov2, int posX,int posY,long orient){
//	System.out.println("target location:"+ang2+" "+mov2+" "+posX+" "+posY+" "+orient);
	orient=(orient+ang2)%360;
	String orientnext=""+orient;
	SetnextOrientation(orientnext);
	double degrees=orient;
	double radians=Math.toRadians(degrees);
	int posXnext=(int) (Math.cos(radians)*mov2);					
	int posYnext=(int) (Math.sin(radians)*mov2);
	posXnext=posXnext+posX;
	posYnext=posYnext+posY;
	String posXn=""+posXnext;
	SetnextPosX(posXn);
	String posYn=""+posYnext;
	SetnextPosY(posYn);
	}
	public static void PosActualise(long ang2,long mov2) {
		// TODO Auto-generated method stub
	//	SetInitialPosition();
		SetcurrentLocProb();
	    indScan.setText(RobotMainServer.idscanG);
	 	String ss =orientP.getText();
//	 	System.out.println("actualise "+ang2+" "+mov2 );
	  	int orient=Integer.parseInt(ss);
	    	ss =posXP.getText();
//	    	System.out.println(ss);
	     int posXP=Integer.parseInt(ss);
	    	ss =posYP.getText();
//	    	System.out.println(ss);
	    	int posYP=Integer.parseInt(ss);
	    	TargetLocation(ang2,mov2,posXP,posYP,orient);
	}
	public static void ValidePosition(int posX2, int posY2, int alpha) {
	      label.setText("posX, posY, orientation");   
	      SetcurrentLocProb();
	//	 	String ss =orient.getText();
	//	      SetcurrentOrientation(ss);
			 	SetcurrentOrientation(String.valueOf(alpha));
//		 	System.out.println(ss);
//		  	int orientN=Integer.parseInt(ss);
	//	    	ss =posX.getText();
			//      SetcurrentPosX(ss);
		  	SetcurrentPosX(String.valueOf(posX2));
	//	    	System.out.println(ss);
//		     int posXN=Integer.parseInt(ss);
//		    	ss =posY.getText();
//			      SetcurrentPosY(ss);
				  	SetcurrentPosY(String.valueOf(posY2));
				  	
//		    	System.out.println(ss);
//		    	int posYN=Integer.parseInt(ss);
		    	//ss =indScan.getText();
	//	    	String ss=RobotMainServer.idscanG;
//		    	System.out.println(ss);
		    	//int indsN=Integer.parseInt(ss);
//	      InitPos initRobot = new InitPos();
//	      initRobot.InitRobot(posX2, posY2, alpha, indsN);
	  		PanneauGraphique.point(posX2,posY2,alpha);
	  		graph.repaint();

}
	public static void ValidePositionX(int value) {
	  	SetcurrentPosX(String.valueOf(value));
	}
	public static void ValidePositionY(int value) {
	  	SetcurrentPosY(String.valueOf(value));
	}
	public static void ValideOrientation(int value) {
	  	SetcurrentOrientation(String.valueOf(value));
	}
	public static void ValideL(int value) {
	  	SetcurrentOrientation(String.valueOf(value));
	}
	public void Refresh() {
		
	}
	  public static String byteToHex(byte b) {
		    int i = b & 0xFF;
		    return Integer.toHexString(i);
		  }
}