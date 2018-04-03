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
package com.ltu.fm.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.ltu.fm.service.dynamodb.AmazonDynamoDBClientUtil;

/**
 * The Class AbstractDao.
 *
 * @param <T> the generic type
 */
public abstract class AbstractDao<T> implements Dao<T> {

	/** The client. */
	public static AmazonDynamoDBClient client = AmazonDynamoDBClientUtil.getInstance();
	
	/** The mapper. */
	static DynamoDBMapper mapper = new DynamoDBMapper(client);
	
	/** The clazz. */
	private final Class<T> clazz;
	
	/**
	 * Instantiates a new abstract dao.
	 *
	 * @param clazz the clazz
	 */
	public AbstractDao(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public T get(String id) {
		return mapper.load(clazz, id);
	}
	
	@Override
	public T find(String id) {
		return get(id);
	}
	
	@Override
	public T save(T t) {
		mapper.save(t);
		return t;
	}

	@Override
	public void delete(T t) {
		mapper.delete(t);
	}
	
}
