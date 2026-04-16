package cn.onebound.sdk.domestic.micro.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.domestic.micro.model.response.ItemSearchResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MicroItemApi extends BaseApi {
    
    public MicroItemApi(ClientConfig config) {
        super(config, "micro");
    }
    
    public ItemSearchResponse search(String q, String page) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        return execute("item_search", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse search(String q) throws IOException {
        return search(q, null);
    }
    
    public ItemSearchResponse searchShop(String userId, String page, String sort, String sortOrder) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("userid", userId);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        if (sortOrder != null) params.put("sort_order", sortOrder);
        return execute("item_search_shop", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchShop(String userId) throws IOException {
        return searchShop(userId, null, null, null);
    }
    
    public BaseResponse getItem(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_get", params, BaseResponse.class);
    }
    
    public BaseResponse getFee(String numIid, String areaId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (areaId != null) params.put("area_id", areaId);
        return execute("item_fee", params, BaseResponse.class);
    }
    
    public BaseResponse getFee(String numIid) throws IOException {
        return getFee(numIid, null);
    }
}