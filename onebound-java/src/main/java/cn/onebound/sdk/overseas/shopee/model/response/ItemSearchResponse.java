package cn.onebound.sdk.overseas.shopee.model.response;

import cn.onebound.sdk.core.model.response.BaseResponse;
import java.util.List;

public class ItemSearchResponse extends BaseResponse {
    
    private List<SearchItem> items;
    private Integer totalResults;
    private Integer page;
    private Integer pageSize;
    
    public List<SearchItem> getItems() { return items; }
    public void setItems(List<SearchItem> items) { this.items = items; }
    public Integer getTotalResults() { return totalResults; }
    public void setTotalResults(Integer totalResults) { this.totalResults = totalResults; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    
    public static class SearchItem {
        private String numIid;
        private String title;
        private String price;
        private String picUrl;
        private String detailUrl;
        private String shopName;
        
        public String getNumIid() { return numIid; }
        public void setNumIid(String numIid) { this.numIid = numIid; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }
        public String getPicUrl() { return picUrl; }
        public void setPicUrl(String picUrl) { this.picUrl = picUrl; }
        public String getDetailUrl() { return detailUrl; }
        public void setDetailUrl(String detailUrl) { this.detailUrl = detailUrl; }
        public String getShopName() { return shopName; }
        public void setShopName(String shopName) { this.shopName = shopName; }
    }
}