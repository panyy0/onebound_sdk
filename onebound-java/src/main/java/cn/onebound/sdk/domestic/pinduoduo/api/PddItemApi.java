package cn.onebound.sdk.domestic.pinduoduo.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.domestic.pinduoduo.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PddItemApi extends BaseApi {
    
    public PddItemApi(ClientConfig config) {
        super(config, "pinduoduo");
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
    
    public ItemGetResponse getItemAppPro(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_app_pro", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemAppPro(String numIid) throws IOException {
        return getItemAppPro(numIid, null);
    }
    
    public ItemGetResponse getItemEasy(String goodsToken) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("goods_token", goodsToken);
        return execute("item_get_easy", params, ItemGetResponse.class);
    }
}
