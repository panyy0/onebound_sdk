package cn.onebound.sdk.domestic.jd.api;

import cn.onebound.sdk.core.api.BaseApi;
import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.core.model.response.BaseResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JdOtherApi extends BaseApi {
    
    public JdOtherApi(ClientConfig config) {
        super(config, "jd");
    }
    
    public BaseResponse getCat(String cid, String grade) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (cid != null) params.put("cid", cid);
        if (grade != null) params.put("grade", grade);
        return execute("cat_get", params, BaseResponse.class);
    }
    
    public BaseResponse getCat(String cid) throws IOException {
        return getCat(cid, null);
    }
    
    public BaseResponse getItemCat(String cid, String grade) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (cid != null) params.put("cid", cid);
        if (grade != null) params.put("grade", grade);
        return execute("item_cat_get", params, BaseResponse.class);
    }
    
    public BaseResponse getItemCat(String cid) throws IOException {
        return getItemCat(cid, null);
    }
    
    public BaseResponse getReviews(String numIid, String page, String sort, String isSku, String rawData) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        if (isSku != null) params.put("is_sku", isSku);
        if (rawData != null) params.put("raw_data", rawData);
        return execute("item_review", params, BaseResponse.class);
    }
    
    public BaseResponse getReviews(String numIid) throws IOException {
        return getReviews(numIid, null, null, null, null);
    }
    
    public BaseResponse getReviewsApp(String numIid, String page, String sort, String isSku, String rawData) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        if (sort != null) params.put("sort", sort);
        if (isSku != null) params.put("is_sku", isSku);
        if (rawData != null) params.put("raw_data", rawData);
        return execute("item_review_app", params, BaseResponse.class);
    }
    
    public BaseResponse getReviewsApp(String numIid) throws IOException {
        return getReviewsApp(numIid, null, null, null, null);
    }
    
    public BaseResponse getQuestionAnswer(String numIid, String page) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("num_iid", numIid);
        if (page != null) params.put("page", page);
        return execute("item_question_answer", params, BaseResponse.class);
    }
    
    public BaseResponse getQuestionAnswer(String numIid) throws IOException {
        return getQuestionAnswer(numIid, null);
    }
    
    public BaseResponse getQuestionAnswerInfo(String clusterId, String page) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (clusterId != null) params.put("cluster_id", clusterId);
        if (page != null) params.put("page", page);
        return execute("item_question_answer_info", params, BaseResponse.class);
    }
    
    public BaseResponse getQuestionAnswerInfo(String clusterId) throws IOException {
        return getQuestionAnswerInfo(clusterId, null);
    }
    
    public BaseResponse getPassword(String word) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("word", word);
        return execute("item_password", params, BaseResponse.class);
    }
    
    public BaseResponse uploadImage(String imgCode) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("imgcode", imgCode);
        return execute("upload_img", params, BaseResponse.class);
    }
    
    public BaseResponse custom(String apiName, Map<String, String> params) throws IOException {
        if (params == null) params = new HashMap<>();
        return execute(apiName, params, BaseResponse.class);
    }
}
