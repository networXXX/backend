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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.auth.TokenProvider;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.user.UpdateLocationRequest;
import com.ltu.fm.model.action.user.UserResponse;
import com.ltu.fm.model.geo.UserPointDAO;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;
import com.ltu.fm.utils.AppUtil;

public class UpdateLocationAction extends AbstractLambdaAction{
	private LambdaLogger logger;

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        UpdateLocationRequest input = getGson().fromJson(request, UpdateLocationRequest.class);

        if (input == null ||
                input.getUserId() == null ||
                input.getUserId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (input == null ||
                input.getAuthorization() == null ||
                input.getAuthorization().trim().equals("")) {
        	throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        String principalId = AppUtil.getUserId(input.getAuthorization());
        if (!input.getUserId().equals(principalId)) {
        	throw new BadRequestException(ExceptionMessages.EX_NO_PERMISSION);
		}
        
        UserPointDAO dao = DAOFactory.getUserPointDAO();
        UserDAO userDAO = DAOFactory.getUserDAO();
        User updateUser;
        
        
        updateUser = userDAO.find(input.getUserId());
        
        if (updateUser != null) {
	        if (!TokenProvider.getInstance().validateToken(input.getAuthorization())) {
	        	throw new BadRequestException(ExceptionMessages.EX_INVALID_AUTHORIZATION);
	        }
	        
	        try {
	    		updateUser.setLat(input.getLat());
	    		updateUser.setLng(input.getLng());
	    		dao.putUser(updateUser);
	    		userDAO.update(updateUser);
	        } catch (final DAOException e) {
	            logger.log("Error while creating new device\n" + e.getMessage());
	            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
	        }
	        
	        if (updateUser.getId() == null || updateUser.getId().trim().equals("")) {
	            logger.log("UserID is null or empty");
	            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
	        }
        }

        UserResponse output = new UserResponse();
        output.setItem(updateUser);

        return getGsonExcludeFields().toJson(output);
    }

}