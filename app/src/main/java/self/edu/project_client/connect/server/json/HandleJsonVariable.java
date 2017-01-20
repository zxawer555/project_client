package self.edu.project_client.connect.server.json;

import java.util.HashMap;

/**
 * Created by Creasant on 4/8/2015.
 */
public class HandleJsonVariable {

    //<for receive Json String use
    public String uid;
    public String first_name;
    public String last_name;
    public String phone_number;
    public String sex;
    public String eid;
    public String firebase_token;
    public String identifier;
    public String message;
    public String photo_link;
    public String latitude;
    public String longitude;
    public String update_time;


    private static HandleJsonVariable _Instance;

    public static HandleJsonVariable getInstance() {
        if (_Instance == null) {
            _Instance = new HandleJsonVariable();
        }
        return _Instance;
    }

    public HandleJsonVariable() {
    }

    public HandleJsonVariable hashMapToList(HashMap<String, String> hashMapVarName) {
        //<for receive Json String use
        HandleJsonVariable jsonVariableController = new HandleJsonVariable();
        jsonVariableController.uid = hashMapVarName.get("uid");
        jsonVariableController.first_name = hashMapVarName.get("first_name");
        jsonVariableController.last_name = hashMapVarName.get("last_name");
        jsonVariableController.phone_number = hashMapVarName.get("phone_number");
        jsonVariableController.sex = hashMapVarName.get("sex");
        jsonVariableController.eid = hashMapVarName.get("eid");
        jsonVariableController.firebase_token = hashMapVarName.get("firebase_token");
        jsonVariableController.identifier = hashMapVarName.get("identifier");
        jsonVariableController.message = hashMapVarName.get("message");
        jsonVariableController.photo_link = hashMapVarName.get("photo_link");
        jsonVariableController.latitude = hashMapVarName.get("latitude");
        jsonVariableController.longitude = hashMapVarName.get("longitude");
        jsonVariableController.update_time = hashMapVarName.get("update_time");

        return jsonVariableController;
    }

}
