package com.kipu.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {

    private static final Logger logger = LoggerFactory.getLogger(Logging.class);

    public static void main(String[] args) {

        String parameter = "parameter";

        logger.trace("This is a trace message with {}.!", parameter);
        logger.debug("This is a debug message with {}.!", parameter);
        logger.info("This is an info message with {}.!", parameter);
        logger.warn("This is a warn message with {}.!", parameter);
        logger.error("This is an error message with {}.!", parameter);
    }
}
