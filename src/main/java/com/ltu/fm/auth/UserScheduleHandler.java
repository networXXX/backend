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

import java.util.Calendar;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;


/**
 * Handles IO for a Java Lambda function as a custom authorizer for API Gateway.
 *
 * @author uyphu created on May 20, 2017
 */
public class UserScheduleHandler implements RequestHandler<String, String> {

	private LambdaLogger logger;

	@Override
	public String handleRequest(String input, Context context) {
		logger = context.getLogger();
		UserDAO dao = DAOFactory.getUserDAO();
		String cursor = null;
		List<User> users;
		do {
			users = dao.search("status:P", 10, cursor, null);
			if (!users.isEmpty()) {
				for (User user : users) {
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_YEAR, -1);
					if (user.getCreatedAt().before(calendar.getTime())) {
						try {
							logger.log("Deleting userId: " + user.getId());
							dao.delete(user.getId());
						} catch (DAOException e) {
							logger.log("Cannot delete user. " + e.getMessage());
						}
					}
				}
				cursor = users.get(users.size() - 1).getId();
			} else {
				cursor = null;
			}
		} while (cursor != null);

		return null;
	}

}
