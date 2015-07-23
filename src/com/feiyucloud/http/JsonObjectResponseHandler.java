package com.feiyucloud.http;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonObjectResponseHandler extends TextResponseHandler {

    @Override
    public void onSuccess(String response) {
        try {
            JSONObject json = new JSONObject(response);
            onSuccess(json);
        } catch (JSONException e) {
            onFailure(e);
        }

    }

    public abstract void onSuccess(JSONObject response);

}
