package cn.onebound.sdk.overseas.shop.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.overseas.shop.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShopItemApi extends BaseApi {
    
    public ShopItemApi(ClientConfig config) {
        super(config, "shop");
    }
    
    public ItemGetResponse getItem(String numIid, String skuId, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (skuId != null) params.put("sku_id", skuId);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItem(String numIid) throws IOException {
        return getItem(numIid, null, null);
    }
    
    public Map<String, Object> search(Map<String, String> extraParams) throws IOException {
        return executeRaw("item_search", extraParams != null ? extraParams : new HashMap<>());
    }
    
    public Map<String, Object> search() throws IOException {
        return search(null);
    }
    
    public Map<String, Object> area(Map<String, String> extraParams) throws IOException {
        return executeRaw("item_area", extraParams != null ? extraParams : new HashMap<>());
    }
    
    public Map<String, Object> area() throws IOException {
        return area(null);
    }
}