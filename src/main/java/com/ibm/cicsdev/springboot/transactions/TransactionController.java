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
/**
 * Transaction Controller
 *
 */
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
	
@Autowired SpringTransactional transactional; 
	
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
	
	
	@Autowired SpringTransactionTemplate springTempTran;
	
	/**
	 * Spring Template managed transaction rollback web request
	 * @return message
	 */
	@GetMapping("/STEMProllback")
	public String rollbackBMT() {

		springTempTran.exampleSpringTemplateManangedTransaction("rollback");;
		return "Greetings from com.ibm.cicsdev.springboot.transaction template servlet rollback";
	}
	
	/**
	 * Spring Template managed transaction commit web request
	 * @return message
	 */
	@GetMapping("/STEMPcommit")
	public String commitBMT() {

		springTempTran.exampleSpringTemplateManangedTransaction("commit");;
		return "Greetings from com.ibm.cicsdev.springboot.transaction template servlet commit";
	}
	
	@Autowired JEEUserTransaction jeeTran;
	
	/**
	 * JEEUser Commit Web request
	 * @return message
	 */
	@GetMapping("/JEEcommit")
	public String jndiCommit() {

		jeeTran.exampleJEEUserCommit();
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet jeeCommit";
	}
	
	/**
	 * JEEUser Commit and Rollback Web request
	 * @return message
	 */
	@GetMapping("/JNDIcommitAndRollback")
	public String jndiCommitAndRollback() {

		jeeTran.exampleJEEUserCommitAndRollback();
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet jeeCommitAndRollback";
	}
	
	
	
	
}