package i.safareli.georadio;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PlayerBroadcastController extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int position = MyApplication.getCurrentAudioPosition();

		int playerOption = intent.getIntExtra(
				MyApplication.KAY_CONTROLLER_OPTIONS, 0);

		// Get radio name and url
		// String name = MyApplication.getParameters(MyApplication.TAG_NAME,
		// position);
		// String url = MyApplication.getParameters(MyApplication.TAG_URL,
		// position);
		String url;
		// TODO +1=2
		switch (playerOption) {
		case Player.CONTROLLER_NEXT:
			
			MyApplication.setCurrentAudioPosition(++position);
			url = MyApplication.getParameters(MyApplication.TAG_URL, position);
			
			try {
				MyApplication.setPlayer(url).prepareAsync();
				MyApplication.setNotification();
			} catch (IllegalArgumentException e) {
				Helper.showException("IllegalArgumentException", e);
			} catch (SecurityException e) {
				Helper.showException("SecurityException", e);
			} catch (IllegalStateException e) {
				Helper.showException("IllegalStateException", e);
			} catch (IOException e) {
				Helper.showException("IOException", e);
			} catch (Exception e) {
				Helper.showException(e);
			}
			break;
		case Player.CONTROLLER_PREVIOUS:
			
			MyApplication.setCurrentAudioPosition(--position);
			url = MyApplication.getParameters(MyApplication.TAG_URL, position);
			
			try {
				MyApplication.setPlayer(url).prepareAsync();
				MyApplication.setNotification();
			} catch (IllegalArgumentException e) {
				Helper.showException("IllegalArgumentException", e);
			} catch (SecurityException e) {
				Helper.showException("SecurityException", e);
			} catch (IllegalStateException e) {
				Helper.showException("IllegalStateException", e);
			} catch (IOException e) {
				Helper.showException("IOException", e);
			} catch (Exception e) {
				Helper.showException(e);
			}
			break;
		case Player.CONTROLLER_STOP:
			Log.d("sapara", "stop");
			MyApplication.getPlayer().stop();
			break;

		default:
			break;
		}
	}
}
