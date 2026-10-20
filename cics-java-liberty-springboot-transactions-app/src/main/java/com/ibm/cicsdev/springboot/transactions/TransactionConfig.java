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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Explicit configuration for the JTA PlatformTransactionManager in Liberty.
 * 
 * Binds Spring's PlatformTransactionManager to Liberty's JTA UserTransaction
 * and TransactionManager via JNDI.
 */
@Configuration
public class TransactionConfig 
{
	@Bean
	public PlatformTransactionManager transactionManager() 
	{
		return new JtaTransactionManager();
	}
}
