package com.feiyucloud.http.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.feiyucloud.http.AsyncHttpClient;
import com.feiyucloud.http.FileResponseHandler;
import com.feiyucloud.http.JsonObjectResponseHandler;
import com.feiyucloud.http.Request;
import com.feiyucloud.http.TextResponseHandler;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by ting on 15/7/31.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private AsyncHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        findViewById(R.id.btn_text_get).setOnClickListener(this);
        findViewById(R.id.btn_json_get).setOnClickListener(this);
        findViewById(R.id.btn_text_post).setOnClickListener(this);
        findViewById(R.id.btn_json_post).setOnClickListener(this);
        findViewById(R.id.btn_download).setOnClickListener(this);
        mClient = new AsyncHttpClient();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_text_get:
                getText();
                break;

            case R.id.btn_json_get:
                getJson();
                break;

            case R.id.btn_text_post:
                postText();
                break;

            case R.id.btn_json_post:
                postJson();
                break;

            case R.id.btn_download:
                download();
                break;
        }
    }

    private void getText() {
        Request req = new Request(Request.Method.GET);
        req.url("http://mm.gk.sdo.com/Rest/MobileMarketFacade/AppGetDetail?format=json&packname=com.geak.mobile.sync");
        mClient.doRequest(req, new TextResponseHandler() {
            @Override
            public void onSuccess(String response) {
                log(response);
            }

            @Override
            public void onFailure(Throwable e) {
                log(e.getMessage());
            }
        });
    }

    private void getJson() {
        Request req = new Request(Request.Method.GET);
        req.url("http://mm.gk.sdo.com/Rest/MobileMarketFacade/AppGetDetail?format=json&packname=com.geak.mobile.sync");
        mClient.doRequest(req, new JsonObjectResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                log(response.toString());
            }

            @Override
            public void onFailure(Throwable e) {
                log(e.getMessage());
            }
        });
    }

    private void postText() {
        Request req = new Request(Request.Method.POST);
        req.url("http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10");
        mClient.doRequest(req, new TextResponseHandler() {
            @Override
            public void onSuccess(String response) {
                log(response);
            }

            @Override
            public void onFailure(Throwable e) {
                log(e.getMessage());
            }
        });
    }

    private void postJson() {
        Request req = new Request(Request.Method.POST);
        req.url("http://sdk.feiyucloud.com/accountau/accountstatus!getAccountOnlineStatus.action");
        mClient.doRequest(req, new JsonObjectResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                log(response.toString());
            }

            @Override
            public void onFailure(Throwable e) {
                log(e.getMessage());
            }
        });
    }


    int precent = 0;
    ProgressDialog dialog;

    private void download() {
        File file = new File("/sdcard/asd12345.apk");
        String url = "http://res.gk.sdo.com/Apk/GeakSyncMobile_1.7.2.1.apk";
        Request params = new Request(Request.Method.GET);
        params.url(url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.doRequest(params, new FileResponseHandler(file) {

            @Override
            public void onSuccess(File file) {
                log("onSuccess, file:" + file);
            }

            @Override
            public void onFailure(Throwable e, File file) {
                log("onFailure,  file:" + file + ", " + e.getMessage());
            }

            @Override
            public void onStart() {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMessage("正在下载");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMax(100);
                dialog.setProgress(0);
                dialog.show();
            }

            @Override
            public void onProgress(long bytesReceived, long totalBytes) {
                int p = (int) ((totalBytes > 0) ? (bytesReceived * 1.0 / totalBytes) * 100 : -1);
                if (p != precent) {
                    precent = p;
                    log("precent:" + precent);

                    if (dialog != null) {
                        dialog.setProgress(precent);
                    }
                }
            }

            @Override
            public void onFinish() {
                log("onfinish");
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

    private void log(String msg) {
        Log.d("asd", msg);
    }


}
