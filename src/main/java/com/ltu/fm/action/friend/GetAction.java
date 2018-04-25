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
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.friend.FriendResponse;
import com.ltu.fm.model.action.friend.GetFriendRequest;
import com.ltu.fm.model.friend.Friend;
import com.ltu.fm.model.friend.FriendDAO;

public class GetAction extends AbstractLambdaAction {
	//private LambdaLogger logger;
	
	@Override
	public String handle(JsonObject request, Context lambdaContext, String token) throws BadRequestException, InternalErrorException {
		return null;
	}

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        GetFriendRequest input = getGson().fromJson(request, GetFriendRequest.class);

        if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        FriendDAO dao = DAOFactory.getFriendDAO();

        Friend getFriend = dao.find(input.getId());

        if (getFriend == null) {
            throw new InternalErrorException(ExceptionMessages.EX_FRIEND_NOT_FOUND);
		}

        FriendResponse output = new FriendResponse();
        output.setItem(getFriend);

        return getGson().toJson(output);
    }
}
