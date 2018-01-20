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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationService {

	/** The log. */
	private final Logger log = LogManager.getLogger(NotificationService.class);

	/** The instance. */
	private static NotificationService instance = null;

	/**
	 * Gets the single instance of ContactService.
	 *
	 * @return single instance of ContactService
	 */
	public static NotificationService getInstance() {
		if (instance == null) {
			// synchronized (NotificationService.class) {
			if (instance == null) {
				instance = new NotificationService();
			}
			// }
		}
		return instance;
	}

//	public boolean pushNotification(String userId, String senderId, String conferenceId, String sessionId, String type) {
//		UserDAO userDAO = DAOFactory.getUserDAO();
//		User user = userDAO.find(userId);
//		User sender = userDAO.find(senderId);
//		return pushNotification(user, sender, conferenceId, sessionId, type);
//	}
//	
//	public boolean pushNotification(User user, User sender, String conferenceId, String sessionId, String type) {
//		FirebaseNotification firebase = new FirebaseNotification();
//		firebase.setPriority(FirebaseNotification.PRIORITY_HIGH);
//		Data data = new Data(type, sender.getId(), conferenceId, sessionId, sender.getDisplayedName(), sender.getProfileImageUrl());
//		firebase.setData(data);
//		if (Notification.CATEGORY_CALL.equals(type)) {
//			firebase.setNotification(new Notification(Constants.APP_NAME, sender.getDisplayedName() + Constants.CALL_STRING));
//		} else if (Notification.CATEGORY_INVITE.equals(type)) {
//			firebase.setNotification(new Notification(Constants.APP_NAME, sender.getDisplayedName() + Constants.INVITE_STRING));
//		} else if (Notification.CATEGORY_DECLINE.equals(type)) {
//			firebase.setNotification(new Notification(Constants.APP_NAME, sender.getDisplayedName() + Constants.DECLINE_STRING));
//		} else if (Notification.CATEGORY_HOLD.equals(type)) {
//			firebase.setNotification(new Notification(Constants.APP_NAME, Constants.HOLD_STRING));
//		} 
//		
//		DeviceDAO deviceDAO = DAOFactory.getDeviceDAO();
//		//Had better use findByUserId
//		QueryResultPage<Device> list = deviceDAO.findByUserId(user.getId(), null, null);
//
//		for (Device device : list.getResults()) {
//			firebase.setTo(device.getPushToken());
//			sendMsg(firebase, device.getType());
//		}
//		return true;
//	}
//	
//	public boolean pushNotificationIgnoreDevice(User user, User sender, String conferenceId, String sessionId, String type, String deviceId) {
//		FirebaseNotification firebase = new FirebaseNotification();
//		firebase.setPriority(FirebaseNotification.PRIORITY_HIGH);
//		Data data = new Data(type, sender.getId(), conferenceId, sessionId, sender.getDisplayedName(), sender.getProfileImageUrl());
//		firebase.setData(data);
//		if (Notification.CATEGORY_CALL.equals(type)) {
//			firebase.setNotification(new Notification(Constants.APP_NAME, sender.getDisplayedName() + Constants.CALL_STRING));
//		} else if (Notification.CATEGORY_INVITE.equals(type)) {
//			firebase.setNotification(new Notification(Constants.APP_NAME, sender.getDisplayedName() + Constants.INVITE_STRING));
//		} else if (Notification.CATEGORY_DECLINE.equals(type)) {
//			firebase.setNotification(new Notification(Constants.APP_NAME, sender.getDisplayedName() + Constants.DECLINE_STRING));
//		} else if (Notification.CATEGORY_HOLD.equals(type)) {
//			firebase.setNotification(new Notification(Constants.APP_NAME, Constants.HOLD_STRING));
//		} 
//		
//		DeviceDAO deviceDAO = DAOFactory.getDeviceDAO();
//		//Had better use findByUserId
//		QueryResultPage<Device> list = deviceDAO.findByUserId(user.getId(), null, null);
//
//		for (Device device : list.getResults()) {
//			if (deviceId == null || !deviceId.equals(device.getId())) {
//				firebase.setTo(device.getPushToken());
//				sendMsg(firebase, device.getType());
//			}
//		}
//		return true;
//	}
//
//	private void sendMsg(FirebaseNotification firebase, String type) {
//		try {
//			Gson gson = new GsonBuilder().create();
//			NotificationUtil.pushMsg(gson.toJson(firebase), type);
//		} catch (InternalErrorException e) {
//			log.error(e.getMessage(), e.getCause());
//		}
//	}

}
