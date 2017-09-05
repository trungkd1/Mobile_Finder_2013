package com.nahi.vn.mobilefinder.entity;


// TODO: Auto-generated Javadoc
/**
 * The Class SMSEntry.
 */
public class SMSEntry {


	/**
	 * Gets the m name.
	 *
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}

	/**
	 * Sets the m name.
	 *
	 * @param mName the mName to set
	 */
	public void setmName(String mName) {
		this.mName = mName;
	}

	/**
	 * Gets the m number.
	 *
	 * @return the mNumber
	 */
	public String getmNumber() {
		return mNumber;
	}

	/**
	 * Sets the m number.
	 *
	 * @param mNumber the mNumber to set
	 */
	public void setmNumber(String mNumber) {
		this.mNumber = mNumber;
	}

	/**
	 * Gets the m body.
	 *
	 * @return the mBody
	 */
	public String getmBody() {
		return mBody;
	}

	/**
	 * Sets the m body.
	 *
	 * @param mBody the mBody to set
	 */
	public void setmBody(String mBody) {
		this.mBody = mBody;
	}

	/**
	 * Gets the m type.
	 *
	 * @return the mType
	 */
	public String getmType() {
		return mType;
	}

	/**
	 * Sets the m type.
	 *
	 * @param mType the mType to set
	 */
	public void setmType(String mType) {
		this.mType = mType;
	}

	/**
	 * Gets the m date.
	 *
	 * @return the mDate
	 */
	public String getmDate() {
		return mDate;
	}

	/**
	 * Sets the m date.
	 *
	 * @param mDate the mDate to set
	 */
	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

	/** The m name. */
	public String mName;
	
	/** The m number. */
	public String mNumber;
	
	/** The m body. */
	public String mBody;
	
	/** The m type. */
	public String mType;
	
	/** The m date. */
	public String mDate;

	/** The m read. */
	public String mRead;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SMSEntry other = (SMSEntry) obj;
		if (mNumber == null) {
			if (other.mNumber != null)
				return false;
		} else if (!mNumber.equals(other.mNumber))
			return false;
		return true;
	}
}
