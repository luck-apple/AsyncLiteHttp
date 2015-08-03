package com.feiyucloud.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Request {
    public static final String UTF8 = "UTF-8";
    public static final String DEFAULT_USER_AGENT = "AsyncLiteHttp/1.0";

    /**
     * Supported HTTP request methods
     */
    public static enum Method {
        GET,
        POST
    }

    private final Method mMethod;
    private String mUrl;
    private Map<String, String> mStringParams;
    private Map<String, File> mFileParams;
    private Map<String, String> mHeaders;

    public Request(Method method) {
        this.mMethod = method;
        this.mStringParams = new TreeMap<String, String>();
        this.mFileParams = new TreeMap<String, File>();
        this.mHeaders = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        setUserAgent(DEFAULT_USER_AGENT);
    }

    public String getUserAgent() {
        return mHeaders.get("User-Agent");
    }

    public void setUserAgent(String userAgent) {
        mHeaders.put("User-Agent", userAgent);
    }

    public void setHeader(String name, String value) {
        mHeaders.put(name, value);
    }

    public void removeHeader(String name) {
        mHeaders.remove(name);
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public Method getMethod() {
        return mMethod;
    }

    public Request url(String url) {
        this.mUrl = url;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public Request put(String key, String value) {
        mStringParams.put(key, value);
        return this;
    }

    public Request put(String key, int value) {
        mStringParams.put(key, Integer.toString(value));
        return this;
    }

    public Request put(String key, double value) {
        mStringParams.put(key, Double.toString(value));
        return this;
    }

    public Request put(String key, float value) {
        mStringParams.put(key, Float.toString(value));
        return this;
    }

    public Request put(String key, long value) {
        mStringParams.put(key, Long.toString(value));
        return this;
    }

    public Request put(String key, boolean value) {
        mStringParams.put(key, Boolean.toString(value));
        return this;
    }

    public Request put(String key, File file) {
        mFileParams.put(key, file);
        return this;
    }

    public boolean containsKey(String key) {
        return mStringParams.containsKey(key) || mFileParams.containsKey(key);
    }

    public void removeString(String key) {
        mStringParams.remove(key);
    }

    public void removeFile(String key) {
        mFileParams.remove(key);
    }

    public String getString(String key) {
        return mStringParams.get(key);
    }

    public File getFile(String key) {
        return mFileParams.get(key);
    }

    public boolean hasFiles() {
        return mFileParams.size() > 0;
    }

    public int size() {
        return mStringParams.size() + mFileParams.size();
    }

    public Set<Map.Entry<String, String>> stringEntrySet() {
        return mStringParams.entrySet();
    }

    public Set<Map.Entry<String, File>> fileEntrySet() {
        return mFileParams.entrySet();
    }

    byte[] encodeParameters() {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : mStringParams.entrySet()) {
                if (encodedParams.length() > 0) {
                    encodedParams.append("&");
                }
                encodedParams.append(URLEncoder.encode(entry.getKey(), UTF8));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), UTF8));
            }
            return encodedParams.toString().getBytes(UTF8);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + UTF8, uee);
        }
    }

}
