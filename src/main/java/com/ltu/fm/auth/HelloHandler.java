/*
 * Copyright 2017 ltu.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://ltu.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.ltu.fm.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class HelloHandler implements RequestHandler<String, String>{
	// Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(HelloHandler.class);
    
    @Override
    public String handleRequest(String name, Context context) {
    	// System.out: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.out.println("1System.out.println.log data from stdout \n this is continuation of system.out");

       // System.err: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.err.println("2System.err.println.log data from stderr. \n this is a continuation of system.err");

        // Use log4j to log the same thing as above and AWS Lambda will log only one event in CloudWatch.
        logger.debug("DEBUG:log data from log4j debug \n this is continuation of log4j debug");

        logger.error("ERROR:log data from log4j err. \n this is a continuation of log4j.err");
        
        logger.info("INFO: Test log4j");

        // Return will include the log stream name so you can look
        // up the log later.
        return String.format("Hello %s. log stream = %s", name, context.getLogStreamName());
    }

}

