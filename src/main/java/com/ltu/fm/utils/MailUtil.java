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

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ltu.fm.configuration.AppConfiguration;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.exception.CommonException;
import com.ltu.fm.exception.ErrorCodeDetail;

/**
 * The Class MailUtil.
 * @author uyphu
 * created on May 21, 2017
 */
public class MailUtil {
	
	/** The logger. */
	private static final Logger logger = LogManager.getLogger("MailUtil");
	
	/** The Constant FROM. */
	//static final String FROM = S3ResourceLoaderUtil.getProperty(AppConfiguration.FROM_MAIL);  // Replace with your "From" address. This address must be verified.
                                                      
                                                      /** The Constant SUBJECT. */
                                                      // sandbox, this address must be verified.
    static final String SUBJECT = "Activate account";
    
    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    //static final String SMTP_USERNAME = "AKIAIGBFYQWVAT4LXGUA";  // Replace with your SMTP username.
    //static final String SMTP_PASSWORD = "Ak6BPBz0hw8BUGfoLDJi560/l393brVCQnYNoqy9rLU0";  // Replace with your SMTP password.
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    //static final String HOST = "email-smtp.us-east-1.amazonaws.com";//"email-smtp.us-west-2.amazonaws.com";    
    
    /** The Constant SMTP_USERNAME. */
    //static final String SMTP_USERNAME = S3ResourceLoaderUtil.getProperty(AppConfiguration.SMTP_USERNAME);
    
    /** The Constant SMTP_PASSWORD. */
    //static final String SMTP_PASSWORD = S3ResourceLoaderUtil.getProperty(AppConfiguration.SMTP_PASSWORD);
    
    /** The Constant HOST. */
    //static final String HOST = S3ResourceLoaderUtil.getProperty(AppConfiguration.HOST_MAIL);  
    
    
    private static MailUtil instance;
    
    private MailUtil(){}
    
    public static MailUtil getInstance(){
        if(instance == null){
            instance = new MailUtil();
        }
        return instance;
    }
    
    // Port we will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    /** The Constant PORT. */
    // STARTTLS to encrypt the connection.
    static final int PORT = 25;
    
	/**
	 * Send activate email.
	 *
	 * @param to the to
	 * @param activateKey the activate key
	 * @return true, if successful
	 * @throws CommonException the common exception
	 */
	public static boolean sendActivateEmail(String to, String activateKey) throws CommonException {

		try {
			// Create a Properties object to contain connection configuration
			// information.
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", PORT);

			// Set properties indicating that we want to use STARTTLS to encrypt
			// the connection.
			// The SMTP session will begin on an unencrypted connection, and
			// then the client
			// will issue a STARTTLS command to upgrade to an encrypted
			// connection.
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.starttls.required", "true");

			// Create a Session object to represent a mail session with the
			// specified properties.
			Session session = Session.getDefaultInstance(props);

			// Create a message with the specified information.
			MimeMessage msg = new MimeMessage(session);
			//msg.setFrom(new InternetAddress(FROM));
			msg.setFrom(new InternetAddress(S3ResourceLoaderUtil.getProperty(AppConfiguration.FROM_MAIL)));
			msg.setRecipient(javax.mail.Message.RecipientType.TO,
					new InternetAddress(to));
			msg.setSubject(SUBJECT);
			String body = buildMessageBody(activateKey);
			msg.setContent(body, "text/plain");

			// Create a transport.
			Transport transport = session.getTransport();

			// Send the message.
			try {
				// System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");

				// Connect to Amazon SES using the SMTP username and password
				// you specified above.
				//transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
				transport.connect(S3ResourceLoaderUtil.getProperty(AppConfiguration.HOST_MAIL), 
						S3ResourceLoaderUtil.getProperty(AppConfiguration.SMTP_USERNAME), 
						S3ResourceLoaderUtil.getProperty(AppConfiguration.SMTP_PASSWORD));

				// Send the email.
				transport.sendMessage(msg, msg.getAllRecipients());
				// System.out.println("Email sent!");
			} catch (Exception ex) {
				//System.out.println("The email was not sent.");
				//System.out.println("Error message: " + ex.getMessage());
				logger.error("The email was not sent." + ex.getMessage());
				throw new CommonException(ErrorCodeDetail.ERROR_SEND_EMAIL.getMsg(), ex);
			} finally {
				// Close and terminate the connection.
				transport.close();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(ErrorCodeDetail.ERROR_SEND_EMAIL.getMsg(), e);
		}
		return true;
	}
	
	/**
	 * Send email.
	 *
	 * @param to the to
	 * @param subject the subject
	 * @param body the body
	 * @return true, if successful
	 * @throws CommonException the common exception
	 */
	public static boolean sendEmail(String to, String subject, String  body) throws CommonException{

		try {
			// Create a Properties object to contain connection configuration
			// information.
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", PORT);

			// Set properties indicating that we want to use STARTTLS to encrypt
			// the connection.
			// The SMTP session will begin on an unencrypted connection, and
			// then the client
			// will issue a STARTTLS command to upgrade to an encrypted
			// connection.
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.starttls.required", "true");

			// Create a Session object to represent a mail session with the
			// specified properties.
			Session session = Session.getDefaultInstance(props);

			// Create a message with the specified information.
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(S3ResourceLoaderUtil.getProperty(AppConfiguration.FROM_MAIL)));
			msg.setRecipient(javax.mail.Message.RecipientType.TO,
					new InternetAddress(to));
			msg.setSubject(subject);
			msg.setContent(body, "text/plain");

			// Create a transport.
			Transport transport = session.getTransport();

			// Send the message.
			try {
				// System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");

				// Connect to Amazon SES using the SMTP username and password
				// you specified above.
				//transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
				transport.connect(S3ResourceLoaderUtil.getProperty(AppConfiguration.HOST_MAIL), 
						S3ResourceLoaderUtil.getProperty(AppConfiguration.SMTP_USERNAME), 
						S3ResourceLoaderUtil.getProperty(AppConfiguration.SMTP_PASSWORD));

				// Send the email.
				transport.sendMessage(msg, msg.getAllRecipients());
				// System.out.println("Email sent!");
			} catch (Exception ex) {
				//System.out.println("The email was not sent.");
				//System.out.println("Error message: " + ex.getMessage());
				logger.error("The email was not sent." + ex.getMessage());
				throw new CommonException(ErrorCodeDetail.ERROR_SEND_EMAIL.getMsg(), ex);
			} finally {
				// Close and terminate the connection.
				transport.close();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(ErrorCodeDetail.ERROR_SEND_EMAIL.getMsg(), e);
		}
		return true;
	}
	
	/**
	 * Builds the message body.
	 *
	 * @param activateKey the activate key
	 * @return the string
	 */
	private static String buildMessageBody(String activateKey) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(S3ResourceLoaderUtil.getProperty(AppConfiguration.ACTIVATION_MESSAGE));
		builder.append(Constants.SPACE_STRING);
		//builder.append(S3ResourceLoaderUtil.getProperty(AppConfiguration.WEB_URL_KEY));
		//builder.append("activateaccount/?activatecode=");
		builder.append(activateKey);
		return builder.toString();
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			sendActivateEmail("uyphu@yahoo.com", "324234");
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

