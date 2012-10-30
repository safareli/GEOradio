package i.safareli.georadio;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class SingleRadioActivity  extends Activity {
	Button bPlay, bStop;
	Player mediaPlayer;
	TextView tvInfo;
	ProgressDialog dialog;
	//MyApplication myApp;

	// JSON node keys
	private static final String TAG_NAME = "name";
	private static final String TAG_URL = "url";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        String url = in.getStringExtra(TAG_URL);
        
        // Displaying all values on the screen
        TextView tvInfo = (TextView) findViewById(R.id.tvInfo); 
        tvInfo.setText("radio::: "+name+"\nurl::: "+url);
        
    	Log.v("sapara", "onCreate");
		setUpVariables();

		try {
			//myApp = (MyApplication) getApplication();
			mediaPlayer = MyApplication.setPlayer(url, this);
			setPlayerControler();
			if (!mediaPlayer.isPlaying()) {
				mediaPlayer.prepareAsync();
			}
			 } catch (Exception e) {
			e.printStackTrace();
			Log.v("sapara", "MediaPlayer Exception", e);
		}
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
		//TODO hendler from app
		mediaPlayer.setHandler(MyApplication.getHandler());
	}// END setPlayerControler

//	@Override
//	protected void onPause() {
//		Log.v("sapara", "onPause");
//		mediaPlayer.release();
//		mediaPlayer = null; 
//		super.onPause();
//	}// END onPause
}
