package Testing;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import testPackage.Employee;

public class EmployeeClassTests {
	
	@Test
	void contructorTest() {
		Employee bankEmp = new Employee();
		System.out.println(bankEmp.getEmployeePassword());
		System.out.println(bankEmp.getUsername());
		
	}

	@Test
	void OverloadedConstructorTest() throws UnknownHostException, IOException {
		Employee bankEmp = new Employee("123", "Blah");
		//Employee("123", "Blah");
		
		System.out.println(bankEmp.getEmployeePassword());
		System.out.println(bankEmp.getUsername());
		assertTrue(bankEmp.getEmployeePassword() == "123");
	}
	
	
	
	@Test
	void NoOfEmployeesTest() throws UnknownHostException, IOException {
		Employee bankEmp = new Employee("123", "Blah");
		System.out.println(bankEmp.getEmployeeNumber());
		
	}

	

	

}
