package cn.onebound.sdk.vertical.alimama.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AlimamaItemApi extends BaseApi {
    
    public AlimamaItemApi(ClientConfig config) {
        super(config, "alimama");
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
    
    public BaseResponse itemSearch(String q, String cat, String startPrice, String endPrice, String sort, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (q != null) params.put("q", q);
        if (cat != null) params.put("cat", cat);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (sort != null) params.put("sort", sort);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search", params, BaseResponse.class);
    }
    
    public BaseResponse itemSearch(String q, String page) throws IOException {
        return itemSearch(q, null, null, null, null, page, null);
    }
    
    public BaseResponse itemSearch(String q) throws IOException {
        return itemSearch(q, null, null, null, null, null, null);
    }
    
    public BaseResponse sellerInfo(String nick, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("nick", nick);
        if (extraParams != null) params.putAll(extraParams);
        return execute("seller_info", params, BaseResponse.class);
    }
    
    public BaseResponse sellerInfo(String nick) throws IOException {
        return sellerInfo(nick, null);
    }
    
    public BaseResponse itemLink(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_link", params, BaseResponse.class);
    }
    
    public BaseResponse itemLink(String numIid) throws IOException {
        return itemLink(numIid, null);
    }
    
    public BaseResponse itemId(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_id", params, BaseResponse.class);
    }
    
    public BaseResponse itemId(String numIid) throws IOException {
        return itemId(numIid, null);
    }
    
    public BaseResponse itemSearchPro(String q, String cat, String startPrice, String endPrice, String sort, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (q != null) params.put("q", q);
        if (cat != null) params.put("cat", cat);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (sort != null) params.put("sort", sort);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_pro", params, BaseResponse.class);
    }
    
    public BaseResponse itemSearchPro(String q, String page) throws IOException {
        return itemSearchPro(q, null, null, null, null, page, null);
    }
    
    public BaseResponse itemSearchPro(String q) throws IOException {
        return itemSearchPro(q, null, null, null, null, null, null);
    }
}