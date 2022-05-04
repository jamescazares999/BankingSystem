public class Account {
  private int linkedMember;
  private double balance;

  Account(int member) {
    this.linkedMember = member;
    this.balance = 0;
  }
  public int getLinkedMember() {
    return linkedMember;
  }
  public void setLinkedMember(int linkedMember) {
    this.linkedMember = linkedMember;
  }
  public double getBalance() {
    return balance;
  }
  public void setBalance(double balance) {
    this.balance = balance;
  }

}
