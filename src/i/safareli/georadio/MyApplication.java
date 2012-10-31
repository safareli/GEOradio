package i.safareli.georadio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyApplication extends Application {
	protected static Player mp = null;
	protected static Handler handler;
	protected static ArrayList<HashMap<String, String>> radioList;

	private static final String TAG_RADIOS = "radios";
	public static final String TAG_NAME = "name";
	public static final String TAG_URL = "url";
	public static final String JSON_URL = "http://safareli.github.com/radios.json";
	public static final String KAY_POSITION = "position";
	public static final int NOTIFICATION_ID = 0;
	public static NotificationManager notificationManager;

	public static Player setPlayer(String url, Activity activity)
			throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {
		if (mp == null) {
			mp = new Player(url, activity);
		} else if (mp.getDataSource() == null
				|| !mp.getDataSource().equals(url)) {
			mp.release();
			mp = new Player(url, activity);
		}
		return mp;
	}

	public static Player getPlayer() {
		return mp;
	}

	public static Handler getHandler() {
		if (handler == null) {
			handler = new Handler();
		}
		return handler;
	}

	public static ArrayList<HashMap<String, String>> getRadioList() {
		return radioList;
	}

	public static void setRadioList(JSONObject json) {
		radioList = new ArrayList<HashMap<String, String>>();
		JSONArray radios;
		try {
			radios = json.getJSONArray(TAG_RADIOS);
			for (int i = 0; i < radios.length(); i++) {
				JSONObject c = radios.getJSONObject(i);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_NAME, c.getString(TAG_NAME));
				map.put(TAG_URL, c.getString(TAG_URL));
				Log.v("sapara",
						c.getString(TAG_NAME) + ":::" + c.getString(TAG_URL));
				radioList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isSetRadioList() {
		if (radioList == null) {
			return false;
		} else {
			return true;
		}
	}

	public static void setNotification(Activity activity, int position) {
		// Set Notification Manager
		notificationManager = (NotificationManager) activity
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Prepare intent which is triggered if the notification is selected
		Intent intent = new Intent(activity, SingleRadioActivity.class);
		intent.putExtra(MyApplication.KAY_POSITION, position);
		PendingIntent pIntent = PendingIntent.getActivity(activity, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		// Build notification
		Notification noti = new NotificationCompat.Builder(activity)
				.setContentTitle("RADIO: " + getParameters(TAG_NAME, position))
				.setContentText(getParameters(TAG_URL, position))
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				// .addAction(R.drawable.ic_launcher, "stop", pPlayIntent)
				.build();

		// Hide the notification after its selected
		//noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		//set  Notification 
		notificationManager.notify(MyApplication.NOTIFICATION_ID, noti);

	}
	public static void cancelNotification() {
		notificationManager.cancel(MyApplication.NOTIFICATION_ID);
	}
	public static String getParameters(String tagName, int position) {
		HashMap<String, String> params = radioList.get(position);
		if (tagName == TAG_NAME) {
			return params.get(TAG_NAME);
		} else if (tagName == TAG_URL) {
			return params.get(TAG_URL);
		} else {
			return null;
		}
	}
}
