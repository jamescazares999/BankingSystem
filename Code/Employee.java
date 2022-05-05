import java.io.*;
import java.net.*;

public class Employee {

  private static int employeeNumber = 0;
  private String username;
  private String employeePassword;
  private Socket host;
  protected OutputStream out;
  protected InputStream in ;
  protected ObjectOutputStream objOut;
  protected ObjectInputStream objIn;
  private Message message;

  Employee(String password, String user) throws UnknownHostException, IOException {
    Employee.employeeNumber++;
    this.employeePassword = password;
    this.username = user;
  }

  public Employee() {
    // TODO Auto-generated constructor stub
  }

  //Connect to server
  public void connectToHost() throws UnknownHostException, IOException {
    this.host = new Socket("localhost", 1111);
    this.in = host.getInputStream();
    this.out = host.getOutputStream();
    this.objOut = new ObjectOutputStream(out);
    this.objIn = new ObjectInputStream( in );
  }
  //login

  /*Send Employee():
   *  send an Employee information to server for login
   *  returns string success for Interface to handle 
   *  returns nothing if fail to receive message
   */
  public String sendEmployee() throws IOException, ClassNotFoundException {
    message = new Message("login", "Employee", 0, 0, username, employeePassword); // use the proper for connection
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();
    // if logged in it returns what to verify?// what if it fails?
    String status = " ";
    if (message.getStatus().equals("success")) {
      status = "success";
    }
    return status;
  }
  //NEW Member
  /*
   * !!!!Not yet decided!!!!****!!!!!****!!!!!!!
   */

//  public String newMember() { // stub
//
//    return message.getStatus();
//  }

  //open an existing account

  /*
   * openAccount :
   * sends Message to open new account for Member class sent
   * 
   * closeAccount :
   * sends Message to close account from Member class sent
   * 
   * accessAccount:
   * sends Message to get Account to update from Member class sent
   */
  
  // The term account here referes to checkings and savings accounts
  public String openAccount(String type, String status, Member mem, String who) throws IOException, ClassNotFoundException {
    message = new Message("openAccount", "Undefined", mem, "Employee");
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();
    return message.getStatus();
  }
  public String closeAccount(String who, Member mem) throws IOException, ClassNotFoundException {
    message = new Message("closeAccount", "Undefined", mem, "Employee"); //
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();
    return message.getStatus();
  }
  public Member accessAcount() throws IOException, ClassNotFoundException {
    message = new Message("getMember", "Undefined", null, "Employee"); //
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();
    return message.getMember();
  }

  //// verify
  /*
   * sendDebit():
   * sends the Debit Card information to locate Member and tied accounts 
   */

  public String sendDebit(int debitCardNumber, int code) throws IOException, ClassNotFoundException {
    message = new Message("login", null, debitCardNumber, code, "ATM", null); //ATM or Employee too?
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();
    // if logged in it returns what to verify?// what if it fails?
//    String status = " ";
//    if (message.getStatus().equals("success")) {
//      status = "succcess";
//    }
    return message.getStatus();
  }

  /*
   * checkMember():
   * checks if member exists-- subject to change... No validation
   * 
   * Note: If Exists then continue else create NEW Member with given info
   */

  //	public void checkMember(int num) throws IOException { // how we going to check for the Members? - 
  //		//no constructor for simple Message(type .. ,Debit.., status..,message..) or Message(type .. ,status..,message..) 
  //		message = new Message();
  //		objOut.writeObject(message);
  //		objOut.reset();
  //	}

  /*
   * updateMember():
   * will find send Message to update existing member 
   */

  //	public void updateMember(int num) throws IOException {//<--- could pass who /// decision: pass objects or pass just messages with the info needed to create an object?
  //		message = new Message();
  //		objOut.writeObject(message);
  //		objOut.reset();
  //	}

  ////banking

  /*
   * Who is the account in question or Employee or ATM?
   */
  public double deposit(double amount, String who) throws IOException, ClassNotFoundException {
	double parsedWho = 0;
    message = new Message("deposit", "undefined", parsedWho, amount, "Employee", who);
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();

    return message.getAccNumOrBal();
  }

  public double withdrawl(double amount, String who) throws IOException, ClassNotFoundException {
	double parsedWho = 0;
    message = new Message("withdrawl", "undefined", parsedWho, amount, "Employee", who);
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();

    return message.getAccNumOrBal();
  }

  public double checkBalance(int acc) throws IOException, ClassNotFoundException {
    message = new Message("check balance", null, 0.0, 0, "Employee", String.valueOf(acc));
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();

    return message.getAccNumOrBal();
  }
  //transfer

  /*
   * transfer():
   * moves currency from one account to another
   */
  public double tranfer(int acc, String toWho, double amount) throws IOException, ClassNotFoundException {
    message = new Message("transfer", null, acc, amount, "employee", toWho);
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();

    return message.getAccNumOrBal();
  }

  // !!!display information!!!

  ////***vvv Was in the class documents IDK???
  //	public String checkStatement() throws IOException, ClassNotFoundException {
  //		String statement =""; 
  //		message = new Message();
  //		objOut.writeObject(message);
  //		objOut.reset();
  //		message = (Message)objIn.readObject();
  //		
  //		return statement; //stub
  //	}

  /*
   * !!!not yet decided!!!
   */
  public String checkAccountLogs() throws IOException, ClassNotFoundException {
    String logs = "";
    message = new Message();
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();

    return logs; //stub

  }

  //Mutators & setters

  public int getEmployeeNumber() {
    return employeeNumber;
  }

  public void setEmployeeNumber(int employeeNumber) {
    Employee.employeeNumber = employeeNumber;
  }

  public String getEmployeePassword() {
    return employeePassword;
  }

  public void setEmployeePassword(String employeePassword) {
    this.employeePassword = employeePassword;
  }

  public Socket getHost() {
    return host;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
