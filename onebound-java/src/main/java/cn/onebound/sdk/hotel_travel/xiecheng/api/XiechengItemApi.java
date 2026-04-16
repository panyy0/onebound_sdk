package cn.onebound.sdk.hotel_travel.xiecheng.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XiechengItemApi extends BaseApi {
    
    public XiechengItemApi(ClientConfig config) {
        super(config, "xiecheng");
    }
    
    public BaseResponse itemLocalCuisine(String areaId, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("area_id", areaId);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_local_cuisine", params, BaseResponse.class);
    }
    
    public BaseResponse itemLocalCuisine(String areaId, String page) throws IOException {
        return itemLocalCuisine(areaId, page, null);
    }
    
    public BaseResponse itemLocalCuisine(String areaId) throws IOException {
        return itemLocalCuisine(areaId, null, null);
    }
    
    public BaseResponse itemLocalRestaurant(String areaId, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("area_id", areaId);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_local_restaurant", params, BaseResponse.class);
    }
    
    public BaseResponse itemLocalRestaurant(String areaId, String page) throws IOException {
        return itemLocalRestaurant(areaId, page, null);
    }
    
    public BaseResponse itemLocalRestaurant(String areaId) throws IOException {
        return itemLocalRestaurant(areaId, null, null);
    }
    
    public BaseResponse itemSearchScenic(String areaId, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("area_id", areaId);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_scenic", params, BaseResponse.class);
    }
    
    public BaseResponse itemSearchScenic(String areaId, String page) throws IOException {
        return itemSearchScenic(areaId, page, null);
    }
    
    public BaseResponse itemSearchScenic(String areaId) throws IOException {
        return itemSearchScenic(areaId, null, null);
    }
    
    public BaseResponse itemGetScenic(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_scenic", params, BaseResponse.class);
    }
    
    public BaseResponse itemGetScenic(String numIid) throws IOException {
        return itemGetScenic(numIid, null);
    }
    
    public BaseResponse itemImgScenic(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_img_scenic", params, BaseResponse.class);
    }
    
    public BaseResponse itemImgScenic(String numIid) throws IOException {
        return itemImgScenic(numIid, null);
    }
    
    public BaseResponse itemReviewScenic(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_review_scenic", params, BaseResponse.class);
    }
    
    public BaseResponse itemReviewScenic(String numIid) throws IOException {
        return itemReviewScenic(numIid, null);
    }
    
    public BaseResponse itemSearchHotel(String q, String city, String page, String sort, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        if (city != null) params.put("city", city);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_hotel", params, BaseResponse.class);
    }
    
    public BaseResponse itemSearchHotel(String q, String city, String page) throws IOException {
        return itemSearchHotel(q, city, page, null, null);
    }
    
    public BaseResponse itemSearchHotel(String q, String city) throws IOException {
        return itemSearchHotel(q, city, null, null, null);
    }
    
    public BaseResponse itemSearchHotel(String q) throws IOException {
        return itemSearchHotel(q, null, null, null, null);
    }
    
    public BaseResponse itemGetApp(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_app", params, BaseResponse.class);
    }
    
    public BaseResponse itemGetApp(String numIid) throws IOException {
        return itemGetApp(numIid, null);
    }
}