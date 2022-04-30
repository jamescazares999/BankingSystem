import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.util.Scanner;
public class ATM_interface {
	
	
	
	//-----------------------the login GUI-------------------------//
		private static void atmLogin() {
			JFrame frame2 = new JFrame("ATM Login");
			frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			loginUI(frame2);
			frame2.setSize(600,600); //setting the frame size for the gui
			frame2.setVisible(true);
			frame2.setLocationRelativeTo(null);
			
		}
		
		private static void loginUI(final JFrame frame2) {
			JTextField textField = new JTextField(40);
			JPanel panel2 = new JPanel();
			JLabel pinLabel = new JLabel("please insert pin");
			pinLabel.setBounds(150, 220, 150, 30);
			JButton button = new JButton("submit");
			JButton goBack = new JButton("going back to the previous page");
			
			
			button.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		           JOptionPane.showMessageDialog(frame2, "text");
		           goBack.setEnabled(true);//used to return back to the previous page
		        }
		     });
			
			
			
			
			panel2.add(pinLabel);
			panel2.add(textField);
			panel2.add(button);
			frame2.getContentPane().add(panel2);
		}
	
	
	//----------------------------the atm GUI---------------------//
	private static void atmWindow() {
		JFrame frame = new JFrame("ATM GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		atmUI(frame); // setting frame
		frame.setSize(600,600); //setting the frame size for the gui
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);  // Center on screen
		
	}
	
	
	//contains the main buttons
	private static void atmUI(final JFrame frame) {
	JPanel panel = new JPanel();
	JLabel user_label, password_label;
	//user_label = new JLabel();
	//user_label.setText("please input your PIN");
	
	
	password_label = new JLabel();
	
	//JTextField textField = new JTextField(40);
	//JTextField textField2 = new JTextField(40);
	JButton withsavingButton = new JButton("withdraw savings");
    JButton depsavingButton = new JButton("deposit savings");
    JButton withcheckButton = new JButton("withdraw checkings");
    JButton depcheckButton = new JButton("deposit checkings");
    JButton goBack = new JButton("going back to the previous page");
   // goBack.setEnabled(false);
    
   
    
    
    
    withsavingButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
           JOptionPane.showMessageDialog(frame, "test");
           goBack.setEnabled(true);//used to return back to the previous page
        }
     });
    
    depsavingButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, "Ok Button clicked. Cancel Enabled");
            goBack.setEnabled(true);//used to return back to the previous page

         }
      });
    
    withcheckButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
           JOptionPane.showMessageDialog(frame, "test");
           goBack.setEnabled(true);//used to return back to the previous page
        }
     });
    
    depcheckButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
           JOptionPane.showMessageDialog(frame, "Ok Button clicked. Cancel Enabled");
           goBack.setEnabled(true);//used to return back to the previous page

        }
     });
    
   
    //panel.add(textField);
	panel.add(withsavingButton);
	panel.add(depsavingButton);
	panel.add(withcheckButton);
	panel.add(depcheckButton);
	//panel.add(textField2);
	frame.getContentPane().add(panel);
	}
	
	
	
	public static void main(String args[]){
	    Scanner myObj = new Scanner(System.in);   
	   //int var1 = myObj.nextInt(); 
	    
		atmLogin();
		atmWindow();
		
	    //possibly use a if statement
	    /*
	    if(var1 == 1){
	    	System.out.println("if statement works");
	    	atmLogin();
	       }
	       else {
	    	   System.out.println("if statement works");
	    	   atmWindow(); 
	       }
	      */ 
	    }
	    
}