package i.safareli.georadio;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PlayerBroadcastController extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int position = IApp.getCurrentAudioPosition();
		String playerOption = intent.getAction();
		String url;
		if (playerOption == Player.CONTROLLER_STOP) {

			// stop mediaplayer
			IApp.getPlayer().stop();

		} else if (playerOption == Player.CONTROLLER_NEXT) {
			IApp.setCurrentAudioPosition(++position);
			position = IApp.getCurrentAudioPosition();
			url = IApp.getParameters(IApp.TAG_URL, position);
			try {
				IApp.setPlayer(url).prepareAsync();
				IApp.setNotification();
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
		} else if (playerOption == Player.CONTROLLER_PREVIOUS) {
			IApp.setCurrentAudioPosition(--position);
			position = IApp.getCurrentAudioPosition();
			url = IApp.getParameters(IApp.TAG_URL, position);

			try {
				IApp.setPlayer(url).prepareAsync();
				IApp.setNotification();
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
		}
	}
}
