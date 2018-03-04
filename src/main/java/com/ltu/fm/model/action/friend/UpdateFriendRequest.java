/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.ltu.fm.model.action.friend;

/**
 * The Class InsertDeviceRequest.
 */
public class UpdateFriendRequest {

	/** The id. */
	private String id;
	
	/** The user id. */
	private String userId;
	
	/** The other id. */
	private String otherId;
	
	/** The status. */
	private String status;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the other id.
	 *
	 * @return the other id
	 */
	public String getOtherId() {
		return otherId;
	}
	
	/**
	 * Sets the other id.
	 *
	 * @param otherId the new other id
	 */
	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Instantiates a new update friend request.
	 */
	public UpdateFriendRequest() {
		
	}
	
	/**
	 * Instantiates a new update friend request.
	 *
	 * @param id the id
	 * @param userId the user id
	 * @param otherId the other id
	 * @param status the status
	 */
	public UpdateFriendRequest(String id, String userId, String otherId, String status) {
		this.id = id;
		this.userId = userId;
		this.otherId = otherId;
		this.status = status;
	}
	
	
}
