package com.nahi.vn.mobilefinder.worker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.entity.ContactObj;
import com.nahi.vn.mobilefinder.entity.ContentSender;
import com.nahi.vn.mobilefinder.entity.ContentSender.ApiType;
import com.nahi.vn.mobilefinder.entity.ContentSender.SenderType;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class ContentSenderWorker.
 */
public class ContentSenderWorker extends AsyncTask<Void, ContentSender, Void>{
	
	/** The context. */
    private Context context;
	
	/** The pending sender list. */
    private List<ContentSender> pendingSenderList;
	
    /** The filter numbers. */
    private List<String> filterNumbers = new ArrayList<String>();
    
	/**
	 * Instantiates a new content sender worker.
	 *
	 * @param context the context
	 * @param pendingSenderList the pending sender list
	 */
	public ContentSenderWorker(Context context, List<ContentSender> pendingSenderList) {
		this.context = context;
		this.pendingSenderList = pendingSenderList;
	}

	/**
	 * Do in background.
	 *
	 * @param params the params
	 * @return the void
	 */
	@Override
	protected Void doInBackground(Void... params) {
		//Get filter numbers
		ArrayList<ContactObj> contacts = App.getDB().getAllContacts();
		if(contacts.size() > 0){
			for(ContactObj co : contacts){
				filterNumbers.add(co.number);
			}
		}
		//Start send content
		if(pendingSenderList != null && pendingSenderList.size() > 0){
			for(int i = 0; i < pendingSenderList.size(); i++){
				//Start a sender
				sendContent(i);
				//After a sender, pending 5s for the next sender
				try {
					Thread.sleep(Config.SENDER_PENDING_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(ContentSender... values) {
		super.onProgressUpdate(values);
		ContentSender sender = values[0];
		//If sender type is nahi message, make http request to nahi server
		if(!TextUtils.isEmpty(ProfileData.getInstance(context).getToken())){
			String token = ProfileData.getInstance(context).getToken();
			if(sender.getApi() == ApiType.GET_LOCATION){
				String latLng = sender.getContent();
				if(!TextUtils.isEmpty(latLng) && latLng.contains(Config.SPLIT_PATTERN)){
					ArrayList<String> latLngValues = AppUtil.splitToArrayList(latLng, Config.SPLIT_PATTERN);
					//Get lat and lng of location
					if(latLngValues.size() > 2){
						HttpClientHelper.getInstance().getLocation(token, latLngValues.get(0), latLngValues.get(1), latLngValues.get(2));	
					} else {
						HttpClientHelper.getInstance().getLocation(token, latLngValues.get(0), latLngValues.get(1), "");
					}
					//The sender is sent
					updateSender(sender);
				}
			} else if(sender.getApi() == ApiType.UPLOAD_PICTURE){
				ArrayList<String> fileList = AppUtil.splitToArrayList(sender.getFiles(), Config.SPLIT_PATTERN);
				if(fileList.size() > 0){
					//Upload image to server
					HttpClientHelper.getInstance().uploadPicture(token, fileList);
					//The sender is sent
					updateSender(sender);
				}
			} else {
				ArrayList<String> fileList = AppUtil.splitToArrayList(sender.getFiles(), Config.SPLIT_PATTERN);
				for(String filePath : fileList){
					switch (sender.getApi()) {
						case BACKUP_CONTACT_SERVER:
							//Backup contact to server
							HttpClientHelper.getInstance().backupContactServer(token, filePath);
							//The sender is sent
							updateSender(sender);
							break;
						case BACKUP_SMS_SERVER:
							//Backup sms to server
							HttpClientHelper.getInstance().backupSmsServer(token, filePath);
							//The sender is sent
							updateSender(sender);
							break;
						case BACKUP_CALL_LOG_SERVER:
							//Backup call log to server
							HttpClientHelper.getInstance().backupCallLogServer(token, filePath);
							//The sender is sent
							updateSender(sender);
							break;
						case TRACKING_DEVICE:
							//Tracking location
							HttpClientHelper.getInstance().trackingDevice(token, filePath);
							//The sender is sent
							updateSender(sender);
							break;
						default:
							break;
					}
				}
			}
		}
	}
	
	/**
	 * Send content.
	 *
	 * @param senderIndex the sender index
	 */
	private void sendContent(int senderIndex){
		ContentSender sender = pendingSenderList.get(senderIndex);
		switch (sender.getType()) {
			case SMS:
				//If sender type is sms, send sms message to the filter numbers
				if(filterNumbers.size() > 0){
					for(String number : filterNumbers){
						//Send sms message
						AppUtil.sendSmsMessage(number, sender.getContent());
					}
					//The sender is sent
					updateSender(sender);
				}
				break;
			case EMAIL:
				//If sender type is email, send email
				if(!TextUtils.isEmpty(ProfileData.getInstance(context).getEmail())){
					String toEmail = ProfileData.getInstance(context).getEmail();
					ArrayList<String> fileList = AppUtil.splitToArrayList(sender.getFiles(), Config.SPLIT_PATTERN); 
					//Send email with attach files(if has)
					AppUtil.sendEmailMessage(context, toEmail, sender.getContent(), fileList);
					//The sender is sent
					updateSender(sender);
				}
				break;
			case NAHI_MESSAGE: case AUTO_BACKUP:
				publishProgress(sender);
				break;								
			default:
				break;
		}
	}
	
	/**
	 * Update sender.
	 *
	 * @param sender the sender
	 * @return the content sender
	 */
	private ContentSender updateSender(ContentSender sender){
		if(sender != null && !sender.isSent()){
			sender.setSent(true);
			App.getDB().updateContentSender(sender);
		}
		return sender;
	}
	
	/**
	 * Creates the sms sender.
	 *
	 * @param content the content
	 * @return the content sender
	 */
	public static ContentSender createSmsSender(String content){
		ContentSender sms = new ContentSender();
		sms.setType(SenderType.SMS);
		sms.setContent(content);
		sms.setCreateTime(Calendar.getInstance().getTimeInMillis());
		return sms;
	}

	/**
	 * Creates the nahi request sender.
	 *
	 * @param content the content
	 * @param fileList the file list
	 * @param api the api
	 * @return the content sender
	 */
	public static ContentSender createNahiRequestSender(String content, List<String> fileList, ApiType api){
		ContentSender request = new ContentSender();
		request.setType(SenderType.NAHI_MESSAGE);
		request.setApi(api);
		request.setContent(content);
		request.setCreateTime(Calendar.getInstance().getTimeInMillis());
		request.setFiles(AppUtil.mergeArrToString(fileList, Config.SPLIT_PATTERN));
		return request;
	}
	
	/**
	 * Creates the auto backup request sender.
	 *
	 * @param content the content
	 * @param fileList the file list
	 * @param api the api
	 * @return the content sender
	 */
	public static ContentSender createAutoBackupRequestSender(String content, List<String> fileList, ApiType api){
		ContentSender request = new ContentSender();
		request.setType(SenderType.AUTO_BACKUP);
		request.setApi(api);
		request.setContent(content);
		request.setCreateTime(Calendar.getInstance().getTimeInMillis());
		request.setFiles(AppUtil.mergeArrToString(fileList, Config.SPLIT_PATTERN));
		return request;
	}
	
	/**
	 * Insert content sender.
	 *
	 * @param senders the senders
	 */
	public static void insertContentSenders(ContentSender...senders){
		if(senders.length > 0){
			for(ContentSender sender : senders){
				App.getDB().insertContentSender(sender);
			}
		}
	}
	
	/**
	 * Insert content senders.
	 *
	 * @param senders the senders
	 */
	public static void insertContentSenders(List<ContentSender> senders){
		if(senders.size() > 0){
			for(ContentSender sender : senders){
				App.getDB().insertContentSender(sender);
			}
		}
	}
}
