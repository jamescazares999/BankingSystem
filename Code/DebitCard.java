import java.time.LocalDate;

public class DebitCard {
  protected int cardNumber = 0;
  protected int securityCode = 0;
  protected int tiedAccount = 0;
  protected LocalDate createdDate;
  protected LocalDate expiryDate;
  protected String nameOnCard = "Null";

  public DebitCard(int cardNumber, int securityCode, Member account) {
    this.setCardNumber(cardNumber);
    this.setSecurityCode(securityCode);
    this.setTiedAccount(account.getAccountNumber());
    this.setCreatedDate(LocalDate.now());
    this.setExpiryDate(createdDate.plusYears(3));
    this.setNameOnCard(account.getLegalName());
  }

  public int getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(int cardNumber) {
    this.cardNumber = cardNumber;
  }

  public int getSecurityCode() {
    return securityCode;
  }

  public void setSecurityCode(int securityCode) {
    this.securityCode = securityCode;
  }

  public int getTiedAccount() {
    return tiedAccount;
  }

  public void setTiedAccount(int tiedAccount) {
    this.tiedAccount = tiedAccount;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  public String getNameOnCard() {
    return nameOnCard;
  }

  public void setNameOnCard(String nameOnCard) {
    this.nameOnCard = nameOnCard;
  }
}
