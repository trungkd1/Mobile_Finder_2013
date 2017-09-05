package com.nahi.vn.mobilefinder.entity;



// TODO: Auto-generated Javadoc
/**
 * The Class CallLogEntry.
 */
public class CallLogEntry {
	
	/** The name. */
	private String name;
	
	/** The number. */
	private String number;
	
	/** The type. */
	private String type;
	
	/** The date. */
	private String date;

	/** The duration. */
	private String duration;
	
	/**
	 * Instantiates a new call log entry.
	 */
	public CallLogEntry() {
	}

	/**
	 * Instantiates a new call log entry.
	 *
	 * @param mName the m name
	 * @param mNumber the m number
	 * @param mType the m type
	 * @param mDate the m date
	 * @param mDuration the m duration
	 */
	public CallLogEntry(String mName, String mNumber, String mType,
			String mDate, String mDuration) {
		this.name = mName;
		this.number = mNumber;
		this.type = mType;
		this.date = mDate;
		this.duration = mDuration;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the number.
	 *
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Sets the number.
	 *
	 * @param number the new number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * Sets the duration.
	 *
	 * @param duration the new duration
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

}
