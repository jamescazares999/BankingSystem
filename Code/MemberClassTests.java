package Testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import testPackage.Member;

public class MemberClassTests {

	@Test
	void defaultContructortest() {
		
		Member newMem = new Member();
		
		//Member newMem1 = new Member(1234567891,  "Blah",  20,  "Female", "Unknown", 50.3);
		
		System.out.println(newMem.getDebitCardNumber());
		System.out.println(newMem.getLegalName());
		System.out.println(newMem.getAge());
		System.out.println(newMem.getGender());
		System.out.println(newMem.getAddress());
		System.out.println(newMem.getBalance());
	}
	
	@Test
	void overloadedContructortest() {
		Member newMem = new Member(1234567891,  "Me",  20,  "M", "Not Sure", 20.3);
		
		System.out.println(newMem.getDebitCardNumber());
		System.out.println(newMem.getLegalName());
		System.out.println(newMem.getAge());
		System.out.println(newMem.getGender());
		System.out.println(newMem.getAddress());
		System.out.println(newMem.getBalance());
		
	}

}
