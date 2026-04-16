package cn.onebound.sdk.domestic.ali1688.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Ali1688OtherApi extends BaseApi {
    
    public Ali1688OtherApi(ClientConfig config) {
        super(config, "1688");
    }
    
    public BaseResponse getItemFee(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_fee", params, BaseResponse.class);
    }
    
    public BaseResponse getPasswordRealUrl(String taobaoPassword) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("taobao_password", taobaoPassword);
        return execute("item_password", params, BaseResponse.class);
    }
    
    public BaseResponse uploadImage(String image) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("image", image);
        return execute("upload_img", params, BaseResponse.class);
    }
    
    public BaseResponse img2Text(String img) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("img", img);
        return execute("img2text", params, BaseResponse.class);
    }
    
    public BaseResponse custom(String apiName, Map<String, String> params) throws IOException {
        return execute(apiName, params, BaseResponse.class);
    }
}