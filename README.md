# AsyncLiteHttp
Android异步http库，基于HttpURLConnection。

* 支持GET，POST
* 支持异步和同步

## 异步请求使用
* GET

```java
AsyncHttpClient client = new AsyncHttpClient();
client.doRequest("http://xxx.xxx.com/xxx", new TextResponseHandler() {
    @Override
    public void onFailure(Throwable e) {
       // TODO
    }

    @Override
    public void onSuccess(String response) {
        // TODO
    }
});
```

* POST

```java
AsyncHttpClient client = new AsyncHttpClient();
HashMap<Stirng, String> map = new HashMap<>();
map.put("key", "value");
client.doRequest("http://xxx.xxx.com/xxx", map, new JsonObjectResponseHandler() {

    @Override
    public void onFailure(Throwable e) {
       // TODO
    }

    @Override
    public void onSuccess(JSONObject response) {
        // TODO
    }
});
```


* 下载

```java
File file = new File("/sdcard/xxx.apk");
client.doRequest("http://xxx.xxx.com/xxx", new FileResponseHandler(file) {

    @Override
    public void onSuccess(File file) {
        // TODO
    }

    @Override
    public void onFailure(Throwable e, File file) {
        // TODO 
    }

    @Override
    public void onStart() {
        // TODO
    }

    @Override
    public void onFinish() {
        // TODO
    }

    @Override
    public void onProgress(long bytesReceived, long totalBytes) {
        // TODO
    }
});
```

## 同步请求使用
* GET

```java
new Thread(new Runnable() {
    @Override
    public void run() {
    SyncHttpClient client = new SyncHttpClient();
    String url = "http://xxx.xxx.com/xxx.action";
        client.get(url, new TextResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // TODO
            }

            @Override
            public void onFailure(Throwable e) {
                // TODO
            }
        });
    }
}).start();
```

* POST

```java
new Thread(new Runnable() {
    @Override
    public void run() {
    SyncHttpClient client = new SyncHttpClient();
    String url = "http://xxx.xxx.com/xxx.action";
    HashMap<Stirng, String> map = new HashMap<>();
    map.put("key", "value");
        client.post(url, map, new TextResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // TODO
            }

            @Override
            public void onFailure(Throwable e) {
                // TODO
            }
        });
    }
}).start();
```
