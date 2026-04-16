package cn.onebound.sdk.domestic.taobao.model.response;

import cn.onebound.sdk.core.model.response.BaseResponse;
import com.google.gson.JsonObject;

public class SellerInfoResponse extends BaseResponse {
    
    private String shopId;
    private String sellerId;
    private String nick;
    private String shopName;
    private String shopUrl;
    private String gradeUrl;
    private String logoUrl;
    
    @Override
    public void parse(JsonObject json) {
        super.parse(json);
        if (json.has("user")) {
            JsonObject user = json.getAsJsonObject("user");
            if (user.has("shop_id")) this.shopId = user.get("shop_id").getAsString();
            if (user.has("seller_id")) this.sellerId = user.get("seller_id").getAsString();
            if (user.has("nick")) this.nick = user.get("nick").getAsString();
            if (user.has("shop_name")) this.shopName = user.get("shop_name").getAsString();
            if (user.has("shop_url")) this.shopUrl = user.get("shop_url").getAsString();
            if (user.has("grade_url")) this.gradeUrl = user.get("grade_url").getAsString();
            if (user.has("logo_url")) this.logoUrl = user.get("logo_url").getAsString();
        }
    }
    
    public String getShopId() { return shopId; }
    public String getSellerId() { return sellerId; }
    public String getNick() { return nick; }
    public String getShopName() { return shopName; }
    public String getShopUrl() { return shopUrl; }
    public String getGradeUrl() { return gradeUrl; }
    public String getLogoUrl() { return logoUrl; }
}