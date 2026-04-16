package cn.onebound.sdk.overseas.lazada.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.overseas.lazada.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LazadaItemApi extends BaseApi {
    
    public LazadaItemApi(ClientConfig config) {
        super(config, "lazada");
    }
    
    public ItemGetResponse getItem(String numIid, String nation, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (nation != null) params.put("nation", nation);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItem(String numIid, String nation) throws IOException {
        return getItem(numIid, nation, null);
    }
    
    public ItemGetResponse getItem(String numIid) throws IOException {
        return getItem(numIid, null, null);
    }
    
    public Map<String, Object> search(String q, String nation, String page, String pageSize, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (nation != null) params.put("nation", nation);
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_search", params);
    }
    
    public Map<String, Object> search(String q, String nation) throws IOException {
        return search(q, nation, null, null, null);
    }
    
    public Map<String, Object> search(String q) throws IOException {
        return search(q, null, null, null, null);
    }
    
    public Map<String, Object> review(String numIid, String nation, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (nation != null) params.put("nation", nation);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_review", params);
    }
    
    public Map<String, Object> review(String numIid, String nation) throws IOException {
        return review(numIid, nation, null, null);
    }
    
    public Map<String, Object> password(String password, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_password", params);
    }
}