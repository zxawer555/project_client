package self.edu.project_client.method.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EddieWong on 30/9/2016.
 */

public class CustomerSharePreference {

    private static final String PREFS_NAME = "CustomerPerfsFile";
    public static final String UID = "UID";
    public static final String Email = "email";
    public static final String First_Name = "first_name";
    public static final String Last_Name = "last_name";
    public static final String Login = "isLogin";

    private static CustomerSharePreference _Instance;
    public static Context mContext;

    public static CustomerSharePreference getInstance() {

        if (_Instance == null) {
            _Instance = new CustomerSharePreference();
        }
        return _Instance;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setStringPreference(String key, String value) {
        // MY_PREFS_NAME - a static String variable like:
        // public static final String MY_PREFS_NAME = "MyPrefsFile";
        // MODE_PRIVATE - can user-defined MODE (default 0)
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Setting should save value (key, value) to shared_prefs
        editor.putString(key, value);
        editor.commit();
    }

    public void setBooleanPreference(String key, Boolean value) {
        // MY_PREFS_NAME - a static String variable like:
        // public static final String MY_PREFS_NAME = "MyPrefsFile";
        // MODE_PRIVATE - can user-defined MODE (default 0)
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Setting should save value (key, value) to shared_prefs
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getStringPreference(String key) {
        String result;

        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);
        result = settings.getString(key, "");
        return result;
    }

    public Boolean getBooleanPreference(String key) {
        Boolean result;

        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);
        result = settings.getBoolean(key, false);
        return result;
    }

}
