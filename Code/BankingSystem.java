import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class BankingSystem {
  /** The name of the data file that contains customer data */
  private static String customerSource = "CustomerFile.txt";
  private static String employeeSource = "EmployeeFile.txt";
  private static String debitCardSource = "DebitFile.txt";

  private InetAddress IP;
  private static int port = 1111;
  
  //Used for holding all customer and employee data 
  private static HashMap < String, Employee > bankWorkers = new HashMap < String, Employee > ();
  private static HashMap < Integer, Member > customerRecords = new HashMap < Integer, Member > ();
  private static HashMap < Integer, DebitCard > debitCardVerify = new HashMap < Integer, DebitCard > ();

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
        try {
          saveDebitCards();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }, new Date().getTime(), 300000);

    try {
      BankingServer = new ServerSocket(port);
      BankingServer.setReuseAddress(true);
      
      /*
       * Server loads files from directory into respective data hashMaps for holding those objects.
       * Loads customer data, employee data, and debit card data into respective objects
       * */
      String customerFile = System.getProperty("user.dir") + "\\src\\" + customerSource;
      loadCustomer(customerFile);
      String employeeFile = System.getProperty("user.dir") + "\\src\\" + employeeSource;
      loadEmployee(employeeFile);
      String debitFile = System.getProperty("user.dir") + "\\src\\" + debitCardSource;
      loadDebitCards(debitFile);
      
      
      // running infinite loop for getting
      // client request
      while (true) {

        // socket object to receive incoming client
        // requests
        Socket client = BankingServer.accept();

        // Displaying that new client is connected
        // to server
        System.out.println("New client connected " +
          client.getInetAddress()
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

    } catch (IOException e) {
      e.printStackTrace();

    }
  }

  // ClientHandler class
  private static class ClientHandler implements Runnable {
    private final Socket clientSocket;

    // Constructor
    public ClientHandler(Socket socket) {
      this.clientSocket = socket;
    }
    public void run() {
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
      
      System.out.println("Client Made it to RUN");

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

        // makes sure that client is logged in first before handling requested data information or logout message(s)
        // if a login fails twice from incorrect login or passcode, connection is closed.
        boolean loggedIn = false;
        int logFailcounter = 0;

        Employee currently;
        Member now = null;
        
        
        // infinite loop so that any number of messages from the client is received before stopping at logout
        // loggedIn == false && logFailcounter < 2
        while (loggedIn == false && logFailcounter < 2) {

          // recast to message object from inputStream
          message = (Message) serverInputStream.readObject();

          // if message object is login message, it runs this block

          if (message.getWho().equals("employee")) {
        	  
        	  //checks to see that employee login exists in the records
            if (bankWorkers.containsKey(message.getEmpLogin())) {
              currently = (Employee) bankWorkers.get(message.getEmpLogin());
              
              //retrieves employee password if account exists for employee, then compares to password sent from message
              if (currently.getEmployeePassword() == message.getEmpPassword()) {
                
            	  //logins in employee if true
            	  message.setStatus("Success");
            	  loggedIn = true;
              } else {
            	  
            	  //requests password another time if incorrect
            	  message.setStatus("Failed... Wrong Password");
            	  logFailcounter++;
              }
            } else {
            	
            	//requests logID that exists
            	message.setStatus("Failed... LogID doesn't exist");
            }
          }

          //if message is from atm
          if (message.getWho().equals("ATM")) {
        	  System.out.println("Detected ATM");
        	  
        	  // Gets debitCard Number and Security Code from message
        	  int debitCardNum = Integer.parseInt(message.getToWho()); 
        	  int SecCode = Integer.parseInt(message.getStatus());
        	  
        	  System.out.println(debitCardNum + " " + SecCode);
        	  
        	  System.out.println(debitCardVerify.containsKey(debitCardNum));
        	  
        	  //Verify if debit card exists.
        	  if (debitCardVerify.containsKey(debitCardNum)) {
        		  System.out.println("Verying Card...");
        		  
        		  //Obtain Member information if debit card exists
        		  //Account number thats linked to the debit card is retrieved to get customer data
        		  now = (Member) customerRecords.get(debitCardVerify.get(debitCardNum).getTiedAccount());
        		  
        		  //verifies security code
        		  if (debitCardVerify.get(debitCardNum).getSecurityCode() == SecCode) {
        			  
        			  //if security code is correct, customer account number is sent back to atm.
        			  //client is logged in
        			  String passAccNum = String.valueOf(now.getAccountNumber());
        			  message.setStatus(passAccNum);
        			  loggedIn = true;
        			  System.out.println("Client Logged in");
     
        		  } else {
        			  
        			  //requests secuirty another time if incorrect
        			  message.setStatus("Failed... Wrong Pin Number");
        			  logFailcounter++;
        		  }
        	  } else {
        		  
        		  //requests debitCard number that exists
        		  message.setStatus("Failed... DebitCard not in System");
        	 }
          }
          
          // change login type status to success in message
          // from client
          // serverOutputStream.reset();
          
          
          // sends message back to client with changes
          serverOutputStream.writeObject(message);
          serverOutputStream.reset();

        }
        
        //Main loop for operations
        /*
         * Withdraw requests 
         * Deposit requests
         * Check Balance requests 
         * Transfer requests
         * Logout requests 
         * */
        while (loggedIn) {
        	
        	  message = (Message) serverInputStream.readObject();
        	  
          //Member object will hold balance for checkings and savings 
        	
          //deposit
          if (message.getType().equals("deposit")) {
        	  
        	  //Checks who its from
            if (message.getWho().equals("Employee")) {
            	
            	//Gets account number of customer from message
            	//Gets balance to add from message
            	//Gets new balance from deposit function
              int AccNum = Integer.parseInt(message.getToWho());
              double addBal = message.getTransferBalOrRegBalance();
              double newBal = deposit(checkBalance(AccNum), addBal);
              
              //updates that account number customers' records with now balance
              customerRecords.get(Integer.parseInt(message.getToWho())).setBalance(newBal);
              message.setAccNumOrBal(addBal);
              message.setStatus("Success...");
            }
            
            
            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);
            serverOutputStream.reset();

          }

          // if message object is withdraw message, it runs this block
          if (message.getType().equals("withdraw")) {
            if (message.getWho().equals("Employee")) {
            	
            	//Makes sure balance is greater than 0
              if (checkBalance(Integer.parseInt(message.getToWho())) < message.getTransferBalOrRegBalance()) {
            	  
            	  //if not deny request
            	  message.setStatus("Denied... Insufficient Funds.");
            	  
              } else {
            	  
                //change balance if balance is less than or equal to withdraw amount
            	  //Get withdraw amount from message
            	  //Get new balance from withdraw function
                double withdrawn = message.getTransferBalOrRegBalance();
                double newbal = withdraw(checkBalance(Integer.parseInt(message.getToWho())), withdrawn);
                
                //Update that customer's balance with new bal
                customerRecords.get(Integer.parseInt(message.getToWho())).setBalance(newbal);
                
                //sends back withdrawn amount
                message.setAccNumOrBal(withdrawn);
                message.setStatus("Success...");
                //message.setStatus("success..." + withdrawn + " amount withdrawn.");
              }
            }
            
            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);
            serverOutputStream.reset();

          }

          // if message object is check balance message, it runs this block
          if (message.getType().equals("check balance")) {
            if (message.getWho().equals("Employee")) {
            	
            	//Get account Number from message, then use it to get the accounts balance
            	double d = checkBalance(Integer.parseInt(message.getToWho()));
            	
            	//send back amount in message
            	message.setStatus("success..." + checkBalance(Integer.parseInt(message.getToWho())) + " in account.");
            	message.setAccNumOrBal(d);
            }

            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);
            serverOutputStream.reset();

          }

          /*
           * 
           * 
           * 	THIS NEEDS EDITING/ EMPLOYEE INTERFACE NEEDS TO BE FINISHED FIRST
           * 
           * */
          // if message object is transfer message, it runs this block
          if (message.getType().equals("transfer")) {
            if (checkBalance(Integer.parseInt(message.getStatus())) < message.getTransferBalOrRegBalance()) {
            	message.setStatus("denied... insufficient funds.");
            } else {
              //change balance
              int AccNum = Integer.parseInt(message.getStatus());
              double newbal = withdraw(checkBalance(AccNum), message.getTransferBalOrRegBalance());
              customerRecords.get(AccNum).setBalance(newbal);
              customerRecords.get(Integer.parseInt(message.toWho())).setBalance(message.getTransferBalOrRegBalance());
              
              message.setStatus("success..." + message.getTransferBalOrRegBalance() + " amount transfered" + " to " + Integer.parseInt(message.toWho()));
              
              //AccNum used to send back amount transfered.
              message.setAccNumOrBal(message.getTransferBalOrRegBalance());
            }
            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);
            serverOutputStream.reset();

          }


          //Member object will hold balance for checkings and savings 
          // if message object is create account message, it runs this block
          if (message.getType().equals("create account")) {
        	  
        	 //creates account from member object sent from Employee
            customerRecords.put(message.getMember().getAccountNumber(), message.getMember());
            message.setStatus("success... account created.");

            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);
            serverOutputStream.reset();
            
          }

          // if message object is delete account message, it runs this block
          if (message.getType().equals("close account")) {
        	  
            //deletes account sent by message.
            customerRecords.remove(message.getAccNumOrBal());
            message.setStatus("success... account removed.");

            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);
            serverOutputStream.reset();
            
          }
          

          // if message object is logout message, it runs this block
          if (message.getType().equals("logout")) {

            // change login status to success in message
            // from client
            message.setStatus("success");

            // sends message back to client with changes
            serverOutputStream.writeObject(message);
           break;

            //break;
          }
        }
     // closes connection to client from server
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  //returns new balance after deposit
  public static double deposit(double balance, double addAmount){
    balance = balance + addAmount;
    return balance;
  }

  //withdraw the requested amount and update balance
  public static double withdraw(double balance, double withdrawAmount) {
    balance = balance - withdrawAmount;
    return balance;
  }

  //check balance of given card number
  public static double checkBalance(int mem) {
    double balance = customerRecords.get(mem).getBalance();
    return balance;
  }

  //Loads all Customers into hashmap data storage
  public static void loadCustomer(String filename) {
		customerSource = filename;
			
			try {
				File file = new File(filename);
				Scanner scan = new Scanner(file);
				scan.useDelimiter("/");
				while(scan.hasNextLine()) {

					String accNum = scan.next();
					String debCNum = scan.next();
					String legname = scan.next();
					String age = scan.next();
					String gender = scan.next();
					String address = scan.next();
					String balance = scan.next();
					
					customerRecords.put(Integer.parseInt(accNum), new Member( Integer.parseInt(debCNum),legname,Integer.parseInt(age), gender,
							address, Double.parseDouble(balance)));
					
					
				}
				scan.close();
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}

	}

  //Saves all info of Customers row by row in string format 
  public static void saveCustomers() {
    try {
      FileWriter file = new FileWriter(customerSource);
      for (int i = 0; i < customerRecords.size(); i++) {
        file.write(toStringCustomer(customerRecords.get(i)));
      }
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  //Saves all info of Customers in string format 
  public static String toStringCustomer(Member m) {

    String info = "";
    info += m.getAccountNumber();
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
    info += m.getBalance();

    return info;
  }

  //Loads all Employees into hashmap data storage
  public static void loadEmployee(String filename) {
		employeeSource = filename;
		try {
			File file = new File(filename);
			Scanner scan = new Scanner(file);
			scan.useDelimiter("/");
			while(scan.hasNextLine()) {
				//String temp = scan.next();
				String empLog = scan.next();
				String empPass = scan.next();
				
				try {
					bankWorkers.put(empLog, new Employee(empPass, empLog));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			scan.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

//Saves all info of Employees row by row in string format 
  public static void saveEmployees() {
    try {
      FileWriter file = new FileWriter(customerSource);
      bankWorkers.forEach((key, value) -> {
        try {
          file.write(toStringEmployee(value));
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  
  //Saves all info of Employees in string format 
  public static String toStringEmployee(Employee e) {

    String info = "";
    info += e.getEmployeeNumber();
    info += "/";
    info += e.getEmployeePassword();
    return info;
  }

  //Loads all Debits cards into hashmap system for recored keeping
  public static void loadDebitCards(String filename) {
		debitCardSource = filename;
		try {
			File file = new File(filename);
			Scanner scan = new Scanner(file);
			scan.useDelimiter("/");
			while(scan.hasNextLine()) {
				String CardNumber = scan.next();
				String SecurityCode = scan.next();
				String TiedAccount = scan.next();
				String CreatedDate = scan.next();
				String ExpiryDate = scan.next();
				String NameOnCard = scan.next();
				
				debitCardVerify.put(Integer.parseInt(CardNumber), new DebitCard(Integer.parseInt(CardNumber), Integer.parseInt(SecurityCode),
						customerRecords.get(Integer.parseInt(TiedAccount))));
				debitCardVerify.get(Integer.parseInt(CardNumber)).setCreatedDate(LocalDate.parse(CreatedDate));
				debitCardVerify.get(Integer.parseInt(CardNumber)).setExpiryDate(LocalDate.parse(ExpiryDate));
				debitCardVerify.get(Integer.parseInt(CardNumber)).setNameOnCard(NameOnCard);
			}
			scan.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

  //Saves all info of Debit Cards row by row in string format 
  public static void saveDebitCards() throws IOException {
    try {
      FileWriter file = new FileWriter(debitCardSource);
      debitCardVerify.forEach((key, value) -> {
        try {
          file.write(toStringDebitCards(value));
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  // Saves all info of Debit Cards in string format
  public static String toStringDebitCards(DebitCard cd) {
    String info = "";
    info += cd.getCardNumber();
    info += "/";
    info += cd.getSecurityCode();
    info += "/";
    info += cd.getTiedAccount();
    info += "/";
    info += cd.getCreatedDate();
    info += "/";
    info += cd.getExpiryDate();
    info += "/";
    info += cd.getNameOnCard();
    return info;
  }

}
