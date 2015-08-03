package com.feiyucloud.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public abstract class ResponseHandler {
    protected static final int MSG_SUCCESS = 0;
    protected static final int MSG_FAILURE = 1;
    protected static final int MSG_START = 2;
    protected static final int MSG_FINISH = 3;
    protected static final int MSG_PROGRESS = 4;
    private Handler handler;
    private Looper looper = null;

    public ResponseHandler() {
        this(null);
    }

    public ResponseHandler(Looper looper) {
        this.looper = looper == null ? Looper.myLooper() : looper;
        handler = new ResponderHandler(this, this.looper);
    }

    public void onStart() {
        // Do nothing by default
    }

    public void onProgress(long bytesReceived, long totalBytes) {
        // Do nothing by default
    }

    public void onFinish() {
        // Do nothing by default
    }

    public abstract void onSuccess(byte[] response);

    public abstract void onFailure(Throwable e);

    void processResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        // Map<String, List<String>> responseHeaders =
        // connection.getHeaderFields();
        // Response
        long contentLength = connection.getContentLength();

        // 'Successful' response codes will be in interval [200,300)
        if (responseCode >= 200 && responseCode < 300) {
            byte[] responseContent = readFrom(connection.getInputStream(), contentLength);
            sendSuccessMessage(responseContent);
        } else {
            sendFailureMessage(new Throwable("responseCode is " + responseCode));
        }
    }

    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SUCCESS:
                onSuccess((byte[]) msg.obj);
                break;
            case MSG_FAILURE:
                onFailure((Throwable) msg.obj);
                break;
            case MSG_START:
                onStart();
                break;
            case MSG_FINISH:
                onFinish();
                break;
            case MSG_PROGRESS:
                Object[] obj = (Object[]) msg.obj;
                if (obj != null && obj.length >= 2) {
                    onProgress((Long) obj[0], (Long) obj[1]);
                }
                break;
        }
    }

    byte[] readFrom(InputStream inputStream, long length) throws IOException {
        if (inputStream == null) {
            return new byte[0];
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
        os.close();
        return os.toByteArray();
    }

    final protected void sendProgressMessage(long bytesWritten, long bytesTotal) {
        handler.sendMessage(obtainMessage(MSG_PROGRESS, new Object[] { bytesWritten, bytesTotal }));
    }

    final protected void sendSuccessMessage(byte[] responseBytes) {
        handler.sendMessage(obtainMessage(MSG_SUCCESS, responseBytes));
    }

    final protected void sendFailureMessage(Throwable throwable) {
        handler.sendMessage(obtainMessage(MSG_FAILURE, throwable));
    }

    final protected void sendStartMessage() {
        handler.sendMessage(obtainMessage(MSG_START, null));
    }

    final protected void sendFinishMessage() {
        handler.sendMessage(obtainMessage(MSG_FINISH, null));
    }

    protected Message obtainMessage(int responseMessageId, Object responseMessageData) {
        return Message.obtain(handler, responseMessageId, responseMessageData);
    }

    /**
     * Avoid leaks by using a non-anonymous handler class.
     */
    private static class ResponderHandler extends Handler {
        private final ResponseHandler mResponder;

        ResponderHandler(ResponseHandler mResponder, Looper looper) {
            super(looper);
            this.mResponder = mResponder;
        }

        @Override
        public void handleMessage(Message msg) {
            mResponder.handleMessage(msg);
        }
    }

}
