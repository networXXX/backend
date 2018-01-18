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

import java.util.List;

import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.user.QueryRadiusRequest;
import com.ltu.fm.model.action.user.QueryUserPointResponse;
import com.ltu.fm.model.geo.UserPoint;
import com.ltu.fm.model.geo.UserPointDAO;
import com.ltu.fm.provider.CredentialsProvider;
import com.ltu.fm.provider.ProviderFactory;

public class Register1Action extends AbstractLambdaAction{
	//private LambdaLogger logger;
	
	private CredentialsProvider cognito = ProviderFactory.getCredentialsProvider();

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        QueryRadiusRequest input = getGson().fromJson(request, QueryRadiusRequest.class);

        UserPointDAO dao = DAOFactory.getUserPointDAO();
        

        GeoPoint centerPoint = new GeoPoint(input.getLat(), input.getLng());
    	List<UserPoint> result;
		try {
			result = dao.queryRadius(centerPoint, input.getRadiusInMeter(), 2, null);
		} catch (DAOException e) {
			throw new InternalErrorException(e.getMessage());
		}

        QueryUserPointResponse output = new QueryUserPointResponse();
        output.setItems(result);

        return getGson().toJson(output);
    }

}
