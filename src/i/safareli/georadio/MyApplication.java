package i.safareli.georadio;

import java.io.IOException;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

public class MyApplication extends Application{
	protected static Player mp = null;
	protected static Handler handler;

	public static Player setPlayer(String url, Activity activity) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
		if (mp == null) {
			mp = new Player(url, activity);
		} else if (mp.getDataSource()==null || !mp.getDataSource().equals(url)){
			mp.release();
			mp = new Player(url, activity);
		}
		return mp;
	}
	public static Handler getHandler() {
		if (handler == null) {
			handler = new Handler();
		}
		return handler;
	}
	
}
