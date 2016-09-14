/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package de.appwerft.headphonebuttons;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollEventCallback;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;

@Kroll.module(name = "Headphonekeyboard", id = "de.appwerft.headphonebuttons")
public class HeadphonebuttonsModule extends KrollModule {
	public static final String LCAT = "HeadPhoneButtons  📢📢";
	HeadphoneButtonReceiver headphoneButtonReceiver;
	static KrollFunction callback;
	ComponentName receiver;
	static TiApplication mApp;
	AudioManager audioManager;

	public HeadphonebuttonsModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(final TiApplication app) {

		mApp = app;
	}

	public void onDestroy() {
		if (audioManager != null)
			audioManager.unregisterMediaButtonEventReceiver(receiver);
	}

	@Override
	public int addEventListener(String arg0, KrollEventCallback arg1) {
		// TODO Auto-generated method stub
		return super.addEventListener(arg0, arg1);
	}

	boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(LCAT, "🎧 🎧 🎧 Keycode from onKeyDownIntent" + keyCode);

		switch (keyCode) {
		case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
			// code for fast forward
			return true;
		case KeyEvent.KEYCODE_MEDIA_NEXT:
			// code for next
			return true;
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			// code for play/pause
			return true;
		case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
			// code for previous
			return true;
		case KeyEvent.KEYCODE_MEDIA_REWIND:
			// code for rewind
			return true;
		case KeyEvent.KEYCODE_MEDIA_STOP:
			// code for stop
			return true;
		}
		return false;
	}

	@Kroll.method
	public void registerListener() {
		Activity activity = TiApplication.getAppRootOrCurrentActivity();

		Context ctx = activity.getApplicationContext();
		audioManager = (AudioManager) ctx
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.requestAudioFocus(new OnAudioFocusChangeListener(),
				AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
		audioManager.registerMediaButtonEventReceiver(new ComponentName(ctx
				.getPackageName(), HeadphoneButtonReceiver.class.getName()));
		Log.d(LCAT, "headphoneButtonReceiver registered");
	}

	private class OnAudioFocusChangeListener implements
			AudioManager.OnAudioFocusChangeListener {
		@Override
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
				Log.e("ClassOnAudioFocusChangeListener: ",
						"AUDIOFOCUS_LOSS_TRANSIENT");
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
				Log.e("ClassOnAudioFocusChangeListener: ",
						"AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				Log.e("ClassOnAudioFocusChangeListener: ", "AUDIOFOCUS_GAIN");
				// mAudioManager.registerMediaButtonEventReceiver(mMediaButtonEventComponenName);
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				Log.e("ClassOnAudioFocusChangeListener: ", "AUDIOFOCUS_LOSS");
			}
		}
	};

	public static void sendBack(KrollDict event) {
		mApp.fireAppEvent("mediaButton", event);
	}

}
