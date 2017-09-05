package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactEntry.
 */
public class ContactEntry {

	/** The name. */
	private String name;
	
	/** The number. */
	private String number;
	
	/** The type. */
	private String type;
	
	/**
	 * Instantiates a new contact entry.
	 */
	public ContactEntry() {
	}

	/**
	 * Instantiates a new contact entry.
	 *
	 * @param name the name
	 * @param number the number
	 * @param type the type
	 */
	public ContactEntry(String name, String number, String type) {
		this.name = name;
		this.number = number;
		this.type = type;
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
	
}
