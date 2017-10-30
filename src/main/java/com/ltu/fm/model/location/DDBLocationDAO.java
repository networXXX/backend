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
package com.ltu.fm.model.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
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

/**
 * The Class DDBLocationDAO.
 * 
 * @author uyphu
 */
public class DDBLocationDAO extends AbstractDao<Location> implements LocationDAO {

	/** The instance. */
	private static DDBLocationDAO instance = null;

	/**
	 * Returns an initialized instance of the DDBLocationDAO object. DAO objects
	 * should be retrieved through the DAOFactory class
	 *
	 * @return An initialized instance of the DDBLocationDAO object
	 */
	public static DDBLocationDAO getInstance() {
		if (instance == null) {
			instance = new DDBLocationDAO();
		}
		return instance;
	}

	/**
	 * Instantiates a new DDB Location dao.
	 */
	protected DDBLocationDAO() {
		super(Location.class);
	}

	@Override
	public Location insert(Location location) throws DAOException {
//		Location item = findByPhoneId(location.getPhoneId());
//		if (item != null) {
//			throw new DAOException(ExceptionMessages.EX_DUPLICATED_ITEM);
//		}
		location.setCreatedAt(AppUtil.getUTCDateTime());
		return super.save(location);
	}

	@Override
	public Location update(Location location) throws DAOException {
		if (location.getId() == null) {
			throw new DAOException(ExceptionMessages.EX_LOCATION_NOT_FOUND);
		}
		//FIXME need to remove location.setCreatedAt(C); 
		location.setCreatedAt(AppUtil.getUTCDateTime());
		return super.save(location);
	}

	@Override
	public Location merge(Location location) throws DAOException {
		if (location.getId() != null) {
			return update(location);
		} else {
//			Location old = findByPhoneId(location.getPhoneId());
//			if (old != null ) {
//				location.setId(old.getId());
//				return update(location);
//			}
			return insert(location);
		}
	}

	@Override
	public void delete(String id) throws DAOException {
		Location item = get(id);
		if (item == null) {
			throw new DAOException(ErrorCodeDetail.ERROR_RECORD_NOT_FOUND.getMsg());
		} else {
			super.delete(item);
		}
	}

	@Override
	public Location find(String id) {
		return super.find(id);
	}

	@Override
	public List<Location> search(String query, Integer limit, String cursor) {
		if (query == null) {
			return mapperScan(query, limit, cursor);
		}
		return scan(query, limit, cursor);
	}
	
	private Map<String, AttributeValue> buildExclusiveStartKey(String cursor) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(cursor));
		return exclusiveStartKey;
	}

	public Location findByUserId(String userId) {
		QueryResultPage<Location> list = findByUserId(userId, 1, null);
		if (!list.getResults().isEmpty()) {
			return list.getResults().get(0);
		}
		return null;
	}
	
	private ScanRequest buildScan(String query, int limit) {
		ScanRequest scanRequest = new ScanRequest(DynamoDBConfiguration.LOCATION_TABLE_NAME);

		if (query != null) {
			HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("userId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("userId", condition);
				} else if (field.indexOf("type:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("type", condition);
				} else if (field.indexOf("lastLoginUserId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("lastLoginUserId", condition);
				}
			}

			scanRequest.withScanFilter(scanFilter);
		}
		scanRequest.setLimit(DynamoDBConfiguration.SCAN_LIMIT);
        
		return scanRequest;
	}
	
	private DynamoDBScanExpression buildScanMapper(String query, Integer limit) {
		DynamoDBScanExpression dbScanExpression = new DynamoDBScanExpression();

		if (query != null) {
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("userId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("userId", condition);
				} else if (field.indexOf("type:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("type", condition);
				} else if (field.indexOf("lastLoginUserId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("lastLoginUserId", condition);
				}
			}
		}
//		if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT) {
//			limit = DynamoDBConfiguration.SCAN_LIMIT;
//		}
//		dbScanExpression.setLimit(limit);
		
		dbScanExpression.setLimit(DynamoDBConfiguration.SCAN_LIMIT);
        
		return dbScanExpression;
	}
	
	private DynamoDBQueryExpression<Location> buildQueryMapper(String query, Integer limit) {
		DynamoDBQueryExpression<Location> dbQueryExpression = new DynamoDBQueryExpression<Location>();

		if (query != null) {
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("userId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbQueryExpression.withQueryFilterEntry("userId", condition);
				}
			}
		}
		//FIXME phuLTU needs to review this
//		if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT) {
//			limit = DynamoDBConfiguration.SCAN_LIMIT;
//		}
//		dbQueryExpression.withLimit(limit);
		dbQueryExpression.withLimit(DynamoDBConfiguration.SCAN_LIMIT);
		return dbQueryExpression;
	}

	@Override
	public List<Location> scan(String query, Integer limit, String cursor) {
		int count = limit != null ? limit : 0;
		ScanRequest scanRequest = buildScan(query, count);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor);
		List<Location> locations = new ArrayList<Location>();
		
		do {
			if (exclusiveStartKey != null) {
				scanRequest.setExclusiveStartKey(exclusiveStartKey);
			}
			ScanResult scanResult = client.scan(scanRequest);
			
			if (scanResult != null && scanResult.getItems().size() > 0) {
				for (Map<String, AttributeValue> item : scanResult.getItems()) {
					locations.add(new Location(item));
					if (count == locations.size()) {
						return locations;
					}
				}
			}
			exclusiveStartKey = scanResult.getLastEvaluatedKey();
		} while (exclusiveStartKey != null);
		
		return locations;
	}

	@Override
	public List<Location> mapperScan(String query, int limit, String cursor) {
		DynamoDBScanExpression scanExpression = buildScanMapper(query, limit);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor);
		if (exclusiveStartKey != null) {
			scanExpression.setExclusiveStartKey(exclusiveStartKey);
		}
		PaginatedScanList<Location> scanList = getMapper().scan(Location.class, scanExpression);
		return scanList;
	}
	
	@Override
	public List<Location> mapperQuery(String query, int limit, String cursor) {
		//FIXME PhuLTU Illegal query expression: No hash key condition is found in the query
		DynamoDBQueryExpression<Location> dbQueryExpression = buildQueryMapper(query, limit);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor);
		if (exclusiveStartKey != null) {
			dbQueryExpression.setExclusiveStartKey(exclusiveStartKey);
		}
		return getMapper().query(Location.class, dbQueryExpression);
	}
	
	private Map<String, AttributeValue> buildExclusiveStartKeyWithUserId(String cursor) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		
		Location item = find(cursor);
		if (item == null) {
			return null;
		}
		
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(item.getId()));
		//exclusiveStartKey.put("lastLoginUserId", new AttributeValue(item.getLastLoginUserId()));
		return exclusiveStartKey;
	}
	
	@Override
	public QueryResultPage<Location> findByUserId(String userId, Integer limit, String cursor) {
		
		HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":lastLoginUserId", new AttributeValue().withS(userId));
		DynamoDBQueryExpression<Location> queryExpression = new DynamoDBQueryExpression<Location>()
				.withIndexName("lastLoginUserId-index")
				.withConsistentRead(false)
				.withKeyConditionExpression("lastLoginUserId = :lastLoginUserId")
				.withExpressionAttributeValues(eav).withLimit(limit);
		queryExpression.setExclusiveStartKey(buildExclusiveStartKeyWithUserId(cursor));

		QueryResultPage<Location> list = getMapper().queryPage(Location.class, queryExpression);

		return list;
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
