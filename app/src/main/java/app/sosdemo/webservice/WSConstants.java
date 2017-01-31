package app.sosdemo.webservice;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;

/**
 * Created  on 19/08/15.
 */
public class WSConstants {
    public static String BASE_URL = "http://kawach.ilabindia.com/Services/NagrikService.asmx/";
    public static String BASE_URL_UPLOAD_PHOTO = "";
    public static String CONST_HTTP = "http://";
    public static String CONST_REPLACE_STRING1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    public static String CONST_REPLACE_STRING2 = "<string xmlns=\"http://tempuri.org/\">";
    public static String CONST_REPLACE_STRING3 = "</string>";

    public static int CONNECTION_TIMEOUT = 30 * 1000;
    public static int STATUS_FAIL = 1;
    public static int STATUS_NETWORK_ERROR = 501;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String METHOD_ANGANMSGBORADCAST = "anganMsgBoradcast";
    public static final String METHOD_ANGANAUTHENTICATEUSER = "authenticateLoginData";
    public static final String METHOD_REGISTER = "signupLoginData";
    public static final String METHOD_SOS = "SetSOSMobileService";
    public static final String METHOD_ALERTTYPELIST = "getAlertTypeList";
    public static final String METHOD_EMERGENCY = "getEmergencyCenterService";

    public static final String PARAMS_USERID = "userid";
    public static final String PARAMS_USERPASSWORD = "password";

    //---------------Register-----------------------------

    public static final String PARAMS_prefix = "prefix";
    public static final String PARAMS_FIRSTNAME = "firstname";
    public static final String PARAMS_MIDDLENAME = "middlename";
    public static final String PARAMS_LASTNAME = "lastname";
    public static final String PARAMS_GENDER = "gender";
    public static final String PARAMS_ADDRESS = "address";
    public static final String PARAMS_AREA = "area";
    public static final String PARAMS_CITY = "city";
    public static final String PARAMS_PINCODE = "pincode";
    public static final String PARAMS_DISTRICT = "district";
    public static final String PARAMS_STATE = "state";
    public static final String PARAMS_MOBILE = "mobile";
    public static final String PARAMS_LANDLINE = "landline";
    public static final String PARAMS_EMAILADDRESS = "emailaddress";
    public static final String PARAMS_DOB = "dob";
    public static final String PARAMS_MACID = "macid";
    public static final String PARAMS_SIMID = "simid";

    //----------------------SOS----------------------

    public static final String PARAMS_TripID = "TripID";
    public static final String PARAMS_MacID = "MacID";
    public static final String PARAMS_SimID = "SimID";
    public static final String PARAMS_TimeStamp = "TimeStamp";
    public static final String PARAMS_Lat = "Lat";
    public static final String PARAMS_Long = "Long";
    public static final String PARAMS_AlertType = "AlertType";
    public static final String PARAMS_LogStatus = "LogStatus";

    public static final String PARAMS_SUPERVISORID = "SupervisorID";
    public static final String PARAMS_LAT = "Lat";
    public static final String PARAMS_LNG = "Lng";
    public static final String PARAMS_LOGINUSERID = "LoginUserID";
    public static final String PARAMS_ACTIVITY_CODE = "ActivityCode";
    public static final String PARAMS_ACTIVITY_TYPE = "ActivityType";

    public static final String URL_QUESTION_MARK = "?";
    public static final String URL_EQUALS_TO = "=";
    public static final String URL_AND = "&";


    public static final String RETURN_CODE = "ReturnCode";
    public static final String GROUP_ID = "GroupID";
    public static final String GROUP_DESC = "GroupDesc";
    public static final String PK_ID = "pkID";
    public static final String QUESTION = "Question";
    public static final String P_GROUP_ID = "pGroupID";
    public static final String EMP_ID = "EmpID";
    public static final String ANSWER_KEY = "AnswerKey";
    public static final String DATA_LIST = "dataList";


    public static String getNetworkError() {

        try {
            final JSONObject object = new JSONObject();
            object.put("status", STATUS_FAIL);
            object.put("code", STATUS_NETWORK_ERROR);
            object.put("message", "Network error, Please try after some time.");
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
