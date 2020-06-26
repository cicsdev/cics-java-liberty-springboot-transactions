package com.ibm.cicsdev.springboot.transactions;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.TSQ;
/**
 * @Transactional
 * @author ChrisAtkinson
 *
 */
@Component
public class ContainerTransactions {
	/**
	 * Example @transactional commit or rollback
	 * @param text
	 * @throws CicsConditionException 
	 */
	@Transactional (rollbackFor=Exception.class)
	public void exampleTransactional(String... text) throws CicsConditionException {


	    // Set up our TSQ to be ready to write
		TSQ targetQueue = new TSQ();
		targetQueue.setName("EXAMPLE");

		
		for (String texts : text) {
		   if (texts.contains("error")) {
			   throw new RuntimeException("Texts contains error so rollback"); 
		   }
		   targetQueue.writeString("Writing " + texts);
		
		}
	}
}