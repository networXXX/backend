package com.ltu.fm.model.action.friend;


/**
 * The Class InsertDeviceRequest.
 */
public class InsertFriendRequest {

	private String userId;
	private String otherId;
	private String status;
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public InsertFriendRequest() {

	}
	
}
