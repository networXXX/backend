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
package com.ltu.fm.dao.factory;

import com.ltu.fm.model.friend.DDBFriendDAO;
import com.ltu.fm.model.friend.FriendDAO;
import com.ltu.fm.model.geo.DDBUserPointDAO;
import com.ltu.fm.model.geo.UserPointDAO;
import com.ltu.fm.model.location.DDBLocationDAO;
import com.ltu.fm.model.location.LocationDAO;
import com.ltu.fm.model.user.DDBUserDAO;
import com.ltu.fm.model.user.UserDAO;

/**
 * The DAO Factory object to abstract the implementation of DAO interfaces.
 */
public class DAOFactory {
    
    /**
     * Contains the implementations of the DAO objects. By default we only have a DynamoDB implementation
     *
     * @author FPT LA
     * @date Oct 29, 2017
     */
    public enum DAOType {
        
        /** The Dynamo db. */
        DynamoDB
    }

    /**
     * Returns the default UserDAO object.
     *
     * @return The default implementation of the UserDAO object - by default this is the DynamoDB implementation
     */
    public static UserDAO getUserDAO() {
        return getUserDAO(DAOType.DynamoDB);
    }

    /**
     * Returns a UserDAO implementation.
     *
     * @param daoType A value from the DAOType enum
     * @return The corresponding UserDAO implementation
     */
    public static UserDAO getUserDAO(DAOType daoType) {
        UserDAO dao = null;
        switch (daoType) {
            case DynamoDB:
                dao = DDBUserDAO.getInstance();
                break;
        }

        return dao;
    }
    
    /**
     * Gets the user point DAO.
     *
     * @return the user point DAO
     */
    public static UserPointDAO getUserPointDAO() {
        return getUserPointDAO(DAOType.DynamoDB);
    }

    /**
     * Gets the user point DAO.
     *
     * @param daoType the dao type
     * @return the user point DAO
     */
    public static UserPointDAO getUserPointDAO(DAOType daoType) {
    	UserPointDAO dao = null;
        switch (daoType) {
            case DynamoDB:
                dao = DDBUserPointDAO.getInstance();
                break;
        }

        return dao;
    }
    
    /**
     * Gets the location DAO.
     *
     * @return the location DAO
     */
    public static LocationDAO getLocationDAO() {
        return getLocationDAO(DAOType.DynamoDB);
    }

    /**
     * Gets the location DAO.
     *
     * @param daoType the dao type
     * @return the location DAO
     */
    public static LocationDAO getLocationDAO(DAOType daoType) {
    	LocationDAO dao = null;
        switch (daoType) {
            case DynamoDB:
                dao = DDBLocationDAO.getInstance();
                break;
        }

        return dao;
    }
        
    /**
     * Gets the friend DAO.
     *
     * @return the friend DAO
     */
    public static FriendDAO getFriendDAO() {
        return getFriendDAO(DAOType.DynamoDB);
    }

    /**
     * Gets the friend DAO.
     *
     * @param daoType the dao type
     * @return the friend DAO
     */
    public static FriendDAO getFriendDAO(DAOType daoType) {
    	FriendDAO dao = null;
        switch (daoType) {
            case DynamoDB:
                dao = DDBFriendDAO.getInstance();
                break;
        }

        return dao;
    }
    
}
