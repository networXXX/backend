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
package com.ltu.fm.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.model.user.User;

/**
 * The Class GoogleService.
 * @author uyphu
 */
public class GoogleService {
	
	/** The log. */
	private final Logger log = LogManager.getLogger(GoogleService.class);
	
	/** The instance. */
	private static GoogleService instance = null;
	 
	/**
	 * Gets the single instance of ContactService.
	 *
	 * @return single instance of ContactService
	 */
	public static GoogleService getInstance() {
		if (instance == null) {
			synchronized (GoogleService.class) {
				if (instance == null) {
					instance = new GoogleService();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Authenticate.
	 *
	 * @return the string
	 */
	public User authenticate(String token) throws BadRequestException{
		User user = new User();
		try {
			String address = "https://www.googleapis.com/oauth2/v2/tokeninfo?access_token=" + token;

			URL url = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			
			String strEmail = String.valueOf("\"email\": \"");
			
			String str;
			while ((str = in.readLine()) != null) {
				str = str.trim();
				if (str.startsWith(strEmail)) {
					user.setEmail(str.substring(strEmail.length(), str.length() - 2));
					
				}
			}
			user.setType(Constants.GOOGLE_TYPE);
			in.close();
			
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
			throw new BadRequestException(ExceptionMessages.EX_AUTHENTICATE_GOOGLE);
		}
		
		return user;
	}

}
