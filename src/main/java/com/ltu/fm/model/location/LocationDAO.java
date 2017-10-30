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

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.ltu.fm.exception.DAOException;

/**
 * The Interface LocationDAO.
 */
public interface LocationDAO {
    
    /**
     * Insert.
     *
     * @param user the user
     * @return the Location
     * @throws DAOException the DAO exception
     */
    Location insert(Location user) throws DAOException;
    
    /**
     * Update.
     *
     * @param user the user
     * @return the Location
     * @throws DAOException the DAO exception
     */
    Location update(Location user) throws DAOException;
    
    /**
     * Merge.
     *
     * @param user the user
     * @return the Location
     * @throws DAOException the DAO exception
     */
    Location merge(Location user) throws DAOException;
    
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
     * @return the Location
     * @throws DAOException the DAO exception
     */
    Location find(String id);
    
    /**
     * Search.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     * @throws DAOException the DAO exception
     */
    List<Location> search(String query, Integer limit, String cursor);
    
    /**
     * Scan.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    List<Location> scan(String query, Integer limit, String cursor);
    
    /**
     * Query.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    List<Location> mapperScan(String query, int limit, String cursor);
    
    /**
     * Mapper query.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    List<Location> mapperQuery(String query, int limit, String cursor);
    
    /**
     * Find by user id.
     *
     * @param userId the user id
     * @param limit the limit
     * @param cursor the cursor
     * @return the query result page
     */
    QueryResultPage<Location> findByUserId(String userId, Integer limit, String cursor);
    
    
}
