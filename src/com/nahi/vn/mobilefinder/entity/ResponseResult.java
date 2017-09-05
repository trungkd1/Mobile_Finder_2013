package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class ResponseResult.
 */
public class ResponseResult {

	/** The Constant STATUS_OK. */
	public final static int STATUS_OK = 1000;
	
	/** The status. */
	private int status;
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new response result.
	 */
	public ResponseResult() {
	}

	/**
	 * Instantiates a new response result.
	 *
	 * @param status the status
	 * @param message the message
	 */
	public ResponseResult(int status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
