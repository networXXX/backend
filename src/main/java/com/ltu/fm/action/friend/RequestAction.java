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
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.friend.FriendResponse;
import com.ltu.fm.model.action.friend.InsertFriendRequest;
import com.ltu.fm.model.friend.Friend;
import com.ltu.fm.model.friend.FriendDAO;


/**
 * Action that creates a new Friend
 * <p/>
 * POST to /pets/
 */
public class RequestAction extends AbstractLambdaAction{
	private LambdaLogger logger;

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        InsertFriendRequest input = getGson().fromJson(request, InsertFriendRequest.class);

        if (input == null ||
                input.getUserId() == null ||
                input.getUserId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (input == null ||
                input.getOtherId() == null ||
                input.getOtherId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        FriendDAO dao = DAOFactory.getFriendDAO();

        Friend newFriend = new Friend();
        
        try {
        	newFriend.setUserId(input.getOtherId());
        	newFriend.setOtherId(input.getUserId());
        	newFriend.setStatus(Constants.PENDING_STATUS);
        	newFriend = dao.insert(newFriend);

        } catch (final DAOException e) {
            logger.log("Error while creating new friend\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (newFriend.getId() == null || newFriend.getId().trim().equals("")) {
            logger.log("FriendID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        FriendResponse output = new FriendResponse();
        output.setItem(newFriend);

        return getGson().toJson(output);
    }

}
