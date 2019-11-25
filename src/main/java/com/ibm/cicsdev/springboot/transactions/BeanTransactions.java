package com.ibm.cicsdev.springboot.transactions;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.springframework.stereotype.Component;

import com.ibm.cics.server.TSQ;

@Component
public class BeanTransactions {
	
	public void exampleCommit() {

		UserTransaction tran = lookupContext();

		// Commit a message to the TSQ
		try {
			// Set up our TSQ to be ready to write
			TSQ targetQueue = new TSQ();
			targetQueue.setName("EXAMPLE");

			// Start our user transaction
			tran.begin();

			// Write a simple phrase to the queue and then commit
			targetQueue.writeString("Example of a commit");

			tran.commit();

		} catch (Exception e) {
			System.out.println("exampleCommit: exception");
			e.printStackTrace();
		}
	}
	
	public void exampleCommitAndRollback() {

		UserTransaction tran = lookupContext();

		// Commit a message to the TSQ
		try {
			// Set up our TSQ to be ready to write
			TSQ targetQueue = new TSQ();
			targetQueue.setName("EXAMPLE");

			// Start our user transaction
			tran.begin();

			// Write a simple phrase to the queue and then commit
			targetQueue.writeString("Example of a commit before rollback");

			tran.commit();
			
			// Start another user transaction that will be rolled back		
			tran.begin();

			targetQueue.writeString("This will be rolled back");

			tran.rollback();

		} catch (Exception e) {
			System.out.println("exampleCommit: exception");
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
			System.out.println("exampleCommit: JTA JNDI lookup failed");
		}

		return tran;
	}

}
