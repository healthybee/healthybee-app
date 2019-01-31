package com.app.healthybee.volleyrequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.app.healthybee.utils.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends JsonRequest<T> {

    public enum REQ_TYPE{
        RETRIEVE_CARTS
    }
    private Gson gson=new Gson();
    private Map<String, String> header;
    private Listener<T> listener;
    private Class<T> clazz;

    private GsonRequest(int requestType, String url, String requestBody, Class<T> clazz, Map<String,String> header, Listener<T> listener,Response.ErrorListener errorListener){
        super(requestType,url,requestBody,listener,errorListener);
        this.header=header;
        this.listener=listener;
        this.clazz=clazz;
    }

    public static <T> GsonRequest<T> getGsonRequest(Context context,REQ_TYPE req_type, String requestBody, Class<T> clazz, Listener<T> listener, Response.ErrorListener errorListener){
        int httpRequestType = 0;
        String url=null;
        Map<String,String> header= new HashMap<String, String>();
        header.put("Content-Type","application/json");
        switch (req_type){
            case RETRIEVE_CARTS:
                httpRequestType=Method.GET;
                url="https://healthybee-mob-api.herokuapp.com/carts";
                header.put("Authorization", "Bearer " + SharedPrefUtil.getToken(context));

        }
        GsonRequest<T> gsonRequest=new GsonRequest<>(httpRequestType,url,requestBody,clazz,header,listener,errorListener);
        return gsonRequest;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header!=null? header:super.getHeaders();
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    protected void deliverResponse(T response) {
    listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String string=new String(response.data,HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(string,clazz),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
