package com.nahi.vn.mobilefinder.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.broadcast.DeviceAdmin.TriggerAction;
import com.nahi.vn.mobilefinder.broadcast.LockTriggerReceiver;
import com.nahi.vn.mobilefinder.entity.ContentSender;
import com.nahi.vn.mobilefinder.entity.ContentSender.ApiType;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.Log;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.worker.ContentSenderWorker;

// TODO: Auto-generated Javadoc
/**
 * The Class CaptureActivity.
 */
public class CaptureActivity extends Activity {

	/** The preview. */
	private SurfaceView preview = null;
	
	/** The preview holder. */
	private SurfaceHolder previewHolder = null;
	
	/** The camera. */
	private Camera camera = null;
	
	/** The in preview. */
	private boolean inPreview = false;
	
	/** The camera configured. */
	private boolean cameraConfigured = false;
	
	/** The path root. */
	private String pathRoot;
	
	/** The count. */
	private int count = 0;
	
	/** The list files. */
	private ArrayList<String> listFiles = new ArrayList<String>();
	
	/** The container. */
	private LinearLayout container;
	
	/** The loading surface view time. */
	private final long LOADING_SURFACE_VIEW_TIME = 500;
	
	/** The finish loading surface view. */
	private final int FINISH_LOADING_SURFACE_VIEW = 1111;
	
	/** The handler. */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
				case FINISH_LOADING_SURFACE_VIEW:
					takePicture();
					break;
	
				default:
					break;
			}
		}
		
	};
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.layout_capture_image);
	
		container = (LinearLayout) findViewById(R.id.container);
	    preview = new SurfaceView(this);
	    container.addView(preview);
	    previewHolder = preview.getHolder();
	    previewHolder.addCallback(surfaceCallback);
	    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    
		pathRoot = AppUtil.getAppInternalDirectory(getPackageName()) + File.separator + Config.APP_NAME;
		File files = new File(pathRoot);
		if (!files.exists()) {
			files.mkdirs();
		} else {			
			File[] file = files.listFiles();
			int lenght = files.listFiles().length;
			for (int i = 0; i < lenght; i++) {
				file[i].delete();
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		try{
			if((hasBackCamera() && hasFrontCamera()) || hasBackCamera()){
				//Open back camera
				if (camera == null) {
					camera = Camera.open();
				}
			} else {
				if (camera == null) {
					camera = openFrontCamera();
				}
			}
			startPreview();
		} catch(Exception e){
			Log.e("Camera error:" + e.toString());
			finish();
		}
	}
	
	private Camera openFrontCamera(){
		Camera.CameraInfo info = new Camera.CameraInfo();
		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				return Camera.open(i);
			}
		}
		return null;
	}

	@Override
	protected void onDestroy() {
		resetCamera();
		super.onDestroy();
	}
	
	/**
	 * Take picture.
	 */
	private void takePicture(){
		if (inPreview) {
			camera.takePicture(null, null, photoCallback);
			inPreview = false;
		}
	}
	
	/**
	 * Gets the best preview size.
	 *
	 * @param width the width
	 * @param height the height
	 * @param parameters the parameters
	 * @return the best preview size
	 */
	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	/**
	 * Gets the smallest picture size.
	 *
	 * @param parameters the parameters
	 * @return the smallest picture size
	 */
	private Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPictureSizes()) {
			if (result == null) {
				result = size;
			} else {
				int resultArea = result.width * result.height;
				int newArea = size.width * size.height;

				if (newArea < resultArea) {
					result = size;
				}
			}
		}

		return (result);
	}

	/**
	 * Inits the preview.
	 *
	 * @param width the width
	 * @param height the height
	 */
	private void initPreview(int width, int height) {
		if (camera != null && previewHolder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				Log.e("Exception in setPreviewDisplay()" + t.toString());
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);
				Camera.Size pictureSize = getSmallestPictureSize(parameters);

				if (size != null && pictureSize != null) {
					parameters.setPreviewSize(size.width, size.height);
					parameters.setPictureSize(pictureSize.width,
							pictureSize.height);
					parameters.setPictureFormat(ImageFormat.JPEG);
					camera.setParameters(parameters);
					cameraConfigured = true;
				}
			}
		}
	}

	/**
	 * Send content messages.
	 */
	private void sendContentMessages() {
		if(listFiles.size() > 0){
			List<ContentSender> senderList = new ArrayList<ContentSender>();
			//Save the capture image content senders to db
			if((SaveData.getInstance(this).getPremium() == 1) && (SaveData.getInstance(this).getExpirePremium() > System.currentTimeMillis())){
				senderList.add(ContentSenderWorker.createSmsSender(getString(R.string.capture_image_sender_content)));
			}
			senderList.add(ContentSenderWorker.createNahiRequestSender(getString(R.string.capture_image_sender_content), listFiles, ApiType.UPLOAD_PICTURE));
			ContentSenderWorker.insertContentSenders(senderList);
			
			//Send the "receive content sender" broadcast 
			AppUtil.sendBroadcast(this, Config.INTENT_RECEIVE_CONTENT_SENDER);
			//Send the "lock trigger" broadcast
			if(SaveData.getInstance(this).isTriggerRunning()){
				Bundle bundle = new Bundle();
				bundle.putInt(LockTriggerReceiver.KEY_ACTION_DONE, TriggerAction.CAPTURE_IMAGE.value());
				AppUtil.sendBroadcast(this, Config.INTENT_RECEIVE_LOCK_TRIGGER, bundle);
			}
		}
		finish();
	}
	
	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		String fileName = pathRoot + "/Image" + count + "_" + formatter.format(now) + ".jpg";
		listFiles.add(fileName);
		return fileName;
	}
	
	/**
	 * Checks for front camera.
	 *
	 * @return true, if successful
	 */
	private boolean hasFrontCamera() {
	    Camera.CameraInfo camInfor = new Camera.CameraInfo();
	    for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
	        Camera.getCameraInfo(i, camInfor);
	        if (camInfor.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	        	return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Checks for back camera.
	 *
	 * @return true, if successful
	 */
	private boolean hasBackCamera() {
	    Camera.CameraInfo camInfor = new Camera.CameraInfo();
	    for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
	        Camera.getCameraInfo(i, camInfor);
	        if (camInfor.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
	        	return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Start preview.
	 */
	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}
	
	/** The surface callback. */
	private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			initPreview(width, height);
			startPreview();
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(LOADING_SURFACE_VIEW_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(FINISH_LOADING_SURFACE_VIEW);
				}
			});
			th.start();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	};

	/** The photo callback. */
	private Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
		public void onPictureTaken(final byte[] data, Camera camera) {
			camera.startPreview();
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					saveImage(data);
				}
			});
			th.start();
			inPreview = true;
		}
	};

	/**
	 * Write file.
	 *
	 * @param data the data
	 * @return true, if successful
	 */
	private boolean writeFile(byte[] data){
		File photo = new File(getFileName());
		try {
			FileOutputStream fos = new FileOutputStream(photo.getPath());
			fos.write(data);
			fos.close();
			return true;
		} catch (java.io.IOException e) {
			Log.e("Exception in photoCallback:" + e.toString());
			sendContentMessages();
			return false;
		}
	}
	
	/**
	 * Save image.
	 *
	 * @param data the data
	 */
	private void saveImage(byte[] data){
		//Write image to the storage
		if(writeFile(data)){
			count++;
		}
		
		//Check if has front camera, capture 1 more picture by front camera
		if(hasFrontCamera()){
			if(count > 1){
				//If count > 1 means that captured 2 pictures by front and end camera
				//Let's send the content messages
				sendContentMessages();
			}
			else{
				if(hasBackCamera()){
					//If has front camera and just capture 1 picture
					//Let's capture 1 more picture by front camera
					try {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//Reset surface view and camera
								resetCamera();
								//Open front camera
								if(camera == null){
									camera = openFrontCamera();
								}
							}
						});
					} catch (NoSuchMethodError e) {
						e.printStackTrace();
						//Let's send the content messages
						sendContentMessages();
					} catch (Exception e) {
						e.printStackTrace();
						//Let's send the content messages
						sendContentMessages();
					}
				} else {
					//Only has front camera
					//Let's send the content messages
					sendContentMessages();
				}
			}
		}
		else{
			if(count > 0){
				//If count > 0 in the case don't have front camera, means that captured 1 picture by end camera 
				//Let's send the content messages
				sendContentMessages();
			}
			else{
				//Retry capture picture by end camera
				takePicture();
			}
		}
	}
	
	/**
	 * Reset camera.
	 */
	private void resetCamera() {
		if (camera != null) {
			try{
				if (inPreview) {
					camera.stopPreview();
				}
				camera.release();
				camera = null;
				inPreview = false;
				
				container.removeView(preview);
				preview = null;
				preview = new SurfaceView(this);
			    container.addView(preview);
			    previewHolder = preview.getHolder();
			    previewHolder.addCallback(surfaceCallback);
			    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
