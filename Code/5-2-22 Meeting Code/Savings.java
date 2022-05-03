
public class Savings extends Account {
	private int noOfDepositLeft;
	private double maxWithDraw;
	
	Savings(int member) {
		super(member);
		this.noOfDepositLeft = 0;
		this.maxWithDraw = 0;
	}

	public int getNoOfDepositLeft() {
		return noOfDepositLeft;
	}

	public void setNoOfDepositLeft(int noOfDepositLeft) {
		this.noOfDepositLeft = noOfDepositLeft;
	}

	public double getMaxWithDraw() {
		return maxWithDraw;
	}

	public void setMaxWithDraw(double maxWithDraw) {
		this.maxWithDraw = maxWithDraw;
	}

}