package com.feiyucloud.http;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class JsonArrayResponseHandler extends TextResponseHandler {

    @Override
    public void onSuccess(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            onSuccess(jsonArray);
        } catch (JSONException e) {
            onFailure(e);
        }
    }

    public abstract void onSuccess(JSONArray response);

}
