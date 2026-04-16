package cn.onebound.sdk.overseas.tglobal.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.overseas.tglobal.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TglobalItemApi extends BaseApi {
    
    public TglobalItemApi(ClientConfig config) {
        super(config, "tglobal");
    }
    
    public ItemGetResponse getItem(String numIid, String accessToken, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (accessToken != null) params.put("access_token", accessToken);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse getItem(String numIid, String accessToken) throws IOException {
        return getItem(numIid, accessToken, null);
    }
    
    public ItemGetResponse getItem(String numIid) throws IOException {
        return getItem(numIid, null, null);
    }
    
    public Map<String, Object> search(String q, String lang, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (lang != null) params.put("lang", lang);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_search", params);
    }
    
    public Map<String, Object> search(String q) throws IOException {
        return search(q, null, null);
    }
    
    public Map<String, Object> searchImg(String imgid, String lang, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("imgid", imgid);
        if (lang != null) params.put("lang", lang);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_search_img", params);
    }
    
    public Map<String, Object> searchImg(String imgid) throws IOException {
        return searchImg(imgid, null, null);
    }
    
    public Map<String, Object> uploadImg(String image, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("image", image);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("upload_img", params);
    }
    
    public Map<String, Object> custom(String apiName, Map<String, String> params) throws IOException {
        return executeRaw(apiName, params);
    }
}