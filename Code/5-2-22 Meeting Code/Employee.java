import java.io.*;
import java.net.*;

//import java.io.*;

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
    this.connectToHost();
  }

  //open an existing account

  public void openAccount(String type, String status, Member mem, String who) throws IOException {
    message = new Message(String type, String status, Member mem, String who);
    objOut.writeObject(message);
    objOut.reset();
  }
  public void closeAccount(String who, int accNum) throws IOException {
    message = new Message(String type, String status, Member mem, String who); // <---- too much--- using a simply Constructor works
    objOut.writeObject(message);
    objOut.reset();
  }

  //// verify
  public void checkMember(int num) throws IOException { // how we going to check for the Members? - 
    //no constructor for simple Message(type .. ,Debit.., status..,message..) or Message(type .. ,status..,message..) 
    message = new Message();
    objOut.writeObject(message);
    objOut.reset();
  }

  public void updateMember(int num) throws IOException { //<--- could pass who /// decision: pass objects or pass just messages with the info needed to create an object?
    message = new Message();
    objOut.writeObject(message);
    objOut.reset();
  }
  ////banking

  public double deposit(String type, String status, DebitCard debcard, double amount, String who) throws IOException, ClassNotFoundException {
    message = new Message(type, status, debcard, amount, who);
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();

    return amount; //stub
  }

  public double withdrawl(String type, String status, DebitCard debcard, double amount, String who) throws IOException, ClassNotFoundException {
    message = new Message(type, status, debcard, amount, who);
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();
    return amount; //stub
  }

  public String checkBalance(String type, String status, DebitCard debcard, double amount, String who) throws IOException, ClassNotFoundException {
    String balance = "";
    message = new Message();
    objOut.writeObject(message);
    objOut.reset();
    message = (Message) objIn.readObject();

    return balance; //stub
  }
  ////***vvv Was in the class documents IDK???
  //	public String checkStatement() throws IOException, ClassNotFoundException {
  //		String statement =""; 
  //		message = new Message();
  //		objOut.writeObject(message);
  //		objOut.reset();
  //		message = (Message)objIn.readObject();
  //		
  //		return statement; //stub
}

public String checkAccountLogs() throws IOException, ClassNotFoundException {
  String logs = "";
  message = new Message();
  objOut.writeObject(message);
  objOut.reset();
  message = (Message) objIn.readObject();

  return logs; //stub

}

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

//Connect to server
public void connectToHost() throws UnknownHostException, IOException {
  this.host = new Socket("localhost", 1111);
  this.in = host.getInputStream();
  this.out = host.getOutputStream();
  this.objOut = new ObjectOutputStream(out);
  this.objIn = new ObjectInputStream( in );
}

public String getUsername() {
  return username;
}

public void setUsername(String username) {
  this.username = username;
}
//	public String ReadMessage() throws ClassNotFoundException, IOException {
//		message = (Message)objIn.readObject();
//		return message.getMessage();

}
public String sendEmployee() throws IOException, ClassNotFoundException {
  message = new Message("login", username, password, "undefined"); // use the proper for connection
  objOut.writeObject(message);
  objOut.reset();
  message = (Message) objIn.readObject();
  // if logged in it returns what to verify?// what if it fails?
  String status = " ";
  if (message.getStatus().equals("success")) {
    status = "succcess";
  }
  return status; //stub
}

public String sendDebit() throws IOException, ClassNotFoundException {
  message = new Message("debit", cardNumber, code, "undefined"); // use the proper for connection
  objOut.writeObject(message);
  objOut.reset();
  message = (Message) objIn.readObject();
  // if logged in it returns what to verify?// what if it fails?
  String status = " ";
  if (message.getStatus().equals("success")) {
    status = "succcess";
  }
  return status;
}
}