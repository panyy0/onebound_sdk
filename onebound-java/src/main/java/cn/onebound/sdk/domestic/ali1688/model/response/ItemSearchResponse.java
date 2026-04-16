package cn.onebound.sdk.domestic.ali1688.model.response;

import cn.onebound.sdk.core.model.response.BaseResponse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ItemSearchResponse extends BaseResponse {
    
    @SerializedName("items")
    private List<SearchItem> items;
    
    @SerializedName("total_results")
    private String totalResults;
    
    @SerializedName("page")
    private String page;
    
    @SerializedName("page_size")
    private String pageSize;
    
    public List<SearchItem> getItems() { return items; }
    public String getTotalResults() { return totalResults; }
    public String getPage() { return page; }
    public String getPageSize() { return pageSize; }
    
    public static class SearchItem {
        @SerializedName("num_iid")
        private String numIid;
        
        @SerializedName("title")
        private String title;
        
        @SerializedName("price")
        private String price;
        
        @SerializedName("pic_url")
        private String picUrl;
        
        @SerializedName("detail_url")
        private String detailUrl;
        
        @SerializedName("shop_name")
        private String shopName;
        
        public String getNumIid() { return numIid; }
        public String getTitle() { return title; }
        public String getPrice() { return price; }
        public String getPicUrl() { return picUrl; }
        public String getDetailUrl() { return detailUrl; }
        public String getShopName() { return shopName; }
    }
}