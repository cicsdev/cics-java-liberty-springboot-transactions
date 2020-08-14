# cics-java-liberty-springboot-transactions

This sample project demonstrates how a Spring Boot application deployed to a Liberty JVM server, can use different techniques to integrate with CICS transactions. The application uses a web browser front end and makes use of the Java™ Transaction API (JTA). The three techniques demonstrated are: Java EE User Transaction, Spring's `@Transactional` annotation, and the Spring Transaction Template. 

The Java™ Transaction API (JTA) is described in the [IBM Java™ Transaction API (JTA)](https://www.ibm.com/support/knowledgecenter/en/SSGMCP_5.4.0/applications/developing/java/dfhpj2_jta.html)

For more information on `@Transactional` and Spring Transaction Templates see [Spring Transaction Management](https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/transaction.html)

The artifact built from this project is a WAR file that can be deployed into CICS Liberty in a variety of ways from a direct <application> entry in server.xml, to a CICS bundle project. For more information, see **Deploying**.

## Prerequisites

- CICS TS V5.3 or later
- A configured Liberty JVM server in CICS
- Java SE 1.8 on the z/OS system
- Java SE 1.8 on the workstation
- An Eclipse development environment on the workstation (optional)
- Either Gradle or Apache Maven on the workstation (optional if using Wrappers)
- A CICS TSMODEL resource with the attribute `Recovery(ON)` for the TSQ called `EXAMPLE`.

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

```xml	
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

Ensure you have the `jsp-2.3` feature (which itself contains `servlet`) configured in `server.xml`:

Either:    
1. Create a CICS bundle project and copy the WAR file into it.

2. Add a *Dynamic Web Project include* to the project to point at the local WAR file.

3. Deploy the CICS bundle project to CICS.

4. Enable the JVM server and CICS bundle.

Or:
1. Manually upload the WAR file to zFS and add an `<application>` configuration element to server.xml:

```xml
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

1. With the application installed, the root URL for the sample application can be found in messages.log e.g. `http://myzos.mycompany.com:32000/cics-java-liberty-springboot-transactions-0.1.0/`.

2. Visit the URL from the browser to review the 'Usage' guide.

3. To demonstrate the `@Transactional` container managed transaction, drive the `/transactionalCommit` end-point. You should see *hello CICS from transactionalCommit()* at the browser and a corresponding entry in the TSQ 'EXAMPLE'. You can browse the contents of the TSQ using the CEBR transaction in CICS.

4. Now try the same TSQ write operation `/transactionalRollback`. This time the application is designed to write to the TSQ then throw an exception causing Spring Boot to rollback the transaction. If you have not installed a TSMODEL resource to make the EXAMPLE TSQ recoverable, you will see a second entry in the TSQ! If you have already made the TSQ recoverable then there should be no such entry due to rollback of the CICS UOW.

5. Next, try the *Spring Transaction Template* and *Java EE User Transaction* demos at `/STcommit` and `/JEEcommit` respectively. Along with their rollback counterparts `/STrollback` and `/JEErollback`. 

6. For confirmation of the behaviour, you can run the sample before your TSQ is designated as recoverable (through a TSMODEL) and again afterwards. Observe how the entries to the TSQ are either committed, or written - then rolled back.



## License
This project is licensed under [Eclipse Public License - v 2.0](LICENSE). 


