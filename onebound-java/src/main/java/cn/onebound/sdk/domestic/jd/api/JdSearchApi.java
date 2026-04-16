package cn.onebound.sdk.domestic.jd.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.domestic.jd.model.response.ItemGetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JdSearchApi extends BaseApi {
    
    public JdSearchApi(ClientConfig config) {
        super(config, "jd");
    }
    
    public ItemGetResponse search(String q, String startPrice, String endPrice, String page, String cat, String discountOnly, String sort, String sellerInfo) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (page != null) params.put("page", page);
        if (cat != null) params.put("cat", cat);
        if (discountOnly != null) params.put("discount_only", discountOnly);
        if (sort != null) params.put("sort", sort);
        if (sellerInfo != null) params.put("seller_info", sellerInfo);
        return execute("item_search", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse search(String q) throws IOException {
        return search(q, null, null, null, null, null, null, null);
    }
    
    public ItemGetResponse searchPro(String q, String startPrice, String endPrice, String page, String cat, String discountOnly, String sort, String sellerInfo) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (page != null) params.put("page", page);
        if (cat != null) params.put("cat", cat);
        if (discountOnly != null) params.put("discount_only", discountOnly);
        if (sort != null) params.put("sort", sort);
        if (sellerInfo != null) params.put("seller_info", sellerInfo);
        return execute("item_search_pro", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse searchPro(String q) throws IOException {
        return searchPro(q, null, null, null, null, null, null, null);
    }
    
    public ItemGetResponse searchImg(String imgid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("imgid", imgid);
        return execute("item_search_img", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse searchShop(String shopId, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (shopId != null) params.put("shop_id", shopId);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_search_shop", params, ItemGetResponse.class);
    }
    
    public ItemGetResponse searchShop(String shopId) throws IOException {
        return searchShop(shopId, null, null);
    }
    
    public ItemGetResponse searchSimilar(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_search_similar", params, ItemGetResponse.class);
    }
    
    public BaseResponse getRecommend(String type) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (type != null) params.put("type", type);
        return execute("item_recommend", params, BaseResponse.class);
    }
    
    public BaseResponse getRecommend() throws IOException {
        return getRecommend(null);
    }
    
    public BaseResponse getHistoryPrice(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_history_price", params, BaseResponse.class);
    }
}
