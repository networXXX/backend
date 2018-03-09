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
package com.ltu;


import com.amazonaws.services.lambda.runtime.Context;
import com.ltu.fm.auth.APIGatewayAuthorizerHandler;
import com.ltu.fm.auth.TokenAuthorizerContext;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAuthorizerHandler extends TestCase {

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public TestAuthorizerHandler(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestAuthorizerHandler.class);
	}
	
	/**
	 * Creates the context.
	 * 
	 * @return the context
	 */
	private Context createContext() {
		TestContext ctx = new TestContext();

		ctx.setFunctionName("Your Function Name");

		return ctx;
	}

	/**
	 * Rigourous Test :-)
	 */
//	public void testApp() {
//		Context context = createContext();
//		TestUtils.callAPI(context, "token.input.json", "token.output.json");
//		assertTrue(true);
//	}
	
	//@org.junit.Test
	public void testLambdaFunctionHandler() {
//	    APIGatewayAuthorizerHandler handler = new APIGatewayAuthorizerHandler();
//	    Context ctx = createContext();
//	    TokenAuthorizerContext input = new TokenAuthorizerContext("TOKEN", "51f8328a-8f2d-497e-b265-68cd749c73ed:1528161208123:39164c1065f6f479ca26ef73a96da9d0", "arn:aws:execute-api:us-east-1:205631098170:9csrrzydk8/null/GET/");
//	    Object output = handler.handleRequest(input, ctx);
//	    if (output != null) {
//	        System.out.println(output.toString());
//	    }
	    //Assert.assertEquals("sourcebucket", output);
	}
	
//	//@Test
//	public void testLambdaFunctionHandler() {
//	    //UserScheduleHandler handler = new UserScheduleHandler();
//	    Context ctx = createContext();
//	    //TokenAuthorizerContext input = new TokenAuthorizerContext("TOKEN", "e67ee89b-89a1-44be-a2ea-f00947c48060:1520040447633:4118d57e9cca675522f7500d399dad9a", "arn:aws:execute-api:us-east-1:205631098170:yoj0jqj2q4/null/GET/");
//	    Object output = handler.handleRequest("", ctx);
//	    if (output != null) {
//	        System.out.println(output.toString());
//	    }
//	    //Assert.assertEquals("sourcebucket", output);
//	}
	
}
