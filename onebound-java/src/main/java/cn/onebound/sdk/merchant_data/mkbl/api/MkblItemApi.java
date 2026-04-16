package cn.onebound.sdk.merchant_data.mkbl.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MkblItemApi extends BaseApi {
    
    public MkblItemApi(ClientConfig config) {
        super(config, "mkbl");
    }
    
    public BaseResponse itemGet(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get", params, BaseResponse.class);
    }
    
    public BaseResponse itemGet(String numIid) throws IOException {
        return itemGet(numIid, null);
    }
    
    public BaseResponse itemSearch(String q, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (q != null) params.put("q", q);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search", params, BaseResponse.class);
    }
    
    public BaseResponse itemSearch(String q) throws IOException {
        return itemSearch(q, null, null);
    }
}