package i.safareli.georadio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class Helper {

	static protected void checkInternetConnection() {
		if (!isConnectingToInternet()) {
			AlertDialog alertDialog = new AlertDialog.Builder(
					IApp.getActivity())
					.setTitle("Internet Connection Error")
					.setMessage(
							"you aren't connected to internet to use this app you need Internet Connection")
					.setIcon(R.drawable.warning)
					.setPositiveButton("OK", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							IApp.getActivity().finish();
						}
					}).setCancelable(false).create();
			alertDialog.show();
		}
	}

	static protected void showException(Exception e) {
		Log.e("sapara", "exeption", e);
		Toast.makeText(IApp.getAppContext(), "There is an exeption",
				Toast.LENGTH_SHORT).show();
		Toast.makeText(IApp.getAppContext(),
				"Exeption Message is :\n" + e.getMessage(), Toast.LENGTH_LONG)
				.show();
	}

	static protected void showException(String title, Exception e) {
		Log.d("sapara", title);
		Log.e("sapara", "exeption " + title, e);
		Toast.makeText(IApp.getAppContext(), "There is an exeption",
				Toast.LENGTH_SHORT).show();
		Toast.makeText(IApp.getAppContext(), title, Toast.LENGTH_SHORT)
				.show();
		Toast.makeText(IApp.getAppContext(),
				"Exeption Message is :\n" + e.getMessage(), Toast.LENGTH_LONG)
				.show();
	}

	static protected boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) IApp
				.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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