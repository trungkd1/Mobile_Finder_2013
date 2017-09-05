package com.nahi.vn.mobilefinder.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class ContentSender.
 */
public class ContentSender {

	/** The id. */
	private int id;
	
	/** The content. */
	private String content;
	
	/** The files. */
	private String files;
	
	/** The type. */
	private SenderType type;
	
	/** The is sent. */
	private boolean isSent;
	
	/** The api. */
	private ApiType api;
	
	/** The create time. */
	private long createTime;
	
	/**
	 * The Enum SenderType.
	 */
	public enum SenderType{
		
		/** The sms. */
		SMS(0),
		
		/** The email. */
		EMAIL(1),
		
		/** The nahi message. */
		NAHI_MESSAGE(2),
		
		/** The auto backup. */
		AUTO_BACKUP(3);
		
		/** The value. */
		private int value;  
		 
	    /**
    	 * Instantiates a new sender type.
    	 *
    	 * @param value the value
    	 */
    	private SenderType(int value) {  
	        this.value = value;  
	    }  
	  
	    /**
    	 * From value.
    	 *
    	 * @param value the value
    	 * @return the sender type
    	 */
    	public static SenderType fromValue(int value) {  
	        for (SenderType t: SenderType.values()) {  
	            if (t.value == value) {  
	                return t;  
	            }  
	        }  
	        return null;  
	    }  
    	
    	/**
	     * Merge value to string.
	     *
	     * @param types the types
	     * @return the string
	     */
	    public static String mergeValueToString(SenderType...types){
    		String text = "";
    		if(types.length > 0){
    			for(int i = 0; i < types.length; i++){
    				text += types[i].value();
    				if(types.length > 1 && i < types.length - 1){
    					text += ",";
    				}
    			}
    		}
    		return text;
    	}
	  
	    /**
    	 * Value.
    	 *
    	 * @return the int
    	 */
    	public int value() {  
	        return value;  
	    }  
	}
	
	/**
	 * The Enum ApiType.
	 */
	public enum ApiType{
		
		/** The none. */
		NONE(0),
		
		/** The backup contact server. */
		BACKUP_CONTACT_SERVER(1),
		
		/** The backup sms server. */
		BACKUP_SMS_SERVER(2),
		
		/** The backup call log server. */
		BACKUP_CALL_LOG_SERVER(3),
		
		/** The upload picture. */
		UPLOAD_PICTURE(4),
		
		/** The tracking device. */
		TRACKING_DEVICE(5),
		
		/** The get location. */
		GET_LOCATION(6);
		
		/** The value. */
		private int value;  
		 
    	/**
	     * Instantiates a new api type.
	     *
	     * @param value the value
	     */
	    private ApiType(int value) {  
	        this.value = value;  
	    }  
	  
    	/**
	     * From value.
	     *
	     * @param value the value
	     * @return the api type
	     */
	    public static ApiType fromValue(int value) {  
	        for (ApiType t: ApiType.values()) {  
	            if (t.value == value) {  
	                return t;  
	            }  
	        }  
	        return null;  
	    }  
	  
	    /**
    	 * Value.
    	 *
    	 * @return the int
    	 */
    	public int value() {  
	        return value;  
	    }  
	}

	/**
	 * Instantiates a new content sender.
	 */
	public ContentSender() {
	}
	
	/**
	 * Instantiates a new content sender.
	 *
	 * @param id the id
	 * @param content the content
	 * @param files the files
	 * @param type the type
	 * @param isSent the is sent
	 * @param api the api
	 * @param createTime the create time
	 */
	public ContentSender(int id, String content, String files, SenderType type,
			boolean isSent, ApiType api, long createTime) {
		this.id = id;
		this.content = content;
		this.files = files;
		this.type = type;
		this.isSent = isSent;
		this.api = api;
		this.createTime = createTime;
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
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public SenderType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(SenderType type) {
		this.type = type;
	}

	/**
	 * Gets the creates the time.
	 *
	 * @return the creates the time
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the creates the time.
	 *
	 * @param createTime the new creates the time
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * Checks if is sent.
	 *
	 * @return true, if is sent
	 */
	public boolean isSent() {
		return isSent;
	}

	/**
	 * Sets the sent.
	 *
	 * @param isSent the new sent
	 */
	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}

	/**
	 * Gets the files.
	 *
	 * @return the files
	 */
	public String getFiles() {
		return files;
	}

	/**
	 * Sets the files.
	 *
	 * @param files the new files
	 */
	public void setFiles(String files) {
		this.files = files;
	}

	/**
	 * Gets the api.
	 *
	 * @return the api
	 */
	public ApiType getApi() {
		if(api != null){
			return api;	
		}
		return ApiType.NONE;
	}

	/**
	 * Sets the api.
	 *
	 * @param api the new api
	 */
	public void setApi(ApiType api) {
		this.api = api;
	}
	
}
