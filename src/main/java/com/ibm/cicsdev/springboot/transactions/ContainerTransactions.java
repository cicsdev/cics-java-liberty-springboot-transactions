package com.ibm.cicsdev.springboot.transactions;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.cics.server.IOErrorException;
import com.ibm.cics.server.ISCInvalidRequestException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.InvalidSystemIdException;
import com.ibm.cics.server.ItemErrorException;
import com.ibm.cics.server.LengthErrorException;
import com.ibm.cics.server.NotAuthorisedException;
import com.ibm.cics.server.TSQ;
@Component
public class ContainerTransactions {
	@Transactional
	public void exampleTransactional(String... text) throws InvalidRequestException, IOErrorException, LengthErrorException, InvalidSystemIdException, ISCInvalidRequestException, ItemErrorException, NotAuthorisedException {


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