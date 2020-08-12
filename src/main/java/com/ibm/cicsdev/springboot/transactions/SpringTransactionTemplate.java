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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.TSQ;


/**
 * This component demonstrates use of the Spring PlatformTransactionManager 
 * and TransactionTemplate to manage transactions in a Bean orientated 
 * manner.
 */
@Component
public class SpringTransactionTemplate 
{	
	@Autowired
    private PlatformTransactionManager transactionManager;
	
    
    /**
     * @param text
     * @return status message
     */
    public String writeTSQ(String text) 
    {   
    	// Create transaction template
    	TransactionTemplate tranTemplate = new TransactionTemplate(this.transactionManager);
		tranTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
        // Execute the method to do the CICS update
    	tranTemplate.execute(new TransactionCallbackWithoutResult() 
    	{    	
    	    @Override
			protected void doInTransactionWithoutResult(TransactionStatus status) 
    	    {    	    	
    	    	if (status.isNewTransaction()) 
    	    	{
    	    		System.out.println("SpringTransactionTemplate.writeTSQ(): Starting new transaction");    	    		
    	    	}  	    	
    	        			
    			// Force a rollback if the input matches a specific string 
    			if (text.contains("rollback")) 
    			{
    				status.setRollbackOnly();    				
    			}
    			
    			// Create JCICS TSQ object
    			TSQ tsq = new TSQ();
    			tsq.setName(Application.TSQNAME);
    			
    			// Write the string to the TSQ 
    			try 
    			{    				
					tsq.writeString(text);									
				} 
    			catch (CicsConditionException e) 
    			{				
				    // If JCICS command fails 
    				// then force a rollback of the transaction (which rolls back the CICS UOW)
					System.out.println("SpringTransactionTemplate.writeTSQ(): " + e.getMessage() +  ". Unexpected exception, rollback");
					status.setRollbackOnly();
					e.printStackTrace();
				} 		    			    		
    	    }
    	});
    	
    	return text;
    }        
}
