/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.ltu.fm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ltu.fm.action.NetworkAction;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.InternalErrorException;

/**
 * This class contains the main event handler for the Lambda function.
 * 
 * @author Uy Phu 
 * created on May 18, 2017
 */
public class RequestRouter {

	/**
	 * Lambda handler.
	 *
	 * @param request the request
	 * @param response the response
	 * @param context the context
	 * @throws BadRequestException the bad request exception
	 * @throws InternalErrorException the internal error exception
	 */
	public static void lambdaHandler(InputStream request, OutputStream response, Context context)
			throws BadRequestException, InternalErrorException {
		LambdaLogger logger = context.getLogger();

		JsonParser parser = new JsonParser();
		JsonObject inputObj;
		try {
			inputObj = parser.parse(IOUtils.toString(request)).getAsJsonObject();
		} catch (IOException e) {
			logger.log("Error while reading request\n" + e.getMessage());
			throw new InternalErrorException(e.getMessage());
		}

		if (inputObj == null || inputObj.get("action") == null
				|| inputObj.get("action").getAsString().trim().equals("")) {
			logger.log("Invald inputObj, could not find action parameter");
			throw new BadRequestException("Could not find action value in request");
		}

		String actionClass = inputObj.get("action").getAsString();
		NetworkAction action;
		
		try {
			action = NetworkAction.class.cast(Class.forName(actionClass).newInstance());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BadRequestException("Invalid action class");
		}

		try {
			action = NetworkAction.class.cast(Class.forName(actionClass).newInstance());
		} catch (final InstantiationException e) {
			logger.log("Error while instantiating action class\n" + e.getMessage());
			throw new InternalErrorException(e.getMessage());
		} catch (final IllegalAccessException e) {
			logger.log("Illegal access while instantiating action class\n" + e.getMessage());
			throw new InternalErrorException(e.getMessage());
		} catch (final ClassNotFoundException e) {
			logger.log("Action class could not be found\n" + e.getMessage());
			throw new InternalErrorException(e.getMessage());
		}

		if (action == null) {
			logger.log("Action class is null");
			throw new BadRequestException("Invalid action class");
		}

		JsonObject body = null;
		if (inputObj.get("body") != null) {
			body = inputObj.get("body").getAsJsonObject();
		}

		String output = action.handle(body, context);

		try {
			IOUtils.write(output, response);
		} catch (final IOException e) {
			logger.log("Error while writing response\n" + e.getMessage());
			throw new InternalErrorException(e.getMessage());
		}
	}
}