package cn.onebound.sdk.domestic.ali1688.model.response;

import cn.onebound.sdk.core.model.response.BaseResponse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ItemGetResponse extends BaseResponse {
    
    @SerializedName("item")
    private ItemData item;
    
    @SerializedName("company_info")
    private CompanyInfo companyInfo;
    
    public ItemData getItem() { return item; }
    public CompanyInfo getCompanyInfo() { return companyInfo; }
    
    public static class ItemData {
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
        
        @SerializedName("cid")
        private String cid;
        
        @SerializedName("freight")
        private String freight;
        
        @SerializedName("express")
        private String express;
        
        public String getNumIid() { return numIid; }
        public String getTitle() { return title; }
        public String getPrice() { return price; }
        public String getPicUrl() { return picUrl; }
        public String getDetailUrl() { return detailUrl; }
        public String getCid() { return cid; }
        public String getFreight() { return freight; }
        public String getExpress() { return express; }
    }
    
    public static class CompanyInfo {
        @SerializedName("company_id")
        private String companyId;
        
        @SerializedName("company_name")
        private String companyName;
        
        @SerializedName("type")
        private String type;
        
        @SerializedName("established_date")
        private String establishedDate;
        
        @SerializedName("capital")
        private String capital;
        
        @SerializedName("address")
        private String address;
        
        public String getCompanyId() { return companyId; }
        public String getCompanyName() { return companyName; }
        public String getType() { return type; }
        public String getEstablishedDate() { return establishedDate; }
        public String getCapital() { return capital; }
        public String getAddress() { return address; }
    }
}