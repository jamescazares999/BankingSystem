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
public class ATM_interface {
	
	private static void atmWindow() {
		JFrame frame = new JFrame("ATM GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setSize(600,600); //setting the frame size for the gui
		frame.setVisible(true);
		atmUI(frame);
	}
	
	//contains the main buttons
	private static void atmUI(final JFrame frame) {
	JPanel panel = new JPanel();
	JTextField textField = new JTextField(40);
	JButton withsavingButton = new JButton("withdraw savings");
    JButton depsavingButton = new JButton("deposit savings");
    JButton goBack = new JButton("going back to the previous page");
    goBack.setEnabled(false);
    
   
    withsavingButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
           JOptionPane.showMessageDialog(frame, "");
           goBack.setEnabled(true);//used to return back to the previous page
        }
     });
    
    //depsavingButton
    

	panel.add(textField);
	panel.add(withsavingButton);
	}
	public static void main(String args[]){
	       atmWindow();
	      
	    }
}