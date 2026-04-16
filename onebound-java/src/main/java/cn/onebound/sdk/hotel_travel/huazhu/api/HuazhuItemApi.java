package cn.onebound.sdk.hotel_travel.huazhu.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HuazhuItemApi extends BaseApi {
    
    public HuazhuItemApi(ClientConfig config) {
        super(config, "huazhu");
    }
    
    public BaseResponse itemSearchApp(String cityId, String page, String checkin, String checkout, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (cityId != null) params.put("cityid", cityId);
        if (page != null) params.put("page", page);
        if (checkin != null) params.put("checkin", checkin);
        if (checkout != null) params.put("checkout", checkout);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_app", params, BaseResponse.class);
    }
    
    public BaseResponse itemSearchApp(String cityId, String page, String checkin, String checkout) throws IOException {
        return itemSearchApp(cityId, page, checkin, checkout, null);
    }
    
    public BaseResponse itemSearchApp(String cityId) throws IOException {
        return itemSearchApp(cityId, null, null, null, null);
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
    
    public BaseResponse itemArea(String cityName, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("cityname", cityName);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_area", params, BaseResponse.class);
    }
    
    public BaseResponse itemArea(String cityName) throws IOException {
        return itemArea(cityName, null);
    }
}