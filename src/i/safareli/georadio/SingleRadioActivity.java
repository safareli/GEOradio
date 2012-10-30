package i.safareli.georadio;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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

		// getting intent data
		Intent in = getIntent();
		// Get JSON values from previous intent
		position = in.getIntExtra(MyApplication.KAY_POSITION, 0);
		name = MyApplication.getRadioList().get(position)
				.get(MyApplication.TAG_NAME);
		url = MyApplication.getRadioList().get(position)
				.get(MyApplication.TAG_URL);

		// Displaying all values on the screen
		TextView tvInfo = (TextView) findViewById(R.id.tvInfo);
		tvInfo.setText(name + "\n " + url);

		Log.v("sapara", "onCreate");
		setUpVariables();

		try {
			// myApp = (MyApplication) getApplication();
			mediaPlayer = MyApplication.setPlayer(url, this);
			setPlayerControler();
			if (!mediaPlayer.isPlaying()) {
				mediaPlayer.prepareAsync();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.v("sapara", "MediaPlayer Exception", e);
		}
		createNotification();
	}

	@Override
	protected void onDestroy() {
		mediaPlayer.dismissBufferingDialog();
		super.onDestroy();
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

	public void createNotification() {
		// Prepare intent which is triggered if the
		// notification is selected
		Intent intent = new Intent(this, SingleRadioActivity.class);
		intent.putExtra(MyApplication.KAY_POSITION, position);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		// Build notification
		Notification noti = new NotificationCompat.Builder(this)
				.setContentTitle("tqven usment: " + name).setContentText(url)
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				// .addAction(R.drawable.ic_launcher, "stop", pPlayIntent)
				.build();

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti);

	}
	// @Override
	// protected void onPause() {
	// Log.v("sapara", "onPause");
	// mediaPlayer.release();
	// mediaPlayer = null;
	// super.onPause();
	// }// END onPause
}
