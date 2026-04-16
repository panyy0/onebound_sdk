package cn.onebound.sdk.domestic.taobao.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaobaoReviewApi extends BaseApi {
    
    public TaobaoReviewApi(ClientConfig config) {
        super(config, "taobao");
    }
    
    public BaseResponse list(String numIid, String sort, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (sort != null) params.put("sort", sort);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_review", params, BaseResponse.class);
    }
    
    public BaseResponse list(String numIid) throws IOException {
        return list(numIid, null, null, null);
    }
    
    public BaseResponse listPro(String numIid, String sort, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (sort != null) params.put("sort", sort);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_review", params, BaseResponse.class);
    }
    
    public BaseResponse show(String numIid, String uuid, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (uuid != null) params.put("uuid", uuid);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_review_show", params, BaseResponse.class);
    }
    
    public BaseResponse show(String numIid, String uuid) throws IOException {
        return show(numIid, uuid, null, null);
    }
    
    public BaseResponse questionAnswer(String numIid, String page, Map<String, String> extraParams) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        if (extraParams != null) params.putAll(extraParams);
        return execute("item_question_answer", params, BaseResponse.class);
    }
    
    public BaseResponse questionAnswer(String numIid) throws IOException {
        return questionAnswer(numIid, null, null);
    }
}