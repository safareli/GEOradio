package i.safareli.georadio;

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

	private Helper help = new Helper(this);

	// @SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio_list);
		help.checkInternetConnection();

		if (!MyApplication.isSetRadioList()) {
			new JSONTask().execute(MyApplication.JSON_URL);
		} else {

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
			return new JSONParser().getJSONFromUrl(params[0]);
		}

		@Override
		protected void onPostExecute(JSONObject jsonResult) {
			super.onPostExecute(jsonResult);
			MyApplication.setRadioList(jsonResult);
			doJob();
		}

	}

	public void doJob() {

		ListAdapter adapter = new SimpleAdapter(RadioListActivity.this,
				MyApplication.getRadioList(), R.layout.list_item, new String[] {
						MyApplication.TAG_NAME, MyApplication.TAG_URL },
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
				in.putExtra(MyApplication.KAY_POSITION, position);
				startActivity(in);

			}
		});

	}
}
