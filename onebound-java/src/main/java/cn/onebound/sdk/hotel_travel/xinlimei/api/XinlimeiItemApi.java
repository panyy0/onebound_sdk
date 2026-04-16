package cn.onebound.sdk.hotel_travel.xinlimei.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XinlimeiItemApi extends BaseApi {
    
    public XinlimeiItemApi(ClientConfig config) {
        super(config, "xinlimei");
    }
    
    public BaseResponse itemGetApp(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (numIid != null) params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_get_app", params, BaseResponse.class);
    }
    
    public BaseResponse itemGetApp(String numIid) throws IOException {
        return itemGetApp(numIid, null);
    }
    
    public BaseResponse itemSearchApp(String cityId, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (cityId != null) params.put("cityid", cityId);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_search_app", params, BaseResponse.class);
    }
    
    public BaseResponse itemSearchApp(String cityId, String page) throws IOException {
        return itemSearchApp(cityId, page, null);
    }
    
    public BaseResponse itemSearchApp(String cityId) throws IOException {
        return itemSearchApp(cityId, null, null);
    }
    
    public BaseResponse itemArea(Map<String, String> extraParams) throws IOException {
        return execute("item_area", extraParams != null ? extraParams : new HashMap<>(), BaseResponse.class);
    }
    
    public BaseResponse itemArea() throws IOException {
        return itemArea(null);
    }
}