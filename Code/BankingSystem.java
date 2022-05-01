import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class BankingSystem{
	/** The name of the data file that contains customer data */
	private String customerSource;
	private String employeeSource;
	
	private InetAddress IP;
	private static int port = 1111;
	private static HashMap<Integer, Employee> bankWorkers = new HashMap<Integer, Employee>();
	private static HashMap<Integer, Member> customerRecords = new HashMap<Integer, Member>();

	    public static void main(String[] args) throws IOException, ClassNotFoundException {
	        // Create a ServerSock on localhost:2251
	        ServerSocket BankingServer = null;
	        
	        //Every 5 minutes saves customer records into a file. Runs in background
	        Timer savingRecords = new Timer();
	        savingRecords.schedule(new TimerTask() {
	            @Override
	            public void run() {
	                saveCustomers();
	                saveEmployees();
	            }
	        }, new Date().getTime(), 300000);

	        try {
	        	BankingServer = new ServerSocket(port);
	        	BankingServer.setReuseAddress(true);
	        	loadCustomer(customerSource);
	        	loadEmployee(employeeSource);
	        	// running infinite loop for getting
	        				// client request
				while (true) {

					// socket object to receive incoming client
					// requests
					Socket client = BankingServer.accept();

					// Displaying that new client is connected
					// to server
					System.out.println("New client connected "
									+ client.getInetAddress()
											.getHostAddress());

					// create a new thread object
					ClientHandler clientSock
						= new ClientHandler(client);

					// This thread will handle the client
					// separately
					new Thread(clientSock).start();
					
					//prints client disconnected after they logout
					System.out.println("Client Disconnected");
					}

	        }catch(IOException e) {
	        	e.printStackTrace();
	        	
	        }
	    }
	    

	 // ClientHandler class
	 	private static class ClientHandler implements Runnable {
	 		private final Socket clientSocket;

	 		// Constructor
	 		public ClientHandler(Socket socket)
	 		{
	 			this.clientSocket = socket;
	 		}
	 		public void run()
	 		{
	 			// get the input stream from the connected socket
	 			InputStream inputStream = null;

	 			// create a serverInputStream so we can read data from it.
	 			ObjectInputStream serverInputStream = null;
	 			
	 			// get the output stream from the connected socket
	 			OutputStream outputStream = null;
	 			
	 			// create a serverOutputStream so we can read data from it.
	 			ObjectOutputStream serverOutputStream = null;
	 			
	 			// message object to received and then sent from/to streams
	 			Message message = null;

	 			try {
	 					
	 				// get the inputStream of client
	 				inputStream = clientSocket.getInputStream();

	 				// get the inputStream of client
	 				serverInputStream = new ObjectInputStream(inputStream);
	 				
	 				//message = (Message) serverInputStream.readObject();		
	 				
	 				// get the outputStream of client
	 				outputStream = clientSocket.getOutputStream();
	 				
	 				// get the outputStream of client
	 				serverOutputStream = new ObjectOutputStream(outputStream);
	 				
	 				message = (Message) serverInputStream.readObject();
	 				//message.getWho();
	 				
	 				// makes sure that client is logged in first before sending text or logout message(s)
	 				boolean loggedIn = false;
	 				int logFailcounter = 0;
	 				
	 				Employee currently;
 					Member now;
	 				// infinite loop so that any number of messages from the client is received before stopping at logout
	 				while(loggedIn == false && logFailcounter < 2) {
	 					
	 					// recast to message object from inputStream
	 					message = (Message) serverInputStream.readObject();

	 					// if message object is login message, it runs this block
	 	 					
	 	 				if(message.getWho().equals("employee")) {
	 	 				//Employee work = (Employee) message.getEmp();
	 	 						
	 	 					if(bankWorkers.containsKey(message.getlogId())) {
	 	 						currently = (Employee) bankWorkers.get(message.getlogId());
	 	 						if(currently.getEmployeePassword() == message.getEmpPassword()) {
		 	 						message.setStatus("Success");
		 	 						loggedIn = true;
		 	 					}
		 	 					else {
		 	 						message.setStatus("Failed... Wrong Password");
		 	 						logFailcounter++;
		 	 					}
	 	 					}
	 	 					else {
	 	 						message.setStatus("Failed... Pin doesn't exist");
	 	 					}
	 	 				}
	 	 				
 	 					if(message.getWho().equals("ATM")) {
 	 						DebitCard customer = (DebitCard) message.getDebit();
 	 						if(customerRecords.containsKey(customer.getTiedAccount())) {
	 	 						now = (Member) customerRecords.get(customer.getTiedAccount());
	 	 					}
	 	 					else {
	 	 						message.setStatus("Failed... DebitCard not in System");
	 	 					}
	 	 					if(customer.getSecurityCode() == message.getPin()) {
	 	 						message.setStatus("Success");
	 	 						loggedIn = true;
	 	 					}
	 	 					else {
	 	 						message.setStatus("Failed... Wrong Pin Number");
	 	 						logFailcounter++;
	 	 					}
 	 						
 	 					}
 	 					// change login type status to success in message
 	 					// from client
 	 					
 	 					// sends message back to client with changes
 	 					serverOutputStream.writeObject(message);
 	 					//System.out.println("Success");
 	 				
	 				}
	 				while(loggedIn) {
	 					
	 					//deposit
	 					if(message.getType().equals("deposit") && loggedIn == true) {
	 						if(message.getWho().equals("employee")) {
	 							double newbal = deposit(checkBalance(message.getAccNum()), message.getAmount());
	 							customerRecords.get(message.getAccNum()).setBalance(newbal);
	 							message.setStatus("success..." + message.getAmount() + " amount deposited.");
	 	 					}
	 						if(message.getWho().equals("ATM")) {
		 						
		 						double newbal = deposit(checkBalance(message.getDebit().getTiedAccount()), message.getAmount());
		 						//change balance
								customerRecords.get(message.getDebit().getTiedAccount()).setBalance(newbal);
								message.setStatus("success..." + message.getAmount() + " amount deposited.");
		 	 					
		 	 				}
		 	 					// sends message back to client with changes
		 	 				serverOutputStream.writeObject(message);

	 					}
	 					
	 					// if message object is withdraw message, it runs this block
	 					if(message.getType().equals("withdraw") && loggedIn == true) {
	 						if(message.getWho().equals("employee")) {
	 							if(checkBalance(message.getAccNum()) < 0) {
	 								message.setStatus("denied... get your money up, not your funny up.");
	 							}
	 							else {
	 							//change balance
	 								double newbal = withdraw(checkBalance(message.getAccNum()), message.getAmount());
	 								customerRecords.get(message.getAccNum()).setBalance(newbal);
	 								message.setStatus("success..." + message.getAmount() + " amount withdrawn.");
	 	 						}
	 	 					}
	 	 					
	 						if(message.getWho().equals("ATM")) {
	 							if(checkBalance(message.getDebit().getTiedAccount()) < 0) {
	 								message.setStatus("denied... get your money up, not your funny up.");
	 							}
	 							else if(message.getAmount() >= 1000){
	 								message.setStatus("denied... Go see clerk.");
	 							}
	 							else {
	 								double newbal = withdraw(checkBalance(message.getDebit().getTiedAccount()), message.getAmount());
	 								//change balance
	 								customerRecords.get(message.getDebit().getTiedAccount()).setBalance(newbal);
	 								message.setStatus("success..." + message.getAmount() + " amount withdrawn.");
	 	 						}
	 	 					}
	 	 					// sends message back to client with changes
	 	 					serverOutputStream.writeObject(message);

	 					}
	 					
	 				// if message object is check balance message, it runs this block
	 					if(message.getType().equals("check balance") && loggedIn == true) {
	 						if(message.getWho().equals("employee")) {
	 								message.setStatus("success..." + checkBalance(message.getAccNum()) + " in account.");
	 	 						}
	 	 					
	 						if(message.getWho().equals("ATM")) {
	 								message.setStatus("success..." + checkBalance(message.getDebit().getTiedAccount()) + " in account.");
	 	 					}
	 	 					// sends message back to client with changes
	 	 					serverOutputStream.writeObject(message);

	 					}
	 					
	 				// if message object is transfer message, it runs this block
	 					if(message.getType().equals("transfer") && loggedIn == true) {
	 							if(checkBalance(message.getAccNum()) < message.getAmount()) {
	 								message.setStatus("denied... get your money up, not your funny up.");
	 							}
	 							else {
	 							//change balance
	 								double newbal = withdraw(checkBalance(message.getAccNum()), message.getAmount());
	 								customerRecords.get(message.getAccNum()).setBalance(newbal);
	 								customerRecords.get(message.getTAcc()).setBalance(message.getAmount());
	 								message.setStatus("success..." + message.getAmount() + " amount transfered" + " to " + message.getTAcc());
	 	 						}
	 	 					// sends message back to client with changes
	 	 					serverOutputStream.writeObject(message);

	 					}
	 					
	 				// if message object is create account message, it runs this block
	 					if(message.getType().equals("create account") && loggedIn == true) {
	 						//change balance
	 						customerRecords.put(message.getMember().getAccountNumber(), message.getMember());
	 						message.setStatus("success... account created.");
	 						
	 	 					// sends message back to client with changes
	 	 					serverOutputStream.writeObject(message);

	 					}
	 					
	 				// if message object is delete account message, it runs this block
	 					if(message.getType().equals("delete account") && loggedIn == true) {
	 						//change balance
	 						customerRecords.remove(message.getMember().getAccountNumber());
	 						message.setStatus("success... account removed.");
	 						
	 	 					// sends message back to client with changes
	 	 					serverOutputStream.writeObject(message);
	 	 					
	 					}
	 					
		 				// if message object is check logs message, it runs this block
	 					if(message.getType().equals("checklogs") && loggedIn == true) {
	 						

	 					}
	 					
	 	 			// if message object is logout message, it runs this block
	 					if(message.getType().equals("logout") && loggedIn == true) {

	 	 				// change login status to success in message
	 	 				// from client
	 						String type = "success";
	 						message.setStatus(type);
	  					
	 	 				// sends message back to client with changes
	 						serverOutputStream.writeObject(message);
	 						loggedIn = false;
	 	 					
	 						//break;
	 					}
	 				
	 				// closes connection to client from server
					clientSocket.close();
	 				}
	 			}
	 			catch (IOException e) {
	 				e.printStackTrace();
	 			} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		}
	 		
	}
	 	public static double deposit(double balance, double addAmount) 
		
		{
				balance = balance + addAmount;				
				return balance;
		}
			
			
		//withdraw the requested amount and update balance
		public static double withdraw(double balance,  double withdrawAmount) {
				
			balance = balance - withdrawAmount;
			return balance;
		}
			
			
			//check balance of given card number
		public static double checkBalance(int mem) {
				double balance = customerRecords.get(mem).getBalance(); 
				return balance;
				
		}
	 	
 	public void loadCustomer(String filename) {
		customerSource = filename;
		try {
			File file = new File(filename);
			Scanner scan = new Scanner(file);
			scan.useDelimiter("/");
			while(scan.hasNextLine()) {
				//String temp = scan.next();
				String accNum = scan.next();
				String pinNum = scan.next();
				String routNum = scan.next();
				String debCNum = scan.next();
				String legname = scan.next();
				String age = scan.next();
				String gender = scan.next();
				String address = scan.next();
				String social = scan.next();
				String legalID = scan.next();
				String balance = scan.next();
				
				customerRecords.put(Integer.parseInt(accNum), Member(Integer.parseInt(pinNum), Integer.parseInt(routNum), 
						Integer.parseInt(debCNum),legname,Integer.parseInt(age), gender, address, Integer.parseInt(social), 
						legalID, Double.parseDouble(balance)));
			}
			scan.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}

	}
 	
 	public void saveCustomers() {
		try {
			FileWriter file = new FileWriter(customerSource);
			for(int i = 0; i < customerRecords.size(); i++) {
				file.write(toStringCustomer(customerRecords.get(i)));
			}
		file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
 	
 	public String toStringCustomer(Member m) {
 		
		String info = "";
		info += m.getAccountNumber();
		info += "/";
		info += m.getRoutingNumber();
		info += "/";
		info += m.getDebitCardNumber();
		info += "/";
		info += m.getLegalName();
		info += "/";
		info += m.getAge();
		info += "/";
		info += m.getGender();
		info += "/";
		info += m.getAddress();
		info += "/";
		info += m.getSocialSecurity();
		info += "/";
		info += m.getLegalIDNum();
		info += "/";
		info += m.getBalance();
		
		return info;
	}
 	
 	
 	public void loadEmployee(String filename) {
		employeeSource = filename;
		try {
			File file = new File(filename);
			Scanner scan = new Scanner(file);
			scan.useDelimiter("/");
			while(scan.hasNextLine()) {
				//String temp = scan.next();
				int empNum = Integer.parseInt(scan.next());
				String empPass = scan.next();
				
				bankWorkers.put(empNum, Employee(empPass);
			}
			scan.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
 	
 	public void saveEmployees() {
		try {
			FileWriter file = new FileWriter(customerSource);
			for(int i = 0; i < customerRecords.size(); i++) {
				file.write(toStringEmployee(bankWorkers.get(i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
 	
 	public String toStringEmployee(Employee e) {
 		
		String info = "";
		info += e.getEmployeeNumber();
		info += "/";
		info += e.getEmployeePassword();
		info += "/";
		return info;
	}
 	
}
