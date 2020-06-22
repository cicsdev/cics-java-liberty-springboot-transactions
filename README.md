jsp-2.3# cics-java-liberty-springboot-transactions

This sample project demonstrates how you can write a Spring Boot application to integrate with IBM CICS transactions when you deploy it to a Liberty JVM server.  The application uses a web browser front end, the web request then uses Java™ Transaction API (JTA) that manages a simple CICS write to a CICS TSQ using a UserTransaction context.

Java™ Transaction API (JTA) is described in the [IBM Java™ Transaction API (JTA)](https://www.ibm.com/support/knowledgecenter/en/SSGMCP_5.4.0/applications/developing/java/dfhpj2_jta.html)

The sample also demonstrates how to use a Spring Boot @Transactional annotation to manage transactions instead of using Java™ Transaction API (JTA) directly.  

Information on [@Transactional](https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/transaction.html)

The built war file from this project is deployed into CICS via a CICS Bundle Project.

For more information, see blog post - link TBC.

## Prerequisites

- CICS TS V5.3 or later
- A configured Liberty JVM server in CICS
- Java SE 1.8 on the z/OS system
- Java SE 1.8 on the workstation
- An Eclipse development environment on the workstation (optional)
- Either Gradle or Apache Maven on the workstation (optional if using Wrappers)
- A TSModel with Recovery ON for TSQ EXAMPLE

## Downloading

- Clone the repository using your IDEs support, such as the Eclipse Git plugin
- **or**, download the sample as a [ZIP](https://github.com/cicsdev/cics-java-liberty-springboot-transactions/archive/master.zip) and unzip onto the workstation

>*Tip: Eclipse Git provides an 'Import existing Projects' check-box when cloning a repository.*

### Check dependencies
 
Before building this sample, you should verify that the correct CICS TS bill of materials (BOM) is specified for your target release of CICS. The BOM specifies a consistent set of artifacts, and adds information about their scope. In the example below the version specified is compatible with CICS TS V5.5 with JCICS APAR PH25409, or newer. That is, the Java byte codes built by compiling against this version of JCICS will be compatible with later CICS TS versions and subsequent JCICS APARs. 
You can browse the published versions of the CICS BOM at [Maven Central.](https://mvnrepository.com/artifact/com.ibm.cics/com.ibm.cics.ts.bom)
 
Gradle (build.gradle): 

`compileOnly enforcedPlatform("com.ibm.cics:com.ibm.cics.ts.bom:5.5-20200519131930-PH25409")`

Maven (POM.xml):

``` xml	
<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>com.ibm.cics</groupId>
        <artifactId>com.ibm.cics.ts.bom</artifactId>
        <version>5.5-20200519131930-PH25409</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  ```

  
## Building 

You can build the sample using an IDE of your choice, or you can build it from the command line. For both approaches, using the supplied Gradle or Maven wrapper is the recommended way to get a consistent version of build tooling. 

On the command line, you simply swap the Gradle or Maven command for the wrapper equivalent, `gradlew` or `mvnw` respectively.
  
For an IDE, taking Eclipse as an example, the plug-ins for Gradle *buildship* and Maven *m2e* will integrate with the "Run As..." capability, allowing you to specify whether you want to build the project with a Wrapper, or a specific version of your chosen build tool.

The required build-tasks are typically `clean bootWar` for Gradle and `clean package` for Maven. Once run, Gradle will generate a WAR file in the `build/libs` directory, while Maven will generate it in the `target` directory.

**Note:** When building a WAR file for deployment to Liberty it is good practice to exclude Tomcat from the final runtime artifact. We demonstrate this in the pom.xml with the *provided* scope, and in build.gradle with the *providedRuntime()* dependency.

**Note:** If you import the project to your IDE, you might experience local project compile errors. To resolve these errors you should run a tooling refresh on that project. For example, in Eclipse: right-click on "Project", select "Gradle -> Refresh Gradle Project", **or** right-click on "Project", select "Maven -> Update Project...".

>Tip: *In Eclipse, Gradle (buildship) is able to fully refresh and resolve the local classpath even if the project was previously updated by Maven. However, Maven (m2e) does not currently reciprocate that capability. If you previously refreshed the project with Gradle, you'll need to manually remove the 'Project Dependencies' entry on the Java build-path of your Project Properties to avoid duplication errors when performing a Maven Project Update.*  

#### Gradle Wrapper (command line)

Run the following in a local command prompt:

On Linux or Mac:

```shell
./gradlew clean bootWar
```
On Windows:

```shell
gradlew.bat clean bootWar
```

This creates a WAR file inside the `build/libs` directory.

#### Maven Wrapper (command line)


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

