package i.safareli.georadio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class IApp extends Application {
	private static Player _mp = null;
	private static Handler _handler;
	private static ArrayList<HashMap<String, String>> _radioList;
	private static Activity _activity;
	private static Context _context;
	private static NotificationManager _notificationManager;
	private static int _currentAudioPosition;

	protected static final String TAG_RADIOS = "radios";
	protected static final String TAG_NAME = "name";
	protected static final String TAG_URL = "url";
	protected static final String JSON_URL = "http://safareli.github.com/radios.json";
	protected static final String KAY_POSITION = "position";
	protected static final String KAY_CONTROLLER_OPTIONS = "controllerOptions";
	protected static final int NOTIFICATION_ID = 0;

	public static Player setPlayer(String url) throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException, Exception {
		Log.d("sapara", url);
		if (_mp == null) {
			_mp = new Player(url);
		} else if (_mp.getDataSource() == null
				|| !_mp.getDataSource().equals(url)) {
			_mp.release();
			_mp = new Player(url);
		}
		return _mp;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		_context = getApplicationContext();
		_handler = new Handler();
		// Set Notification Manager
		_notificationManager = (NotificationManager) _context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static Context getAppContext() {
		return _context;
	}

	public static void setActivity(Activity activity) {
		_activity = activity;
	}

	public static Activity getActivity() {
		return _activity;
	}

	public static Player getPlayer() {
		return _mp;
	}

	public static Handler getHandler() {
		return _handler;
	}

	public static ArrayList<HashMap<String, String>> getRadioList() {
		return _radioList;
	}

	public static void setRadioList(JSONObject json) throws JSONException {
		_radioList = new ArrayList<HashMap<String, String>>();
		JSONArray radios;
		radios = json.getJSONArray(TAG_RADIOS);
		for (int i = 0; i < radios.length(); i++) {
			JSONObject c = radios.getJSONObject(i);

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(TAG_NAME, c.getString(TAG_NAME));
			map.put(TAG_URL, c.getString(TAG_URL));
			Log.v("sapara",
					c.getString(TAG_NAME) + ":::" + c.getString(TAG_URL));
			_radioList.add(map);
		}

	}

	public static boolean isSetRadioList() {
		if (_radioList == null) {
			return false;
		} else {
			return true;
		}
	}

	public static void setNotification() {

		// Prepare intent which is triggered if the notification is selected
		Intent intent = new Intent(_context, SingleRadioActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(_context, 0, intent,
				0);

		// intent for stop button
		Intent Stop = new Intent(_context, PlayerBroadcastController.class);
		Stop.putExtra(IApp.KAY_CONTROLLER_OPTIONS, Player.CONTROLLER_STOP);
		PendingIntent pStop = PendingIntent.getBroadcast(_context, 0, Stop, 0);

		// intent for Previous button
		Intent Previous = new Intent(_context, PlayerBroadcastController.class);
		Previous.putExtra(IApp.KAY_CONTROLLER_OPTIONS,
				Player.CONTROLLER_PREVIOUS);
		PendingIntent pPrevious = PendingIntent.getBroadcast(_context, 0,
				Previous, 0);

		// intent for Next button
		Intent Next = new Intent(_context, PlayerBroadcastController.class);
		Next.putExtra(IApp.KAY_CONTROLLER_OPTIONS, Player.CONTROLLER_NEXT);
		PendingIntent pNext = PendingIntent.getBroadcast(_context, 0, Next, 0);

		// Build notification
		Notification noti = new NotificationCompat.Builder(_context)
				.setContentTitle(
						"RADIO: "
								+ getParameters(TAG_NAME, _currentAudioPosition))
				.setContentText(getParameters(TAG_URL, _currentAudioPosition))
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				.addAction(R.drawable.prevous, "Previous", pPrevious)
				.addAction(R.drawable.pause, "Stop", pStop)
				.addAction(R.drawable.next, "Next", pNext).build();

		// Hide the notification after its selected
		// noti.flags |= Notification.FLAG_AUTO_CANCEL;

		// set Notification
		cancelNotification();
		_notificationManager.notify(NOTIFICATION_ID, noti);

	}

	public static void cancelNotification() {
		_notificationManager.cancel(NOTIFICATION_ID);
	}

	public static String getParameters(String tagName, int position) {
		HashMap<String, String> params = _radioList.get(position);
		if (tagName == TAG_NAME) {
			return params.get(TAG_NAME);
		} else if (tagName == TAG_URL) {
			return params.get(TAG_URL);
		} else {
			return null;
		}
	}

	public static void setCurrentAudioPosition(int position) {
		if (position == _radioList.size()) {
			_currentAudioPosition = 0;
		} else if (position == -1) {
			_currentAudioPosition = _radioList.size() - 1;
		} else {
			_currentAudioPosition = position;
		}
	}

	public static int getCurrentAudioPosition() {
		return _currentAudioPosition;
	}

	public static void lockorientation() {
		int current_orientation = IApp.getActivity().getResources()
				.getConfiguration().orientation;
		if (current_orientation == Configuration.ORIENTATION_LANDSCAPE) {
			IApp.getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			IApp.getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	public static void unlockorientation() {
		IApp.getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}
	// TODO killYourSelf
	// Process.killProcess(Process.myPid());
}
