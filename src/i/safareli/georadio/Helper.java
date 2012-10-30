package i.safareli.georadio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Helper implements OnClickListener {

	private Activity _activity;

	public Helper(Activity activity) {
		_activity = activity;
	}

	public void checkoInternetConnection() {
		if (!isConnectingToInternet()) {
			AlertDialog alertDialog = new AlertDialog.Builder(_activity)
					.setTitle("Internet Connection Error")
					.setMessage(
							"you aren't connected to internet to use this app you need Internet Connection")
					.setIcon(R.drawable.fail).setPositiveButton("OK", this)
					.setCancelable(false).create();
			alertDialog.show();
		}
	}

	private boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void onClick(DialogInterface dialog, int which) {
		_activity.finish();
	}

}