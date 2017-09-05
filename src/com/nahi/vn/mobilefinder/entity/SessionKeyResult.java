package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class SessionKey.
 */
public class SessionKeyResult {

	/** The session key. */
	private String sessionKey;
	
	/** The expired time. */
	private String expiredTime;

	/**
	 * Instantiates a new session key.
	 *
	 * @param sessionKey the session key
	 * @param expiredTime the expired time
	 */
	public SessionKeyResult(String sessionKey, String expiredTime) {
		this.sessionKey = sessionKey;
		this.expiredTime = expiredTime;
	}

	/**
	 * Gets the session key.
	 *
	 * @return the session key
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * Sets the session key.
	 *
	 * @param sessionKey the new session key
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
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
