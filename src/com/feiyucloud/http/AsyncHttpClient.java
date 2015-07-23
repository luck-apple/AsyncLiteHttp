package com.feiyucloud.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncHttpClient extends SyncHttpClient {
    private final ExecutorService threadPool;

    public AsyncHttpClient() {
        super();
        threadPool = Executors.newCachedThreadPool();
    }

    public void doRequest(final Request request, final ResponseHandler handler) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                AsyncHttpClient.super.doRequest(request, handler);
            }
        });
    }
}
