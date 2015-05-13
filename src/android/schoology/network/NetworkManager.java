package android.schoology.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;

public class NetworkManager {

	private static final String gradeURL = "https://api.myjson.com/bins/3icap";  
	private HttpsURLConnection urlConnection;
	
	public void request(final Class cls, final RequestCallback cb) {
		(new HTTPTask(cb)).execute(gradeURL);
	}
	
	private String connect(String uri) {
		StringBuilder total = null;
		try {
			URL url = new URL(uri);
			urlConnection = (HttpsURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
		} catch (Exception e) {
			if (urlConnection != null)
				urlConnection.disconnect();
			return null;
		} finally {
			return total == null ? null : total.toString();
		}
	}
	
	public interface RequestCallback {
		public void onRequest(String raw);
	}
	
	private class HTTPTask extends AsyncTask<String, Void, Void> {
		RequestCallback cb;
		public HTTPTask(RequestCallback cb) {
			this.cb = cb;
		}
		
		protected Void doInBackground(String... params) {
			cb.onRequest(connect(params[0]));
			return null;
		}
		
	};
}
