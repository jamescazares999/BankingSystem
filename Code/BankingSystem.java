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
      String customerFile = System.getProperty("user.dir") + "\\src\\" + customerSource;
      loadCustomer(customerFile);
      String employeeFile = System.getProperty("user.dir") + "\\src\\" + employeeSource;
      loadEmployee(employeeFile);
      String debitFile = System.getProperty("user.dir") + "\\src\\" + debitCardSource;
      loadEmployee(debitFile);
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
        // System.out.println("Client Disconnected");
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

        //message = (Message) serverInputStream.readObject();
        //message.getWho();

        // makes sure that client is logged in first before sending text or logout message(s)
        boolean loggedIn = false;
        int logFailcounter = 0;

        Employee currently;
        Member now = null;
        // infinite loop so that any number of messages from the client is received before stopping at logout
        // loggedIn == false && logFailcounter < 2
        while (loggedIn == false) {

          // recast to message object from inputStream
          message = (Message) serverInputStream.readObject();

          // if message object is login message, it runs this block

          if (message.getWho().equals("employee")) {
            //Employee work = (Employee) message.getEmp();

            if (bankWorkers.containsKey(message.getEmpLogin())) {
              currently = (Employee) bankWorkers.get(message.getEmpLogin());
              if (currently.getEmployeePassword() == message.getEmpPassword()) {
                message.setStatus("Success");
                loggedIn = true;
              } else {
                message.setStatus("Failed... Wrong Password");
                //logFailcounter++;
                loggedIn = true;
              }
            } else {
              message.setStatus("Failed... Pin doesn't exist");
              loggedIn = true;
            }
          }

          if (message.getWho().equals("ATM")) {
        	  System.out.println("Detected ATM");
            int debitCardNum = (int) message.getAccNumOrBal();
            int SecCode = (int) message.getTransferBalOrRegBalance();
            if (debitCardVerify.containsKey(debitCardNum)) {
              now = (Member) customerRecords.get(debitCardVerify.get(debitCardNum).getTiedAccount());
              if (debitCardVerify.get(debitCardNum).getSecurityCode() == SecCode) {
                  String passAccNum = String.valueOf(now.getAccountNumber());
                  message.setStatus(passAccNum);
                  System.out.println("Client Logged in");
                  loggedIn = true;
                } else {
                  message.setStatus("Failed... Wrong Pin Number");
                  //logFailcounter++;
                }
            } else {
              message.setStatus("Failed... DebitCard not in System");
            }
          }
          // change login type status to success in message
          // from client
          // serverOutputStream.reset();
          // sends message back to client with changes
          serverOutputStream.writeObject(message);
          //System.out.println("Success");

        }
        while (loggedIn) {
        	message = (Message) serverInputStream.readObject();
        	System.out.println("Made it to logged in while loop");
          //deposit
          if (message.getType().equals("deposit")) {
            if (message.getWho().equals("Employee")) {
              int AccNum = Integer.parseInt(message.getToWho());
              double addBal = message.getTransferBalOrRegBalance();
              double newBal = deposit(checkBalance(AccNum), addBal);
              customerRecords.get(message.getAccNumOrBal()).setBalance(newBal);
              message.setAccNumOrBal(addBal);
              message.setStatus("Success...");
            }
            if (message.getWho().equals("ATM")) {
              int debCard = (int) message.getAccNumOrBal();
              double addBal = message.getTransferBalOrRegBalance();
              double newbal = deposit(checkBalance(debitCardVerify.get(debCard).getTiedAccount()), addBal);
              //change balance
              customerRecords.get(debCard).setBalance(newbal);
              message.setStatus("success..." + addBal + " amount deposited.");

            }
            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);

          }

          // if message object is withdraw message, it runs this block
          if (message.getType().equals("withdraw")) {
            if (message.getWho().equals("Employee")) {
              if (checkBalance(Integer.parseInt(message.getToWho())) < 0) {
                message.setStatus("Denied... Insufficient Funds.");
              } else {
                //change balance
                double withdrawn = message.getTransferBalOrRegBalance();
                double newbal = withdraw(checkBalance(Integer.parseInt(message.getToWho())), withdrawn);
                customerRecords.get(Integer.parseInt(message.getToWho())).setBalance(newbal);
                message.setStatus("success..." + withdrawn + " amount withdrawn.");
              }
            }

            if (message.getWho().equals("ATM")) {
              int AccNum = debitCardVerify.get((int) message.getAccNumOrBal()).getTiedAccount();
              if (checkBalance(AccNum) < 0) {
                message.setStatus("denied... get your money up, not your funny up.");
              } else if (message.getTransferBalOrRegBalance() >= 1000) {
                message.setStatus("denied... Go see clerk.");
              } else {
                double withdrawn = message.getTransferBalOrRegBalance();
                double newbal = withdraw(checkBalance(AccNum), withdrawn);
                //change balance
                customerRecords.get(AccNum).setBalance(newbal);
                message.setStatus("success..." + withdrawn + " amount withdrawn.");
              }
            }
            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);

          }

          // if message object is check balance message, it runs this block
          if (message.getType().equals("check balance")) {
            if (message.getWho().equals("Employee")) {
            	double d = checkBalance(Integer.parseInt(message.getToWho()));
              message.setStatus("success..." + checkBalance((int) message.getAccNumOrBal()) + " in account.");
              message.setAccNumOrBal(d);
            }

            if (message.getWho().equals("ATM")) {

              message.setStatus("success...");
              message.setToWho(checkBalance(debitCardVerify.get((int) message.getAccNumOrBal()).getTiedAccount()) + " in account.");
            }
            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);

          }

          // if message object is transfer message, it runs this block
          if (message.getType().equals("transfer")) {
            if (checkBalance((int) message.getAccNumOrBal()) < message.getTransferBalOrRegBalance()) {
              message.setStatus("denied... insufficient funds.");
            } else {
              //change balance
              int AccNum = (int) message.getAccNumOrBal();
              double newbal = withdraw(checkBalance(AccNum), message.getTransferBalOrRegBalance());
              customerRecords.get(AccNum).setBalance(newbal);
              customerRecords.get(Integer.parseInt(message.toWho())).setBalance(message.getTransferBalOrRegBalance());
              message.setStatus("success..." + message.getTransferBalOrRegBalance() + " amount transfered" + " to " + Integer.parseInt(message.toWho()));
            }
            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);

          }

          // if message object is create account message, it runs this block
          if (message.getType().equals("create account")) {
            //change balance
            customerRecords.put(message.getMember().getAccountNumber(), message.getMember());
            message.setStatus("success... account created.");

            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);

          }

          // if message object is delete account message, it runs this block
          if (message.getType().equals("close account")) {
            //change balance
            customerRecords.remove(message.getAccNumOrBal());
            message.setStatus("success... account removed.");

            // sends message back to client with changes
            //serverOutputStream.reset();
            serverOutputStream.writeObject(message);

          }

          // if message object is check logs message, it runs this block
          //if(message.getType().equals("checklogs") && loggedIn == true) {
          //	
          //
          //}

          // if message object is logout message, it runs this block
          if (message.getType().equals("logout")) {

            // change login status to success in message
            // from client
            message.setStatus("success");

            // sends message back to client with changes
            serverOutputStream.writeObject(message);
            loggedIn = false;

            //break;
          }

          // closes connection to client from server
          clientSocket.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }
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

  public static void loadCustomer(String filename) {
		customerSource = filename;
		customerRecords.put(1, new Member(123 , "nem", 21, "male",
				"address" , 100000.25));
//		
//		try {
//			File file = new File(filename);
//			Scanner scan = new Scanner(file);
//			scan.useDelimiter("/");
//			while(scan.hasNextLine()) {
//				//String temp = scan.next();
//				String accNum = scan.next();
//				//String pinNum = scan.next();
//				//String routNum = scan.next();
//				String debCNum = scan.next();
//				String legname = scan.next();
//				String age = scan.next();
//				String gender = scan.next();
//				String address = scan.next();
//				//String social = scan.next();
//				//String legalID = scan.next();
//				String balance = scan.next();

				//customerRecords.put(Integer.parseInt(accNum), new Member( Integer.parseInt(debCNum),legname,Integer.parseInt(age), gender,
				//		address, Double.parseDouble(balance)));


			//}
			//scan.close();
		//}
		//catch(FileNotFoundException e) {
		//	e.printStackTrace();
		//}

	}

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

  public static String toStringCustomer(Member m) {

    String info = "";
    info += m.getAccountNumber();
    info += "/";
    //info += m.getRoutingNumber();
    //info += "/";
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
    //info += m.getSocialSecurity();
    //info += "/";
    //info += m.getLegalIDNum();
    //info += "/";
    info += m.getBalance();

    return info;
  }

  public static void loadEmployee(String filename) {
		employeeSource = filename;
//		try {
//			File file = new File(filename);
//			Scanner scan = new Scanner(file);
//			scan.useDelimiter("/");
//			while(scan.hasNextLine()) {
//				//String temp = scan.next();
//				String empLog = scan.next();
//				String empPass = scan.next();
//				
//				bankWorkers.put(empLog, new Employee(empPass, empLog));
//			}
//			scan.close();
//		}
//		catch(FileNotFoundException e) {
//			e.printStackTrace();
//		}
		try {
			bankWorkers.put("testrun", new Employee("testRun", "password"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

  public static String toStringEmployee(Employee e) {

    String info = "";
    info += e.getEmployeeNumber();
    info += "/";
    info += e.getEmployeePassword();
    return info;
  }

  public static void loadDebitCards(String filename) {
		debitCardSource = filename;
//		try {
//			File file = new File(filename);
//			Scanner scan = new Scanner(file);
//			scan.useDelimiter("/");
//			while(scan.hasNextLine()) {
//				//String temp = scan.next();
//				String CardNumber = scan.next();
//				String SecurityCode = scan.next();
//				String TiedAccount = scan.next();
//				String CreatedDate = scan.next();
//				String ExpiryDate = scan.next();
//				String NameOnCard = scan.next();
//				
//				//debitCardVerify.put(Integer.parseInt(CardNumber), new DebitCard(Integer.parseInt(CardNumber), Integer.parseInt(SecurityCode),
//				//		customerRecords.get(Integer.parseInt(TiedAccount))));
//				//debitCardVerify.get(Integer.parseInt(CardNumber)).setCreatedDate(LocalDate.parse(CreatedDate));
//				//debitCardVerify.get(Integer.parseInt(CardNumber)).setExpiryDate(LocalDate.parse(ExpiryDate));
//				//debitCardVerify.get(Integer.parseInt(CardNumber)).setNameOnCard(NameOnCard);
//			}
//			scan.close();
//		}
//		catch(FileNotFoundException e) {
//			e.printStackTrace();
//		}
		debitCardVerify.put(123, new DebitCard(123, 123, customerRecords.get(1)));
	}

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

// 	public static void loadCustomer(String filename) {
//		customerSource = filename;
//		customerRecords.put(1, new Member(43772822 , "nem",21, "male",
//				"address" , 100000.25));
////		
////		try {
////			File file = new File(filename);
////			Scanner scan = new Scanner(file);
////			scan.useDelimiter("/");
////			while(scan.hasNextLine()) {
////				//String temp = scan.next();
////				String accNum = scan.next();
////				//String pinNum = scan.next();
////				//String routNum = scan.next();
////				String debCNum = scan.next();
////				String legname = scan.next();
////				String age = scan.next();
////				String gender = scan.next();
////				String address = scan.next();
////				//String social = scan.next();
////				//String legalID = scan.next();
////				String balance = scan.next();
//				
//				//customerRecords.put(Integer.parseInt(accNum), new Member( Integer.parseInt(debCNum),legname,Integer.parseInt(age), gender,
//				//		address, Double.parseDouble(balance)));
//				
//				
//			//}
//			//scan.close();
//		//}
//		//catch(FileNotFoundException e) {
//		//	e.printStackTrace();
//		//}
//
//	}
// 	
// 	public static void saveCustomers() {
//		try {
//			FileWriter file = new FileWriter(customerSource);
//			for(int i = 0; i < customerRecords.size(); i++) {
//				file.write(toStringCustomer(customerRecords.get(i)));
//			}
//		file.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
// 	
// 	public static String toStringCustomer(Member m) {
// 		
//		String info = "";
//		info += m.getAccountNumber();
//		info += "/";
//		//info += m.getRoutingNumber();
//		//info += "/";
//		info += m.getDebitCardNumber();
//		info += "/";
//		info += m.getLegalName();
//		info += "/";
//		info += m.getAge();
//		info += "/";
//		info += m.getGender();
//		info += "/";
//		info += m.getAddress();
//		info += "/";
//		//info += m.getSocialSecurity();
//		//info += "/";
//		//info += m.getLegalIDNum();
//		//info += "/";
//		info += m.getBalance();
//		
//		return info;
//	}
// 	
// 	
// 	public static void loadEmployee(String filename) {
//		employeeSource = filename;
////		try {
////			File file = new File(filename);
////			Scanner scan = new Scanner(file);
////			scan.useDelimiter("/");
////			while(scan.hasNextLine()) {
////				//String temp = scan.next();
////				String empLog = scan.next();
////				String empPass = scan.next();
////				
////				bankWorkers.put(empLog, new Employee(empPass, empLog));
////			}
////			scan.close();
////		}
////		catch(FileNotFoundException e) {
////			e.printStackTrace();
////		}
//		try {
//			bankWorkers.put("testrun", new Employee("testRun", "password"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
// 	
// 	public static void saveEmployees() {
//		try {
//			FileWriter file = new FileWriter(customerSource);
//			bankWorkers.forEach((key, value) -> {
//				try {
//					file.write(toStringEmployee(value));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
// 	
// 	public static String toStringEmployee(Employee e) {
// 		
//		String info = "";
//		info += e.getEmployeeNumber();
//		info += "/";
//		info += e.getEmployeePassword();
//		return info;
//	}
// 	
// 	public static void loadDebitCards(String filename) {
//		debitCardSource = filename;
////		try {
////			File file = new File(filename);
////			Scanner scan = new Scanner(file);
////			scan.useDelimiter("/");
////			while(scan.hasNextLine()) {
////				//String temp = scan.next();
////				String CardNumber = scan.next();
////				String SecurityCode = scan.next();
////				String TiedAccount = scan.next();
////				String CreatedDate = scan.next();
////				String ExpiryDate = scan.next();
////				String NameOnCard = scan.next();
////				
////				//debitCardVerify.put(Integer.parseInt(CardNumber), new DebitCard(Integer.parseInt(CardNumber), Integer.parseInt(SecurityCode),
////				//		customerRecords.get(Integer.parseInt(TiedAccount))));
////				//debitCardVerify.get(Integer.parseInt(CardNumber)).setCreatedDate(LocalDate.parse(CreatedDate));
////				//debitCardVerify.get(Integer.parseInt(CardNumber)).setExpiryDate(LocalDate.parse(ExpiryDate));
////				//debitCardVerify.get(Integer.parseInt(CardNumber)).setNameOnCard(NameOnCard);
////			}
////			scan.close();
////		}
////		catch(FileNotFoundException e) {
////			e.printStackTrace();
////		}
//		debitCardVerify.put(43772822, new DebitCard(43772822, 772, customerRecords.get(1)));
//	}
// 	
// 	public static void saveDebitCards(){
//		try {
//			FileWriter file = new FileWriter(debitCardSource);
//			debitCardVerify.forEach((key, value) -> {
//				try {
//					file.write(toStringDebitCards(value));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
// 	
// 	public static String toStringDebitCards(DebitCard cd) {
//		String info = "";
//		info += cd.getCardNumber();
//		info += "/";
//		info += cd.getSecurityCode();
//		info += "/";
//		info += cd.getTiedAccount();
//		info += "/";
//		info += cd.getCreatedDate();
//		info += "/";
//		info += cd.getExpiryDate();
//		info += "/";
//		info += cd.getNameOnCard();
//		return info;
//	}
// 	
//}
