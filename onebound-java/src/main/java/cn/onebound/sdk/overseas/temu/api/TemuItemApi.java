package cn.onebound.sdk.overseas.temu.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.overseas.temu.model.response.ItemSearchResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemuItemApi extends BaseApi {
    
    public TemuItemApi(ClientConfig config) {
        super(config, "temu");
    }
    
    public ItemSearchResponse search(String q, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse search(String q) throws IOException {
        return search(q, null);
    }
}