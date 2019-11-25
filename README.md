# cics-java-liberty-springboot-transactions

This sample project demonstrates how you can write a Spring Boot application to integrate with IBM CICS transactions when you deploy it to a Liberty JVM server.  The application uses a web browser front end, the web request then uses Java™ Transaction API (JTA) to manage a simple CICS write to a CICS TSQ using a UserTransaction context.

Java™ Transaction API (JTA) is descrbed in the [IBM Java™ Transaction API (JTA)](https://www.ibm.com/support/knowledgecenter/en/SSGMCP_5.4.0/applications/developing/java/dfhpj2_jta.html)

The sample also demonstrates how to use SpringBoots @Transactional annotation to manage transactions instead of using Java™ Transaction API (JTA).  

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

You can choose to build the project using Gradle or Maven. They will produce the same results.  The project includes the Gradle and Maven wrappers, which will automatically download the correct version of the tools if not present on your workstation.

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

    - servlet-3.1
    
2. Create a CICS Bundle Project with a Dynamic Web Project include for the built war file.

3. Deploy the CICS Bundle Project to CICS.

4. Enable the JVM Server and CICS Bundle

    
## Trying out the sample

1. Find the URL for the application in messages.log e.g. `http://myzos.mycompany.com:32000/com.ibm.cicsdev.springboot.transactions-1.0.0/`. 
2. Visit the URL from the browser.
3. The browser will respond with the message "Greetings from com.ibm.cicsdev.springboot.transaction servlet", and the TSQ will have the message "Example of a commit".
4. If you then visit the rollback url e.g `http://myzos.mycompany.com:32000/com.ibm.cicsdev.springboot.transactions-1.0.0/rollback/`
5. The browser will respond with the message "Greetings from com.ibm.cicsdev.springboot.transaction servlet rollback" and the TSQ will have the message "Example of a commit before rollback", but the message "This will be rolled back" will not be present on the TSQ as this is rolled back by the Java™ Transaction API (JTA) manager.
6. If you then visit the @Transaction url e.g `http://myzos.mycompany.com:32000/com.ibm.cicsdev.springboot.transactions-1.0.0/transactional/`
7. The browser will respond with the message "Greetings from com.ibm.cicsdev.springboot.transaction transactional" and the TSQ will have the messages  Writing hello, Writing cics,Writing transaction, Writing onto, Writing next, Writing one, but the transaction request that had "goodbye","error","fred" will not be present on the TSQ, as SpringBoots transaction manager will have rolled back.           

## License
This project is licensed under [Apache License Version 2.0](LICENSE). 

