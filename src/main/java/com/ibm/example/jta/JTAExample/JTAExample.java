package com.ibm.example.jta.JTAExample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JTAExample {

	@Autowired
	JTAExampleTSQ JtaexampleTsq;

	@RequestMapping("/")
	public String index() {

		JtaexampleTsq.exampleCommit();
		return "Greetings from JTAExample";
	}

	@RequestMapping("/rollback")
	public String rollback() {

		JtaexampleTsq.exampleCommitAndRollback();
		return "Greetings from JTAExample RollBack";
	}

}
