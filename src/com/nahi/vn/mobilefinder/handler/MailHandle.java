package com.nahi.vn.mobilefinder.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.util.Config;


// TODO: Auto-generated Javadoc
/**
 * The Class MailHandle.
 */
public class MailHandle extends javax.mail.Authenticator { 
	
	/** The address. */
	private String address; 
	
	/** The pass. */
	private String pass;
	
	/** The port. */
	private String port; 
	
	/** The sport. */
	private String sport;
	
	/** The host. */
	private String host;
	
	/** The subject. */
	private String subject; 
	
	/** The body. */
	private String body; 
	
	/** The to email. */
	private String toEmail; 

	/** The auth. */
	private boolean auth;
	
	/** The debuggable. */
	private boolean debuggable; 

	/** The multipart. */
	private Multipart multipart; 

	/** The mail. */
	public static MailHandle mail;

	/**
	 * Gets the single instance of MailHandle.
	 *
	 * @param context the context
	 * @return single instance of MailHandle
	 */
	public static MailHandle getInstance(Context context){
		if(mail == null){
			mail = new MailHandle(context);
		}
		return mail;
	}

	/**
	 * Instantiates a new mail handle.
	 *
	 * @param context the context
	 */
	private MailHandle(Context context) { 
		host = "smtp.gmail.com"; // default smtp server 
		port = "465"; // default smtp port 
		sport = "465"; // default socketfactory port 
		address = Config.NAHI_SENDER_EMAIL; // username 
		pass = Config.NAHI_SENDER_EMAIL_PASSWORD; // password
		subject = App.APP_NAME; // email subject 

		debuggable = false; // debug mode on or off - default off 
		auth = true; // smtp authentication - default on 

		// There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added. 
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
		CommandMap.setDefaultCommandMap(mc); 
	}

	/**
	 * Send email.
	 *
	 * @param toEmail the to email
	 * @param body the body
	 * @param listFileNames the list file names
	 */
	public void sendEmail(String toEmail,String body, ArrayList<String> listFileNames){
		this.toEmail = toEmail;
		this.body = body;
		multipart = new MimeMultipart();

		if(listFileNames != null){
			for(String fileName : listFileNames){
				if(new File(fileName).exists()){
					addAttachment(fileName); 
				}		
			}
		} 

		send();
	}

	/**
	 * Send.
	 *
	 * @return true, if successful
	 */
	private boolean send(){ 
		try{
			Properties props = setProperties(); 
			if(!TextUtils.isEmpty(address) && !TextUtils.isEmpty(pass)) {
				Session session = Session.getInstance(props, this); 

				final MimeMessage msg = new MimeMessage(session); 
				InternetAddress sendAddress = new InternetAddress(address);
				msg.setFrom(sendAddress); 

				InternetAddress[] addressTo = new InternetAddress[]{
						new InternetAddress(toEmail)}; 

				msg.setRecipients(MimeMessage.RecipientType.TO, addressTo); 

				msg.setSubject(subject); 
				msg.setSentDate(new Date()); 

				// setup message body 
				BodyPart messageBodyPart = new MimeBodyPart(); 
				messageBodyPart.setText(body); 
				multipart.addBodyPart(messageBodyPart); 

				// Put parts in message 
				msg.setContent(multipart); 

				// send email 
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						try {
							Transport.send(msg);
						} catch (MessagingException e) {
							e.printStackTrace();
						}
						return null;
					}
					
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
					};
				}.execute();

				return true; 
			} else { 
				return false; 
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} 

	/**
	 * Adds the attachment.
	 *
	 * @param filename the filename
	 */
	private void addAttachment(String filename){ 
		try{
			BodyPart messageBodyPart = new MimeBodyPart(); 
			DataSource source = new FileDataSource(filename); 
			messageBodyPart.setDataHandler(new DataHandler(source)); 
			messageBodyPart.setFileName(filename); 

			multipart.addBodyPart(messageBodyPart); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	/* (non-Javadoc)
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	@Override 
	public PasswordAuthentication getPasswordAuthentication() { 
		return new PasswordAuthentication(address, pass); 
	} 

	/**
	 * Sets the properties.
	 *
	 * @return the properties
	 */
	private Properties setProperties() { 
		Properties props = new Properties(); 

		props.put("mail.smtp.host", host); 

		if(debuggable) { 
			props.put("mail.debug", "true"); 
		} 

		if(auth) { 
			props.put("mail.smtp.auth", "true"); 
		} 

		props.put("mail.smtp.port", port); 
		props.put("mail.smtp.socketFactory.port", sport); 
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
		props.put("mail.smtp.socketFactory.fallback", "false"); 
		
//		props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.user", FROM_ADDRESS);
//        props.put("mail.smtp.password", PASSWORD);
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
		
		return props; 
	}
} 