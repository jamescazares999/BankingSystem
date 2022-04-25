//import java.io.*;

public class Employee {
private static int employeeNumber;
private String employeePassword;
//private Account account;


Employee(String password){
	Employee.employeeNumber ++;
	this.employeePassword = password;
}


////open an existing account

//public openAccount() {
//	
//}
//public closeAccount(int accountNum){
//
//}


////Connect to server???

//// verify
public void checkMember(int num) {
	
}

public void updateMember(int num) {
	
}
////banking

public double deposit(Double amount) {
	return amount;// stub
}

public double withdrawl(Double amount) {
	return amount;//stub
}

public String checkBalance() {
	String balance =""; 
	
	return balance; //stub
}

public String checkStatement() {
	String statement =""; 
	
	return statement; //stub
}

public String checkAccountLogs(){
	String logs =""; 
	
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


}
