# 1. slf4j - abstraction

In the java code, slf4j should be used for logging. This is a logging abstraction for various logging frameworks, like logback, log4j or log4j2. http://www.slf4j.org/manual.html#fluent
		
Benefits:
- logging implementation could be changed in the future without code change.
- no need for "isDebugEnabled" check (http://www.slf4j.org/faq.html#logging_performance)



# 2. log4j 2 - should be used for logging framework

Log4j 2 is the new version of log4j, with has some new features and improvements. https://logging.apache.org/log4j/2.x/index.html

If we use slf4j, the above dependencies should be added to the pom file. After configuring the log4j2.xml properties file, the application will log as desired.

     <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-api</artifactId>
          <version>1.7.30</version>
      </dependency>

      <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core -->
      <dependency>
          <groupId>org.apache.logging.log4j</groupId>
          <artifactId>log4j-core</artifactId>
          <version>2.13.3</version>
      </dependency>

      <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api -->
      <dependency>
          <groupId>org.apache.logging.log4j</groupId>
          <artifactId>log4j-api</artifactId>
          <version>2.13.3</version>
      </dependency>

      <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl -->
      <dependency>
          <groupId>org.apache.logging.log4j</groupId>
          <artifactId>log4j-slf4j-impl</artifactId>
          <version>2.13.3</version>
      </dependency>



# 3. Converting from log4j to log4j 2

When converting form log4j to slf4j, there is a lot of manual task to be done, but as we plan to migrate from log4j to slf4j, please check next topic.
https://logging.apache.org/log4j/2.x/manual/migration.html


# 4.  Converting form log4j to slf4j

There is a migration tool on the official slf4j website. We can try it, but I would like to prefer to change the source files one by one.

http://www.slf4j.org/migrator.html


# 5. Logging to elastic stack.

The currently prefered method is to use Filebeat. If we plan to send the logs directly to elasticsearch, it is a good practice to log directly in JSON format. Doing this, we need to add another dependency to the pom file:


        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.3</version>
        </dependency>
		
		
With this dependency, we can create a new File appender in the log4j2.xml file:

        <File name="JsonFileAppender" fileName="logs/log4filebeat.log">
            <JSONLayout compact="true" eventEol="true">
                <KeyValuePair key="@timestamp" value="$${date:yyyy-MM-dd'T'HH:mm:ss.SSSZ}" />
            </JSONLayout>
        </File>


# 6. saplink project


->SapLink.java

Todos:

- We have defaultConfiguration method, which will be set, when log4j config file is not found. Why do we need this check? We could add a check at the beginning of the program, that it will not start without a config. In this method, appenders, patternlayouts and loggers are created programatically. This should be avoided, and all this configuration should be put into the log4j2.xml config file.

- Changing the import statements to slf4j.

- At the beginning we use DOMConfigurator.configureAndWatch to automatically detect changes in the config file. When configured from a File, Log4j has the ability to automatically detect changes to the configuration file and reconfigure itself. If the monitorInterval attribute is specified on the configuration element and is set to a non-zero value then the file will be checked the next time a log event is evaluated and/or logged and the monitorInterval has elapsed since the last check https://stackoverflow.com/questions/32843757/how-to-implement-configureandwatch-in-log4j2

- No need for "isDebugEnabled" check (http://www.slf4j.org/faq.html#logging_performance)

- There is no fatal level in slf4j. Markers should be used if it is really needed. I suggest to use error instead.
http://www.slf4j.org/faq.html#fatal


