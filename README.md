jsp-2.3# cics-java-liberty-springboot-transactions

This sample project demonstrates how you can write a Spring Boot application to integrate with IBM CICS transactions when you deploy it to a Liberty JVM server.  The application uses a web browser front end, the web request then uses Java™ Transaction API (JTA) that manages a simple CICS write to a CICS TSQ using a UserTransaction context.

Java™ Transaction API (JTA) is described in the [IBM Java™ Transaction API (JTA)](https://www.ibm.com/support/knowledgecenter/en/SSGMCP_5.4.0/applications/developing/java/dfhpj2_jta.html)

The sample also demonstrates how to use a Spring Boot @Transactional annotation to manage transactions instead of using Java™ Transaction API (JTA) directly.  

Information on [@Transactional](https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/transaction.html)

The built war file from this project is deployed into CICS via a CICS Bundle Project.

For more information, see blog post - link TBC.

## Prerequisites

  - CICS TS V5.5 or later
  - A configured Liberty JVM server 
  - Java SE 1.8 or later on the z/OS system
  - Java SE 1.8 or later on the workstation
  - A TSModel with Recovery ON for TSQ EXAMPLE

## Building 

You can choose to build the project using Gradle or Maven. The project includes both Gradle and Maven wrappers, these wrappers will automatically download required components from your chosen build tool; if not already present on your workstation.

You can also build the sample project through plug-in tooling of your chosen IDE. Both Gradle buildship and Maven m2e will integrate with Eclipse's "Run As..." capability allowing you to specify the required build-tasks. There are typically clean bootWar for Gradle and clean package for Maven, as reflected in the command line approach shown later.

Note: When building a WAR file for deployment to Liberty it is good practice to exclude Tomcat from the final runtime artifact. We demonstrate this in the pom.xml with the provided scope, and in build.gradle with the providedRuntime() dependency.

Note: If you import the project to an IDE of your choice, you might experience local project compile errors. To resolve these errors you should refresh your IDEs configuration. For example, in Eclipse: for Gradle, right-click on "Project", select "Gradle -> Refresh Gradle Project", or for Maven, right-click on "Project", select "Maven -> Update Project...".

### Gradle

Run the following in a local command prompt:

On Linux or Mac:
```shell
./gradlew clean war
```

On Windows:
```shell
gradlew.bat clean war
```

This creates a WAR file inside the `build/libs` directory.

### Maven

Run the following in a local command prompt:

On Linux or Mac:
```shell
./mvnw clean package
```

On Windows:
```shell
mvnw.cmd clean package
```

This creates a WAR file inside the `target` directory.

## Deploying

1. Ensure you have the following feature in `server.xml`:

    - jsp-2.3
    
2. Create a CICS bundle project with a Dynamic Web Project include for the built war file.

3. Deploy the CICS bundle project to CICS.

4. Enable the JVM server and CICS bundle.

5. Optionally, manually upload the WAR file to zFS and add an <application> configuration element to server.xml:

``` XML
   <application id="cics-java-liberty-springboot-transactions-0.1.0"  
     location="${server.config.dir}/springapps/cics-java-liberty-springboot-transactions-0.1.0.war"  
     name="cics-java-liberty-springboot-transactions-0.1.0" type="war">
     <application-bnd>
        <security-role name="cicsAllAuthenticated">
            <special-subject type="ALL_AUTHENTICATED_USERS"/>
        </security-role>
     </application-bnd>  
   </application>
```

    
## Trying out the sample

1. Find the URL for the application in messages.log e.g. `http://myzos.mycompany.com:32000/cics-java-liberty-springboot-transactions-0.1.0/transactional`. 
2. Visit the URL from the browser which will demonstrate an @Transactional container managed bean.
3. The browser will respond with the message "Greetings from com.ibm.cicsdev.springboot.transaction transactional" and the TSQ will have the messages  Writing hello, Writing cics,Writing transaction, Writing onto, Writing next, Writing one, but the transaction request that had "goodbye","error","fred" will not be present on the TSQ, as SpringBoots transaction manager will have rolled back.
4. Next, try the SpringBoot Bean managed way of managing transactions by visting the url `http://myzos.mycompany.com:32000/cics-java-liberty-springboot-transactions-0.1.0/BMTcommit` and the TSQ will have Example of a BMT commit.  If you try the rollback version of this at `http://myzos.mycompany.com:32000/cics-java-liberty-springboot-transactions-0.1.0/BMTrollback` the transaction is rolled back and nothing is written to the TSQ.
5. Finally, try the JNDI way by visiting the rollback url e.g `http://myzos.mycompany.com:32000/cics-java-liberty-springboot-transactions-0.1.0/JNDIcommit/`, and the browser will respond with the message "Greetings from com.ibm.cicsdev.springboot.transaction servlet jndiCommit" and a message "Example of a JNDIcommit" will be written to the TSQ.  Utilising the rollback version the browser at `http://myzos.mycompany.com:32000/cics-java-liberty-springboot-transactions-0.1.0/JNDIcommitAndRollback/`  will respond with the message "Greetings from com.ibm.cicsdev.springboot.transaction servlet jndiCommitAndRollback" and the TSQ will have the message "Example of a [JNDI]commit before rollback", but the message "This will be rolled back" will not be present on the TSQ as this is rolled back by the Java™ Transaction API (JTA) manager.          

## License
This project is licensed under [Apache License Version 2.0](LICENSE). 

