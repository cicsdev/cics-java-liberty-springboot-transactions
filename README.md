# cics-java-liberty-springboot-transactions

This sample project demonstrates how you can write a Spring Boot application to integrate with IBM CICS transactions when you deploy it to a Liberty JVM server.  The application uses a web browser front end, the web request then uses Java™ Transaction API (JTA) to manage a simple CICS write to a CICS TSQ using a UserTransaction context.

Java™ Transaction API (JTA) is descrbed in the [IBM Java™ Transaction API (JTA](https://www.ibm.com/support/knowledgecenter/en/SSGMCP_5.4.0/applications/developing/java/dfhpj2_jta.html)

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

1. Transfer the WAR file to zFS for example using FTP. 

2. Ensure you have the following features in `server.xml`:

    - springBoot-2.0
    - servlet-3.1
    
3. Add the app configuration to `server.xml`

    Here's an example of configuration needed in `server.xml`:

    ```
    <application location="/u/myuser/spring/com.ibm.cics.springboot.transactions-1.0.0.war" type="war">
    </application> 
    ```

    
## Trying out the sample

1. Find the URL for the application in messages.log e.g. `http://myzos.mycompany.com:32000/com.ibm.cics.springboot.transactions-1.0.0/`. 
2. Visit the URL from the browser.
3. The browser will respond with the message "Greetings from com.ibm.cics.springboot.transaction servlet", and the TSQ will have the message "Example of a commit".
4. If you then visit the rollback url e.g `http://myzos.mycompany.com:32000/com.ibm.cics.springboot.transactions-1.0.0/rollback/`
5. The browser will respond with the message "Greetings from com.ibm.cics.springboot.transaction servlet rollback" and the TSQ will have the message "Example of a commit before rollback", but the message "This will be rolled back" will not be present on the TSQ as this is rolled back by the Java™ Transaction API (JTA) manager.

## License
This project is licensed under [Apache License Version 2.0](LICENSE). 

