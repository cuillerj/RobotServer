import javax.swing.JFrame;

public class FenetreGraphiqueSonar extends JFrame {
 public FenetreGraphiqueSonar(){   
   this.setTitle("Echo Scale black:1m - yellow/green Front - red/blue back");
   this.setSize(500, 500);
   this.setLocation(60,160);               
  // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   this.setContentPane(new PanneaugraphiqueSonar());
   this.setVisible(true);
//   System.out.println("fenetre echo loc Je suis exécutée !");
 }


public void SetInitialPosition() {
	// TODO Auto-generated method stub
	
}     
public void SetPoint() {
	// TODO Auto-generated method stub

}     
}               


