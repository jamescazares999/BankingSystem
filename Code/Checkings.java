
public class Checkings extends Account {
	private int checkingNumber = 0;
	
	Checkings(int member) {
		super(member);
		checkingNumber++;
	}

	public int getCheckingNumber() {
		return checkingNumber;
	}

//	public void setCheckingNumber(int checkingNumber) {
//		this.checkingNumber = checkingNumber;
//	}

}
