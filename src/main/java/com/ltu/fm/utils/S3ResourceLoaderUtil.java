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
package com.ltu.fm.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.ltu.fm.configuration.AppConfiguration;

/**
 * The Class S3ResourceLoaderUtil.
 * @author uyphu
 * created on May 22, 2017
 */
public class S3ResourceLoaderUtil {
	
	/** The log. */
	private static final Logger logger = LogManager.getLogger("S3ResourceLoaderUtil");
	
	/** The props. */
	public static Properties props = new Properties();

	/**
	 * Load properties.
	 */
	private static void loadProperties() {
		 
		InputStream inputStream;
		try {
			String propFileName = AppConfiguration.CONFIG_FILE_NAME;
 
			inputStream = AppUtil.class.getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				props.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			inputStream.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		} 
	}
	
	/**
	 * Load s3 properties.
	 */
	private static void loadS3Properties() {
		try {
			AmazonS3 client = new AmazonS3Client();
			
			String bucket = System.getenv(AppConfiguration.S3_BUCKET);
			if (bucket == null) {
				bucket = AppConfiguration.BUCKET_NAME;
			}
			
			S3Object xFile = client.getObject(bucket, AppConfiguration.CONFIG_FILE_NAME);
			InputStream contents = xFile.getObjectContent();
			
			if (contents != null) {
				props.load(contents);
			} else {
				throw new FileNotFoundException("property file 'config.properties'  not found in bucket 'config-no-deleting' S3 ");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * Gets the property.
	 *
	 * @param key the key
	 * @return the property
	 */
	public static String getProperty(String key) {
		if (!props.containsKey(key)) {
			loadS3Properties();
		}
		String value = props.getProperty(key);
		if (value == null) {
			if (!props.containsKey(key)) {
				props = new Properties();
				loadProperties();
			}
			value = props.getProperty(key);
		}
		return value;
	}
	
	public static void main(String[] args) {
		System.getenv("S3_BUCKET");
		System.out.println(getProperty("senderEmail"));
	}
}

