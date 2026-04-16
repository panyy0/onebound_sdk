package cn.onebound.sdk.domestic.ali1688.api;

import cn.onebound.sdk.domestic.ali1688.model.response.ItemGetResponse;
import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Ali1688ItemApi extends BaseApi {
    
    public Ali1688ItemApi(ClientConfig config) {
        super(config, "1688");
    }
    
    public ItemGetResponse getItem(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_get", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemPro(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_get_pro", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemApp(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_get_app", params, ItemGetResponse.class);
    }
}