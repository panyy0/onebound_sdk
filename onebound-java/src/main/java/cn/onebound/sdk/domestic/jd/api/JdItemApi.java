package cn.onebound.sdk.domestic.jd.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.domestic.jd.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JdItemApi extends BaseApi {
    
    public JdItemApi(ClientConfig config) {
        super(config, "jd");
    }
    
    public ItemGetResponse getItem(String numIid, String domainType, String watermark) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (domainType != null) params.put("domain_type", domainType);
        if (watermark != null) params.put("watermark", watermark);
        return execute("item_get", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItem(String numIid) throws IOException {
        return getItem(numIid, null, null);
    }
    
    public ItemGetResponse getItemPro(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_get_pro", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemApp(String numIid, String domainType) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (domainType != null) params.put("domain_type", domainType);
        return execute("item_get_app", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItemApp(String numIid) throws IOException {
        return getItemApp(numIid, null);
    }
    
    public ItemGetResponse getVideo(String numIid, String videoId, String domainType) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (videoId != null) params.put("video_id", videoId);
        if (domainType != null) params.put("domain_type", domainType);
        return execute("item_video", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getVideo(String numIid) throws IOException {
        return getVideo(numIid, null, null);
    }
    
    public ItemGetResponse getSku(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_sku", params, ItemGetResponse.class);
    }
    
    public BaseResponse getPrice(String skuId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("sku_id", skuId);
        return execute("item_price", params, BaseResponse.class);
    }
    
    public BaseResponse getDesc(String numIid, String mainSkuId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (mainSkuId != null) params.put("main_sku_id", mainSkuId);
        return execute("item_get_desc", params, BaseResponse.class);
    }
    
    public BaseResponse getDesc(String numIid) throws IOException {
        return getDesc(numIid, null);
    }
}
