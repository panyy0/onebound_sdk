package cn.onebound.sdk.core.api;

import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public abstract class BaseApi {
    
    protected static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    protected static final Gson GSON = new Gson();
    
    protected final ClientConfig config;
    protected final OkHttpClient httpClient;
    protected final String platform;
    
    protected BaseApi(ClientConfig config, String platform) {
        this.config = config;
        this.platform = platform;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(config.getTimeout(), java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(config.getTimeout(), java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(config.getTimeout(), java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }
    
    protected String buildUrl(String apiName, Map<String, String> params) {
        TreeMap<String, String> sortedParams = new TreeMap<>(params);
        sortedParams.put("key", config.getKey());
        sortedParams.put("secret", config.getSecret());
        sortedParams.put("api_name", apiName);
        sortedParams.put("cache", config.getCache());
        sortedParams.put("result_type", config.getResultType());
        sortedParams.put("lang", config.getLang());
        if (config.getVersion() != null && !config.getVersion().isEmpty()) {
            sortedParams.put("version", config.getVersion());
        }
        
        StringBuilder url = new StringBuilder(config.getApiUrl());
        if (!config.getApiUrl().endsWith("/")) {
            url.append("/");
        }
        url.append(platform).append("/").append(apiName).append("/?");
        
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                try {
                    url.append(entry.getKey())
                       .append("=")
                       .append(java.net.URLEncoder.encode(entry.getValue(), "UTF-8"))
                       .append("&");
                } catch (java.io.UnsupportedEncodingException e) {
                    url.append(entry.getKey())
                       .append("=")
                       .append(entry.getValue())
                       .append("&");
                }
            }
        }
        
        return url.toString();
    }
    
    protected int checkErrorCode(JsonObject json) {
        if (json.has("error_code")) {
            return json.get("error_code").getAsInt();
        }
        if (json.has("code")) {
            return json.get("code").getAsInt();
        }
        return 0;
    }
    
    protected void checkLoginError(int errorCode) throws ApiException {
        if (ClientConfig.isLoginError(errorCode)) {
            throw new ApiException(errorCode, ClientConfig.getLoginMessage());
        }
    }
    
    protected <T extends BaseResponse> T execute(String apiName, Map<String, String> params, Class<T> responseClass) throws IOException {
        int retryCount = config.getRetryCount();
        IOException lastException = null;
        
        for (int attempt = 0; attempt < retryCount; attempt++) {
            String url = buildUrl(apiName, params);
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    if (attempt < retryCount - 1) {
                        config.rotateUrl();
                        continue;
                    }
                    throw new IOException("Unexpected response code: " + response.code());
                }
                
                String body = response.body().string();
                JsonObject json = GSON.fromJson(body, JsonObject.class);
                
                int errorCode = checkErrorCode(json);
                if (errorCode != 0) {
                    checkLoginError(errorCode);
                }
                
                T result = GSON.fromJson(body, responseClass);
                result.parse(json);
                return result;
            } catch (IOException e) {
                lastException = e;
                if (attempt < retryCount - 1) {
                    config.rotateUrl();
                }
            }
        }
        
        throw new IOException("API request failed after " + retryCount + " attempts", lastException);
    }
    
    protected Map<String, Object> executeRaw(String apiName, Map<String, String> params) throws IOException {
        int retryCount = config.getRetryCount();
        IOException lastException = null;
        
        for (int attempt = 0; attempt < retryCount; attempt++) {
            String url = buildUrl(apiName, params);
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    if (attempt < retryCount - 1) {
                        config.rotateUrl();
                        continue;
                    }
                    throw new IOException("Unexpected response code: " + response.code());
                }
                
                String body = response.body().string();
                JsonObject json = GSON.fromJson(body, JsonObject.class);
                
                int errorCode = checkErrorCode(json);
                if (errorCode != 0) {
                    checkLoginError(errorCode);
                }
                
                return GSON.fromJson(body, Map.class);
            } catch (IOException e) {
                lastException = e;
                if (attempt < retryCount - 1) {
                    config.rotateUrl();
                }
            }
        }
        
        throw new IOException("API request failed after " + retryCount + " attempts", lastException);
    }
    
    public static class ApiException extends IOException {
        private final int errorCode;
        
        public ApiException(int errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
        }
        
        public int getErrorCode() {
            return errorCode;
        }
    }
}
