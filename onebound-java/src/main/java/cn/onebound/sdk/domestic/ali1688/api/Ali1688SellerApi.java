package cn.onebound.sdk.domestic.ali1688.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Ali1688SellerApi extends BaseApi {
    
    public Ali1688SellerApi(ClientConfig config) {
        super(config, "1688");
    }
    
    public BaseResponse getSellerInfo(String sellerId, String nick) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (sellerId != null) params.put("seller_id", sellerId);
        if (nick != null) params.put("nick", nick);
        return execute("seller_info", params, BaseResponse.class);
    }
    
    public BaseResponse getSellerInfo(String sellerId) throws IOException {
        return getSellerInfo(sellerId, null);
    }
    
    public BaseResponse getCompany(String companyId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("company_id", companyId);
        return execute("item_get_company", params, BaseResponse.class);
    }
    
    public BaseResponse getFactory(String factoryId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("factory_id", factoryId);
        return execute("item_get_factory", params, BaseResponse.class);
    }
    
    public BaseResponse getStrength(String strengthId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("strength_id", strengthId);
        return execute("item_get_strength", params, BaseResponse.class);
    }
    
    public BaseResponse getCategory(String cid) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (cid != null) params.put("cid", cid);
        return execute("cat_get", params, BaseResponse.class);
    }
    
    public BaseResponse getItemCategory(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_cat_get", params, BaseResponse.class);
    }
}