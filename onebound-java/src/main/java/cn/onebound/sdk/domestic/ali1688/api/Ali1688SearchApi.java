package cn.onebound.sdk.domestic.ali1688.api;

import cn.onebound.sdk.domestic.ali1688.model.response.ItemSearchResponse;
import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Ali1688SearchApi extends BaseApi {
    
    public Ali1688SearchApi(ClientConfig config) {
        super(config, "1688");
    }
    
    public ItemSearchResponse search(String q, String page, String sort, String startPrice, String endPrice) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        return execute("item_search", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse search(String q) throws IOException {
        return search(q, null, null, null, null);
    }
    
    public ItemSearchResponse searchImg(String q, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_search_img", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchImg(String q) throws IOException {
        return searchImg(q, null, null);
    }
    
    public ItemSearchResponse searchSuggest(String q) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        return execute("item_search_suggest", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchShop(String shopId, String nick, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (shopId != null) params.put("shop_id", shopId);
        if (nick != null) params.put("nick", nick);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_search_shop", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchShop(String shopId) throws IOException {
        return searchShop(shopId, null, null, null);
    }
    
    public ItemSearchResponse searchSeller(String q, String page) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        return execute("item_search_seller", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchSeller(String q) throws IOException {
        return searchSeller(q, null);
    }
    
    public ItemSearchResponse searchBest(String q, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_search_best", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchBest(String q) throws IOException {
        return searchBest(q, null, null);
    }
    
    public ItemSearchResponse searchFactory(String q, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_search_factory", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchFactory(String q) throws IOException {
        return searchFactory(q, null, null);
    }
    
    public ItemSearchResponse searchNew(String q, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_search_new", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchNew(String q) throws IOException {
        return searchNew(q, null, null);
    }
    
    public ItemSearchResponse searchPeerBuy(String numIid, String sid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (sid != null) params.put("sid", sid);
        return execute("item_search_peerbuy", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchPeerBuy(String numIid) throws IOException {
        return searchPeerBuy(numIid, null);
    }
}