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
import com.ltu.fm.model.action.friend.RequestFriendRequest;
import com.ltu.fm.model.friend.Friend;
import com.ltu.fm.model.friend.FriendDAO;


/**
 * Action that creates a new Friend
 * <p/>
 * POST to /pets/
 */
public class ConfirmAction extends AbstractLambdaAction{
	private LambdaLogger logger;
	
	@Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        RequestFriendRequest input = getGson().fromJson(request, RequestFriendRequest.class);

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
        
        if (input.getOtherId().trim().equals(input.getUserId().trim())) {
            throw new BadRequestException(ExceptionMessages.EX_USERID_SAME_ORTHERID);
        }
        
        FriendDAO dao = DAOFactory.getFriendDAO();

        Friend friend = dao.findByFriend(input.getUserId(), input.getOtherId());
        
        if (friend == null) {
        	throw new BadRequestException(ExceptionMessages.EX_FRIEND_NOT_FOUND);
		}
        
        if (!Constants.PENDING_STATUS.equals(friend.getStatus())) {
        	throw new BadRequestException(ExceptionMessages.EX_CANNOT_CONFIRM_FRIEND);
		}
        
        Friend friend2 = dao.findByFriend(input.getOtherId(), input.getUserId());
        
        try {
        	
        	friend.setStatus(Constants.YES_STATUS);
        	friend2.setStatus(Constants.YES_STATUS);
        	friend = dao.update(friend);
        	dao.update(friend2);

        } catch (final DAOException e) {
            logger.log("Error while creating new friend\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (friend.getId() == null || friend.getId().trim().equals("")) {
            logger.log("FriendID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        FriendResponse output = new FriendResponse();
        output.setItem(friend);

        return getGson().toJson(output);
    }

    

}
