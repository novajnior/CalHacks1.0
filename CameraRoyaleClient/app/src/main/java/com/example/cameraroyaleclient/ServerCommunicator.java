package com.example.cameraroyaleclient;

import android.content.Context;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;

/*import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;*/

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class ServerCommunicator {
    public static final String URL_BASE = "http://ftp.nurlink.com:8080/";
    public static final String URL_REGISTRATION_SUFFIX = "addUser";
    public static final String URL_LOGIN_SUFFIX = "login";
    public static final String URL_LOGOUT_SUFFIX = "loginout";
    public static final String URL_LINK_SUFFIX = "addUserDevice";
    public static final String URL_LOCATION_SUFFIX = "getCurrentCoordinates";
    public static final String URL_TRACK_SUFFIX = "getTrack";
    public static final String URL_GET_GEOFENCE_SUFFIX = "getGeoFence";
    public static final String URL_ADD_GEOFENCE_SUFFIX = "addGeoFence";
    public static final String URL_DELETE_GEOFENCE_SUFFIX = "deleteGeoFence";
    public static final String URL_TOKEN_QUERY = "token=";
    public static final String URL_NODEID_QUERY = "nodeId=";
    public static final String URL_START_TIME_QUERY = "start_time=";
    public static final String URL_END_TIME_QUERY = "end_time=";

    private Context context;
    private RequestQueue queue;

    private static final Response.ErrorListener ERROR_LISTENER = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.getMessage() == null) {
                Log.e("BAD", "Null error message from volleyerror.");
            } else {
                Log.e("BAD", error.getMessage());
            }
        }
    };


    public ServerCommunicator(Context context) {
        queue = QueueSingleton.getInstance(context).getRequestQueue();
        this.context = context;
    }
    /*public void sendUserAndPassToURL(String username, String password, String url, Response.Listener<String> listener){
        JSONObject payload = new JSONObject();
        payload.put("usr", username);
        payload.put("pwd", password);
        StringRequest stringRequest = constructPOSTRequest(url, listener, payload);
        queue.add(stringRequest);
    }*/

    private static StringRequest constructPOSTRequest(String url, Response.Listener<String> listener, JSONObject payload) {
        final byte[] payloadbytes = payload.toString().getBytes();
        return new StringRequest(Request.Method.POST, url, listener, ERROR_LISTENER) {
            @Override
            public byte[] getBody() {
                return payloadbytes;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
    }
    private static StringRequest constructGETRequest(String url, Response.Listener<String> listener) {
        return new StringRequest(Request.Method.GET, url, listener, ERROR_LISTENER) {
        };
    }
    public static JSONObject getJSON(String string) {
        try {
            return (JSONObject) new JSONTokener(string).nextValue();
        } catch (JSONException e) {
            Log.e("BAD", "JSON parse failed");
        }
        return null;
    }
}
