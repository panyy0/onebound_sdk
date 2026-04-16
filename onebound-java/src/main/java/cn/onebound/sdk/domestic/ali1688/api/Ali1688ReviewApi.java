package cn.onebound.sdk.domestic.ali1688.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Ali1688ReviewApi extends BaseApi {
    
    public Ali1688ReviewApi(ClientConfig config) {
        super(config, "1688");
    }
    
    public BaseResponse getReviews(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_review", params, BaseResponse.class);
    }
}