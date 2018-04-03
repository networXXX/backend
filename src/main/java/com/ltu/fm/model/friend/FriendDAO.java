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
package com.ltu.fm.model.friend;

import java.util.List;

import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.action.user.ListResponse;
import com.ltu.fm.model.user.User;

// TODO: Auto-generated Javadoc
/**
 * This interface defines the methods required for an implementation of the UserDAO object.
 */
public interface FriendDAO {
    

    /**
     * Creates a new friend in the data store.
     *
     * @param friend The new friend information
     * @return The friendname of the friend that was created
     * @throws DAOException the DAO exception
     */
    String createUser(Friend friend) throws DAOException;
    
    
    /**
     * Insert.
     *
     * @param friend the friend
     * @return the device
     * @throws DAOException the DAO exception
     */
    Friend insert(Friend friend) throws DAOException;
    
    /**
     * Update.
     *
     * @param friend the friend
     * @return the device
     * @throws DAOException the DAO exception
     */
    Friend update(Friend friend) throws DAOException;
    
    /**
     * Merge.
     *
     * @param friend the friend
     * @return the device
     * @throws DAOException the DAO exception
     */
    Friend merge(Friend friend) throws DAOException;
    
    /**
     * Delete.
     *
     * @param id the id
     * @throws DAOException the DAO exception
     */
    void delete(String id) throws DAOException;
    
    /**
     * Gets the.
     *
     * @param id the id
     * @return the device
     */
    Friend find(String id);    
    
    /**
     * Find by email.
     *
     * @param email the email
     * @return the friend
     */
    Friend findByFriend(String userId, String otherId);
    
    /**
     * Search.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    List<Friend> search(String query, int limit, String cursor, String indexStr);
    
    /**
     * Mapper scan.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    public List<Friend> mapperScan(String query, int limit, String cursor, String indexStr);
    
    /**
     * Scan.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    public List<Friend> scan(String query, int limit, String cursor, String indexStr);
    
    
    /**
     * Scan user.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @param indexStr the index str
     * @return the list
     */
    public ListResponse<User> queryUser(String query, int limit, String cursor, String indexStr);
}

