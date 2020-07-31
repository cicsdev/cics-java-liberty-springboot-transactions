package com.ibm.cicsdev.springboot.transactions;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.springframework.stereotype.Component;

import com.ibm.cics.server.TSQ;

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
 * This uses a jndi look up for a user transaction context.
 * It then uses JTA to manage the transaction.
 *
 */
@Component
public class JEEUserTransaction {
	/**
	 * JNDI Commit
	 */
	public void exampleJEEUserCommit() {

		UserTransaction tran = lookupContext();

		// Commit a message to the TSQ
		try {
			// Set up our TSQ to be ready to write
			TSQ targetQueue = new TSQ();
			targetQueue.setName("EXAMPLE");

			// Start our user transaction
			tran.begin();

			// Write a simple phrase to the queue and then commit
			targetQueue.writeString("Example of a JEEUser commit");

			tran.commit();

		} catch (Exception e) {
			System.out.println("exampleJEEUserCommit: exception");
			e.printStackTrace();
		}
	}
	
	/**
	 * JEEUser Commit and Rollback
	 */
	public void exampleJEEUserCommitAndRollback() {

		UserTransaction tran = lookupContext();

		// Commit a message to the TSQ
		try {
			// Set up our TSQ to be ready to write
			TSQ targetQueue = new TSQ();
			targetQueue.setName("EXAMPLE");

			// Start our user transaction
			tran.begin();

			// Write a simple phrase to the queue and then commit
			targetQueue.writeString("Example of a JEEUser commit before rollback");

			tran.commit();
			
			// Start another user transaction that will be rolled back		
			tran.begin();

			targetQueue.writeString("This will be rolled back");

			tran.rollback();

		} catch (Exception e) {
			System.out.println("exampleJEEUserCommit: exception");
			e.printStackTrace();
		}
	}
	
	private UserTransaction lookupContext() {

		// Find the Java EE (Liberty) JNDI transaction manager
		InitialContext ctx;
		UserTransaction tran = null;
		try {
			ctx = new InitialContext();
			tran = (UserTransaction) ctx.lookup("java:comp/UserTransaction");

		} catch (NamingException e1) {
			e1.printStackTrace();
			System.out.println("exampleJEEUserCommit: JEEUser lookup failed");
		}

		return tran;
	}
}