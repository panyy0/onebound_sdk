package cn.onebound.sdk.domestic.taobao.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import cn.onebound.sdk.domestic.taobao.model.response.SellerInfoResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaobaoSellerApi extends BaseApi {
    
    public TaobaoSellerApi(ClientConfig config) {
        super(config, "taobao");
    }
    
    public SellerInfoResponse info(String shopId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("shop_id", shopId);
        return execute("seller_info", params, SellerInfoResponse.class);
    }
    
    public BaseResponse listWeight(String numIids, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iids", numIids);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_list_weight", params, BaseResponse.class);
    }
    
    public BaseResponse listWeight(String numIids) throws IOException {
        return listWeight(numIids, null);
    }
    
    public BaseResponse historyPrice(String numIid, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_history_price", params, BaseResponse.class);
    }
    
    public BaseResponse historyPrice(String numIid) throws IOException {
        return historyPrice(numIid, null);
    }
}