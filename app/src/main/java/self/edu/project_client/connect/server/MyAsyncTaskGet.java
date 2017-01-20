package self.edu.project_client.connect.server;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Creasant on 4/8/2015.
 */
public class MyAsyncTaskGet extends AsyncTask<String, Void, String> {

    public AsyncResponseGet delegate = null;
    private static MyAsyncTaskGet _Instance;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... url) {
        // TODO Auto-generated method stub
        String result = executeHttpGet(url[0]);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processServerFinish(result);
    }

    public static MyAsyncTaskGet getInstance(AsyncResponseGet asyncResponse) {
        _Instance = new MyAsyncTaskGet(asyncResponse);

        return _Instance;
    }

    public MyAsyncTaskGet(AsyncResponseGet asyncResponse) {

        delegate = asyncResponse;

    }

    public String executeHttpGet(String url) {
        String result = "";

        // HttpClient acts like a Browser
        HttpClient client = new DefaultHttpClient();

        // Create object to represent a POST request
        HttpGet request = new HttpGet(url);

        // This will store the response from the server
        HttpResponse response;

        try {
            // Actually call the server
            response = client.execute(request);

            // Extract text message from server
            result = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            result = "[ERROR] " + e.toString();

        }

        delegate.processServerFinish(result);
        return result;
    }

}
