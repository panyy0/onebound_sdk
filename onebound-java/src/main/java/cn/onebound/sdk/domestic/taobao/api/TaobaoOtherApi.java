package cn.onebound.sdk.domestic.taobao.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaobaoOtherApi extends BaseApi {
    
    public TaobaoOtherApi(ClientConfig config) {
        super(config, "taobao");
    }
    
    public BaseResponse categoryList(String cid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);
        return execute("cat_get", params, BaseResponse.class);
    }
    
    public BaseResponse itemCatList(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_cat_get", params, BaseResponse.class);
    }
    
    public BaseResponse brandCatList() throws IOException {
        Map<String, String> params = new HashMap<>();
        return execute("brand_cat", params, BaseResponse.class);
    }
    
    public BaseResponse brandCatTop() throws IOException {
        Map<String, String> params = new HashMap<>();
        return execute("brand_cat_top", params, BaseResponse.class);
    }
    
    public BaseResponse uploadImg(String imgCode, String imgType) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("imgcode", imgCode);
        params.put("img_type", imgType);
        return execute("upload_img", params, BaseResponse.class);
    }
    
    public BaseResponse img2Text(String imgCode) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("imgcode", imgCode);
        return execute("img2text", params, BaseResponse.class);
    }
    
    public BaseResponse fee(String numIid, String areaId, String sku) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (areaId != null) params.put("area_id", areaId);
        if (sku != null) params.put("sku", sku);
        return execute("item_fee", params, BaseResponse.class);
    }
    
    public BaseResponse getSales(String numIid, String monthly) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (monthly != null) params.put("monthly", monthly);
        return execute("item_get_sales", params, BaseResponse.class);
    }
    
    public BaseResponse password(String word, String title) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("word", word);
        if (title != null) params.put("title", title);
        return execute("item_password", params, BaseResponse.class);
    }
    
    public BaseResponse syncAdd(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_sync_add", params, BaseResponse.class);
    }
    
    public BaseResponse syncGet(String numIid) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        return execute("item_sync_get", params, BaseResponse.class);
    }
    
    public BaseResponse custom(Map<String, String> params) throws IOException {
        return execute("custom", params, BaseResponse.class);
    }
}