package cn.onebound.sdk.domestic.taobao.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaobaoBuyerApi extends BaseApi {
    
    public TaobaoBuyerApi(ClientConfig config) {
        super(config, "taobao");
    }
    
    public BaseResponse cartAdd(String numIid, String skuId, String qty, String token, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (skuId != null) params.put("sku_id", skuId);
        params.put("qty", qty);
        params.put("token", token);
        if (extraParams != null) params.putAll(extraParams);
        return execute("buyer_cart_add", params, BaseResponse.class);
    }
    
    public BaseResponse cartAdd(String numIid, String qty, String token) throws IOException {
        return cartAdd(numIid, null, qty, token, null);
    }
    
    public BaseResponse cartRemove(String cartId, String token) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("cart_id", cartId);
        params.put("token", token);
        return execute("buyer_cart_remove", params, BaseResponse.class);
    }
    
    public BaseResponse cartList(String token, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        if (extraParams != null) params.putAll(extraParams);
        return execute("buyer_cart_list", params, BaseResponse.class);
    }
    
    public BaseResponse cartList(String token) throws IOException {
        return cartList(token, null);
    }
    
    public BaseResponse orderList(String token, String tabCode, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        if (tabCode != null) params.put("tab_code", tabCode);
        if (extraParams != null) params.putAll(extraParams);
        return execute("buyer_order_list", params, BaseResponse.class);
    }
    
    public BaseResponse orderList(String token) throws IOException {
        return orderList(token, null, null);
    }
    
    public BaseResponse orderDetail(String orderId, String token, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("token", token);
        if (extraParams != null) params.putAll(extraParams);
        return execute("buyer_order_detail", params, BaseResponse.class);
    }
    
    public BaseResponse orderDetail(String orderId, String token) throws IOException {
        return orderDetail(orderId, token, null);
    }
    
    public BaseResponse orderExpress(String orderId, String sellerId, String token) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("seller_id", sellerId);
        params.put("token", token);
        return execute("buyer_order_express", params, BaseResponse.class);
    }
    
    public BaseResponse addressList() throws IOException {
        Map<String, String> params = new HashMap<>();
        return execute("buyer_address_list", params, BaseResponse.class);
    }
    
    public BaseResponse addressAdd(String contactName, String province, String city, String country, String town, String addr, String phone, String mobilePhone, String token, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("contact_name", contactName);
        params.put("province", province);
        params.put("city", city);
        params.put("country", country);
        if (town != null) params.put("town", town);
        params.put("addr", addr);
        params.put("phone", phone);
        params.put("mobile_phone", mobilePhone);
        params.put("token", token);
        if (extraParams != null) params.putAll(extraParams);
        return execute("buyer_address_add", params, BaseResponse.class);
    }
    
    public BaseResponse info(String nick) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("nick", nick);
        return execute("buyer_info", params, BaseResponse.class);
    }
    
    public BaseResponse token(String nick, String session) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("nick", nick);
        params.put("session", session);
        return execute("buyer_token", params, BaseResponse.class);
    }
}