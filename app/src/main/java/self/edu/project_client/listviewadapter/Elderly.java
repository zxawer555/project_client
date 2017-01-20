package self.edu.project_client.listviewadapter;

/**
 * Created by wongfuchuen on 11/1/2017.
 */

public class Elderly {

    private int id;
    private String eid;
    private String firebase_token;
    private String identifier;
    private String message;
    private String photolink;
    private String gps_status;

    public Elderly(int id, String eid, String firebase_token, String identifier, String message, String photolink, String gps_status) {
        this.id = id;
        this.eid = eid;
        this.firebase_token = firebase_token;
        this.identifier = identifier;
        this.message = message;
        this.photolink = photolink;
        this.gps_status = gps_status;
    }

    public int getId() { return  this.id; }

    public String getEid() {
        return this.eid;
    }

    public String getFirebase_token() {
        return this.firebase_token;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getMessage() {
        return this.message;
    }

    public String getPhotolink() {
        return this.photolink;
    }

    public boolean getGPSStatus() {
        if (this.gps_status.equals("on")) {
            return true;
        } else {
            return false;
        }
    }

    public void setGPSStatus(String gps_status) {
        this.gps_status = gps_status;
    }

}
