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

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.auth.Token;
import com.ltu.fm.auth.TokenProvider;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.helper.PasswordHelper;
import com.ltu.fm.model.action.user.LoginUserResponse;
import com.ltu.fm.model.action.user.LoginWithTokenRequest;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;
import com.ltu.fm.service.FacebookService;

/**
 * Action that creates a new User
 * <p/>
 * POST to /pets/
 */
public class LoginWithFacebookAction extends AbstractLambdaAction{
	private LambdaLogger logger;

    @Override
	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
    	logger = lambdaContext.getLogger();

        LoginWithTokenRequest input = getGson().fromJson(request, LoginWithTokenRequest.class);
        validateInput(input);
        User fbUser = callAPI(input);

        return output(fbUser);
    }

	private String output(User fbUser) {
		//Output token
        TokenProvider provider = TokenProvider.getInstance();
    	Token token = provider.createToken(fbUser);
    	LoginUserResponse output = new LoginUserResponse();
    	output.setExpires(token.getExpires());
    	output.setToken(token.getToken());
    	output.setType(token.getType());
    	output.setItem(fbUser);
        return getGsonExcludeFields().toJson(output);
	}

	private User callAPI(LoginWithTokenRequest input) throws BadRequestException, InternalErrorException {
		User fbUser = getFacebookInfo(input.getToken());
        
        UserDAO dao = DAOFactory.getUserDAO();
        User item = dao.findByEmail(input.getEmail());

		if (item != null) {
			if (!Constants.FACEBOOK_TYPE.equals(item.getType())) {
				throw new BadRequestException(ExceptionMessages.EX_LOGIN_FACEBOOK);
			}
			
			if (item.getActivateCode() != null && !item.getActivateCode().equals(fbUser.getActivateCode())) {
				throw new BadRequestException(ExceptionMessages.EX_LOGIN_FACEBOOK);
			}
			
			fbUser = item;
		} else {
			fbUser.setDisplayName(input.getDisplayName());
			fbUser.setEmail(input.getEmail());
			fbUser = registerValidUser(fbUser);
		}
		
		return fbUser;
	}

	private void validateInput(LoginWithTokenRequest input) throws BadRequestException {
		if (input == null ||
                input.getToken() == null ||
                input.getToken().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_TOKEN_REQUIRED);
        }
        
        if (input.getDisplayName() == null ||
                input.getDisplayName().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_DISPLAYED_NAME_REQUIRED);
        }
        
        if (input.getEmail() == null ||
                input.getEmail().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_EMAIL_REQUIRED);
        }
	}
    
    private User getFacebookInfo(String token) throws BadRequestException {
    	return FacebookService.getInstance().authenticate(token);
	}
    
    public User registerValidUser(User user) throws InternalErrorException {
		try {
			UserDAO dao = DAOFactory.getUserDAO();
			user.setStatus(Constants.YES_STATUS);
			user.setType(Constants.FACEBOOK_TYPE);
			byte[] salt;
			salt = PasswordHelper.generateSalt();
			byte[] encryptedPassword;
			encryptedPassword = PasswordHelper.getEncryptedPassword(Constants.PASSWORD_STRING, salt);
	        user.setPassword(ByteBuffer.wrap(encryptedPassword));
	        user.setSalt(ByteBuffer.wrap(salt));
			user = dao.insert(user);
			return user;
		} catch (final NoSuchAlgorithmException e) {
            logger.log("No algrithm found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_SALT);
        } catch (final InvalidKeySpecException e) {
            logger.log("No KeySpec found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_ENCRYPT);
        } catch (DAOException e) {
        	logger.log("Error registering facebook user\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_REGISTER_FACEBOOK);
		}
        
	}
    
}
