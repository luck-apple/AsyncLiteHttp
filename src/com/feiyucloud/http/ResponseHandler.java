package com.feiyucloud.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public abstract class ResponseHandler {

    public void onStart() {
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
            onSuccess(responseContent);
        } else {
            onFailure(new Throwable("responseCode is " + responseCode));
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

}
