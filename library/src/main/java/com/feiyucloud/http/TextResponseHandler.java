package com.feiyucloud.http;

import java.io.UnsupportedEncodingException;

public abstract class TextResponseHandler extends ResponseHandler {
    public static final String UTF8_BOM = "\uFEFF";

    @Override
    public void onSuccess(byte[] response) {
        onSuccess(getString(response));
    }

    public abstract void onSuccess(String response);

    public static String getString(byte[] stringBytes) {
        try {
            String toReturn = (stringBytes == null) ? null : new String(stringBytes, AsyncHttpClient.UTF8);
            if (toReturn != null && toReturn.startsWith(UTF8_BOM)) {
                return toReturn.substring(1);
            }
            return toReturn;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
