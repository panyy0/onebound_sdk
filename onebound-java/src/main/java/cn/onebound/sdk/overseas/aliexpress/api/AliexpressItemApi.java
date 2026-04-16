package cn.onebound.sdk.overseas.aliexpress.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.overseas.aliexpress.model.response.ItemSearchResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AliexpressItemApi extends BaseApi {
    
    public AliexpressItemApi(ClientConfig config) {
        super(config, "aliexpress");
    }
    
    public BaseResponse getItem(String numIid, String clang, String cCode) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (clang != null) params.put("clang", clang);
        if (cCode != null) params.put("c_code", cCode);
        return execute("item_get", params, BaseResponse.class);
    }
    
    public BaseResponse getItem(String numIid) throws IOException {
        return getItem(numIid, null, null);
    }
    
    public ItemSearchResponse search(String q, String cat, String startPrice, String endPrice, String sort, String page, String pageSize, String discountOnly, String sellerInfo) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (cat != null) params.put("cat", cat);
        if (startPrice != null) params.put("start_price", startPrice);
        if (endPrice != null) params.put("end_price", endPrice);
        if (sort != null) params.put("sort", sort);
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        if (discountOnly != null) params.put("discount_only", discountOnly);
        if (sellerInfo != null) params.put("seller_info", sellerInfo);
        return execute("item_search", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse search(String q) throws IOException {
        return search(q, null, null, null, null, null, null, null, null);
    }
    
    public ItemSearchResponse searchImg(String imgId, String page) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("imgid", imgId);
        if (page != null) params.put("page", page);
        return execute("item_search_img", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse searchImg(String imgId) throws IOException {
        return searchImg(imgId, null);
    }
    
    public BaseResponse getCat(String cid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);
        return execute("cat_get", params, BaseResponse.class);
    }
    
    public ItemSearchResponse catSearch(String q, String country, String currency, String lang, String page, String price, String freight, String nation, String sort, String type, String catId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (country != null) params.put("country", country);
        if (currency != null) params.put("currency", currency);
        if (lang != null) params.put("lang", lang);
        if (page != null) params.put("page", page);
        if (price != null) params.put("price", price);
        if (freight != null) params.put("freight", freight);
        if (nation != null) params.put("nation", nation);
        if (sort != null) params.put("sort", sort);
        if (type != null) params.put("type", type);
        if (catId != null) params.put("catid", catId);
        return execute("item_cat_search", params, ItemSearchResponse.class);
    }
    
    public ItemSearchResponse catSearch(String q) throws IOException {
        return catSearch(q, null, null, null, null, null, null, null, null, null, null);
    }
    
    public BaseResponse getReviews(String numIid, String country, String currency, String lang, String page, String sort) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (country != null) params.put("country", country);
        if (currency != null) params.put("currency", currency);
        if (lang != null) params.put("lang", lang);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        return execute("item_review", params, BaseResponse.class);
    }
    
    public BaseResponse getReviews(String numIid) throws IOException {
        return getReviews(numIid, null, null, null, null, null);
    }
    
    public BaseResponse getQuestionAnswerInfo(String numIid, String country, String currency, String lang, String page) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (country != null) params.put("country", country);
        if (currency != null) params.put("currency", currency);
        if (lang != null) params.put("lang", lang);
        if (page != null) params.put("page", page);
        return execute("item_question_answer_info", params, BaseResponse.class);
    }
    
    public BaseResponse getQuestionAnswerInfo(String numIid) throws IOException {
        return getQuestionAnswerInfo(numIid, null, null, null, null);
    }
}