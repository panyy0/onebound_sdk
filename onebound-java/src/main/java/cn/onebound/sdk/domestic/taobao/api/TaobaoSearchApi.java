package cn.onebound.sdk.domestic.taobao.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.domestic.taobao.model.response.ItemSearchResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaobaoSearchApi extends BaseApi {
    
    public TaobaoSearchApi(ClientConfig config) {
        super(config, "taobao");
    }
    
    public ItemSearchResponse search(String q, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse search(String q) throws IOException {
        return search(q, null);
    }
    
    public ItemSearchResponse searchTmall(String q, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_tmall", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchTmall(String q) throws IOException {
        return searchTmall(q, null);
    }
    
    public ItemSearchResponse searchPro(String q, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_pro", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchPro(String q) throws IOException {
        return searchPro(q, null);
    }
    
    public ItemSearchResponse searchImg(String imgid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("imgid", imgid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_img", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchImg(String imgid) throws IOException {
        return searchImg(imgid, null);
    }
    
    public ItemSearchResponse searchShop(String shopId, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("shop_id", shopId);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_shop", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchShop(String shopId) throws IOException {
        return searchShop(shopId, null);
    }
    
    public ItemSearchResponse searchSeller(String q, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_seller", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchSeller(String q) throws IOException {
        return searchSeller(q, null);
    }
    
    public ItemSearchResponse searchGuang(String q, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_guang", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchGuang(String q) throws IOException {
        return searchGuang(q, null);
    }
    
    public ItemSearchResponse searchSuggest(String q, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_suggest", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchSuggest(String q) throws IOException {
        return searchSuggest(q, null);
    }
    
    public ItemSearchResponse searchJupage(String startPrice, String endPrice, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_jupage", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchJupage(String startPrice, String endPrice) throws IOException {
        return searchJupage(startPrice, endPrice, null, null);
    }
    
    public ItemSearchResponse searchCoupon(String coupon, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("coupon", coupon);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_coupon", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchCoupon(String coupon) throws IOException {
        return searchCoupon(coupon, null);
    }
    
    public ItemSearchResponse searchSameStyle(String sampleId, String numIid, String page, String sort, String pageSize) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (sampleId != null) params.put("sample_id", sampleId);
        if (numIid != null) params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        if (pageSize != null) params.put("page_size", pageSize);
        return execute("item_search_samestyle", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchSameStyle(String sampleId, String numIid) throws IOException {
        return searchSameStyle(sampleId, numIid, null, null, null);
    }
    
    public ItemSearchResponse searchSimilar(String sampleId, String numIid, String page, String sort, String pageSize) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (sampleId != null) params.put("sample_id", sampleId);
        if (numIid != null) params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        if (pageSize != null) params.put("page_size", pageSize);
        return execute("item_search_similar", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchSimilar(String sampleId, String numIid) throws IOException {
        return searchSimilar(sampleId, numIid, null, null, null);
    }
    
    public ItemSearchResponse searchNeighbors(String numIid, String sellerId) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (numIid != null) params.put("num_iid", numIid);
        if (sellerId != null) params.put("seller_id", sellerId);
        return execute("item_search_neighbors", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchNeighbors(String numIid) throws IOException {
        return searchNeighbors(numIid, null);
    }
    
    public ItemSearchResponse getRecommend(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_recommend", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse getRecommend(String numIid) throws IOException {
        return getRecommend(numIid, null);
    }
    
    public ItemSearchResponse searchBest(String cid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_best", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchBest(String cid) throws IOException {
        return searchBest(cid, null);
    }
}
