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
package com.ltu.fm.configuration;

// TODO: Auto-generated Javadoc
/**
 * Configuration parameters for the DynamoDB DAO objects.
 * @author uyphu
 * 
 */
public class DynamoDBConfiguration {
    
    /** The Constant USERS_TABLE_NAME. */
    // TODO: Specify the name of the Users table in DynamoDB
    public static final String USERS_TABLE_NAME = "nx_users";
    
    /** The Constant LOCATION_TABLE_NAME. */
    public static final String LOCATION_TABLE_NAME = "nx_location";
    
    /** The Constant USER_POINT_TABLE_NAME. */
    public static final String USER_POINT_TABLE_NAME = "nx_user_point";
    
    /** The Constant FRIENDS_TABLE_NAME. */
    public static final String FRIENDS_TABLE_NAME = "nx_friends";
    
    /** The Constant SCAN_LIMIT. */
    public static final int SCAN_LIMIT = 50;
    
    /** The Constant USERID_DEVICE_INDEX. */
    public static final String USERID_DEVICE_INDEX = "lastLoginUserId-index";
    
    /** The Constant PHONID_DEVICE_INDEX. */
    public static final String PHONID_DEVICE_INDEX = "phoneId-index";
    
    /** The Constant USER_STARTDATE_USER_EVENT_INDEX. */
    public static final String USER_STARTDATE_USER_EVENT_INDEX = "userId-startDate-index";
    
    /** The Constant USER_STARTDATE_USER_CONFERENCE_INDEX. */
    public static final String USER_STARTDATE_USER_CONFERENCE_INDEX = "userId-startDate-index";
    
    public static final String CREATED_AT_USER_POINT_INDEX = "createdAt-index";
    
    /** The Constant SECRET_USERID_INDEX. */
    public static final String SECRET_USERID_INDEX = "userId-domain-index";
    
    /** The Constant USER_EMAIL_INDEX. */
    public static final String USER_EMAIL_INDEX = "email-index";
    
    /** The Constant USER_ID_EMAIL_INDEX. */
    public static final String USER_ID_INDEX = "userId-index";
    
}

