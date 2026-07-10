/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2020 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */

package com.ibm.cicsdev.springboot.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Standard boilerplate Spring Boot Application Class */
@SpringBootApplication
public class Application 
{
	
	/** Name of the TSQ used throughout this Sample */
	public static final String TSQNAME = "EXAMPLE";
	
	/**
	 * @param args - inputs
	 */
	public static void main(String[] args) 
	{
		SpringApplication.run(Application.class, args);
	}
}
