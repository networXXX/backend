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
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.location.GetLocationRequest;
import com.ltu.fm.model.action.location.LocationResponse;
import com.ltu.fm.model.location.Location;
import com.ltu.fm.model.location.LocationDAO;

public class GetAction extends AbstractLambdaAction {
	//private LambdaLogger logger;
	
	@Override
	public String handle(JsonObject request, Context lambdaContext, String token) throws BadRequestException, InternalErrorException {
		return null;
	}

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        GetLocationRequest input = getGson().fromJson(request, GetLocationRequest.class);

        if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        LocationDAO dao = DAOFactory.getLocationDAO();

        Location getLocation = dao.find(input.getId());

        if (getLocation == null) {
            throw new InternalErrorException(ExceptionMessages.EX_USER_NOT_FOUND);
		}

        LocationResponse output = new LocationResponse();
        output.setItem(getLocation);

        return getGson().toJson(output);
    }
}
