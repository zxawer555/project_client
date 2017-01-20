package self.edu.project_client.listviewadapter;

import java.io.Serializable;

/**
 * Created by wongfuchuen on 20/1/2017.
 */

public class ElderlyLocation implements Serializable {

    private String eid;
    private String latitude;
    private String longitude;
    private String update_time;

    public ElderlyLocation(String eid, String latitude, String longitude, String update_time) {
        this.eid = eid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.update_time = update_time;
    }

    public String getEid() {
        return this.eid;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getUpdate_time() {
        return this.update_time;
    }

}
