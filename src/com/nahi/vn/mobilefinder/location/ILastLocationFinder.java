/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nahi.vn.mobilefinder.location;

import android.app.AlarmManager;
import android.location.Location;
import android.location.LocationListener;

// TODO: Auto-generated Javadoc
/**
 * Interface definition for a Last Location Finder. 
 * 
 * Classes that implement this interface must provide methods to 
 * find the "best" (most accurate and timely) previously detected
 * location using whatever providers are available. 
 * 
 * Where a timely / accurate previous location is not detected, classes
 * should return the last location and create a one-shot update to find
 * the current location. The one-shot update should be returned via the
 * Location Listener passed in through setChangedLocationListener.
 */
public interface ILastLocationFinder {
	
  /** The default radius. */
  public static int DEFAULT_RADIUS = 150;
  
  // The maximum distance the user should travel between location updates. 
  /** The max distance. */
  public static int MAX_DISTANCE = DEFAULT_RADIUS/2;
  
  /** The supports gingerbread. */
  public static boolean SUPPORTS_GINGERBREAD = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
  
  /** The max time. */
  public static long MAX_TIME = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	  
  /**
   * Find the most accurate and timely previously detected location 
   * using all the location providers. Where the last result is beyond
   * the acceptable maximum distance or latency create a one-shot update
   * of the current location to be returned using the {@link LocationListener}
   * passed in through {@link setChangedLocationListener}
   * @param minDistance Minimum distance before we require a location update.
   * @param minTime Minimum time required between location updates.
   * @return The most accurate and / or timely previously detected location.
   */
  public Location getLastBestLocation(int minDistance, long minTime);
  
  /**
   * Set the {@link LocationListener} that may receive a one-shot current location update.
   * @param l LocationListener
   */
  public void setChangedLocationListener(LocationListener l);
  
  /**
   * Cancel the one-shot current location update.
   */
  public void cancel();
}
