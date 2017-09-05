/*
 *  aris-vn.com
 *  Log.java
 *  Created by ThaiLe.
 *
 * Copyright (C) 2009 The Android Open Source Project
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
package com.nahi.vn.mobilefinder.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

// TODO: Auto-generated Javadoc
/**
 * The Class Log. Using for the log on Android Logcat
 */
public class Log {

    /** The Constant LOG. */
    public static final boolean LOG = true;

    /** The Constant TAG. */
    public static final String TAG = "thaile";

    /**
     * I.
     * 
     * @param string the string
     */
    public static void i(String string) {
        if (LOG) {
        	android.util.Log.i(TAG, string);
        	appendLog(string);
        }
    }

    /**
     * E.
     * 
     * @param string the string
     */
    public static void e(String string) {
        if (LOG) {
        	android.util.Log.e(TAG, string);
        	appendLog(string);
        }
    }

    /**
     * D.
     * 
     * @param string the string
     */
    public static void d(String string) {
        if (LOG) {
        	android.util.Log.d(TAG, string);
        	appendLog(string);
        }
    }

    /**
     * V.
     * 
     * @param string the string
     */
    public static void v(String string) {
        if (LOG) {
        	android.util.Log.v(TAG, string);
        	appendLog(string);
        }
    }

    /**
     * W.
     * 
     * @param string the string
     */
    public static void w(String string) {
        if (LOG) {
        	android.util.Log.w(TAG, string);
        	appendLog(string);
        }
    }
    
	/**
	 * Append log.
	 *
	 * @param text the text
	 */
	public static void appendLog(String text) {
		File logFile = new File(Environment.getExternalStorageDirectory(), "finder_log.txt");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
