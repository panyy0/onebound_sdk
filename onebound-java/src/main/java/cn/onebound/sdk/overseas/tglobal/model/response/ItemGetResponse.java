package cn.onebound.sdk.overseas.tglobal.model.response;

import cn.onebound.sdk.core.model.response.BaseResponse;
import java.util.List;
import java.util.Map;

public class ItemGetResponse extends BaseResponse {
    
    private Map<String, Object> item;
    
    public Map<String, Object> getItem() { return item; }
    public void setItem(Map<String, Object> item) { this.item = item; }
}