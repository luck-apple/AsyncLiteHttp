package com.feiyucloud.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public abstract class FileResponseHandler extends ResponseHandler {
    protected final File mFile;

    public FileResponseHandler(File file) {
        asserts(file != null, "File passed into FileResponseHandler constructor must not be null");
        asserts(!file.isDirectory(),
                "File passed into FileResponseHandler constructor must not point to directory");
        if (!file.getParentFile().isDirectory()) {
            asserts(file.getParentFile().mkdirs(),
                    "Cannot create parent directories for requested File location");
        }
        this.mFile = file;
    }

    private void asserts(final boolean expression, final String failedMessage) {
        if (!expression) {
            throw new AssertionError(failedMessage);
        }
    }

    @Override
    public void onSuccess(byte[] response) {
        onSuccess(getTargetFile());
    }

    @Override
    public void onFailure(Throwable e) {
        onFailure(e, getTargetFile());
    }

    protected File getTargetFile() {
        assert (mFile != null);
        return mFile;
    }

    public abstract void onSuccess(File file);

    public abstract void onFailure(Throwable throwable, File file);

    public void onProgress(long bytesReceived, long totalBytes) {
        // Do nothing by default
    }

    @Override
    protected void processResponse(HttpURLConnection connection) throws IOException {
        InputStream instream = connection.getInputStream();
        if (instream == null) {
            throw new IOException("Get InputStream from HttpURLConnection is null.");
        }

        long contentLength = connection.getContentLength();
        FileOutputStream fos = new FileOutputStream(getTargetFile());

        try {
            byte[] tmp = new byte[8192];
            int len, count = 0;
            // do not send messages if request has been cancelled
            while ((len = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
                count += len;
                fos.write(tmp, 0, len);
                sendProgressMessage(count, contentLength);
            }
            if (count == contentLength) {
                onSuccess(getTargetFile());
            } else {
                onFailure(new Exception("received bytes length is not contentLength"), getTargetFile());
            }
        } finally {
            fos.flush();
            fos.close();
            instream.close();
        }
    }

}
