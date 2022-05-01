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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class ATM_interface  {
	double balance; // for the user balance
	
	//JButton loginButton = new JButton("submit");
	//-----------------------the login GUI-------------------------//
		private static void atmLogin(int o) {
			JFrame frame2 = new JFrame("ATM Login");
			frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			loginUI(frame2);
			frame2.setSize(350,350); //setting the frame size for the gui
			frame2.setVisible(true);
			frame2.setLocationRelativeTo(null);
			
		}
		
		private static void loginUI(final JFrame frame2) {
			JTextField textField = new JTextField(30);
			JPanel panel2 = new JPanel();
			JLabel pinLabel = new JLabel("please insert pin");
			pinLabel.setBounds(150, 220, 150, 30);
			JButton loginButton = new JButton("submit");
			JButton goBack = new JButton("going back to the previous page");
			
			
			loginButton.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        String pinCheck;
		        pinCheck = textField.getText();
		        String pin;
		        /*---------------Text file goes in here---------------------------*/
		        File text = new File("C:\\Users\\proto\\eclipse-workspace\\ATM\\src\\testpin.txt");
		        /*------------------------------------------------------------------*/
		        
		        try {
					Scanner myRead = new Scanner(text);
					while (myRead.hasNextLine()) {
						String data = myRead.nextLine();
						goBack.setEnabled(true);//used to return back to the previous page
				           //to check if the pin entered is correct or not
				           if (pinCheck.equalsIgnoreCase(data)){
				                JOptionPane.showMessageDialog(frame2, "Login Successful");
				            atmWindow();// leads to the atm gui window
				           } 
				          
				           else {
				                JOptionPane.showMessageDialog(frame2, "Invalid Username or Password");
				            }	
					}
					myRead.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		        //JOptionPane.showMessageDialog(frame2, "text");
		        /*   
		        goBack.setEnabled(true);//used to return back to the previous page
		           //to check if the pin entered is correct or not
		           if (pinCheck.equalsIgnoreCase("mehtab")){
		                JOptionPane.showMessageDialog(frame2, "Login Successful");
		            atmWindow();// leads to the atm gui window
		           } 
		          
		           else {
		                JOptionPane.showMessageDialog(frame2, "Invalid Username or Password");
		            }
		           	*/
		        }
		        
		    });
			
			//adding the panels to the GUI
			panel2.add(pinLabel);
			panel2.add(textField);
			panel2.add(loginButton);
			frame2.getContentPane().add(panel2);
			
			 
			
		}
		
		
		
	
	//----------------------------the atm GUI---------------------//
	private static void atmWindow() {
		JFrame frame = new JFrame("ATM GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		atmUI(frame); // setting frame
		frame.setSize(600,600); //setting the frame size for the gui
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);  //  to Center UI on screen
		
	}
	
	
	//contains the main buttons
	private static void atmUI(final JFrame frame) {
	JPanel panel = new JPanel();
	//LayoutManager layout = new GridLayout();  // uncomment: To try alternate layouts
    //panel.setLayout(layout);  
	//JLabel user_label, password_label;
	//user_label = new JLabel();
	//user_label.setText("please input your PIN");
	
	
	//password_label = new JLabel();
	
	//JTextField textField = new JTextField(40);
	//JTextField textField2 = new JTextField(40);
	JButton withsavingButton = new JButton("withdraw savings");
    JButton depsavingButton = new JButton("deposit savings");
    JButton withcheckButton = new JButton("withdraw checkings");
    JButton depcheckButton = new JButton("deposit checkings");
    JButton goBack = new JButton("going back to the previous page");
   // goBack.setEnabled(false);
    
   
    //withdraw savings
    withsavingButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
           JOptionPane.showMessageDialog(frame, "test");
           goBack.setEnabled(true);//used to return back to the previous page
        }
     });
    //deposit savings
    depsavingButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, "Ok Button clicked. Cancel Enabled");
            goBack.setEnabled(true);//used to return back to the previous page

         }
      });
    //withdraw checkings
    withcheckButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	panel.add(depcheckButton);
        	JOptionPane.showMessageDialog(frame, "test");
           //withsavingButton.setEnabled(true);
           goBack.setEnabled(true);//used to return back to the previous page
        }
     });
    ////deposit checkings
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
	    //Scanner myObj = new Scanner(System.in);   
	   //int var1 = myObj.nextInt(); 
	    
		atmLogin(1);
		
	    }
	    
}