package com.ibm.cicsdev.springboot.transactions;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.ibm.cics.server.CicsConditionException;
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
 * This uses the Spring PlatformTransactionManager and TransactionTemplate
 * to manage transactions in a Bean orientated manner.
 *
 */
@Component
public class SpringTransactionTemplate {	
	
	@Autowired
    private PlatformTransactionManager transactionManager;
	
	private TransactionTemplate tranTemplate;		   
    
    /**
     * @param str
     */
    public void exampleSpringTemplateManangedTransaction(String str) {
    	
    	tranTemplate = new TransactionTemplate(transactionManager);
        tranTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
    	
        // Execute the transactional method to do the CICS update
    	tranTemplate.execute(new TransactionCallbackWithoutResult() {
    		
    	    protected void doInTransactionWithoutResult(TransactionStatus status) {
    	    	
    	    	if (status.isNewTransaction()) {
    	    		System.out.println("exampleSpringTemplateCommit: Starting new transaction ");    	    		
    	    	}  	    	
    	    
    			
    			// Force a rollback if the input matches a specific string 
    			if (str.equalsIgnoreCase("rollback")) {
    				status.setRollbackOnly();    				
    			}
    			
    			//Initilaise TSQ
    			TSQ targetQueue = new TSQ();
    			targetQueue.setName("EXAMPLE");
    			
    			// Write a string to the TSQ 
    			try {    				
					targetQueue.writeString("Example of a SpringTemplate commit");					
					
				// If JCICS command fails then force a rollback of the transaction and the CICS UOW	
				} catch (CicsConditionException e) {
					
					System.out.println("exampleSpringTemplateCommit: CicsConditionException, forcing rollback");
					status.setRollbackOnly();
					e.printStackTrace();
				} 
				  	        
    	    }
    	});
    } 
    
    
}
