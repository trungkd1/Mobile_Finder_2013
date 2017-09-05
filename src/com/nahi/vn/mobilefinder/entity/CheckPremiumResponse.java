/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2014 Aris-vn, Inc. All rights reserved.
 * Author: TamLe
 * Location: NahiMobileFinder - com.nahi.vn.mobilefinder.entity - CheckPremiumResponse.java
 * Date create: 10:26:59 AM - Jan 22, 2014 - 2014
 * 
 * 
 */
package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class CheckPremiumResponse.
 */
public class CheckPremiumResponse {

	/**
	 * Gets the response result.
	 * 
	 * @return the responseResult
	 */
	public ResponseResult getResponseResult() {
		return responseResult;
	}

	/**
	 * Sets the response result.
	 * 
	 * @param responseResult
	 *            the responseResult to set
	 */
	public void setResponseResult(ResponseResult responseResult) {
		this.responseResult = responseResult;
	}

	/**
	 * Gets the premium result.
	 *
	 * @return the premiumResult
	 */
	public PremiumResult getPremiumResult() {
		return premiumResult;
	}

	/**
	 * Sets the premium result.
	 *
	 * @param premiumResult the premiumResult to set
	 */
	public void setPremiumResult(PremiumResult premiumResult) {
		this.premiumResult = premiumResult;
	}

	/** The response result. */
	private ResponseResult responseResult;
	
	/** The premium result. */
	private PremiumResult premiumResult;

}
