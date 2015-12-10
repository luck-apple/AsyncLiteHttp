package com.feiyucloud.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncHttpClient extends SyncHttpClient {
    private final ExecutorService threadPool;

    public AsyncHttpClient() {
        super();
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void get(final String url, final ResponseHandler handler) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                request(url, Method.GET, null, handler);
            }
        });
    }

    @Override
    public void post(final String url, final Map<String, String> map, final ResponseHandler handler) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                request(url, Method.POST, map, handler);
            }
        });
    }
}
