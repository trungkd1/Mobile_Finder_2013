package com.nahi.vn.mobilefinder.util;

import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;

// TODO: Auto-generated Javadoc
/**
 * The Class Config.
 */
public class Config {
	
	
	/** The Constant URL_NAHI_PREMIUM. */
	public static final String URL_NAHI_PREMIUM = "https://market.nahi.vn/shopping/payment/premium";
	
	/** The Constant URL_NAHI_SERVER. */
	public static final String URL_NAHI_SERVER_OAUTHENTICATION = "https://id.nahi.vn/";
	
	/** The Constant URL_NAHI_SERVER_KIDS_MOBILE. */
	public static final String URL_NAHI_SERVER_MOBILE_FINDER = "https://cp.nahi.vn/";
	
    /** The Constant NAHI_API_KEY. */
    public static final String NAHI_API_KEY = "$(^8d1vh7s7ie&s7vta+#f9xl_*!wjdbo84n3hs+qe*z-422g3tgi2@)";
    
    /** The Constant NAHI_CONTACT_TYPE. */
    public static final String NAHI_CONTACT_TYPE = "CONTACT";
    
    /** The Constant NAHI_SMS_TYPE. */
    public static final String NAHI_SMS_TYPE = "SMS";
    
    /** The Constant NAHI_CALL_LOG_TYPE. */
    public static final String NAHI_CALL_LOG_TYPE = "CALLLOG";
    
	/*------------------------------------------- OAUTHENTICATION APIs -------------------------------------------------------------*/
	/** The Constant URL_MOBILE_LOGIN. */
	public static final String URL_MOBILE_LOGIN = URL_NAHI_SERVER_OAUTHENTICATION + "mobile-login.html"; 
	
	/** The Constant URL_MOBILE_PROFILE. */
	public static final String URL_MOBILE_PROFILE = URL_NAHI_SERVER_OAUTHENTICATION + "mobile-profile.html"; 
	
	/** The Constant URL_REGISTER. */
	public static final String URL_REGISTER = URL_NAHI_SERVER_OAUTHENTICATION + "api-register.html";
	
    /** The Constant URL_REGISTER_ACCOUNT. */
    public static final String URL_REGISTER_ACCOUNT = URL_NAHI_SERVER_OAUTHENTICATION + "register.html"; // not for API

    /** The Constant URL_FORGET_PASSWORD. */
    public static final String URL_FORGET_PASSWORD = URL_NAHI_SERVER_OAUTHENTICATION + "forgetpassword.html"; // not for API
    
    /** The Constant URL_REQUEST_SESSION_KEY. */
    public static final String URL_REQUEST_SESSION_KEY = URL_NAHI_SERVER_OAUTHENTICATION + "api-request-session-key.html";
    
    /*----------------------------------------------FINDER BUSINESS APIs----------------------------------------------------------*/
    
    /** The Constant URL_BACKUP_CONTACT_SERVER. */
    public static final String URL_BACKUP_CONTACT_SERVER = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_backup_contacts_server.html";
    
    /** The Constant URL_BACKUP_SMS_SERVER. */
    public static final String URL_BACKUP_SMS_SERVER = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_backup_sms_server.html";
    
    /** The Constant URL_BACKUP_CALL_LOG_SERVER. */
    public static final String URL_BACKUP_CALL_LOG_SERVER = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_backup_calllog_server.html";
    
    /** The Constant URL_RESTORE_CONTACT_MOBILE. */
    public static final String URL_RESTORE_CONTACT_MOBILE = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_restore_contacts_mobile.html";
    
    /** The Constant URL_RESTORE_SMS_MOBILE. */
    public static final String URL_RESTORE_SMS_MOBILE = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_restore_sms_mobile.html";
    
    /** The Constant URL_RESTORE_CALL_LOG_MOBILE. */
    public static final String URL_RESTORE_CALL_LOG_MOBILE = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_restore_calllog_mobile.html";
    
    /** The Constant URL_UPLOAD_PICTURE. */
    public static final String URL_UPLOAD_PICTURE = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_upload_picture.html";
    
    /** The Constant URL_UPLOAD_TRACKING. */
    public static final String URL_UPLOAD_TRACKING = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_upload_tracking.html";
    
    /** The Constant URL_UPLOAD_LOCATION. */
    public static final String URL_UPLOAD_LOCATION = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_upload_location.html";
    
    /** The Constant URL_SYNC_SETTING_MOBILE. */
    public static final String URL_SYNC_SETTING_MOBILE = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_sync_setting_mobile.html";
    
    /** The Constant URL_SYNC_SETTING_SERVER. */
    public static final String URL_SYNC_SETTING_SERVER = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_sync_setting_server.html";

    /** The Constant URL_REGISTER_GCM. */
    public static final String URL_REGISTER_GCM = URL_NAHI_SERVER_MOBILE_FINDER + "service/register-gcm.html";
    
    /** The Constant URL_CHECK_VALID_ACCOUNT. */
    public static final String URL_CHECK_VALID_ACCOUNT = URL_NAHI_SERVER_MOBILE_FINDER + "service/check_validate_account.html";
    
    /** The Constant URL_UPDATE_STATUS_COMMAND. */
    public static final String URL_UPDATE_STATUS_COMMAND = URL_NAHI_SERVER_MOBILE_FINDER + "service/finder_update_status_command.html";
    
    /** The Constant URL_UPDATE_DEVICE_NAME. */
    public static final String URL_UPDATE_DEVICE_NAME = URL_NAHI_SERVER_MOBILE_FINDER + "service/update_device_name.html";
    
    /*--------------------------------------------------- NAHI GCM ---------------------------------------------------------------------------*/
    
    /** The Constant NAHI_GCM_COMMAND_CODE. */
    public static final String NAHI_GCM_COMMAND_CODE = "code";
    
    /** The Constant NAHI_GCM_COMMAND_MESSAGE. */
    public static final String NAHI_GCM_COMMAND_MESSAGE = "msg";
    
    /** The Constant NAHI_GCM_COMMAND_ID. */
    public static final String NAHI_GCM_COMMAND_ID = "cmd_id";
    
    /** The Constant NAHI_GCM_COMMAND_SENDER. */
    public static final String NAHI_GCM_COMMAND_SENDER = "sender";
    
	/*--------------------------------------------------- NAHI EMAIL ---------------------------------------------------------------------------*/
    
    /** The Constant NAHI_SENDER_EMAIL. */
    public static final String NAHI_SENDER_EMAIL = "managesystem.arisvn@gmail.com";
    
    /** The Constant NAHI_SENDER_EMAIL_PASSWORD. */
    public static final String NAHI_SENDER_EMAIL_PASSWORD = "arisvn1369";
    
    /*--------------------------------------------------- Parameter for API ---------------------------------------------------------------------------*/

    /** The Constant API_KEY. */
    public static final String API_KEY = "api_key";
    
    /** The Constant SESSION_KEY. */
    public static final String SESSION_KEY = "session_key";
    
    /** The Constant EMAIL. */
    public static final String EMAIL = "email";
    
    /** The Constant PASSWORD. */
    public static final String PASSWORD = "password";
    
    /** The Constant PACKAGE. */
    public static final String PACKAGE = "package";
    
    /** The Constant DEVICE_ID. */
    public static final String DEVICE_ID = "deviceid";
    
    /*--------------------------------------------------- Action ---------------------------------------------------------------------------*/
    
    /** The Constant aCTION_CONTACT. */
	public final static int ACTION_CONTACT = 21;

	/** The Constant ACTION_MAIN. */
	public final static String ACTION_MAIN = "ACTION_MAIN";
	
	/** The Constant ACTION_DEVICE_ADMIN. */
	public static final int ACTION_DEVICE_ADMIN = 50;
	
	/* ========== App Internal Directory =========== */
    
	/** The Constant DATA_DIR. */
	public static final String DATA_DIR = "data";
    
	/** The Constant BACKUP_DIR. */
	public static final String BACKUP_DIR = "backup";
	
	/** The Constant BACKUP_CONTACT_FILE. */
	public static final String BACKUP_CONTACT_FILE = "contact.json";
	
	/** The Constant BACKUP_CALL_LOG_FILE. */
	public static final String BACKUP_CALL_LOG_FILE = "call.json";
	
	/** The Constant BACKUP_SMS_FILE. */
	public static final String BACKUP_SMS_FILE = "sms.json";
	
	/** The Constant LOCATION_FILE. */
	public static final String LOCATION_FILE = "location.json";
	
	/** The Constant MAX_GET_ADDRESS_RESULT. */
	public final static int MAX_GET_ADDRESS_RESULT = 5;

	/** The Constant MAX_GET_ADDRESS_TRY_TIMES. */
	public final static int MAX_GET_ADDRESS_TRY_TIMES = 5;
	
	/** The Constant MAX_LOCATION_RESULT. */
	public final static int MAX_LOCATION_RESULT = 6;
	
	/* ==========Fragment =========== */
	
	/** The fragment main option. */
	public static String FRAGMENT_MAIN_OPTION = "FRAGMENT_MAIN_OPTION";
	
	/** The fragment config. */
	public static String FRAGMENT_CONFIG = "FRAGMENT_PHOTO";
	
	/** The fragment protect. */
	public static String FRAGMENT_PROTECT = "FRAGMENT_PROTECT";
	
	/** The fragment auto protect. */
	public static String FRAGMENT_AUTO_PROTECT = "FRAGMENT_AUTO_PROTECT";
	
	/** The fragment backup and restore. */
	public static String FRAGMENT_BACKUP_RESTORE = "FRAGMENT_BACKUP_RESTORE";
	
	/** The fragment control by sim. */
	public static String FRAGMENT_CRL_BY_SIM = "FRAGMENT_CRL_BY_SIM";
	
	/** The fragment control from NAHI. */
	public static String FRAGMENT_CRL_FROM_NAHI = "FRAGMENT_CRL_FROM_NAHI";
	
	/** The fragment main. */
	public static String FRAGMENT_MAIN = "FRAGMENT_MAIN";
	
	/** The fragment login. */
	public static String FRAGMENT_LOGIN = "FRAGMENT_LOGIN";

	/** The Constant REQUEST_CODE_UNINSTALL_FREE_APP. */
	public final static int REQUEST_CODE_UNINSTALL_FREE_APP = 1;
	
	/** The Constant TIME_SPLASH_SCREEN. */
	public final static int TIME_SPLASH_SCREEN = 2000;
	
	/** The Constant TIME_TRACKING. */
	public final static int TIME_TRACKING = 1000 * 60 * 10;  
	
	/** The Constant TIME_TRACKING_SEND_SERVER. */
	public final static int TIME_TRACKING_SEND_SERVER = 1000 * 60 * 60;  
	
	/** The Constant TIME_START_NETWORK. */
	public final static int TIME_START_NETWORK = 2000;
	
	/** The Constant STATUS_IN_PROGRESS. */
	public final static int STATUS_IN_PROGRESS = 0;
	
	/** The Constant STATUS_DONE. */
	public final static int STATUS_DONE = 1;
	
	/** The Constant URL_APP. */
	public final static String URL_APP = "https://play.google.com/store/apps/details?id=";
	
	/** The Constant URL_POST_JSON. */
	public final static String URL_POST_JSON = "";
	
	/** The Constant APP_NAME. */
	public final static String APP_NAME = "MOBILE_FINDER";
	
	
	/* ==========Contact Parameter =========== */
	
	/** The Constant DATA. */
	public final static String DATA = "data";
	
	/** The Constant ADDRESS. */
	public final static String SMS_ADDRESS = "address";
	
	/** The Constant PERSON. */
	public final static String SMS_PERSON = "person";
	
	/** The Constant BODY. */
	public final static String SMS_BODY = "body";
	
	/** The Constant SMS_READ. */
	public static final String SMS_READ = "read";
	
	/** The Constant DATE. */
	public final static String DATE = Calls.DATE;
	
	/** The Constant NAME. */
	public final static String CALL_LOG_NAME = Calls.CACHED_NAME;
	
	/** The Constant NUMBER. */
	public final static String CALL_LOG_NUMBER = Calls.NUMBER;
	
	/** The Constant TYPE. */
	public final static String TYPE =  Calls.TYPE;
	
	/** The Constant _ID. */
	public final static String _ID =  Calls._ID;
	
	/** The Constant CONTACT_NAME. */
	public final static String CONTACT_NAME = "name";
	
	/** The Constant CONTACT_NUMBER. */
	public final static String CONTACT_NUMBER = "number";
	
	/** The Constant CONTACT_TYPE. */
	public final static String CONTACT_TYPE = "type";
	
	/** The Constant DURATION. */
	public final static String CALL_LOG_DURATION = "duration";
 	
	/** The Constant TAB_PICK_CONTACT. */
	public static final String TAB_PICK_CONTACT = "TAB_PICK_CONTACT";
	
	/** The Constant TAB_PICK_CALL_LOG. */
	public static final String TAB_PICK_CALL_LOG = "TAB_PICK_CALL_LOG";
	
	/** The Constant TAB_PICK_MESSAGE. */
	public static final String TAB_PICK_MESSAGE = "TAB_PICK_MESSAGE";
	
	
	/** The Constant PHONE_CONTACT. */
	public static final String[] PHONE_CONTACT = new String[] { Phone._ID,
		Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE
	};

	/** The Constant CALL_LOGS. */
	public static final String[] CALL_LOGS = new String[] { _ID,
		CALL_LOG_NAME, CALL_LOG_NUMBER, CALL_LOG_DURATION, TYPE, DATE };

	/** The Constant SMS. */
	public static final String[] SMS = new String[] { _ID, SMS_PERSON,
		SMS_ADDRESS, SMS_BODY, TYPE, DATE, SMS_READ};

    /** The Constant SITE_MOBILEKID. */
    public static final String SITE_MOBILE_FINDER = "FINDER";
	
    /** The Constant SPLIT_PATTERN. */
    public static final String SPLIT_PATTERN = ";";
    
    /** The Constant SPLIT_PATTERN_COMMA. */
    public static final String SPLIT_PATTERN_COMMA = ",";
    
    /** The Constant WHITE_SPACE_PATTERN. */
    public static final String WHITE_SPACE_PATTERN = " ";
    
    /** The Constant SENDER_PENDING_TIME. */
    public final static int SENDER_PENDING_TIME = 5000;
    
    /** The Constant INTENT_RECEIVE_CONTENT_SENDER. */
    public final static String INTENT_RECEIVE_CONTENT_SENDER = "com.nahi.vn.mobilefinder.RECEIVE_CONTENT_SENDER";
    
    /** The Constant INTENT_RECEIVE_AUTO_BACKUP_SENDER. */
    public final static String INTENT_RECEIVE_AUTO_BACKUP_SENDER = "com.nahi.vn.mobilefinder.RECEIVE_AUTO_BACKUP_SENDER";
    
    /** The Constant DEFAULT_ALLOW_FAIL_UNLOCK_SCREEN_TIMES. */
    public final static int DEFAULT_ALLOW_FAIL_UNLOCK_SCREEN_TIMES = 3;
    
    /** The Constant INTENT_RECEIVE_LOCATION. */
    public final static String INTENT_RECEIVE_LOCATION = "INTENT_RECEIVE_LOCATION";
    
    /** The Constant INTENT_SEND_TRACKING_LOCATION. */
    public final static String INTENT_SEND_TRACKING_LOCATION = "INTENT_SEND_TRACKING_LOCATION";
    
    /** The Constant INTENT_RECEIVE_LOCK_TRIGGER. */
    public final static String INTENT_RECEIVE_LOCK_TRIGGER = "com.nahi.vn.mobilefinder.INTENT_RECEIVE_LOCK_TRIGGER";
    
    /** The Constant INTENT_SCHEDULE_AUTO_BACKUP. */
    public final static String INTENT_SCHEDULE_AUTO_BACKUP = "com.nahi.vn.mobilefinder.INTENT_SCHEDULE_AUTO_BACKUP";
    
	/** The Constant MAX_TRY_LOGIN_FAIL. */
	public final static int MAX_TRY_LOGIN_FAIL = 5;
	
	/** The Constant LOCK_ACTIVITY_NAME. */
	public final static String LOCK_ACTIVITY_NAME = "com.nahi.vn.mobilefinder.activity.LockScreenActivity";
	
	/** The Constant NOTIFY_ACTIVITY_NAME. */
	public final static String NOTIFY_ACTIVITY_NAME = "com.nahi.vn.mobilefinder.activity.NotifyActivity";
	
	/** The Constant CAPTURE_ACTIVITY_NAME. */
	public final static String CAPTURE_ACTIVITY_NAME = "com.nahi.vn.mobilefinder.activity.CaptureActivity";
	
	/** The Constant NOTIFY_PASS_LOCK_ACTIVITY_NAME. */
	public final static String NOTIFY_PASS_LOCK_ACTIVITY_NAME = "com.nahi.vn.mobilefinder.activity.NotifyPassLockActivity";
	
	/** The Constant CAPTURE_PASS_LOCK_ACTIVITY_NAME. */
	public final static String CAPTURE_PASS_LOCK_ACTIVITY_NAME = "com.nahi.vn.mobilefinder.activity.CapturePassLockActivity";
	
	/** The Constant NAHI_KIDS_APP_PACKAGE_NAME. */
	public final static String NAHI_KIDS_APP_PACKAGE_NAME = "nahi.kids";
	
	/** The Constant NAHI_KIDS_MAIN_ACTIVITY_NAME. */
	public final static String NAHI_KIDS_MAIN_ACTIVITY_NAME = "nahikid.main.KidMain";
	
	/** The Constant SKYPE_PACKAGE_NAME. */
	public final static String SKYPE_PACKAGE_NAME = "com.skype.raider";
	
	/** The Constant TRIGGER_ACTIONS. */
	public final static String TRIGGER_ACTIONS = "00000";
	
	/** The Constant LOCATION_DECIMAL_FORMAT. */
	public final static String LOCATION_DECIMAL_FORMAT = "0.000000";

	/** The Constant URL_CHECK_FINDER_PREMIUM. */
	public static final String URL_CHECK_FINDER_PREMIUM = URL_NAHI_SERVER_MOBILE_FINDER + "service/check_finder_premium.html";
}
