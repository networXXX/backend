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
package com.ltu.fm.model.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.ltu.fm.configuration.DynamoDBConfiguration;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.AbstractDao;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.ErrorCodeDetail;
import com.ltu.fm.utils.AppUtil;
import com.ltu.fm.utils.ConvertUtil;

/**
 * DynamoDB implementation of the UserDAO interface. This class reads the
 * configuration from the DyanmoDBConfiguration object in the
 * com.amazonaws.apigatewaydemo.configuration package. Credentials to access
 * DynamoDB are retrieved from the Lambda environment.
 * <p/>
 * The table in DynamoDB should be created with an Hash Key called friendname.
 */
public class DDBFriendDAO extends AbstractDao<Friend> implements FriendDAO {
	
	private static DDBFriendDAO instance = null;
    
	/**
	 * Returns an initialized instance of the DDBUserDAO object. DAO objects
	 * should be retrieved through the DAOFactory class
	 *
	 * @return An initialized instance of the DDBUserDAO object
	 */
	public static DDBFriendDAO getInstance() {
		if (instance == null) {
			instance = new DDBFriendDAO();
		}

		return instance;
	}

	protected DDBFriendDAO() {
		super(Friend.class);
	}	

	/**
	 * Inserts a new row in the DynamoDB friends table.
	 *
	 * @param friend
	 *            The new friend information
	 * @return The friendname that was just inserted in DynamoDB
	 * @throws DAOException
	 */
	@Override
	public String createUser(Friend friend) throws DAOException {
		if (friend.getUserId() == null || friend.getUserId().trim().equals("") || friend.getOtherId() == null || friend.getOtherId().trim().equals("")) {
			throw new DAOException("Cannot create friend with empty friendname");
		}

		return insert(friend).getId();
	}

	@Override
	public Friend insert(Friend friend) throws DAOException {
		Friend item = findByFriend(friend.getUserId(), friend.getOtherId());
		if (item != null) {
			throw new DAOException(ExceptionMessages.EX_DUPLICATED_ITEM);
		}
		friend.setCreatedAt(AppUtil.getUTCDateTime());
		return super.save(friend);
	}

	@Override
	public Friend update(Friend friend) throws DAOException {
		if (friend.getId() == null) {
			throw new DAOException(ExceptionMessages.EX_INVALID_INPUT);
		}
		return super.save(friend);
	}

	@Override
	public Friend merge(Friend friend) throws DAOException {
		if (friend.getId() != null) {
			return update(friend);
		} else {
			return insert(friend);
		}
	}

	@Override
	public void delete(String id) throws DAOException {
		Friend item = get(id);
		if (item == null) {
			throw new DAOException(ErrorCodeDetail.ERROR_RECORD_NOT_FOUND.getMsg());
		} else {
			super.delete(item);
		}
	}

	@Override
	public Friend find(String id) {
		return super.find(id);
	}

	@Override
	public List<Friend> search(String query, int limit, String cursor, String indexStr) {
		if (query == null) {
			return mapperScan(query, limit, cursor, indexStr);
		}
		return scan(query, limit, cursor, indexStr);
	}

	@Override
	public Friend findByFriend(String friendId, String otherId) {
		List<Friend> list = search("userId:" + friendId + Constants.AND_STRING + "otherId:" + otherId, 1, null, DynamoDBConfiguration.USER_ID_INDEX);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}	
	
//	private Map<String, AttributeValue> buildExclusiveStartKey(String cursor) {
//		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
//			return null;
//		}
//		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
//		exclusiveStartKey.put("id", new AttributeValue(cursor));
//		return exclusiveStartKey;
//	}
	
	private Map<String, AttributeValue> buildExclusiveStartKey(String cursor, String indexStr) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		if (DynamoDBConfiguration.USER_ID_INDEX.equals(indexStr)) {
			return buildEmailIndex(cursor);
		}
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(cursor));
		return exclusiveStartKey;
	}
	
	private Map<String, AttributeValue> buildEmailIndex(String cursor) {
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		Friend friend = find(cursor);
		exclusiveStartKey.put("id", new AttributeValue(cursor));
		exclusiveStartKey.put("friendId", new AttributeValue(friend.getUserId()));
		return exclusiveStartKey;
	}

	private ScanRequest buildScan(String query, int limit, String indexStr) {
		ScanRequest scanRequest = new ScanRequest(DynamoDBConfiguration.FRIENDS_TABLE_NAME);
		
		if (indexStr != null) {
			scanRequest.setIndexName(indexStr);
		}

		if (query != null) {
			HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("userId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("userId", condition);
				} else if (field.indexOf("otherId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("otherId", condition);
				} else if (field.indexOf("status:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("status", condition);
				} 
			}

			scanRequest.withScanFilter(scanFilter);
		}
		// if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT) {
		// limit = DynamoDBConfiguration.SCAN_LIMIT;
		// }
		// scanRequest.setLimit(limit);

		scanRequest.setLimit(DynamoDBConfiguration.SCAN_LIMIT);

		return scanRequest;
	}

	private DynamoDBScanExpression buildScanMapper(String query, int limit) {
		DynamoDBScanExpression dbScanExpression = new DynamoDBScanExpression();

		if (query != null) {
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("email:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("email", condition);
				} else if (field.indexOf("pmCode:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("pmCode", condition);
				} else if (field.indexOf("activateCode:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("activateCode", condition);
				} else if (field.indexOf("status:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("status", condition);
				} else if (field.indexOf("type:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("type", condition);
				}
			}
		}
		// if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT) {
		// limit = DynamoDBConfiguration.SCAN_LIMIT;
		// }
		// dbScanExpression.setLimit(limit);

		dbScanExpression.setLimit(DynamoDBConfiguration.SCAN_LIMIT);

		return dbScanExpression;
	}

	@Override
	public List<Friend> scan(String query, int limit, String cursor, String indexStr) {
		ScanRequest scanRequest = buildScan(query, limit, indexStr);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor, indexStr);
		List<Friend> friends = new ArrayList<Friend>();

		do {
			if (exclusiveStartKey != null) {
				scanRequest.setExclusiveStartKey(exclusiveStartKey);
			}
			ScanResult scanResult = client.scan(scanRequest);

			if (scanResult != null && scanResult.getItems().size() > 0) {
				for (Map<String, AttributeValue> item : scanResult.getItems()) {
					friends.add(ConvertUtil.toFriend(item));
					if (limit == friends.size()) {
						return friends;
					}
				}
			}
			exclusiveStartKey = scanResult.getLastEvaluatedKey();
		} while (exclusiveStartKey != null);

		return friends;
	}

	@Override
	public List<Friend> mapperScan(String query, int limit, String cursor, String indexStr) {
		DynamoDBScanExpression scanExpression = buildScanMapper(query, limit);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor, indexStr);
		if (exclusiveStartKey != null) {
			scanExpression.setExclusiveStartKey(exclusiveStartKey);
		}
		PaginatedScanList<Friend> scanList = getMapper().scan(Friend.class, scanExpression);
		return scanList;
	}

	/**
	 * Gets the mapper.
	 *
	 * @return the mapper
	 */
	protected DynamoDBMapper getMapper() {
		return new DynamoDBMapper(client);
	}
}
