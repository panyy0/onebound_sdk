package cn.onebound.sdk.core.config;

import java.util.Arrays;
import java.util.List;

public class ClientConfig {
    
    public static final String DEFAULT_API_URL = "https://api-gw.onebound.cn";
    public static final List<String> API_URLS = Arrays.asList(
        "https://api-gw.onebound.cn",
        "https://api-1.onebound.cn",
        "https://api-2.onebound.cn",
        "https://api-3.onebound.cn",
        "https://api-4.onebound.cn"
    );
    
    public static final String DEFAULT_CACHE = "yes";
    public static final String DEFAULT_RESULT_TYPE = "json";
    public static final String DEFAULT_LANG = "cn";
    public static final int DEFAULT_TIMEOUT = 30;
    public static final int DEFAULT_RETRY_COUNT = 3;
    
    public static final int ERROR_NEED_LOGIN_1 = 4013;
    public static final int ERROR_NEED_LOGIN_2 = 4016;
    public static final String LOGIN_URL = "http://console.openbound.cn/console/?i=c-46989";
    
    private final String key;
    private final String secret;
    private String apiUrl;
    private final String cache;
    private final String resultType;
    private final String lang;
    private final String version;
    private final int timeout;
    private final int retryCount;
    private int urlIndex;
    
    public ClientConfig(String key, String secret) {
        this(key, secret, DEFAULT_API_URL);
    }
    
    public ClientConfig(String key, String secret, String apiUrl) {
        this.key = key;
        this.secret = secret;
        this.apiUrl = apiUrl;
        this.cache = DEFAULT_CACHE;
        this.resultType = DEFAULT_RESULT_TYPE;
        this.lang = DEFAULT_LANG;
        this.version = "";
        this.timeout = DEFAULT_TIMEOUT;
        this.retryCount = DEFAULT_RETRY_COUNT;
        this.urlIndex = 0;
    }
    
    private ClientConfig(Builder builder) {
        this.key = builder.key;
        this.secret = builder.secret;
        this.apiUrl = builder.apiUrl;
        this.cache = builder.cache;
        this.resultType = builder.resultType;
        this.lang = builder.lang;
        this.version = builder.version;
        this.timeout = builder.timeout;
        this.retryCount = builder.retryCount;
        this.urlIndex = 0;
    }
    
    public static class Builder {
        private String key;
        private String secret;
        private String apiUrl = DEFAULT_API_URL;
        private String cache = DEFAULT_CACHE;
        private String resultType = DEFAULT_RESULT_TYPE;
        private String lang = DEFAULT_LANG;
        private String version = "";
        private int timeout = DEFAULT_TIMEOUT;
        private int retryCount = DEFAULT_RETRY_COUNT;
        
        public Builder key(String key) { this.key = key; return this; }
        public Builder secret(String secret) { this.secret = secret; return this; }
        public Builder apiUrl(String apiUrl) { this.apiUrl = apiUrl; return this; }
        public Builder cache(String cache) { this.cache = cache; return this; }
        public Builder resultType(String resultType) { this.resultType = resultType; return this; }
        public Builder lang(String lang) { this.lang = lang; return this; }
        public Builder version(String version) { this.version = version; return this; }
        public Builder timeout(int timeout) { this.timeout = timeout; return this; }
        public Builder retryCount(int retryCount) { this.retryCount = retryCount; return this; }
        public ClientConfig build() { return new ClientConfig(this); }
    }
    
    public String getKey() { return key; }
    public String getSecret() { return secret; }
    public String getApiUrl() { return apiUrl; }
    public String getCache() { return cache; }
    public String getResultType() { return resultType; }
    public String getLang() { return lang; }
    public String getVersion() { return version; }
    public int getTimeout() { return timeout; }
    public int getRetryCount() { return retryCount; }
    
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
    
    public void rotateUrl() {
        urlIndex = (urlIndex + 1) % API_URLS.size();
        this.apiUrl = API_URLS.get(urlIndex);
    }
    
    public static boolean isLoginError(int errorCode) {
        return errorCode == ERROR_NEED_LOGIN_1 || errorCode == ERROR_NEED_LOGIN_2;
    }
    
    public static String getLoginMessage() {
        return "API Error: Please login at " + LOGIN_URL + " to register or recharge your account.";
    }
}
