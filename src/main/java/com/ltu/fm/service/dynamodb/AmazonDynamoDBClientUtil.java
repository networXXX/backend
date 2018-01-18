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
package com.ltu.fm.service.dynamodb;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.ltu.fm.configuration.AppConfiguration;
import com.ltu.fm.utils.S3ResourceLoaderUtil;

/**
 * The Class AmazonDynamoDBClientUtil.
 * @author uyphu
 */
public class AmazonDynamoDBClientUtil {

	/** The dynamo db. */
	private static AmazonDynamoDBClient dynamoDB = null;

	/**
	 * Gets the single instance of AmazonDynamoDBClientUtil.
	 *
	 * @return single instance of AmazonDynamoDBClientUtil
	 */
	public static AmazonDynamoDBClient getInstance() {
		if (dynamoDB == null) {
			dynamoDB = new AmazonDynamoDBClient();
			String region = S3ResourceLoaderUtil.getProperty(AppConfiguration.REGION_KEY);
			if (region != null) {
				dynamoDB.setRegion(Region.getRegion(Regions.fromName(region)));
			}
		}
		return dynamoDB;
		
	}
	
}
