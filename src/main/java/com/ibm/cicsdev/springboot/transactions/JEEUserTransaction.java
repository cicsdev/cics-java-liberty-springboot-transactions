/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2020 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.springboot.transactions;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;
import org.springframework.stereotype.Component;
import com.ibm.cics.server.TSQ;

/**
 * This class demonstrates the use of JNDI to look up a Java EE User Transaction context.
 * It then uses the Java Transaction API (JTA) and Liberty's transaction manager to create 
 * and manage work within a global transaction - to which a CICS UOW is subordinate.
 */
@Component
public class JEEUserTransaction 
{	
	
	/** 
	 * write to a TSQ from within a Java EE UserTransaction 
	 * 
	 * @param text 
	 * @return status message
	 */
	public String writeTSQ(String text) 
	{
		// Look up the Java EE transaction context from JNDI 
		UserTransaction tranContext = lookupContext();			

		// Write a message to the TSQ
		try 
		{
			// Create a JCICS TSQ object
			TSQ tsq = new TSQ();
			tsq.setName(Application.TSQNAME);

			// Start a user transaction, write a simple phrase
			// to the queue and then commit or rollback.
			tranContext.begin();
			
			tsq.writeString(text);			
			if(text.contains("rollback"))
			{
				tranContext.rollback();
				return "JEEUserTransaction.writeTSQ(): TSQ written then 'rollback' issued";
			}
			
			tranContext.commit();
			return "JEEUserTransaction.writeTSQ(): TSQ written then 'commit' issued";					
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
			return "JEEUserTransaction.writeTSQ(): unexpected exception: "  + e.getMessage() + ". Check dfhjvmerr for further details.";
		}
	}
	
		
	private UserTransaction lookupContext() 
	{		
		// Use JNDI to find the Java EE (Liberty) transaction manager
		InitialContext ctx;		
		UserTransaction tran = null;
		try 
		{
			ctx = new InitialContext();
			tran = (UserTransaction) ctx.lookup("java:comp/UserTransaction");

		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
			System.out.println("JEEUserTransaction.lookupContext(): lookup failed: " + e.getMessage());
		}

		// return the UserTransaction context
		return tran;
	}
}
