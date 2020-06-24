package com.ibm.cicsdev.springboot.transactions;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.TSQ;


/**
 * Bean Managed Transactions
 * @author ChrisAtkinson
 *
 */
@Component
public class BeanTransactions {	
	
	@Autowired
    private PlatformTransactionManager transactionManager;
	
	private TransactionTemplate tranTemplate;		   
    
    /**
     * @param str
     */
    public void exampleBeanManangedTransaction(String str) {
    	
    	tranTemplate = new TransactionTemplate(transactionManager);
        tranTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
    	
        // Execute the transactional method to do the CICS update
    	tranTemplate.execute(new TransactionCallbackWithoutResult() {
    		
    	    protected void doInTransactionWithoutResult(TransactionStatus status) {
    	    	
    	    	if (status.isNewTransaction()) {
    	    		System.out.println("exampleBMTCommit: Starting new JTA transaction ");    	    		
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
					targetQueue.writeString("Example of a BMT commit");					
					
				// If JCICS command fails then force a rollback of the JTA transaction and the CICS UOW	
				} catch (CicsConditionException e) {
					
					System.out.println("exampleBMTCommit: CicsConditionException, forcing rollback");
					status.setRollbackOnly();
					e.printStackTrace();
				} 
				  	        
    	    }
    	});
    } 
    
    
}
