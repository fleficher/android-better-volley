# Android Better Volley
Lightweight version of Google Android Volley networking library, but with extra features : OkHttp, streaming, ...

The ImageLoader feature has been removed. Consider using another library for that such as Picasso, Glide, Universal Image Loader, ...

###Supported features :
<ul>
<li> <b>OkHttp</b> : Use OkHttp as the transport layer by default.
OkHttp is developed by the Square team : https://github.com/square/okhttp
</li> 

<li> <b>Streaming</b> : Handle the reponse as an <code>InputStream</code> that allow you to use the Streaming API of your favorite parser (Jackson, GSON, ...). Streaming makes sense if you have to handle a lot of data and you're experienceing <code>java.lang.OutOfMemoryError</code> exceptions during parsing.
</li>

<li> <b>Remove HttpClient</b> : <code>HttpClient</code> has been deprecated since Android 5.0 and removed in Android 6.0. It has been completly removed from this library and replaced with <code>HttpURLConnection</code>.
</li>
 
</ul>

## Getting started

<b>Using Gradle</b> :

```groovy
compile 'com.fleficher.bettervolley:library:1.0.1'
```

<b>Old style jars</b> :

<code>TODO </code>

## USAGE
First, create your request as usual :


```java
public class MyJsonRequest<T> extends JsonRequest<T> {
    private Class<T> mResultClass;

    public MyJsonRequest(int method, String url, String requestBody, Listener listener, ErrorListener errorListener, Class<T> classType) {
        super(method, url, requestBody, listener, errorListener);
        mResultClass = classType;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        ...
    }
    
    @Override
    public int getResponseType() {
        return super.getResponseType();
    }
}

```
Then, you can override The method  <code>getResponseType()</code> and choose between those 3 response types :
 ```java
 public interface ResponseType {
        int IGNORE = -1;
        int BYTES = 0;
        int INPUTSTREAM = 1;
    }
```
<ul>
<li> <b>IGNORE</b> : means that you don't wan't the reponse to be parsed, so response.dataStream & response.data will be null.</li> 
<li> <b>BYTES</b> (default) : The response will be returned as an array of byte in response.data.</li> 
<li> <b>INPUTSTEAM</b> : The response will be returned as an inputstream in response.dataStream.</li> 
</ul>

And... that's all, it's just simple as that ;)

## Example
If you want to parse your response as an inputStream by using Jackson for example, you can do it that way :
```java
public class JacksonRequest<T> extends JsonRequest<T> {
    private static final ObjectReader READER = new ObjectMapper().reader();
    private Class<T> mResultClass;

    public JacksonRequest(int method, String url, String requestBody, Listener listener, ErrorListener errorListener, Class<T> classType) {
        super(method, url, requestBody, listener, errorListener);
        mResultClass = classType;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        T result = null;
        if (response.dataStream != null) {
            result = READER.forType(mResultClass).readValue(response.dataStream);
        }
        return Response.success(result, null);
    }
    
    @Override
    public int getResponseType() {
        return ResponseType.INPUTSTREAM;
    }
}
```

Or by using GSON :
```java
public class GsonRequest<T> extends JsonRequest<T> {
    private static final Gson GSON = new Gson();
    private Class<T> mResultClass;

    public GsonRequest(int method, String url, String requestBody, Listener listener, ErrorListener errorListener, Class<T> classType) {
        super(method, url, requestBody, listener, errorListener);
        mResultClass = classType;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        T result = null;
        if (response.dataStream != null) {
            result = GSON.fromJson(new InputStreamReader(response.dataStream), mResultClass);
        }
        return Response.success(result, null);
    }
    
    @Override
    public int getResponseType() {
        return ResponseType.INPUTSTREAM;
    }
}
```

## Licence

```
Copyright (C) 2011 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
