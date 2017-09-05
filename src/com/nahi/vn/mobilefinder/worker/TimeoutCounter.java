package com.nahi.vn.mobilefinder.worker;

import android.os.CountDownTimer;

// TODO: Auto-generated Javadoc
/**
 * The Class TimeoutCounter.
 */
public class TimeoutCounter extends CountDownTimer{

	/** The timeout listener. */
	private TimeoutListener timeoutListener;
	
	/**
	 * The listener interface for receiving timeout events.
	 * The class that is interested in processing a timeout
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addTimeoutListener<code> method. When
	 * the timeout event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see TimeoutEvent
	 */
	public interface TimeoutListener{
		
		/**
		 * On finish.
		 */
		public void onFinish();
	}
	
	/**
	 * Instantiates a new timeout counter.
	 *
	 * @param millisInFuture the millis in future
	 * @param countDownInterval the count down interval
	 * @param timeoutListener the timeout listener
	 */
	public TimeoutCounter(long millisInFuture, long countDownInterval, TimeoutListener timeoutListener) {
		super(millisInFuture, countDownInterval);
		this.timeoutListener = timeoutListener;
	}

	/* (non-Javadoc)
	 * @see android.os.CountDownTimer#onFinish()
	 */
	@Override
	public void onFinish() {
		if(timeoutListener != null){
			timeoutListener.onFinish();
		}
	}

	/* (non-Javadoc)
	 * @see android.os.CountDownTimer#onTick(long)
	 */
	@Override
	public void onTick(long millisUntilFinished) {
		
	}

}
