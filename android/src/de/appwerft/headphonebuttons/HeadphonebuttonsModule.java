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
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;

@Kroll.module(name = "Headphonekeyboard", id = "de.appwerft.headphonebuttons")
public class HeadphonebuttonsModule extends KrollModule {
	public static final String LCAT = "HeadPhoneButtons  📢📢";
	HeadphoneButtonReceiver headphoneButtonReceiver;
	static KrollFunction callback;
	private ComponentName receiver;
	static TiApplication tiapp;
	private AudioManager audioManager;
	private IntentFilter uiIntentFilter;
	private Activity activity;

	public HeadphonebuttonsModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(final TiApplication app) {

		tiapp = app;
	}

	public void onDestroy() {
		if (audioManager != null)
			audioManager.unregisterMediaButtonEventReceiver(receiver);
		activity.unregisterReceiver(uiMediaReceiver);
	}

	@Override
	public int addEventListener(String arg0, KrollEventCallback arg1) {
		// TODO Auto-generated method stub
		return super.addEventListener(arg0, arg1);
	}

	@Kroll.method
	public void registerListener() {
		activity = TiApplication.getAppRootOrCurrentActivity();

		Context ctx = activity.getApplicationContext();
		audioManager = (AudioManager) ctx
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.requestAudioFocus(new OnAudioFocusChangeListener(),
				AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
		audioManager.registerMediaButtonEventReceiver(new ComponentName(ctx
				.getPackageName(), HeadphoneButtonReceiver.class.getName()));
		uiIntentFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
		uiIntentFilter
				.addAction(Constants.INTENT_ACTION_VIEW_MEDIA_LIST_KEYPRESS);
		uiIntentFilter.setPriority(Integer.MAX_VALUE);
		activity.registerReceiver(uiMediaReceiver, uiIntentFilter);
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
		tiapp.fireAppEvent("mediaButton", event);
	}

	/**
	 * Local broadcast receiver that allows us to handle media button events for
	 * navigation inside the activity.
	 */
	private BroadcastReceiver uiMediaReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())
					|| Constants.INTENT_ACTION_VIEW_MEDIA_LIST_KEYPRESS
							.equals(intent.getAction())) {
				KeyEvent navigationKeyEvent = (KeyEvent) intent.getExtras()
						.get(Intent.EXTRA_KEY_EVENT);
				int keyCode = navigationKeyEvent.getKeyCode();
				if (Utils.isMediaButton(keyCode)) {
					Log.d(LCAT,
							"Media Button Selector: UI is directly handling key: "
									+ navigationKeyEvent);
					if (navigationKeyEvent.getAction() == KeyEvent.ACTION_UP) {
						KrollDict dict = new KrollDict();
						dict.put("keyCode",
								Utils.getAdjustedKeyCode(navigationKeyEvent));
						switch (Utils.getAdjustedKeyCode(navigationKeyEvent)) {
						case KeyEvent.KEYCODE_MEDIA_NEXT:
							dict.put("keyName", "media_next");
							HeadphonebuttonsModule.sendBack(dict);
							break;
						case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
							dict.put("keyName", "media_previous");
							HeadphonebuttonsModule.sendBack(dict);
							break;
						case KeyEvent.KEYCODE_HEADSETHOOK:
						case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
							dict.put("keyName", "media_playpause");
							HeadphonebuttonsModule.sendBack(dict);
							break;
						case KeyEvent.KEYCODE_MEDIA_STOP:
							dict.put("keyName", "media_stop");
							HeadphonebuttonsModule.sendBack(dict);
							break;
						default:
							break;
						}
					}
					if (isOrderedBroadcast()) {
						abortBroadcast();
					}
				}

			}

		}
	};
}
