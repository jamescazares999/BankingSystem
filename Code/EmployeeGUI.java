import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
public class EmployeeGUI {
  double balance; // for the user balance
  private static Employee employee = new Employee();
  static int pinCheck;
  static int debCheck;
  static String passedAccNum;

  EmployeeGUI(){
  }
  
  //JButton loginButton = new JButton("submit");
  //-----------------------the login GUI-------------------------//
  private void empLogin() {
    JFrame frame2 = new JFrame("Employee Login");
    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loginUI(frame2);
    frame2.setSize(350, 350); //setting the frame size for the gui
    frame2.setVisible(true);
    frame2.setLocationRelativeTo(null);
    try {
      employee.connectToHost();
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void loginUI(final JFrame frame2) {
    //titleLabel = new JLabel("Debit Card Number");
    JTextField textField = new JTextField(30);
    JTextField textField2 = new JTextField(30);
    //JPasswordField passwordField = new JPasswordField();
    JPanel panel2 = new JPanel();
    JLabel DebLabel = new JLabel("Please Input Debit Card Number: ");
    JLabel pinLabel = new JLabel("Please Input Pin: ");
    pinLabel.setBounds(150, 220, 150, 30);
    JButton loginButton = new JButton("Submit");
    JButton goBack = new JButton("Return to Previous Page");

    //Login button 
    loginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pinCheck = Integer.parseInt(textField.getText());
        debCheck = Integer.parseInt(textField2.getText());
//        Employee employee = null;

        try {
          passedAccNum = employee.sendDebit(debCheck, pinCheck);
        } catch (ClassNotFoundException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        employeeWindow();

        //JOptionPane.showMessageDialog(frame2, "text");
        /*   
        goBack.setEnabled(true);//used to return back to the previous page
           //to check if the pin entered is correct or not
           if (pinCheck.equalsIgnoreCase("mehtab")){
                JOptionPane.showMessageDialog(frame2, "Login Successful");
            employeeWindow();// leads to the employee gui window
           } 
          
           else {
                JOptionPane.showMessageDialog(frame2, "Invalid Username or Password");
            }
           	*/
      }

    });

    //adding the panels to the GUI
    panel2.add(DebLabel);
    panel2.add(textField2);
    panel2.add(pinLabel);
    panel2.add(textField);
    panel2.add(loginButton);
    frame2.getContentPane().add(panel2);

  }

  //----------------------------the employee GUI---------------------//
  private static void employeeWindow() {
    JFrame frame = new JFrame("Employee GUI");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    empUI(frame); // setting frame
    frame.setSize(400, 400); //setting the frame size for the gui
    frame.setVisible(true);
    frame.setLocationRelativeTo(null); //  to Center UI on screen

  }

  //contains the main buttons
  private static void empUI(final JFrame frame) {
    JPanel panel = new JPanel();
    //LayoutManager layout = new GridLayout();  // uncomment: To try alternate layouts
    //panel.setLayout(layout);  

    //password_label = new JLabel();

    //JTextField textField = new JTextField(40);
    //JTextField textField2 = new JTextField(40);
    JButton withsavingButton = new JButton("Withdraw Savings");
    JButton depsavingButton = new JButton("Deposit Savings");
    JButton withcheckButton = new JButton("Withdraw Checkings");
    JButton depcheckButton = new JButton("Deposit Checkings");
    JButton checkSavings = new JButton("Check Savings Balance");
    JButton checkCheckings= new JButton("Check Checking Balance");
    JButton goBack = new JButton("Return to Previous Page");
    JButton helpMemberButton = new JButton("Help a Current Member");
    JButton createMemberButton = new JButton("Create a New Member");
    // goBack.setEnabled(false);
    
    createMemberButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	try {
        		
				employee.openAccount(passedAccNum, passedAccNum, null, passedAccNum);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
      });
    
    helpMemberButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	try {
        		String memberSearch = JOptionPane.showInputDialog("Enter Account Number to Help: ");
				employee.accessAcount(memberSearch);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
      });

    //withdraw savings
    withsavingButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	double withdrawAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter Amount to Withdraw: "));
    	try {
			employee.withdrawl(withdrawAmount, passedAccNum);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        goBack.setEnabled(true); //used to return back to the previous page
      }
    });
    //deposit savings
    depsavingButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter Amount to Deposit: "));
    	try {
			employee.deposit(depositAmount, passedAccNum);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        goBack.setEnabled(true); //used to return back to the previous page
      }
    });
    //withdraw checkings
    withcheckButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
        	double withdrawAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter Amount to Withdraw: "));
        	try {
    			employee.withdrawl(withdrawAmount, passedAccNum);
    		} catch (ClassNotFoundException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
            goBack.setEnabled(true); //used to return back to the previous page
          }
    });
    ////deposit checkings
    depcheckButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
        	double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter Amount to Deposit: "));
        	try {
    			employee.deposit(depositAmount, passedAccNum);
    		} catch (ClassNotFoundException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
            goBack.setEnabled(true); //used to return back to the previous page
          }
    });
    
    checkSavings.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
        	int accountNum = Integer.parseInt(passedAccNum);
			JOptionPane.showMessageDialog(frame, "Your Savings Balance is: " + employee.checkBalance(accountNum));
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
          goBack.setEnabled(true); //used to return back to the previous page
        }
      });
    
    checkCheckings.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
        	int accountNum = Integer.parseInt(passedAccNum);
  			JOptionPane.showMessageDialog(frame, "Your Savings Balance is: " + employee.checkBalance(accountNum));
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
          goBack.setEnabled(true); //used to return back to the previous page
        }
      });

    //panel.add(textField);

    panel.add(withsavingButton);

    panel.add(depsavingButton);

    panel.add(withcheckButton);

    panel.add(depcheckButton);

    panel.add(checkSavings);

    panel.add(checkCheckings);

    //panel.add(textField2);
    frame.getContentPane().add(panel);
  }

  public static void main(String args[]) {

	  EmployeeGUI employeeGUI = new EmployeeGUI();
	  employeeGUI.empLogin();

  }

}