
public class BankSystem {

	//fields
	private int idNumber;
	private int  cardNumber;
	
	BankSystem (int idNum, int cardNum){
		this.idNumber = idNum;
		this.cardNumber = cardNum;
	}
	
	//methods
	public boolean validateCard(int cardNumber) {
			
			//if ()
			
			return false;
			
	}
		
	
	//deposit the requested amount and update balance
		
	public double deposit(double balance, double addAmount) 
	
	{
			balance = balance + addAmount;
			
			return balance;
			
	}
		
		
	//withdraw the requested amount and update balance
	public double withdraw(double balance,  double withdrawAmount) {
			
		balance = balance - withdrawAmount;
		return balance;
				
			
		}
		
		
		//check balance of given card number
		public void checkBalance(int cardNumber, double balance) {
			
			
			
			
		}

}