package com.nahi.vn.mobilefinder.entity;

import java.util.List;

import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;

// TODO: Auto-generated Javadoc
/**
 * The Class SyncSettingResult.
 */
public class SyncSettingResult {

	/** The reg phone. */
	private List<String> regPhone;
	
	/** The login user. */
	private String loginUser;
	
	/** The allow nahi remote. */
	private boolean allowNahiRemote;
	
	/**
	 * Instantiates a new sync setting result.
	 */
	public SyncSettingResult() {
	}

	/**
	 * Instantiates a new sync setting result.
	 *
	 * @param regPhone the reg phone
	 * @param loginUser the login user
	 * @param allowNahiRemote the allow nahi remote
	 */
	public SyncSettingResult(String regPhone, String loginUser,
			boolean allowNahiRemote) {
		this.loginUser = loginUser;
		this.allowNahiRemote = allowNahiRemote;
		this.regPhone = AppUtil.splitToArrayList(regPhone, Config.SPLIT_PATTERN);
	}

	/**
	 * Checks if is allow nahi remote.
	 *
	 * @return true, if is allow nahi remote
	 */
	public boolean isAllowNahiRemote() {
		return allowNahiRemote;
	}

	/**
	 * Sets the allow nahi remote.
	 *
	 * @param allowNahiRemote the new allow nahi remote
	 */
	public void setAllowNahiRemote(boolean allowNahiRemote) {
		this.allowNahiRemote = allowNahiRemote;
	}

	/**
	 * Gets the reg phone.
	 *
	 * @return the reg phone
	 */
	public List<String> getRegPhone() {
		return regPhone;
	}

	/**
	 * Sets the reg phone.
	 *
	 * @param regPhone the new reg phone
	 */
	public void setRegPhone(String regPhone) {
		this.regPhone = AppUtil.splitToArrayList(regPhone, Config.SPLIT_PATTERN_COMMA);
	}

	/**
	 * Gets the login user.
	 *
	 * @return the login user
	 */
	public String getLoginUser() {
		return loginUser;
	}

	/**
	 * Sets the login user.
	 *
	 * @param loginUser the new login user
	 */
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	/**
	 * Sets the reg phone.
	 *
	 * @param regPhone the new reg phone
	 */
	public void setRegPhone(List<String> regPhone) {
		this.regPhone = regPhone;
	}
	
}
