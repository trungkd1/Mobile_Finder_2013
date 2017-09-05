/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: NahiKidsMobile - com.nahi.vn.kidsmobile.listener - HttpClientListener.java
 * Date create: 2:07:59 PM - Dec 6, 2013 - 2013
 * 
 * 
 */
package com.nahi.vn.mobilefinder.listener;

import java.util.List;

import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;


// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving httpClient events. The class that is
 * interested in processing a httpClient event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addHttpClientListener<code> method. When
 * the httpClient event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see HttpClientEvent
 */
public interface HttpClientListener {

	/**
	 * On http result.
	 *
	 * @param methods the methods
	 * @param data the data
	 */
	public void onHttpResult(Methods methods, List<Object> data);

	/**
	 * On start api.
	 *
	 * @param methods the methods
	 */
	public void onStartAPI(Methods methods);

	/**
	 * On call api fail.
	 *
	 * @param methods the methods
	 * @param statusCode the status code
	 */
	public void onCallAPIFail(Methods methods, int statusCode);

	/**
	 * On progress update.
	 *
	 * @param methods the methods
	 * @param progress the progress
	 */
	public void onProgressUpdate(Methods methods, int progress);
}
