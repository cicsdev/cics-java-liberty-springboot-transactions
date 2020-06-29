package com.ibm.cicsdev.springboot.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.cics.server.CicsConditionException;


/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2020 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */
@RestController
public class TransactionController {


	/**
	 * Root web request
	 * @return message
	 */
	@GetMapping("/")
	public String index() {

		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet";
	
		
	}
	
@Autowired ContainerTransactions transactional; 
	
	/**
	 * Transactional web request
	 * @return message
	 * @throws CicsConditionException 
	 */
	@GetMapping("/transactional")
	public String transactionalrollback() throws CicsConditionException {
		
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
	
	
	@Autowired BeanTransactions beanTran;
	
	/**
	 * Bean transaction rollback web request
	 * @return message
	 */
	@GetMapping("/BMTrollback")
	public String rollbackBMT() {

		beanTran.exampleBeanManangedTransaction("rollback");;
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet rollback";
	}
	
	/**
	 * Bean transaction commit web request
	 * @return message
	 */
	@GetMapping("/BMTcommit")
	public String commitBMT() {

		beanTran.exampleBeanManangedTransaction("commit");;
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet commit";
	}
	
	@Autowired JNDIBeanManagedTransactions jndiTran;
	
	/**
	 * JNDI Commit Web request
	 * @return message
	 */
	@GetMapping("/JNDIcommit")
	public String jndiCommit() {

		jndiTran.exampleJNDICommit();
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet jndiCommit";
	}
	
	/**
	 * JNDI Commit and Rollback Web request
	 * @return message
	 */
	@GetMapping("/JNDIcommitAndRollback")
	public String jndiCommitAndRollback() {

		jndiTran.exampleJNDICommitAndRollback();
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet jndiCommitAndRollback";
	}
	
	
	
	
}