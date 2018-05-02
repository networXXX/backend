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
package com.ltu.fm.action.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.auth.TokenProvider;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.user.DeleteUserRequest;
import com.ltu.fm.model.action.user.DeleteUserResponse;
import com.ltu.fm.model.user.UserDAO;
import com.ltu.fm.utils.AppUtil;

public class DeleteAction extends AbstractLambdaAction{
	//private LambdaLogger logger;
	static final Logger logger = LogManager.getLogger(DeleteAction.class);
	
	@Override
	public String handle(JsonObject request, Context lambdaContext, String token) throws BadRequestException, InternalErrorException {
		//logger = lambdaContext.getLogger();

        DeleteUserRequest input = getGson().fromJson(request, DeleteUserRequest.class);

        if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (!TokenProvider.getInstance().validateToken(token)) {
        	throw new BadRequestException(ExceptionMessages.EX_INVALID_AUTHORIZATION);
        }
        
        String userId = AppUtil.getUserId(token);
        if (!input.getId().equals(userId)) {
        	throw new BadRequestException(ExceptionMessages.EX_NO_PERMISSION);
		}
        
        UserDAO dao = DAOFactory.getUserDAO();

        try {
        	dao.delete(input.getId());
        } catch (final DAOException e) {
            logger.error("Error while deleting user\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        DeleteUserResponse output = new DeleteUserResponse();
        output.setItem(null);

        return getGson().toJson(output);
	}

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        DeleteUserRequest input = getGson().fromJson(request, DeleteUserRequest.class);

        if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        UserDAO dao = DAOFactory.getUserDAO();

        try {
        	dao.delete(input.getId());
        } catch (final DAOException e) {
            logger.error("Error while deleting user\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        DeleteUserResponse output = new DeleteUserResponse();
        output.setItem(null);

        return getGson().toJson(output);
    }
}
