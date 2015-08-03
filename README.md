# AsyncLiteHttp
Android异步http库，基于HttpURLConnection。

* 支持GET，POST
* 支持文件上传和文件下载
* 支持异步和同步

## 使用
* GET请求

```java
Request request = new Request(Method.GET);
request.url("http://xxx.xxx.com/xxx");
AsyncHttpClient client = new AsyncHttpClient();
client.doRequest(request, new TextResponseHandler() {

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

* POST请求

```java
Request request = new Request(Method.POST);
request.url("http://xxx.xxx.com/xxx").put("name", "Tom").put("age", 12);
AsyncHttpClient client = new AsyncHttpClient();
client.doRequest(request, new JsonObjectResponseHandler() {

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
File file = new File("/sdcard/asd12348.apk");
Request request = new Request(Method.GET);
request.url("http://xxx.xxx.com/xxx);
client.doRequest(request, new FileResponseHandler(file) {

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
