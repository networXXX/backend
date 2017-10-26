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
     * Find by phone id.
     *
     * @param phoneId the phone id
     * @return the Location
     */
    Location findByPhoneId(String phoneId);
    
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
    
    /**
     * Find by phone id.
     *
     * @param phoneId the phone id
     * @param limit the limit
     * @param cursor the cursor
     * @return the query result page
     */
    QueryResultPage<Location> findByPhoneId(String phoneId, Integer limit, String cursor);
    
}
