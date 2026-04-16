package cn.onebound.sdk.domestic.jd.model.response;

import cn.onebound.sdk.core.model.response.BaseResponse;
import com.google.gson.JsonObject;

public class ItemGetResponse extends BaseResponse {
    
    private ItemData item;
    
    @Override
    public void parse(JsonObject json) {
        super.parse(json);
    }
    
    public ItemData getItem() { return item; }
    
    public static class ItemData {
        private String numIid;
        private String title;
        private String price;
        private String originalPrice;
        private String picUrl;
        private String detailUrl;
        
        public void parse(JsonObject json) {
            if (json.has("num_iid")) this.numIid = getString(json, "num_iid");
            if (json.has("title")) this.title = getString(json, "title");
            if (json.has("price")) this.price = getString(json, "price");
            if (json.has("original_price")) this.originalPrice = getString(json, "original_price");
            if (json.has("pic_url")) this.picUrl = getString(json, "pic_url");
            if (json.has("detail_url")) this.detailUrl = getString(json, "detail_url");
        }
        
        private String getString(JsonObject json, String key) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : null;
        }
        
        public String getNumIid() { return numIid; }
        public String getTitle() { return title; }
        public String getPrice() { return price; }
        public String getOriginalPrice() { return originalPrice; }
        public String getPicUrl() { return picUrl; }
        public String getDetailUrl() { return detailUrl; }
    }
}
