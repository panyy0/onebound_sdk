package cn.onebound.sdk.domestic.taobao.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaobaoSellerOpApi extends BaseApi {
    
    public TaobaoSellerOpApi(ClientConfig config) {
        super(config, "taobao");
    }
    
    public BaseResponse orderList(String token, String tabCode, String dateBegin, String dateEnd, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        if (tabCode != null) params.put("tabCode", tabCode);
        if (dateBegin != null) params.put("dateBegin", dateBegin);
        if (dateEnd != null) params.put("dateEnd", dateEnd);
        if (extraParams != null) params.putAll(extraParams);
        return execute("seller_order_list", params, BaseResponse.class);
    }
    
    public BaseResponse orderList(String token) throws IOException {
        return orderList(token, null, null, null, null);
    }
    
    public BaseResponse catProps(String token, String catId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("catid", catId);
        return execute("seller_cat_props", params, BaseResponse.class);
    }
    
    public BaseResponse productAdd(Map<String, String> params) throws IOException {
        params.put("sid", ""); // seller id - needs to be provided
        return execute("product_add", params, BaseResponse.class);
    }
}