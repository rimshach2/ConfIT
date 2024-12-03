package com.fyp.confit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

public class HTTPrequest {

    public interface VolleyCallback{
        void onSuccess(String result);
        void onFaliure(String faliure);
    }

    public static void callAPI(String method, String url, final JSONObject params, final JSONObject headers, final VolleyCallback callback, Context context){

        switch (method){
            case "Post":
                RequestQueue queue = Volley.newRequestQueue(context);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,url, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse( JSONObject response ) {
                                Log.e("Response",response.toString());
                                callback.onSuccess(response.toString());

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error);
                        callback.onFaliure(error.toString());

                    }
                });
                queue.add(jsonObjReq);
                break;

            case "Get":
                RequestQueue queue1 = Volley.newRequestQueue(context);

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("Response",response.toString());
                                callback.onSuccess(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error);
                        callback.onFaliure(error.toString());
                    }
                });

                queue1.add(stringRequest);

                break;
        }


    }


    public static void placeRequest(String url, String method, final Map<String, String> params, final Map<String, String> headers, final VolleyCallback callback, Activity name){

        switch (method){
            case "Post":
                RequestQueue queue = Volley.newRequestQueue(name);

                StringRequest jsonObjReq = new StringRequest(Request.Method.POST,url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.e("Response",response.toString());
                                Log.e("Response",response);
                                callback.onSuccess(response.toString());

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error);
                        callback.onFaliure(error.toString());

                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return headers;
                    }
                    @Override
                    protected Map<String, String> getParams() {
                        return params;
                    }
                };
                queue.add(jsonObjReq);
                break;

            case "Get":
                RequestQueue queue1 = Volley.newRequestQueue(name);

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Response",response.toString());
                                Log.e("Response",response);
                                callback.onSuccess(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error);
                        callback.onFaliure(error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return headers;
                    }
                    @Override
                    protected Map<String, String> getParams() {
                        return params;
                    }
                };

// Add the request to the RequestQueue.
                queue1.add(stringRequest);

                break;
        }


    }

    /*
    public static void placeStringRequest(String method, String url, final Map<String, String> params, final Map<String, String> headers, final VolleyCallback callback, Context context){

        switch (method){
            case "Post":
                RequestQueue queue = Volley.newRequestQueue(context);

                StringRequest jsonObjReq = new StringRequest(Request.Method.POST,url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.e("Response",response.toString());
                                Log.e("Response",response);
                                callback.onSuccess(response.toString());

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error);
                        callback.onFaliure(error.toString());

                    }
                });
                queue.add(jsonObjReq);
                break;

            case "Get":
                RequestQueue queue1 = Volley.newRequestQueue(context);

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Response",response.toString());
                                Log.e("Response",response);
                                callback.onSuccess(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error);
                        callback.onFaliure(error.toString());
                    }
                });

                queue1.add(stringRequest);

                break;
        }


    }
*/

}
