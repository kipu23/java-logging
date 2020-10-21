# 1. slf4j - abstraction

In the java code, slf4j should be used for logging. This is a logging abstraction for various logging frameworks, like logback, log4j or log4j2. http://www.slf4j.org/manual.html#fluent
		
Benefits:
- logging implementation could be changed in the future without code change.
- no need for "isDebugEnabled" check (http://www.slf4j.org/faq.html#logging_performance)



# 2. log4j 2 - should be used for logging framework

Log4j 2 is the new version of log4j, with has some new features and improvements. https://logging.apache.org/log4j/2.x/index.html

If we use slf4j, only one dependency should be added to the pom file, and no change is required. After configuring the log4j2.xml properties file, the application will log as desired.

      <dependency>
          <groupId>org.apache.logging.log4j</groupId>
          <artifactId>log4j-slf4j-impl</artifactId>
          <version>2.13.3</version>
      </dependency>
			
The Log4j2 SLF4J Binding has a dependency on the Log4j2 API as well as the SLF4J API, so it is enough to add this dependency to the pom.



# 3. Converting from log4j to log4j 2

When converting form log4j to slf4j, there is a lot of manual task to be done, but as we plan to migrate from log4j to slf4j, please check next topic.
https://logging.apache.org/log4j/2.x/manual/migration.html



# 4.  Converting form log4j to slf4j

There is a migration tool on the official slf4j website. We can try it, but I would like to prefer to change the source files one by one.

http://www.slf4j.org/migrator.html



# 5. Logging to elastic stack.

The currently prefered method is to use Filebeat. It is still a TODO to implement a HelloWorld example.



# 6. saplink project


Beside migrating from log4j to slf4j, there is another task to be done in this project.
Currently, appenders, patternlayouts and loggers are created programatically. This should be avoided, and all this configuration should be put into the log4j2.xml config file.


- We have defaultConfiguration method, which will be set, when log4j config file is not found. Why do we need this check? We could add a check at the beginning of the program, that it will not start without a config, but this kind of over

