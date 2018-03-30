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

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.DynamoDBConfiguration;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.friend.SearchFriendRequest;
import com.ltu.fm.model.action.user.SearchUserResponse;
import com.ltu.fm.model.friend.FriendDAO;
import com.ltu.fm.model.user.User;


public class QueryUserAction extends AbstractLambdaAction{
	//private LambdaLogger logger;

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        SearchFriendRequest input = getGson().fromJson(request, SearchFriendRequest.class);

        if (input == null ||
                input.getQuery() == null ||
                input.getQuery().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        FriendDAO dao = DAOFactory.getFriendDAO();

        List<User> list = dao.queryUser(input.getQuery(), input.getLimit(), input.getCursor(), DynamoDBConfiguration.USER_ID_INDEX);

        SearchUserResponse output = new SearchUserResponse();
        output.setItems(list);

        return getGsonExcludeFields().toJson(output);
    }
}