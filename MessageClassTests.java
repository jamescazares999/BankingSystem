package Testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import testPackage.Member;
import testPackage.Message;

public class MessageClassTests {

	@Test
	void test() {
		Message newMem = new Message();
		System.out.println(newMem.getType());
		System.out.println(newMem.getWho());
		System.out.println(newMem.getStatus());
	}
	
	@Test
	void OverloadedContructTest() {
		Message newMem = new Message("Not Sure", "Success", 20.0, 10.0, "Employee", "Cust");
		System.out.println(newMem.getType());
		System.out.println(newMem.getWho());
		System.out.println(newMem.getStatus());
		System.out.println(newMem.getAccNumOrBal());
		System.out.println(newMem.getTransferBalOrRegBalance());
		
		
	}
	
	@Test
	void setAccBalanceTest() {
		Message newMem = new Message();
		newMem.setAccNumOrBal(40.90);
		System.out.println(newMem.getAccNumOrBal());
	}
	

}
