package cn.onebound.sdk.overseas.jumia.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.overseas.jumia.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JumiaItemApi extends BaseApi {
    
    public JumiaItemApi(ClientConfig config) {
        super(config, "jumia");
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
    
    public Map<String, Object> search(Map<String, String> extraParams) throws IOException {
        return executeRaw("item_search", extraParams != null ? extraParams : new HashMap<>());
    }
    
    public Map<String, Object> search() throws IOException {
        return search(null);
    }
}