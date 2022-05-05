public class Member {
  private static int accountNumber = 0;
  //private int pinNumber;
  //private int routingNumber;
  private int debitCardNumber;
  private String legalName;
  private int age;
  private String gender;
  private String address;
  //protected int socialSecurity;
  //protected String legalIDNum;
  private double balance;
  //private Account[] accountList;

  Member() {
    setAccountNumber(getAccountNumber() + 1);
    //		this.pinNumber=0;
    //		this.routingNumber=0;
    this.debitCardNumber = 0;
    this.legalName = "";
    this.age = 0;
    this.gender = "";
    this.address = "";
    //		this.socialSecurity = 0;
    //		this.legalIDNum = "";
    this.balance = 0;
  }
  Member(int debitNum, String name, int age, String gender, String address, double balance) {
    setAccountNumber(getAccountNumber() + 1);
    //		this.pinNumber = pinNum;
    //		this.routingNumber= routingNum;
    this.debitCardNumber = debitNum;
    this.legalName = name;
    this.age = 0;
    this.gender = gender;
    this.address = address;
    //		this.socialSecurity = social;
    //		this.legalIDNum = IDNum;
    this.balance = balance;
  }
  public String getLegalName() {
    return legalName;
  }
  public void setLegalName(String legalName) {
    this.legalName = legalName;
  }
  public int getAge() {
    return age;
  }
  public void setAge(int age) {
    this.age = age;
  }
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public double getBalance() {
    return balance;
  }
  public void setBalance(double balance) {
    this.balance = balance;
  }
  public static int getAccountNumber() {
    return accountNumber;
  }
  public static void setAccountNumber(int accountNumber) {
    Member.accountNumber = accountNumber;
  }
  //public int getPinNumber() {
  //	return pinNumber;
  //}
  //public void setPinNumber(int pinNumber) {
  //	this.pinNumber = pinNumber;
  //}
  //public int getRoutingNumber() {
  //	return routingNumber;
  //}
  //public void setRoutingNumber(int routingNumber) {
  //	this.routingNumber = routingNumber;
  //}
  public int getDebitCardNumber() {
    return debitCardNumber;
  }
  public void setDebitCardNumber(int debitCardNumber) {
    this.debitCardNumber = debitCardNumber;
  }

  //public void addAccount(Account account) {
  //	
  //	this.accountList[accountList.length] = account ;
  //}
  //
  //public Account  getAccount(int accountNum) throws IOException {
  //	Account act =null;
  //	for (Account acc:accountList) {
  //		if (acc.getID() == accountNum) {
  //			act = acc;
  //			break;
  //		}			
  //	}
  //	return act;
  //}

}