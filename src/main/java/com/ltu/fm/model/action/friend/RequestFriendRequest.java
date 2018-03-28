package com.ltu.fm.model.action.friend;


/**
 * The Class RequestFriendRequest.
 */
public class RequestFriendRequest {

	private String userId;
	private String otherId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOtherId() {
		return otherId;
	}
	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}	
	public RequestFriendRequest() {

	}
	
}
