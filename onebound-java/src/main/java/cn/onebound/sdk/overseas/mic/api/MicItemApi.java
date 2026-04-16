package cn.onebound.sdk.overseas.mic.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.overseas.mic.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MicItemApi extends BaseApi {
    
    public MicItemApi(ClientConfig config) {
        super(config, "mic");
    }
    
    public ItemGetResponse getItem(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItem(String numIid) throws IOException {
        return getItem(numIid, null);
    }
    
    public Map<String, Object> search(String q, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_search", params);
    }
    
    public Map<String, Object> search(String q) throws IOException {
        return search(q, null, null);
    }
}