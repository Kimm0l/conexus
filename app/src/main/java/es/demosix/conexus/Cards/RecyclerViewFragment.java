package es.demosix.conexus.Cards;

/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.demosix.conexus.Cards.CustomAdapter;
import es.demosix.conexus.Modelo.Alarm;
import es.demosix.conexus.R;

public class RecyclerViewFragment extends Fragment {

    private static String TAG = "DevicesActivity: ";

    RecyclerView rv;
    JSONArray responseArray;
    String email;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_alarmas, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rvAlarmas);
        rv.setHasFixedSize(true);

        return rootView;
    }


    @Override
    public void onResume(){
        getToken();
        super.onResume();
    }

    private void getToken() {
        // TODO: Implement this method to send token to your app server.
        // Acciones para enviar token a tu app server
        Log.d(TAG, "Enviando el token");

        String url = "";


        SharedPreferences sharedPref = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        email = sharedPref.getString("email",null);
        final String password = sharedPref.getString("password",null);

        Log.d(TAG,"Strings" +email+"////"+password);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                url,
                createMyReqSuccessListener(),
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

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d(TAG,obj.getString("token").toString());
                    String token = obj.getString("token").toString();
                    getAlarms(email,token);
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


    private void getAlarms(final String email, final String token) {
        // TODO: Implement this method to send token to your app server.
        // Acciones para enviar token a tu app server
        Log.d(TAG, "Enviando el ciporro");

        String url = ""+email;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest myReq = new JsonArrayRequest(
                url,
                createMyReqSuccessListenerAPI(),
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

    private Response.Listener<JSONArray> createMyReqSuccessListenerAPI() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                ArrayList<Alarm> alarms = new ArrayList<>();
                for(int i = response.length()-1; i >= response.length()-41 ; i--){
                    String device = null;
                    String latitud = null;
                    String longitud = null;
                    String time = null;
                    String bateria = null;
                    String tipo = null;
                    try {
                        device = response.getJSONObject(i).get("device").toString();
                        latitud = response.getJSONObject(i).get("latitud").toString();
                        longitud = response.getJSONObject(i).get("longitud").toString();
                        time = response.getJSONObject(i).get("time").toString();
                        bateria = response.getJSONObject(i).get("bateria").toString();
                        tipo = response.getJSONObject(i).get("tipo").toString();
                        switch(tipo){
                            case "0":
                                alarms.add(new Alarm(tipo,"Se ha encendido", time,latitud,longitud,bateria,device));
                                break;
                            case "1":
                                alarms.add(new Alarm(tipo,"Todo funciona correctamente", time,latitud,longitud,bateria,device));
                                break;
                            case "2":
                                alarms.add(new Alarm(tipo,"Inicio del movimiento", time,latitud,longitud,bateria,device));
                                break;
                            case "3":
                                alarms.add(new Alarm(tipo,"Esta en movimiento", time,latitud,longitud,bateria,device));
                                break;
                            case "4":
                                alarms.add(new Alarm(tipo,"Continua en funcionamiento", time,latitud,longitud,bateria,device));
                                break;
                            case "5":
                                alarms.add(new Alarm(tipo,"Se ha restablecido", time,latitud,longitud,bateria,device));
                                break;
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CustomAdapter adapter = new CustomAdapter(getContext(), alarms);
                rv.setAdapter(adapter);

                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(llm);
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