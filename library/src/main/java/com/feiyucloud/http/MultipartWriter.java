package com.feiyucloud.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Writes multipart HTTP data to an OutputStream. Used for uploading files and
 * sending form data.
 */
class MultipartWriter {

    private static final String EOL = "\r\n";
    private static final String TWOHYPHENS = "--";

    private OutputStream outputStream;
    private DataOutputStream dos;

    private String boundary;

    public static void write(HttpURLConnection urlConnection, Request request)
            throws IOException {
        MultipartWriter mpw = new MultipartWriter(urlConnection);
        mpw.writeParts(request);
    }

    private MultipartWriter(HttpURLConnection urlConn)
            throws IOException {
        this.boundary = "===" + System.currentTimeMillis() + "===";

        urlConn.setRequestProperty("Connection", "Keep-Alive");
        urlConn.setRequestProperty("Charset", Request.UTF8);
        urlConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        outputStream = urlConn.getOutputStream();
        dos = new DataOutputStream(outputStream);

    }

    private void writeParts(Request requestParams) throws IOException {
        // Write fields
        for (ConcurrentHashMap.Entry<String, String> param : requestParams.stringEntrySet()) {
            add(param.getKey(), param.getValue());
        }

        // Write files
        for (ConcurrentHashMap.Entry<String, File> param : requestParams.fileEntrySet()) {
            add(param.getKey(), param.getValue());
        }

        // Finish up
        dos.writeBytes(TWOHYPHENS + boundary + TWOHYPHENS + EOL);
        dos.flush();
        dos.close();
        outputStream.close();
    }

    private void add(String name, String value) throws IOException {
        dos.writeBytes(TWOHYPHENS + boundary + EOL);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + EOL);
        dos.writeBytes("Content-Type: text/plain; charset=" + Request.UTF8 + EOL);
        dos.writeBytes(EOL);
        dos.writeBytes(value + EOL);
        dos.flush();
    }

    private void add(String name, File file) throws IOException {
        dos.writeBytes(TWOHYPHENS + boundary + EOL);
        dos.writeBytes("Content-Disposition: form-data;name=\"" + name + "\";filename=\""
                + file.getName() + "\"" + EOL);
        dos.writeBytes(EOL);

        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[8192];

        int count = 0;
        while ((count = fis.read(buffer)) != -1) {
            dos.write(buffer, 0, count);
        }
        fis.close();
        dos.writeBytes(EOL);
        dos.flush();
    }

}
