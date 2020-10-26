# 1. slf4j - abstraction

In the java code, slf4j should be used for logging. This is a logging abstraction for various logging frameworks, like logback, log4j or log4j2. http://www.slf4j.org/manual.html#fluent
		
Key benefits:
- logging implementation could be changed in the future without code change.
- no need for "isDebugEnabled" check (http://www.slf4j.org/faq.html#logging_performance)


# 2. log4j 2 - should be used for logging framework

Key benefits:
- When configured from a File, Log4j has the ability to automatically detect changes to the configuration file and reconfigure itself. If the monitorInterval attribute is specified on the configuration element and is set to a non-zero value then the file will be checked the next time a log event is evaluated and/or logged and the monitorInterval has elapsed since the last check.

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



# 3. Converting from log4j to log4j2

When converting form log4j to log4j2, there is a lot of manual task to be done.
https://logging.apache.org/log4j/2.x/manual/migration.html


# 4.  Converting form log4j to slf4j

There is a migration tool on the official slf4j website.
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
            <JSONLayout compact="true" complete="false" eventEol="true">
                <KeyValuePair key="datetime" value="$${date:yyyy-MM-dd'T'HH:mm:ss.SSSZ}" />
            </JSONLayout>
        </File>

We need to install and configure filebeat to be able to handle our log file. The filebeat.yml should connain the following lines:

- type: log
	enabled: true
    paths:
    - C:\logs\java-logging\log4filebeat.log
    
    json:
    keys_under_root: true
    overwrite_keys: true
    message_key: 'message'

    output.elasticsearch:
      hosts: ["kipukibana.westeurope.cloudapp.azure.com:9200"]
    
    output.elasticsearch.index: "java-logging-%{[agent.version]}-%{+yyyy.MM.dd}"
    setup.template.name: "java-logging"
    setup.template.pattern: "java-logging-*"
    
    processors:
      - decode_json_fields:
          fields: ['message']
          target: json