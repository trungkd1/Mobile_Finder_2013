package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class Session.
 */
public class MobileLoginResult {

	/** The id. */
	private int id;
	
	/** The token. */
	private String token;
	
	/** The expired time. */
	private String expiredTime;

	/**
	 * Instantiates a new session.
	 *
	 * @param id the id
	 * @param token the token
	 * @param expiredTime the expired time
	 */
	public MobileLoginResult(int id, String token, String expiredTime) {
		this.id = id;
		this.token = token;
		this.expiredTime = expiredTime;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the expired time.
	 *
	 * @return the expired time
	 */
	public String getExpiredTime() {
		return expiredTime;
	}

	/**
	 * Sets the expired time.
	 *
	 * @param expiredTime the new expired time
	 */
	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}
	
}
