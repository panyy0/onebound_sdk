package cn.onebound.sdk.online_bookstore.cnbook.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CnbookItemApi extends BaseApi {
    
    public CnbookItemApi(ClientConfig config) {
        super(config, "cnbook");
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