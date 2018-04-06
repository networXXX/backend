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
package com.ltu.fm.utils;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.ltu.fm.model.friend.Friend;
import com.ltu.fm.model.user.User;

/**
 * The Class ConvertUtil.
 * 
 * @author uyphu
 */
public class ConvertUtil {

	/** The log. */
	private static Logger log = LogManager.getLogger(ConvertUtil.class);

	public static User toUser(Map<String, AttributeValue> item) {
		User user = new User();
		try {
			user.setId(item.get("id").getS());
			user.setEmail(item.get("email") != null ? item.get("email").getS() : null);
			user.setPassword(item.get("password") != null ? item.get("password").getB() : null);
			user.setSalt(item.get("salt") != null ? item.get("salt").getB() : null);
			user.setType(item.get("type") != null ? item.get("type").getS() : null);
			user.setDisplayName(item.get("displayName") != null ? item.get("displayName").getS() : null);
			user.setActivateCode(item.get("activateCode") != null ? item.get("activateCode").getS() : null);
			user.setImageUrl(item.get("imageUrl") != null ? item.get("imageUrl").getS() : null);
			user.setSearchText(item.get("searchText") != null ? item.get("searchText").getS() : null);
			user.setStatus(item.get("status").getS());
			user.setCreatedAt(item.get("createdAt") != null ? AppUtil.toDate(item.get("createdAt").getS()) : null);
			user.setCognitoIdentityId(item.get("identityId") != null ? item.get("identityId").getS() : null);
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
		}
		return user;
	}
	
	public static Friend toFriend(Map<String, AttributeValue> item) {
		Friend friend = new Friend();
		try {
			friend.setId(item.get("id").getS());
			friend.setUserId(item.get("userId") != null ? item.get("userId").getS() : null);
			friend.setOtherId(item.get("otherId") != null ? item.get("otherId").getS() : null);
			friend.setStatus(item.get("status") != null ? item.get("status").getS() : null);
			friend.setCreatedAt(item.get("createdAt") != null ? AppUtil.toDate(item.get("createdAt").getS()) : null);
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
		}
		return friend;
	}
	
}

