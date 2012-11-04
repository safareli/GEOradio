package i.safareli.georadio;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RadioListActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio_list);
		IApp.setActivity(this);

		Helper.checkInternetConnection();
		if (!IApp.isSetRadioList()) {
			new JSONTask().execute(IApp.JSON_URL);
		} else {
			doJob();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_android_list_view, menu);
		return true;
	}

	public class JSONTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			return JSONParser.getJSONFromUrl(params[0]);
		}

		@Override
		protected void onPostExecute(JSONObject jsonResult) {
			super.onPostExecute(jsonResult);
			try {
				IApp.setRadioList(jsonResult);
			} catch (JSONException e) {
				Helper.showException(e);
			}
			doJob();
		}

	}

	public void doJob() {
		ListAdapter adapter = new SimpleAdapter(RadioListActivity.this,
				IApp.getRadioList(), R.layout.list_item, new String[] {
						IApp.TAG_NAME, IApp.TAG_URL },
				new int[] { R.id.tvName, R.id.tvUrl });

		RadioListActivity.this.setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = getListView();

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SingleRadioActivity.class);
				IApp.setCurrentAudioPosition(position);
				// in.putExtra(MyApplication.KAY_POSITION, position);
				startActivity(in);
				RadioListActivity.this.finish();
			}

		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		IApp.cancelNotification();
		// setting Activity in case resume happend
		IApp.setActivity(this);
	}

	@Override
	protected void onPause() {
		IApp.setActivity(null);
		if (IApp.getPlayer() != null
				&& IApp.getPlayer().isInState(Player.STATE_STARTED)) {
			IApp.setNotification();
		}

		super.onPause();
	}
 
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		finish();
	}
}
