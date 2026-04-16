package cn.onebound.sdk.overseas.shopee.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.overseas.shopee.model.response.ItemSearchResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShopeeItemApi extends BaseApi {
    
    public ShopeeItemApi(ClientConfig config) {
        super(config, "shopee");
    }
    
    public ItemSearchResponse search(String q, String page, String sort, String startPrice, String endPrice, String pageSize) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (pageSize != null) params.put("page_size", pageSize);
        return execute("item_search", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse search(String q) throws IOException {
        return search(q, null, null, null, null, null);
    }
    
    public BaseResponse getItem(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_get", params, BaseResponse.class);
    }
    
    public BaseResponse getItemApp(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_get_app", params, BaseResponse.class);
    }
    
    public ItemSearchResponse searchShop(String shopId, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("shopid", shopId);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_search_shop", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchShop(String shopId) throws IOException {
        return searchShop(shopId, null, null);
    }
    
    public BaseResponse getSellerInfo(String shopId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("shopid", shopId);
        return execute("seller_info", params, BaseResponse.class);
    }
    
    public BaseResponse getReviews(String numIid, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_review", params, BaseResponse.class);
    }
    
    public BaseResponse getReviews(String numIid) throws IOException {
        return getReviews(numIid, null, null);
    }
}