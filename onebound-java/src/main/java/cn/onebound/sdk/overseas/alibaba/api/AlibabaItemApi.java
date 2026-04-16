package cn.onebound.sdk.overseas.alibaba.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.overseas.alibaba.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AlibabaItemApi extends BaseApi {
    
    public AlibabaItemApi(ClientConfig config) {
        super(config, "alibaba");
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
    
    public Map<String, Object> search(String q, String cat, String startPrice, String endPrice, String sort, String page, String pageSize, String sellerInfo, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (cat != null) params.put("cat", cat);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (sort != null) params.put("sort", sort);
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        if (sellerInfo != null) params.put("seller_info", sellerInfo);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_search", params);
    }
    
    public Map<String, Object> search(String q) throws IOException {
        return search(q, null, null, null, null, null, null, null, null);
    }
    
    public Map<String, Object> searchPopular(Map<String, String> extraParams) throws IOException {
        return executeRaw("item_search_popular", extraParams != null ? extraParams : new HashMap<>());
    }
    
    public Map<String, Object> searchPopular() throws IOException {
        return searchPopular(null);
    }
    
    public Map<String, Object> searchBuyTogether(Map<String, String> extraParams) throws IOException {
        return executeRaw("item_search_buytogether", extraParams != null ? extraParams : new HashMap<>());
    }
    
    public Map<String, Object> searchBuyTogether() throws IOException {
        return searchBuyTogether(null);
    }
    
    public Map<String, Object> transactionHistory(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("transaction_history", params);
    }
    
    public Map<String, Object> transactionHistory(String numIid) throws IOException {
        return transactionHistory(numIid, null);
    }
    
    public Map<String, Object> searchShop(String shopId, String page, String pageSize, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("shop_id", shopId);
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_search_shop", params);
    }
    
    public Map<String, Object> searchShop(String shopId) throws IOException {
        return searchShop(shopId, null, null, null);
    }
    
    public Map<String, Object> searchImg(String imgId, String page, String pageSize, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("imgid", imgId);
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_search_img", params);
    }
    
    public Map<String, Object> searchImg(String imgId) throws IOException {
        return searchImg(imgId, null, null, null);
    }
    
    public Map<String, Object> review(String numIid, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return executeRaw("item_review", params);
    }
    
    public Map<String, Object> review(String numIid) throws IOException {
        return review(numIid, null, null);
    }
}