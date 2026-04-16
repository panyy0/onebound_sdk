package cn.onebound.sdk.overseas.amazon.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.overseas.amazon.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AmazonItemApi extends BaseApi {
    
    public AmazonItemApi(ClientConfig config) {
        super(config, "amazon");
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
    
    public ItemGetResponse getItemApp(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_app", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemApp(String numIid) throws IOException {
        return getItemApp(numIid, null);
    }
    
    public Map<String, Object> search(String q, String cat, String startPrice, String endPrice, String sort, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (cat != null) params.put("cat", cat);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (sort != null) params.put("sort", sort);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_search", params);
    }
    
    public Map<String, Object> search(String q) throws IOException {
        return search(q, null, null, null, null, null, null);
    }
    
    public Map<String, Object> review(String numIid, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_review", params);
    }
    
    public Map<String, Object> review(String numIid) throws IOException {
        return review(numIid, null, null);
    }
}