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
package com.ltu.fm.action.friend;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.location.InsertLocationRequest;
import com.ltu.fm.model.action.location.LocationResponse;
import com.ltu.fm.model.location.Location;
import com.ltu.fm.model.location.LocationDAO;

/**
 * Action that creates a new Location
 * <p/>
 * POST to /pets/
 */
public class InsertAction extends AbstractLambdaAction{
	private LambdaLogger logger;

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        InsertLocationRequest input = getGson().fromJson(request, InsertLocationRequest.class);

        if (input == null ||
                input.getUserId() == null ||
                input.getUserId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        LocationDAO dao = DAOFactory.getLocationDAO();

        Location newLocation = new Location();
        
        try {
        	newLocation = dao.insert(newLocation);
        } catch (final DAOException e) {
            logger.log("Error while creating new location\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (newLocation.getId() == null || newLocation.getId().trim().equals("")) {
            logger.log("LocationID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        LocationResponse output = new LocationResponse();
        output.setItem(newLocation);

        return getGson().toJson(output);
    }

}
