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
package com.ltu.fm.action.location;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.location.LocationResponse;
import com.ltu.fm.model.action.location.UpdateLocationRequest;
import com.ltu.fm.model.location.Location;
import com.ltu.fm.model.location.LocationDAO;

public class UpdateAction extends AbstractLambdaAction{
	private LambdaLogger logger;

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        UpdateLocationRequest input = getGson().fromJson(request, UpdateLocationRequest.class);

        if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (input == null ||
                input.getUserId() == null ||
                input.getUserId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_USER_ID_REQUIRED);
        }

        LocationDAO dao = DAOFactory.getLocationDAO();

        Location updateLocation;

        try {
        	updateLocation = dao.find(input.getId());
        	updateLocation.setUserId(input.getUserId());
            updateLocation.setLat(input.getLat());
            updateLocation.setLng(input.getLng());
        	updateLocation = dao.update(updateLocation);
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (updateLocation.getId() == null || updateLocation.getId().trim().equals("")) {
            logger.log("LocationID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        LocationResponse output = new LocationResponse();
        output.setItem(updateLocation);

        return getGson().toJson(output);
    }

}