package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactResult.
 */
public class ContactResult {

	/** The id. */
	private String id;
	
	/** The name. */
	private String name;
	
	/** The title. */
	private String title;
	
	/** The phone. */
	private String phone;
	
	/** The email. */
	private String email;
	
	/** The company. */
	private String company;
	
	/** The street. */
	private String street;
	
	/** The state. */
	private String state;
	
	/** The city. */
	private String city;
	
	/** The country. */
	private String country;
	
	/** The zipcode. */
	private String zipcode;
	
	/** The neighborhood. */
	private String neighborhood;
	
	/** The pobox. */
	private String pobox;
	
	/** The is_call. */
	private int isCall;
	
	/** The is_voice. */
	private int isVoice;
	
	/** The is_video. */
	private int isVideo;
	
	/** The status. */
	private int status;
	
	/** The group. */
	private int group;
	
	/** The receiver_id. */
	private int receiverId;

	/**
	 * Instantiates a new contact result.
	 */
	public ContactResult() {
	}

	/**
	 * Instantiates a new contact result.
	 *
	 * @param id the id
	 * @param name the name
	 * @param title the title
	 * @param phone the phone
	 * @param email the email
	 * @param company the company
	 * @param street the street
	 * @param state the state
	 * @param city the city
	 * @param country the country
	 * @param zipcode the zipcode
	 * @param neighborhood the neighborhood
	 * @param pobox the pobox
	 * @param isCall the is call
	 * @param isVoice the is voice
	 * @param isVideo the is video
	 * @param status the status
	 * @param group the group
	 * @param receiverId the receiver id
	 */
	public ContactResult(String id, String name, String title, String phone,
			String email, String company, String street, String state,
			String city, String country, String zipcode, String neighborhood,
			String pobox, int isCall, int isVoice, int isVideo, int status,
			int group, int receiverId) {
		this.id = id;
		this.name = name;
		this.title = title;
		this.phone = phone;
		this.email = email;
		this.company = company;
		this.street = street;
		this.state = state;
		this.city = city;
		this.country = country;
		this.zipcode = zipcode;
		this.neighborhood = neighborhood;
		this.pobox = pobox;
		this.isCall = isCall;
		this.isVoice = isVoice;
		this.isVideo = isVideo;
		this.status = status;
		this.group = group;
		this.receiverId = receiverId;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the phone.
	 *
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone.
	 *
	 * @param phone the new phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the company.
	 *
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * Sets the company.
	 *
	 * @param company the new company
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * Gets the street.
	 *
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Sets the street.
	 *
	 * @param street the new street
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country the new country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Gets the zipcode.
	 *
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * Sets the zipcode.
	 *
	 * @param zipcode the new zipcode
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * Gets the neighborhood.
	 *
	 * @return the neighborhood
	 */
	public String getNeighborhood() {
		return neighborhood;
	}

	/**
	 * Sets the neighborhood.
	 *
	 * @param neighborhood the new neighborhood
	 */
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	/**
	 * Gets the pobox.
	 *
	 * @return the pobox
	 */
	public String getPobox() {
		return pobox;
	}

	/**
	 * Sets the pobox.
	 *
	 * @param pobox the new pobox
	 */
	public void setPobox(String pobox) {
		this.pobox = pobox;
	}

	/**
	 * Gets the checks if is call.
	 *
	 * @return the checks if is call
	 */
	public int getIsCall() {
		return isCall;
	}

	/**
	 * Sets the checks if is call.
	 *
	 * @param isCall the new checks if is call
	 */
	public void setIsCall(int isCall) {
		this.isCall = isCall;
	}

	/**
	 * Gets the checks if is voice.
	 *
	 * @return the checks if is voice
	 */
	public int getIsVoice() {
		return isVoice;
	}

	/**
	 * Sets the checks if is voice.
	 *
	 * @param isVoice the new checks if is voice
	 */
	public void setIsVoice(int isVoice) {
		this.isVoice = isVoice;
	}

	/**
	 * Gets the checks if is video.
	 *
	 * @return the checks if is video
	 */
	public int getIsVideo() {
		return isVideo;
	}

	/**
	 * Sets the checks if is video.
	 *
	 * @param isVideo the new checks if is video
	 */
	public void setIsVideo(int isVideo) {
		this.isVideo = isVideo;
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
	 * Gets the group.
	 *
	 * @return the group
	 */
	public int getGroup() {
		return group;
	}

	/**
	 * Sets the group.
	 *
	 * @param group the new group
	 */
	public void setGroup(int group) {
		this.group = group;
	}

	/**
	 * Gets the receiver id.
	 *
	 * @return the receiver id
	 */
	public int getReceiverId() {
		return receiverId;
	}

	/**
	 * Sets the receiver id.
	 *
	 * @param receiverId the new receiver id
	 */
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

}

