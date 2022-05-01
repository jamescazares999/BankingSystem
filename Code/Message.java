//include other classes

import java.io.Serializable;

public class Message implements Serializable {
    protected String type;
    protected String status;
    protected Employee emp;
    protected DebitCard debcard;
    protected Member mem;
    protected String who;
    
    //can be used as debit pin or employee ID
    protected int pin;
    protected String EmpPass;
    protected int accNum;
    protected int transferAcc;
    protected double amount;

    
    public Message(){
        this.type = "Undefined";
        this.status = "Undefined";
        //this.emp = null;
        this.debcard = null;
        this.who = "NoOne";
    }

    //login employee
    public Message(String type, String status, int pin, String EmpPass, String who){
        setType(type);
        setStatus(status);
        setWho(who);
        setlogId(pin);
        setEmpPassword(EmpPass);
        
    }
    
    //login  atm
    public Message(String type, String status, DebitCard debcard, int pin, String who){
        setType(type);
        setStatus(status);
        setDebit(debcard);
        //this.emp = null;
        setWho(who);
    }
    
    // withdraw/deposit debit card 
    public Message(String type, String status, DebitCard debcard, double amount, String who){
        setType(type);
        setStatus(status);
        setDebit(debcard);
        setAmount(amount);
        setWho(who);
    }

 // withdraw/deposit employee
    public Message(String type, String status, Employee emp, int AccNum, double amount, String who){
        setType(type);
        setStatus(status);
        setAccNum(AccNum);
        setAmount(amount);
        setWho(who);
    }
    
    //transfer
    public Message(String type, String status, Employee emp, int AccNum, int transferAcc, double amount){
        setType(type);
        setStatus(status);
        setAccNum(AccNum);
        setAmount(amount);
        setTAcc(transferAcc);
    }
    
  //create account
    public Message(String type, String status, Member mem, String who){
        setType(type);
        setStatus(status);
        setWho(who);
        setlogId(pin);
        setEmpPassword(EmpPass);
        
    }
    
    private void setType(String type){
    	this.type = type;
    }

    public void setStatus(String status){
    	this.status = status;
    }

    public void setMember(Member mem){
    	this.mem = mem;
    }
    
    public Member getMember(){
    	return mem;
    }
    
    public void setDebit(DebitCard debcard){
    	this.debcard = debcard;
    }
    
    public void setWho(String who){
    	this.who = who;
    }
    
    public void setlogId(int loginID){
    	this.pin = loginID;
    }
    
    public void setEmpPassword(String pass){
    	this.EmpPass = pass;
    }
    
    public void setPin(int pin){
    	this.pin = pin;
    }
    
    public void setAmount(double amount){
    	this.amount = amount;
    }
    
    public Employee getEmployee() {
    	return emp;
    }
    
    public int getPin(){
    	return pin;
    }
    
    public void setAccNum(int accNum) {
    	this.accNum = accNum;
    }
    
    public int getAccNum() {
    	return accNum;
    }
    
    public void setTAcc(int transferAcc) {
    	this.transferAcc = transferAcc;
    }
    
    public int getTAcc() {
    	return transferAcc;
    }

    public String getType(){
    	return type;
    }

    public String getStatus(){
    	return status;
    }
    
    public double getAmount(){
    	return amount;
    }

    public int getlogId(){
    	return pin;
    }
    
    public String getEmpPassword(){
    	return EmpPass;
    }
    
    public DebitCard getDebit(){
    	return debcard;
    }
    
    
    public String getWho(){
    	return who;
    }

}