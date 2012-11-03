package i.safareli.georadio;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class SingleRadioActivity extends Activity {
	Button bPlay, bStop;
	Player mediaPlayer;
	TextView tvInfo;
	ProgressDialog dialog;
	int position;
	String name;
	String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		MyApplication.setActivity(this);

		// getting intent data
		// Intent in = getIntent();
		// Get JSON values from previous intent
		// position = in.getIntExtra(MyApplication.KAY_POSITION, 0);
		position = MyApplication.getCurrentAudioPosition();
		// Get radio name and url
		name = MyApplication.getParameters(MyApplication.TAG_NAME, position);
		url = MyApplication.getParameters(MyApplication.TAG_URL, position);

		// Displaying all values on the screen
		TextView tvInfo = (TextView) findViewById(R.id.tvInfo);
		tvInfo.setText(name + "\n " + url);

		Log.v("sapara", "onCreate");
		setUpVariables();
		try {
			mediaPlayer = MyApplication.setPlayer(url);
			setPlayerControler();
			if (mediaPlayer.isInState(Player.STATE_INITIALIZED)
					|| mediaPlayer.isInState(Player.STATE_STOPPED)) {
				mediaPlayer.prepareAsync(); 
			} else if (mediaPlayer.isInState(Player.STATE_PAUSED)
					|| mediaPlayer.isInState(Player.STATE_PREPARED)
					|| mediaPlayer.isInState(Player.STATE_PLAYBACKCOMPLETED)) {
				mediaPlayer.start();
			}
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

	public void setUpVariables() {
		bPlay = (Button) findViewById(R.id.bPlay);
		bStop = (Button) findViewById(R.id.bStop);
	}// END setProgressText

	public void setPlayerControler() {
		mediaPlayer.setStopButton(bStop);
		mediaPlayer.setPlayButton(bPlay);
		// TODO hendler from app
		mediaPlayer.setHandler(MyApplication.getHandler());
	}// END setPlayerControler

	@Override
	protected void onPause() {
		mediaPlayer.dismissBufferingDialog();
		MyApplication.setActivity(null);
		super.onPause();
		finish();
	}// END onPause

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(),
				RadioListActivity.class);
		startActivity(intent);
		// super.onBackPressed();
	}
}
