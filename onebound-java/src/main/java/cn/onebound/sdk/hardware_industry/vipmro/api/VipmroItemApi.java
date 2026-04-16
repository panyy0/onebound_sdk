package cn.onebound.sdk.hardware_industry.vipmro.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VipmroItemApi extends BaseApi {
    
    public VipmroItemApi(ClientConfig config) {
        super(config, "vipmro");
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
}