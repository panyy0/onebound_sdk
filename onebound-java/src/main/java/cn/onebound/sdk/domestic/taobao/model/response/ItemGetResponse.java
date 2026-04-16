package cn.onebound.sdk.domestic.taobao.model.response;

import cn.onebound.sdk.core.model.response.BaseResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class ItemGetResponse extends BaseResponse {
    
    private ItemData item;
    
    @Override
    public void parse(JsonObject json) {
        super.parse(json);
        if (json.has("item") && !json.get("item").isJsonNull()) {
            item = new ItemData();
            item.parse(json.getAsJsonObject("item"));
        }
    }
    
    public ItemData getItem() { return item; }
    
    public static class ItemData {
        private String numIid;
        private String title;
        private String price;
        private String originalPrice;
        private String num;
        private String detailUrl;
        private String picUrl;
        private List<String> images;
        private String cid;
        private String props;
        private String propsName;
        private String freight;
        private String ems;
        private String express;
        private String freightTemplate;
        private String video;
        private SellerInfo seller;
        
        public void parse(JsonObject json) {
            if (json.has("num_iid")) this.numIid = getString(json, "num_iid");
            if (json.has("title")) this.title = getString(json, "title");
            if (json.has("price")) this.price = getString(json, "price");
            if (json.has("original_price")) this.originalPrice = getString(json, "original_price");
            if (json.has("num")) this.num = getString(json, "num");
            if (json.has("detail_url")) this.detailUrl = getString(json, "detail_url");
            if (json.has("pic_url")) this.picUrl = getString(json, "pic_url");
            if (json.has("images") && json.get("images").isJsonArray()) {
                this.images = new ArrayList<>();
                for (JsonElement el : json.getAsJsonArray("images")) {
                    this.images.add(el.getAsString());
                }
            }
            if (json.has("cid")) this.cid = getString(json, "cid");
            if (json.has("props")) this.props = getString(json, "props");
            if (json.has("props_name")) this.propsName = getString(json, "props_name");
            if (json.has("freight")) this.freight = getString(json, "freight");
            if (json.has("ems")) this.ems = getString(json, "ems");
            if (json.has("express")) this.express = getString(json, "express");
            if (json.has("freight_template")) this.freightTemplate = getString(json, "freight_template");
            if (json.has("video")) this.video = getString(json, "video");
            if (json.has("seller") && !json.get("seller").isJsonNull()) {
                this.seller = new SellerInfo();
                this.seller.parse(json.getAsJsonObject("seller"));
            }
        }
        
        private String getString(JsonObject json, String key) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : null;
        }
        
        public String getNumIid() { return numIid; }
        public String getTitle() { return title; }
        public String getPrice() { return price; }
        public String getOriginalPrice() { return originalPrice; }
        public String getNum() { return num; }
        public String getDetailUrl() { return detailUrl; }
        public String getPicUrl() { return picUrl; }
        public List<String> getImages() { return images; }
        public String getCid() { return cid; }
        public String getProps() { return props; }
        public String getPropsName() { return propsName; }
        public String getFreight() { return freight; }
        public String getEms() { return ems; }
        public String getExpress() { return express; }
        public String getFreightTemplate() { return freightTemplate; }
        public String getVideo() { return video; }
        public SellerInfo getSeller() { return seller; }
    }
    
    public static class SellerInfo {
        private String nick;
        private String shopId;
        private String shopName;
        private String shopUrl;
        private String sid;
        private String score;
        private String rateSum;
        private String deliveryCredit;
        private String serviceCredit;
        private String itemScore;
        
        public void parse(JsonObject json) {
            if (json.has("nick")) this.nick = getString(json, "nick");
            if (json.has("shop_id")) this.shopId = getString(json, "shop_id");
            if (json.has("shop_name")) this.shopName = getString(json, "shop_name");
            if (json.has("shop_url")) this.shopUrl = getString(json, "shop_url");
            if (json.has("sid")) this.sid = getString(json, "sid");
            if (json.has("score")) this.score = getString(json, "score");
            if (json.has("rate_sum")) this.rateSum = getString(json, "rate_sum");
            if (json.has("delivery_credit")) this.deliveryCredit = getString(json, "delivery_credit");
            if (json.has("service_credit")) this.serviceCredit = getString(json, "service_credit");
            if (json.has("item_score")) this.itemScore = getString(json, "item_score");
        }
        
        private String getString(JsonObject json, String key) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : null;
        }
        
        public String getNick() { return nick; }
        public String getShopId() { return shopId; }
        public String getShopName() { return shopName; }
        public String getShopUrl() { return shopUrl; }
        public String getSid() { return sid; }
        public String getScore() { return score; }
        public String getRateSum() { return rateSum; }
        public String getDeliveryCredit() { return deliveryCredit; }
        public String getServiceCredit() { return serviceCredit; }
        public String getItemScore() { return itemScore; }
    }
}
