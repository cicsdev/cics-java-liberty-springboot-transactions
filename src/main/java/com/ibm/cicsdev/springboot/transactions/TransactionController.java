package com.ibm.cicsdev.springboot.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.cics.server.IOErrorException;
import com.ibm.cics.server.ISCInvalidRequestException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.InvalidSystemIdException;
import com.ibm.cics.server.ItemErrorException;
import com.ibm.cics.server.LengthErrorException;
import com.ibm.cics.server.NotAuthorisedException;

@RestController
public class TransactionController {

	@Autowired BeanTransactions beanTran;
	
	@RequestMapping("/")
	public String index() {

		beanTran.exampleCommit();
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet";
		
	}
	
	@RequestMapping("/rollback")
	public String rollback() {

		beanTran.exampleCommitAndRollback();
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet rollback";
	}
	
	@Autowired ContainerTransactions transactional; 
	
	@RequestMapping("/transactional")
	public String transactionalrollback() throws InvalidRequestException, IOErrorException, LengthErrorException, InvalidSystemIdException, ISCInvalidRequestException, ItemErrorException, NotAuthorisedException {
		
		//This should work and commit as error is not in data sent in this transaction
		try {
		transactional.exampleTransactional("hello","cics","transaction");
		}
		catch(Exception e){
			System.out.println("Failed to write data to TSQ");
		}
		
		//This should error and rollback as error in data sent in this transaction.  
		//The @Tranascational method will rollback automatically on error.
		try {
		transactional.exampleTransactional("goodbye","error","fred");
		}
		catch(Exception e){
			System.out.println("Failed to write data to TSQ");
		}
		
		//This should work and commit as error is not in data sent in this transaction
		try {
		transactional.exampleTransactional("onto","next","one");
		}
		catch(Exception e){
			System.out.println("Failed to write data to TSQ");
		}
		return "Greetings from com.ibm.cicsdev.springboot.transaction transactional";
	}
	
}
