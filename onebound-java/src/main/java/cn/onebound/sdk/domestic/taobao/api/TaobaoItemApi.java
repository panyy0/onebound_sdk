package cn.onebound.sdk.domestic.taobao.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.domestic.taobao.model.response.ItemGetResponse;
import cn.onebound.sdk.domestic.taobao.model.response.ItemSearchResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaobaoItemApi extends BaseApi {
    
    public TaobaoItemApi(ClientConfig config) {
        super(config, "taobao");
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
    
    public ItemGetResponse getItemPro(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_pro", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemPro(String numIid) throws IOException {
        return getItemPro(numIid, null);
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
    
    public ItemGetResponse getItemWeb(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_web", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemWeb(String numIid) throws IOException {
        return getItemWeb(numIid, null);
    }
    
    public ItemGetResponse getItemHis(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_his", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemHis(String numIid) throws IOException {
        return getItemHis(numIid, null);
    }
    
    public ItemGetResponse getItemVideo(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_video", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemVideo(String numIid) throws IOException {
        return getItemVideo(numIid, null);
    }
    
    public ItemGetResponse getItemDesc(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_desc", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemDesc(String numIid) throws IOException {
        return getItemDesc(numIid, null);
    }
    
    public BaseResponse getItemSku(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_sku", params, BaseResponse.class);
    }
    
    public BaseResponse getItemSku(String numIid) throws IOException {
        return getItemSku(numIid, null);
    }
}
