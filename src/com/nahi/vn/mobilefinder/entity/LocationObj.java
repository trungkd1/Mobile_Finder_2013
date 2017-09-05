package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationObj.
 */
public class LocationObj {
	
	/** The id. */
	private int id;
	
	/** The latitude. */
	private double latitude;
	
	/** The longitude. */
	private double longitude;
	
	/** The time. */
	private long time;
	
	/** The address. */
	private String address;
	
	/** The status. */
	private int status;
	
	/**
	 * Instantiates a new location obj.
	 *
	 * @param id the id
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param time the time
	 * @param address the address
	 * @param status the status
	 */
	public LocationObj(int id, double latitude, double longitude, long time,
			String address, int status) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
		this.address = address;
		this.status = status;
	}

	/**
	 * Instantiates a new location obj.
	 */
	public LocationObj(){
		
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
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 *
	 * @param time the new time
	 */
	public void setTime(long time) {
		this.time = time;
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
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
