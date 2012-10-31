package i.safareli.georadio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class Helper  {

	static protected void checkInternetConnection(final Activity _activity) {
		if (!isConnectingToInternet(_activity)) {
			AlertDialog alertDialog = new AlertDialog.Builder(_activity)
					.setTitle("Internet Connection Error")
					.setMessage(
							"you aren't connected to internet to use this app you need Internet Connection")
					.setIcon(R.drawable.fail)
					.setPositiveButton("OK", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							_activity.finish();
						}
					}).setCancelable(false).create();
			alertDialog.show();
		}
	}

	static protected void showException(Activity _activity, Exception e) {
		String text = "exeption> getCause:" + e.getCause() + "StackTrace:"
				+ e.getStackTrace() + "getMessage:" + e.getMessage();

		Log.d("sapara", "exeption:\n" + e.getStackTrace());
		Toast.makeText(_activity, text, Toast.LENGTH_LONG * 30).show();//30 wami

	}

	static protected boolean isConnectingToInternet(Activity _activity) {
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

}