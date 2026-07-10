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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The main REST controller class
 *
 */
@RestController
public class TransactionController 
{
	// Autowire in the three main demo objects of this Project
	@Autowired SpringTransactional transactional;
	@Autowired SpringTransactionTemplate springTemplateTran;
	@Autowired JEEUserTransaction jeeTran;
	
	
	/**
	 * Provide a root for Usage: information
	 * @return usage message
	 */
	@GetMapping("/")
	public String index() 
	{				
		return "<h1>Spring Boot Transaction REST sample</h1>"
				+ "<h3>Usage:</h3>"
				+ "<b>/transactionalCommit</b> - Demo Spring Transactional annotation commit <br>"
				+ "<b>/transactionalRollback</b> - Demo Spring Transactional annotation rollback <br>"
				+ "<b>/STcommit</b> - Demo Spring Template commit <br>"
				+ "<b>/STrollback</b> - Demo Spring Template rollback <br>"				
				+ "<b>/JEEcommit</b> - Demo Java EE User Transaction commit <br>"
				+ "<b>/JEErollback</b> - Demo Java EE User Transaction rollback";
	}
	
 	
	/**
	 * Demonstrate Transactional annotation commit
	 * @return status message
	 */
	@GetMapping({"/transactionalCommit", "/transactionalcommit"})
	public String transactionalCommit() 
	{
		// Commit a TSQ write using Spring's @Transactional annotation
		try 
		{
			return this.transactional.writeTSQ("hello CICS from transactionalCommit()");
		}
		catch(Exception e)
		{							
			e.printStackTrace();
			return "transactionalCommit: exception: "  + e.getMessage() + ". Check dfhjvmerr for further details.";
		}		
	}
	
	
	/**
	 * Demonstrate Transactional annotation rollback
	 * @return status message
	 */
	@GetMapping({"/transactionalRollback", "/transactionalrollback"})
	public String transactionalRollback()
	{
		// Attempt to write to a TSQ
		// ...but when the string 'rollback' is detected in the input we 
		// intentionally throw a runtime exception which the @Transactional 
		// annotation has been qualified to detect and trigger a rollback.
		try 
		{
			return this.transactional.writeTSQ("rollback from transactionalRollback()");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "transactionalRollback: exception: "  + e.getMessage() + ". Rollback triggered - see dfhjvmerr for further details.";
		}		
	}
	

	/**
	 * Spring Template managed transaction commit web request
	 * @return message
	 */
	@GetMapping({"/STcommit", "/stcommit"})
	public String springTemplateCommit() 
	{
		return this.springTemplateTran.writeTSQ("hello CICS from springTemplateCommit()");
	}

	
	/**
	 * Spring Template managed transaction rollback web request
	 * @return message
	 */
	@GetMapping({"/STrollback", "/strollback"})
	public String springTemplateRollback() 
	{
		return this.springTemplateTran.writeTSQ("rollback from springTemplateRollback()");
	}
	
	
	/**
	 * JEE UserTransaction commit
	 * @return message
	 */
	@GetMapping({"/JEEcommit", "/jeecommit"})
	public String javaEECommit() 
	{
		return this.jeeTran.writeTSQ("hello CICS from javaEECommit()");				
	}
	
	
	/**
	 * JEE UserTransaction rollback
	 * @return message
	 */
	@GetMapping({"/JEErollback", "/jeerollback"})
	public String javaEErollback() 
	{
		return this.jeeTran.writeTSQ("rollback from javaEERollback()");		
	}	
}