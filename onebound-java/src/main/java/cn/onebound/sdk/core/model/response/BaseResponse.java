package cn.onebound.sdk.core.model.response;

import com.google.gson.JsonObject;

public class BaseResponse {
    
    private JsonObject rawResponse;
    private String error;
    private String reason;
    private String errorCode;
    private String cache;
    private String apiInfo;
    private String executionTime;
    private String serverTime;
    private String clientIp;
    private String apiType;
    private String requestId;
    
    public void parse(JsonObject json) {
        this.rawResponse = json;
        if (json.has("error")) this.error = getString(json, "error");
        if (json.has("reason")) this.reason = getString(json, "reason");
        if (json.has("error_code")) this.errorCode = getString(json, "error_code");
        if (json.has("cache")) this.cache = getString(json, "cache");
        if (json.has("api_info")) this.apiInfo = getString(json, "api_info");
        if (json.has("execution_time")) this.executionTime = getString(json, "execution_time");
        if (json.has("server_time")) this.serverTime = getString(json, "server_time");
        if (json.has("client_ip")) this.clientIp = getString(json, "client_ip");
        if (json.has("api_type")) this.apiType = getString(json, "api_type");
        if (json.has("request_id")) this.requestId = getString(json, "request_id");
    }
    
    private String getString(JsonObject json, String key) {
        return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : null;
    }
    
    public boolean isSuccess() {
        return "0000".equals(errorCode) || "success".equals(error);
    }
    
    public JsonObject getRawResponse() { return rawResponse; }
    public String getError() { return error; }
    public String getReason() { return reason; }
    public String getErrorCode() { return errorCode; }
    public String getCache() { return cache; }
    public String getApiInfo() { return apiInfo; }
    public String getExecutionTime() { return executionTime; }
    public String getServerTime() { return serverTime; }
    public String getClientIp() { return clientIp; }
    public String getApiType() { return apiType; }
    public String getRequestId() { return requestId; }
}
