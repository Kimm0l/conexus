package es.demosix.conexus.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.demosix.conexus.R;

public class MapsActivity extends SupportMapFragment implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    String email;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.activity_maps,viewGroup,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        getToken(mMap);
    }

    @Override
    public void onResume(){
        //Sincronizar el mapa?
        super.onResume();
    }
    private void getToken(GoogleMap mMap) {
        // TODO: Implement this method to send token to your app server.
        // Acciones para enviar token a tu app server
        Log.d(TAG, "Enviando el token");

        String url = "";


        SharedPreferences sharedPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        email = sharedPref.getString("email",null);
        final String password = sharedPref.getString("password",null);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                url,
                createMyReqSuccessListener(mMap),
                createMyReqErrorListener()) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", email);
                    jsonBody.put("password", password);
                    jsonBody.put("confirmPassword", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();

                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
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

    private Response.Listener<String> createMyReqSuccessListener(final GoogleMap mMap) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d(TAG, obj.getString("token"));
                    String token = obj.getString("token");
                    getAlarms(email,token,mMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOGIN ERROR:",error.toString());
            }
        };
    }


    private void getAlarms(final String email, final String token, GoogleMap mMap) {
        // TODO: Implement this method to send token to your app server.
        // Acciones para enviar token a tu app server
        Log.d(TAG, "Enviando el ciporro");

        String url = ""+email;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest myReq = new JsonArrayRequest(
                url,
                createMyReqSuccessListenerAPI(mMap),
                createMyReqErrorListenerAPI()) {

            @Override
            public Map<String, String> getHeaders() throws  com.android.volley.AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization","Bearer "+token);
                Log.d(TAG, params.toString());
                return params;
            }
        };
        queue.add(myReq);
    }

    private Response.Listener<JSONArray> createMyReqSuccessListenerAPI(final GoogleMap mMap) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                LatLng nuevo = null;
                for(int i = response.length()-1; i >= response.length()-41 ; i--){
                    String device = null;
                    String latitud = null;
                    String longitud = null;
                    String time = null;
                    String tipo = null;
                    try {
                        device = response.getJSONObject(i).get("device").toString();
                        latitud = response.getJSONObject(i).get("latitud").toString();
                        longitud = response.getJSONObject(i).get("longitud").toString();
                        time = response.getJSONObject(i).get("time").toString();
                        tipo = response.getJSONObject(i).get("tipo").toString();
                        if(tipo == "3") {
                            Double latitud_valor = Double.parseDouble(latitud);
                            Double longitud_valor = Double.parseDouble(longitud);
                            nuevo = new LatLng(latitud_valor, longitud_valor);
                            mMap.addMarker(new MarkerOptions().position(nuevo).title(device).snippet(time));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(nuevo));
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListenerAPI() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOGIN ERROR:",error.toString());
            }
        };
    }
}
