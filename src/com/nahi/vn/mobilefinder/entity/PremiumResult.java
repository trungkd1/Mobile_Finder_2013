/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2014 Aris-vn, Inc. All rights reserved.
 * Author: TamLe
 * Location: NahiMobileFinder - com.nahi.vn.mobilefinder.entity - PremiumResult.java
 * Date create: 10:32:24 AM - Jan 22, 2014 - 2014
 * 
 * 
 */
package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class PremiumResult.
 */
public class PremiumResult {
	
	/**
	 * Gets the premium.
	 *
	 * @return the premium
	 */
	public int getPremium() {
		return premium;
	}
	
	/**
	 * Sets the premium.
	 *
	 * @param premium the premium to set
	 */
	public void setPremium(int premium) {
		this.premium = premium;
	}
	
	/**
	 * Gets the expired.
	 *
	 * @return the expired
	 */
	public String getExpired() {
		return expired;
	}
	
	/**
	 * Sets the expired.
	 *
	 * @param expired the expired to set
	 */
	public void setExpired(String expired) {
		this.expired = expired;
	}
	
	/** The premium. */
	private int premium;
	
	/** The expired. */
	private String expired;

}
