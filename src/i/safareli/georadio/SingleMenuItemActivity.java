package i.safareli.georadio;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {
	Button bPlay, bStop;
	Player mediaPlayer;
	TextView tvInfo;
	ProgressDialog dialog;
	Handler mHandler = new Handler();
	  

	// JSON node keys
	private static final String TAG_NAME = "name";
	private static final String TAG_URL = "url";
	private static final String TAG_FM = "fm";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        String url = in.getStringExtra(TAG_URL);
        String fm = in.getStringExtra(TAG_FM);
        
        // Displaying all values on the screen
        TextView tvInfo = (TextView) findViewById(R.id.tvInfo); 
        
        tvInfo.setText("fm:"+fm+"\n"+name+"\nURL:"+url); 
    	Log.v("sapara", "onCreate");
		setUpVariables();

		try {
			mediaPlayer = new Player(url, this);
			setPlayerControler();

			mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
		} catch (Exception e) {
			e.printStackTrace();
			Log.v("sapara", "MediaPlayer Exception", e);
		}
    }

	public void setUpVariables() {
		bPlay = (Button) findViewById(R.id.bPlay);
		bStop = (Button) findViewById(R.id.bStop);
	}// END setProgressText

	public void setPlayerControler() {
		mediaPlayer.setStopButton(bStop);
		mediaPlayer.setPlayButton(bPlay);
		mediaPlayer.setHandler(mHandler);
		//urlfeeld
	}// END setPlayerControler

	@Override
	protected void onPause() {
		Log.v("sapara", "onPause");
		mediaPlayer.release();
		mediaPlayer = null; 
		super.onPause();
	}// END onPause
}
