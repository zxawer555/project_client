package self.edu.project_client.method.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import self.edu.project_client.R;
import self.edu.project_client.connect.server.AsyncResponseGet;
import self.edu.project_client.connect.server.HttpLink;
import self.edu.project_client.connect.server.MyAsyncTaskGet;
import self.edu.project_client.connect.server.json.HandleJsonMethod;
import self.edu.project_client.connect.server.json.HandleJsonVariable;
import self.edu.project_client.listviewadapter.Elderly;
import self.edu.project_client.listviewadapter.ElderlyLocation;
import self.edu.project_client.method.sharepreference.CustomerSharePreference;

public class ElderlyLocationService extends Service implements AsyncResponseGet {

    public static final String BROADCAST_ACTION =
            "elderlyLocationService";
    private final String TAG = "GPS";

    public static Context mContext;

    public static int elderlyLocationCount = 0;

    Intent serviceIntent;


    public ElderlyLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        serviceIntent = new Intent(BROADCAST_ACTION);

        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(1000 * 10);

                        getElderlyLocation();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getElderlyLocation()  {
        MyAsyncTaskGet.getInstance(this).executeHttpGet(
                HttpLink.URLLink + "get_elderly_location.php?uid=" +
                        CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.UID));
    }

    @Override
    public void processServerFinish(String result) {

        if (result.contains("null")) {

            super.sendBroadcast(serviceIntent);

        } else {

            String[] varName = {"eid", "latitude", "longitude", "update_time"};
            List<HandleJsonVariable> resultList = new ArrayList<HandleJsonVariable>();
            resultList = HandleJsonMethod.getInstance().convertJsonToVariable(
                    result, varName);

            ArrayList<ElderlyLocation> arrayList = new ArrayList<>();

            for (int i=0;i<resultList.size();i++) {
                String eid = resultList.get(i).eid;
                String latitude = resultList.get(i).latitude;
                String longitude = resultList.get(i).longitude;
                String update_time = resultList.get(i).update_time;

                ElderlyLocation location = new ElderlyLocation(eid, latitude, longitude, update_time);

                arrayList.add(location);
            }

            serviceIntent.putExtra("location", arrayList);

            super.sendBroadcast(serviceIntent);
        }
    }
}
