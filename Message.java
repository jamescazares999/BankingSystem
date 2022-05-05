
import java.io.Serializable;

public class Message implements Serializable {
  private String type;
  private String status;
  private Member mem;
  private String fromWho;
  private String toWho;
  private double accNumOrBal;
  private double transferBal;

  //can be used as debit pin or employee ID
  private String login;
  private String EmpPass;

  public Message() {
    this.type = "Undefined";
    this.status = "Undefined";
    //this.emp = null;
    this.fromWho = "NoOne";
  }

  /* 
   * STANDARD CASES: Base Constructor
   * 
   * Login message for Employee:
   * new Message("login", "Employee", 0, 0, "loginIDEmp", "EmpPass")
   * 
   * Login message for ATM:
   * new Message("login", null, debitCardNumber, SecurityPin, "ATM", null)
   * 
   * Deposit message layout example;
   * new Message("deposit", null, AccNum, amountDeposit, "employee", null);
   * 
   * Withdraw message layout example; transferBal can be used as a amount to withdraw
   * new Message("withdraw", null, AccNum, AmountToWithdraw, "employee", null);
   * 
   * Check Balance message layout example;	transferBal will be used to send back amount in account; to who can be what time of account
   * toWho in general will be null for general account
   * 
   * new Message("check balance", null, AccNum, 0, "ATM", null); check last amount in .toWho(); to get amount
   * 
   * Transfer message:				Transfer Bal is transfer amount
   * 
   * new Message("transfer", AccNum, 0, TransferAmount, "employee", ToWho AccNum as string format); 
   * ill parse the last parameters to ints or whatever
   * 
   * Close account message:
   * new Message("close account", null, AccNum, 0, "employee", null)
   * 
   * Logout message:
   * new Message("logout", null, 0, 0, null, null)
   * 
   * */

  //Deposit/withdraw/check balance/ transfer
  public Message(String type, String status, double accNumOrBal, double transferBal, String fromWho, String toWho) {
    setType(type);
    setStatus(status);
    setWho(fromWho);
    this.accNumOrBal = accNumOrBal;
    this.transferBal = transferBal;
    this.fromWho = fromWho;
    this.toWho = toWho;
  }

  //create account
  public Message(String type, String status, Member mem, String who) {
    setType(type);
    setStatus(status);
    setWho(who);
    this.mem = mem;
  }

  //Usual case = login, transfer, withdraw, deposit, check balance
  private void setType(String type) {
    this.type = type;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  //Usual case = account first creation
  public void setMember(Member mem) {
    this.mem = mem;
  }

  //Usual case = account first creation
  public Member getMember() {
    return mem;
  }

  //Usual case = either ATM or EMPLOYEE; Unless transfer put accNum of sender
  public void setWho(String fromWho) {
    this.fromWho = fromWho;
  }

  //Usual case = login, transfer, withdraw, deposit, check balance
  public String getType() {
    return type;
  }

  public String getStatus() {
    return status;
  }

  //Login message retrieval for server
  public String getEmpLogin() {
    return login;
  }

  //login message retrieval for server
  public String getEmpPassword() {
    return EmpPass;
  }

  //put Acc Num for deposit, or withdraw. 
  public double getAccNumOrBal() {
    return accNumOrBal;
  }

  //login message retrieval for server
  public double getTransferBalOrRegBalance() {
    return transferBal;
  }

  //Usual case = either ATM or EMPLOYEE; Unless transfer put accNum of sender
  public String getWho() {
    return fromWho;
  }

  public void setToWho(String toWho) {
    this.toWho = toWho;
  }

  //Usual case = from EMPLOYEE,to transfer account put accNum of receiver; Not used to say to Server leave null
  public String toWho() {
    return toWho;
  }

public void setAccNumOrBal(double addBal) {
	// TODO Auto-generated method stub
	this.accNumOrBal = addBal;
}

public String getToWho() {
	// TODO Auto-generated method stub
	return toWho;
}

}