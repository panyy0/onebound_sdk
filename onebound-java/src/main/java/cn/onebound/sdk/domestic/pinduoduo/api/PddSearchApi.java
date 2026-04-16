package cn.onebound.sdk.domestic.pinduoduo.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.domestic.pinduoduo.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PddSearchApi extends BaseApi {
    
    public PddSearchApi(ClientConfig config) {
        super(config, "pinduoduo");
    }
    
    public ItemGetResponse search(String q, String sort, String page, String pageSize) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (sort != null) params.put("sort", sort);
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        return execute("item_search", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse search(String q) throws IOException {
        return search(q, null, null, null);
    }
    
    public BaseResponse searchData(String keyword) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        return execute("item_search_data", params, BaseResponse.class);
    }
    
    public BaseResponse searchSuggest(String q) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        return execute("item_search_suggest", params, BaseResponse.class);
    }
    
    public BaseResponse catGet(String cid) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (cid != null) params.put("cid", cid);
        return execute("cat_get", params, BaseResponse.class);
    }
    
    public BaseResponse syncAdd(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_sync_add", params, BaseResponse.class);
    }
    
    public BaseResponse syncGet(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_sync_get", params, BaseResponse.class);
    }
    
    public BaseResponse custom(String apiName, Map<String, String> params) throws IOException {
        if (params == null) params = new HashMap<>();
        return execute(apiName, params, BaseResponse.class);
    }
}
