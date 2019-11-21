package com.ibm.cicsdev.springboot.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Servlet {
	
	@Autowired Transactions tran;
	
	@RequestMapping("/")
	public String index() {

		tran.exampleCommit();
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet";
		
	}
	
	@RequestMapping("/rollback")
	public String rollback() {

		tran.exampleCommitAndRollback();
		return "Greetings from com.ibm.cicsdev.springboot.transaction servlet rollback";
	}

}
