package i.safareli.georadio;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RadioListActivity extends ListActivity {
	// url to make request
	private static String url = "http://safareli.github.com/radios.json";

	// JSON Node names
	private static final String TAG_RADIOS = "radios";
	private static final String TAG_NAME = "name";
	private static final String TAG_URL = "url";
	private Helper help = new Helper(this);
	JSONObject json;
	JSONArray radios;

	// @SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio_list);
		help.checkoInternetConnection();
		new JSONTask().execute(url);

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
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			json = result;
			ArrayList<HashMap<String, String>> radioList = new ArrayList<HashMap<String, String>>();
			try {
				radios = json.getJSONArray(TAG_RADIOS);
				for (int i = 0; i < radios.length(); i++) {
					JSONObject c = radios.getJSONObject(i);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put(TAG_NAME, c.getString(TAG_NAME));
					map.put(TAG_URL, c.getString(TAG_URL));
					Log.v("sapara",
							c.getString(TAG_NAME) + ":::"
									+ c.getString(TAG_URL));
					radioList.add(map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ListAdapter adapter = new SimpleAdapter(
					RadioListActivity.this, radioList,
					R.layout.list_item, new String[] { TAG_NAME, TAG_URL },
					new int[] { R.id.tvName, R.id.tvUrl });

			RadioListActivity.this.setListAdapter(adapter);

			// selecting single ListView item
			ListView lv = getListView();

			// Launching new screen on Selecting Single ListItem
			lv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// getting values from selected ListItem
					String name = ((TextView) view.findViewById(R.id.tvName))
							.getText().toString();
					String url = ((TextView) view.findViewById(R.id.tvUrl))
							.getText().toString();

					// Starting new intent
					Intent in = new Intent(getApplicationContext(),
							SingleRadioActivity.class);
					in.putExtra(TAG_NAME, name);
					in.putExtra(TAG_URL, url);
					startActivity(in);

				}
			});

		}

	}

}
