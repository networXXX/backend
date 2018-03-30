package com.ltu.fm.helper;

import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;

/**
 * The Class UserHelper.
 * 
 * @author uyphu
 */
public class UserHelper {

	/**
	 * Convert to user.
	 * 
	 * @param user
	 *            the user
	 * @return the user
	 */
	public static User convertToUser(com.restfb.types.User user) {
		User obj = new User();
		//obj.setEmail(user.getId() + Constants.FACEBOOK_EMAIL);
		obj.setEmail(user.getEmail());
		obj.setActivateCode(user.getId());
		obj.setType(Constants.FACEBOOK_TYPE);
		return obj;
	}

	/**
	 * From json.
	 *
	 * @param userId the user id
	 * @return the user
	 */
//	public static User fromJson(JSONObject json) {
//		return null;
//	}
	
	public static User getUserById(String userId) {
		UserDAO dao = DAOFactory.getUserDAO();
		return dao.find(userId);
	}

}
