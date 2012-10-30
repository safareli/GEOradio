package i.safareli.georadio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
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
}
