package es.demosix.conexus.FCM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    Context context;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("onTokenRefresh(): ", "Refreshed token: " + refreshedToken);

        SharedPreferences sharedPref = getSharedPreferences("user",Context.MODE_PRIVATE);
        boolean online = sharedPref.getBoolean("online", false);
        String email = sharedPref.getString("email",null);
        Log.d("email: ",email);
        if(!online){
            sendRegistrationToServer(email, refreshedToken);
        }
    }

    private void sendRegistrationToServer(final String email, final String refreshedToken) {
        // TODO: Implement this method to send token to your app server.
        // Acciones para enviar token a tu app server

        String url = "";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest myReq = new StringRequest(Request.Method.POST,
                url,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", refreshedToken);
                params.put("email", email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws  com.android.volley.AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                Log.d("headers: ", params.toString());
                return params;
            }
        };
        queue.add(myReq);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
    }
}
