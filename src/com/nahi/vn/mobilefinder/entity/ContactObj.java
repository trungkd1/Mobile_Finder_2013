package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactObj.
 */
public class ContactObj {
	
	/** The id. */
	public long id;
	
	/** The name. */
	public String name;
	
	/** The group. */
	public String group;
	
	/** The number. */
	public String number;
	
	/** The Constant EXISTED. */
	public static final long EXISTED = -2;
	
	/**
	 * New contructor.
	 *
	 * @param name the name
	 * @param number the number
	 */
	public ContactObj(String name, String number){
		this.id = -1;
		this.name = name;
		this.number = number;
	}
	
	/**
	 * Get from database.
	 *
	 * @param id the id
	 * @param name the name
	 * @param number the number
	 */
	public ContactObj(long id, String name, String number){
		this(name, number);
		this.id = id;
	}
}
