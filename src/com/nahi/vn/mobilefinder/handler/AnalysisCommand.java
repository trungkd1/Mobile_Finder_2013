package com.nahi.vn.mobilefinder.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class AnalysisCommand.
 */
public class AnalysisCommand {

	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new analysis command.
	 *
	 * @param context the context
	 */
	public AnalysisCommand(Context context) {
		this.context = context;
	}
	
	/**
	 * The Enum Action.
	 */
	public enum Action{
		
		/** The none. */
		NONE,
		
		/** The alert. */
		ALERT,
		
		/** The get location. */
		GET_LOCATION,
		
		/** The capture image. */
		CAPTURE_IMAGE,
		
		/** The lock device. */
		LOCK_DEVICE,
		
		/** The display message. */
		DISPLAY_MESSAGE,
		
		/** The display icon. */
		DISPLAY_ICON,
		
		/** The backup. */
		BACKUP,
		
		/** The wipe data. */
		WIPE_DATA,
		
		/** The sync setting. */
		SYNC_SETTING
	}
	
	/**
	 * The Enum CommandType.
	 */
	public enum CommandType{
		
		/** The sms. */
		SMS,
		
		/** The gcm. */
		GCM
		
	}
	
	/**
	 * Analysis command.
	 *
	 * @param command the command
	 * @return the action
	 */
	private Action analysisSmsCommand(String command){
		String[] syntaxes = context.getResources().getStringArray(
				R.array.sms_control_device_syntaxes);
		if (command.equalsIgnoreCase(syntaxes[0])) { 
			// Start alert sound
			return Action.ALERT;
		} else if (command.equalsIgnoreCase(syntaxes[1])) { 
			// Get current location
			return Action.GET_LOCATION;
		} else if (command.equalsIgnoreCase(syntaxes[2])) { 
			// Capture image
			return Action.CAPTURE_IMAGE;
		} else if (command.equalsIgnoreCase(syntaxes[3])) { 
			// Lock device
			return Action.LOCK_DEVICE;
		} else if (command.equalsIgnoreCase(syntaxes[4])) { 
			// Display message
			return Action.DISPLAY_MESSAGE;
		} else if (command.equalsIgnoreCase(syntaxes[5])) { 
			// Display icon
			return Action.DISPLAY_ICON;
		} else if (command.equalsIgnoreCase(syntaxes[6])) { 
			// Backup contact, sms & call log
			return Action.BACKUP;
		} else if (command.equalsIgnoreCase(syntaxes[7])) { 
			// Wipe data
			return Action.WIPE_DATA;
		} 
		return Action.NONE;
	}
	
	/**
	 * Analysis gcm command.
	 *
	 * @param command the command
	 * @return the action
	 */
	private Action analysisGcmCommand(String command){
		String[] syntaxes = context.getResources().getStringArray(
				R.array.gcm_control_device_syntaxes);
		if (command.equalsIgnoreCase(syntaxes[0])) { 
			// Lock device
			return Action.LOCK_DEVICE;
		} else if (command.equalsIgnoreCase(syntaxes[1])) { 
			// Start alert sound
			return Action.ALERT;
		} else if (command.equalsIgnoreCase(syntaxes[2])) { 
			// Wipe data
			return Action.WIPE_DATA;
		} else if (command.equalsIgnoreCase(syntaxes[3])) { 
			// Backup contact, sms & call log
			return Action.BACKUP;
		} else if (command.equalsIgnoreCase(syntaxes[4])) { 
			// Get current location
			return Action.GET_LOCATION;
		} else if (command.equalsIgnoreCase(syntaxes[5])) { 
			// Display message
			return Action.DISPLAY_MESSAGE;
		} else if (command.equalsIgnoreCase(syntaxes[6])) { 
			// Sync setting
			return Action.SYNC_SETTING;
		} else {
			return Action.NONE;
		}
	}
	
	/**
	 * Gets the action class.
	 *
	 * @param action the action
	 * @return the action class
	 */
	private Class<?> getActionClass(Action action){
		Class<?> c = null;
		switch (action) {
			case ALERT: 
				c = AlertSound.class;
				break;
			case GET_LOCATION:  
				c = LocateDevice.class;
				break;
			case CAPTURE_IMAGE:
				c = CaptureImage.class;
				break;
			case LOCK_DEVICE:
				c = LockDevice.class;
				break;
			case DISPLAY_MESSAGE:
				c = DisplayMessage.class;
				break;
			case DISPLAY_ICON:
				c = DisplayIcon.class;
				break;
			case BACKUP:
				c = BackupData.class;
				break;
			case WIPE_DATA:
				c = WipeData.class;
				break;
			case SYNC_SETTING:
				c = SyncSetting.class;
				break;
			default:
				break;
		}
		return c;
	}
	
	/**
	 * Do action.
	 *
	 * @param action the action
	 * @param message the message
	 * @return true, if successful
	 */
	private boolean doAction(Action action, String message){
		Object handler = null;
		try {
			Class<?> c = getActionClass(action);
			if(c != null){
				handler = c.getConstructor(Context.class).newInstance(context);
				switch (action) {
					case ALERT:
						((AlertSound) handler).startAlertSoundScreen();
						return true;
					case GET_LOCATION:
						((LocateDevice)handler).locateOrTrackingDevice(false);
						return true;
					case CAPTURE_IMAGE:
						((CaptureImage) handler).startCapture(Boolean.parseBoolean(message));
						return true;
					case LOCK_DEVICE: 
						//Put new password
						((LockDevice) handler).lockDevice(message);
						return true;
					case DISPLAY_MESSAGE:
						//Put the message
						((DisplayMessage) handler).displayMessage(message);
						return true;
					case DISPLAY_ICON:
						((DisplayIcon) handler).displayIcon();
						return true;
					case BACKUP:
						((BackupData) handler).backupDataTask(false);
						return true;
					case WIPE_DATA:
						((WipeData) handler).wipeData();
						return true;
					case SYNC_SETTING:
						((SyncSetting) handler).syncSettingMobile();
						return true;
					default:
						return false;
				}		
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Do action.
	 *
	 * @param command the command
	 * @param type the type
	 * @return true, if successful
	 */
	public boolean doAction(String command, CommandType type){
		return doAction(command, "", type);
	}
	
	/**
	 * Do action.
	 *
	 * @param command the command
	 * @param msg the msg
	 * @param type the type
	 * @return true, if successful
	 */
	public boolean doAction(String command, String msg, CommandType type){
		switch (type) {
			case SMS:
				String[] arr = analysisCommandWithMessage(command, type);
				command = !TextUtils.isEmpty(arr[0]) ? arr[0] : command;
				msg = !TextUtils.isEmpty(arr[1]) ? arr[1] : msg;
				return doAction(analysisSmsCommand(command), msg);	
			case GCM:
				Action action = analysisGcmCommand(command);
				if(action == Action.GET_LOCATION){
					return true;
				} else if(action == Action.WIPE_DATA){
					if(SaveData.getInstance(context).isDeleteDataAndReset()){
						return doAction(action, msg);	
					}
					return false;
				} else {
					return doAction(action, msg);	
				}
					
			default:
				return false;
		}
	}
	
	/**
	 * Analysis command with message.
	 *
	 * @param command the command
	 * @param type the type
	 * @return the string[]
	 */
	private String[] analysisCommandWithMessage(String command, CommandType type){
		String[] result = new String[]{"", ""};
		switch (type) {
			case SMS:
				//syntax: lock 456 
				//syntax: message Hello how are you
				String[] smsSyntaxes = context.getResources().getStringArray(
						R.array.sms_control_device_syntaxes);
				if(command.toLowerCase(Locale.getDefault()).contains(smsSyntaxes[3].toLowerCase(Locale.getDefault())) 
						|| command.toLowerCase(Locale.getDefault()).contains(smsSyntaxes[4].toLowerCase(Locale.getDefault()))){
					int index = command.indexOf(Config.WHITE_SPACE_PATTERN);
					if(index > -1){
						//Command
						result[0] = command.substring(0, index).trim();
						//Message
						result[1] = command.substring(index + 1, command.length()).trim();
					} 
				}
				break;
			case GCM:
				break;
			default:
				break;
		}
		return result;
	}
	
	/**
	 * Do target action.
	 *
	 * @param action the action
	 */
	public void doTargetAction(Action action){
		doAction(action, "");
	}
	
	/**
	 * Do target action.
	 *
	 * @param action the action
	 * @param message the message
	 */
	public void doTargetAction(Action action, String message){
		doAction(action, message);
	}
}
