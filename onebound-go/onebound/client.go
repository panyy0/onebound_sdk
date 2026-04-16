package onebound

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"strings"
)

var apiURLs = []string{
	"https://api-gw.onebound.cn",
	"https://api-1.onebound.cn",
	"https://api-2.onebound.cn",
	"https://api-3.onebound.cn",
	"https://api-4.onebound.cn",
}

const (
	DefaultCache      = "yes"
	DefaultResultType = "json"
	DefaultLang       = "cn"
	DefaultTimeout    = 30
	DefaultRetryCount = 3
)

const (
	ErrorNeedLogin1 = 4013
	ErrorNeedLogin2 = 4016
	LoginURL        = "http://console.openbound.cn/console/?i=c-46989"
)

type Client struct {
	platform   string
	key        string
	secret     string
	apiURL     string
	cache      string
	resultType string
	lang       string
	version    string
	httpClient *http.Client
	urlIndex   int
	retryCount int
}

func NewClient(platform, key, secret string) *Client {
	if platform != "taobao" && platform != "1688" && platform != "jd" && platform != "pinduoduo" && platform != "micro" && platform != "shopee" && platform != "aliexpress" && platform != "temu" && platform != "tglobal" && platform != "lazada" && platform != "amazon" && platform != "ebay" && platform != "kgcg" && platform != "alibaba" && platform != "walgreens" && platform != "mic" && platform != "shop" && platform != "target" && platform != "wildberries" && platform != "daraz" && platform != "mercado" && platform != "dmm" && platform != "walmart" && platform != "jumia" && platform != "ozon" && platform != "xiecheng" && platform != "damai" && platform != "ftx" && platform != "youxia" && platform != "huazhu" && platform != "dongcheng" && platform != "xinlimei" && platform != "klook" && platform != "vip" && platform != "mogujie" && platform != "vancl" && platform != "alimama" && platform != "jumei" && platform != "hc360" && platform != "huobutou" && platform != "k3" && platform != "souhaohuo" && platform != "taobaoke" && platform != "kaola" && platform != "csg" && platform != "yhd" && platform != "ymatou" && platform != "smzdm" && platform != "yqzwd" && platform != "ylw" && platform != "fzw" && platform != "yiwugo" && platform != "vvic" && platform != "suning" && platform != "dewu" && platform != "xmz" && platform != "shihuo" && platform != "qx" && platform != "deli" && platform != "colipu" && platform != "vipmro" && platform != "jiancaiwang" && platform != "huagw" && platform != "ickey" && platform != "zkw" && platform != "china" && platform != "zggk" && platform != "schneider" && platform != "dp123" && platform != "misumi" && platform != "jmweb" && platform != "pbweb" && platform != "txxw" && platform != "zhw" && platform != "xjb" && platform != "weixin" && platform != "smallredbook" && platform != "hksp" && platform != "shortvideo" && platform != "ks" && platform != "bili" && platform != "tiktok" && platform != "youtube" && platform != "ahs" && platform != "goodfish" && platform != "wuziw" && platform != "zhuanzhuan" && platform != "sqw" && platform != "tyc" && platform != "aqc" && platform != "dangdang" && platform != "kfz" && platform != "cnbook" && platform != "jxsggzy" && platform != "zblh" && platform != "cgyzb" && platform != "bbw" && platform != "hbw" && platform != "czw" && platform != "zxw" && platform != "ftx" && platform != "anjuke" && platform != "wbtc" && platform != "anjiago" && platform != "beike" && platform != "yhby" && platform != "yidaba" && platform != "ebdoor" && platform != "mkbl" && platform != "zqw" && platform != "007swz" && platform != "sole" && platform != "zhaosw" && platform != "b2b" && platform != "jmw" && platform != "jingcheng" {
		panic("Unsupported platform: " + platform + ". Supported platforms include: taobao, 1688, jd, pinduoduo, micro, shopee, aliexpress, temu, and many more.")
	}
	return &Client{
		platform:   platform,
		key:        key,
		secret:     secret,
		apiURL:     apiURLs[0],
		cache:      DefaultCache,
		resultType: DefaultResultType,
		lang:       DefaultLang,
		httpClient: &http.Client{},
		urlIndex:   0,
		retryCount: DefaultRetryCount,
	}
}

func (c *Client) buildURL(apiName string, params map[string]string) string {
	values := url.Values{}
	values.Set("key", c.key)
	values.Set("secret", c.secret)
	values.Set("api_name", apiName)
	values.Set("cache", c.cache)
	values.Set("result_type", c.resultType)
	values.Set("lang", c.lang)
	if c.version != "" {
		values.Set("version", c.version)
	}

	for k, v := range params {
		if v != "" {
			values.Set(k, v)
		}
	}

	baseURL := strings.TrimSuffix(c.apiURL, "/")
	return fmt.Sprintf("%s/%s/%s/?%s", baseURL, c.platform, apiName, values.Encode())
}

func (c *Client) rotateURL() {
	c.urlIndex = (c.urlIndex + 1) % len(apiURLs)
	c.apiURL = apiURLs[c.urlIndex]
}

func (c *Client) checkLoginError(result map[string]interface{}) error {
	var errorCode int
	if v, ok := result["error_code"].(float64); ok {
		errorCode = int(v)
	} else if v, ok := result["code"].(float64); ok {
		errorCode = int(v)
	}
	if errorCode == ErrorNeedLogin1 || errorCode == ErrorNeedLogin2 {
		return fmt.Errorf("API Error %d: Please login at %s to register or recharge your account", errorCode, LoginURL)
	}
	return nil
}

func (c *Client) request(apiName string, params map[string]string) (map[string]interface{}, error) {
	var lastErr error

	for attempt := 0; attempt < c.retryCount; attempt++ {
		url := c.buildURL(apiName, params)

		req, err := http.NewRequest("GET", url, nil)
		if err != nil {
			return nil, err
		}
		req.Header.Set("Accept-Encoding", "gzip")

		resp, err := c.httpClient.Do(req)
		if err != nil {
			lastErr = err
			if attempt < c.retryCount-1 {
				c.rotateURL()
			}
			continue
		}
		defer resp.Body.Close()

		body, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			lastErr = err
			if attempt < c.retryCount-1 {
				c.rotateURL()
			}
			continue
		}

		var result map[string]interface{}
		if err := json.Unmarshal(body, &result); err != nil {
			lastErr = err
			continue
		}

		if err := c.checkLoginError(result); err != nil {
			return nil, err
		}

		return result, nil
	}

	return nil, fmt.Errorf("API request failed after %d attempts: %v", c.retryCount, lastErr)
}

func mergeParams(base, extra map[string]string) map[string]string {
	for k, v := range extra {
		if v != "" {
			base[k] = v
		}
	}
	return base
}

func paramsWith(p map[string]string, key, value string) map[string]string {
	if value != "" {
		p[key] = value
	}
	return p
}

// ==================== Item APIs ====================

func (c *Client) ItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemGetPro(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_pro", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemGetWeb(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_web", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemGetHis(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_his", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemVideo(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_video", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemGetDesc(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_desc", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemSku(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_sku", paramsWith(extraParams, "num_iid", numIid))
}

// ==================== Search APIs ====================

func (c *Client) ItemSearch(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", paramsWith(extraParams, "q", q))
}

func (c *Client) ItemSearchTmall(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_tmall", paramsWith(extraParams, "q", q))
}

func (c *Client) ItemSearchPro(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_pro", paramsWith(extraParams, "q", q))
}

func (c *Client) ItemSearchImg(imgid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_img", paramsWith(extraParams, "imgid", imgid))
}

func (c *Client) ItemSearchShop(shopId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_shop", paramsWith(extraParams, "shop_id", shopId))
}

func (c *Client) ItemSearchSeller(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_seller", paramsWith(extraParams, "q", q))
}

func (c *Client) ItemSearchGuang(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_guang", paramsWith(extraParams, "q", q))
}

func (c *Client) ItemSearchSuggest(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_suggest", paramsWith(extraParams, "q", q))
}

func (c *Client) ItemSearchJupage(startPrice, endPrice, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "start_price", startPrice)
	p = paramsWith(p, "end_price", endPrice)
	p = paramsWith(p, "page", page)
	return c.request("item_search_jupage", p)
}

func (c *Client) ItemSearchCoupon(coupon string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_coupon", paramsWith(extraParams, "coupon", coupon))
}

func (c *Client) ItemSearchSameStyle(sampleId, numIid, page, sort, pageSize string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "sample_id", sampleId)
	p = paramsWith(p, "num_iid", numIid)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "sort", sort)
	p = paramsWith(p, "page_size", pageSize)
	return c.request("item_search_samestyle", p)
}

func (c *Client) ItemSearchSimilar(sampleId, numIid, page, sort, pageSize string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "sample_id", sampleId)
	p = paramsWith(p, "num_iid", numIid)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "sort", sort)
	p = paramsWith(p, "page_size", pageSize)
	return c.request("item_search_similar", p)
}

func (c *Client) ItemSearchNeighbors(numIid, sellerId string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	p = paramsWith(p, "seller_id", sellerId)
	return c.request("item_search_neighbors", p)
}

func (c *Client) ItemRecommend(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_recommend", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemSearchBest(cid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_best", paramsWith(extraParams, "cid", cid))
}

// ==================== 1688 Specific Search ====================

func (c *Client) ItemSearchFactory(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_factory", paramsWith(extraParams, "q", q))
}

func (c *Client) ItemSearchNew(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_new", paramsWith(extraParams, "q", q))
}

func (c *Client) ItemSearchPeerBuy(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_peerbuy", paramsWith(extraParams, "num_iid", numIid))
}

// ==================== Seller APIs ====================

func (c *Client) SellerInfo(shopId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("seller_info", paramsWith(extraParams, "shop_id", shopId))
}

func (c *Client) CatGet(cid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("cat_get", paramsWith(extraParams, "cid", cid))
}

func (c *Client) ItemCatGet(cid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_cat_get", paramsWith(extraParams, "cid", cid))
}

func (c *Client) BrandCat(parentId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("brand_cat", paramsWith(extraParams, "parent_id", parentId))
}

func (c *Client) BrandCatTop(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("brand_cat_top", extraParams)
}

func (c *Client) BrandCatList(catId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("brand_cat_list", paramsWith(extraParams, "cat_id", catId))
}

func (c *Client) BrandKeywordList(keyword string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("brand_keyword_list", paramsWith(extraParams, "keyword", keyword))
}

func (c *Client) BrandInfo(brandId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("brand_info", paramsWith(extraParams, "brand_id", brandId))
}

func (c *Client) BrandProductList(brandId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("brand_product_list", paramsWith(extraParams, "brand_id", brandId))
}

// ==================== 1688 Company APIs ====================

func (c *Client) ItemGetCompany(companyId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_company", paramsWith(extraParams, "company_id", companyId))
}

func (c *Client) ItemGetFactory(factoryId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_factory", paramsWith(extraParams, "factory_id", factoryId))
}

func (c *Client) ItemGetStrength(strengthId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_strength", paramsWith(extraParams, "strength_id", strengthId))
}

// ==================== Review APIs ====================

func (c *Client) ItemReview(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_review", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemReviewShow(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_review_show", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemQuestionAnswer(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_question_answer", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemHistoryPrice(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_history_price", paramsWith(extraParams, "num_iid", numIid))
}

// ==================== Buyer APIs (Taobao only) ====================

func (c *Client) BuyerCartAdd(itemId, quantity string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "item_id", itemId)
	p = paramsWith(p, "quantity", quantity)
	return c.request("buyer_cart_add", p)
}

func (c *Client) BuyerCartRemove(itemId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_cart_remove", paramsWith(extraParams, "item_id", itemId))
}

func (c *Client) BuyerCartClear(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_cart_clear", extraParams)
}

func (c *Client) BuyerCartList(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_cart_list", extraParams)
}

func (c *Client) BuyerCartOrder(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_cart_order", extraParams)
}

func (c *Client) BuyerOrderList(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_order_list", extraParams)
}

func (c *Client) BuyerOrderDetail(orderId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_order_detail", paramsWith(extraParams, "order_id", orderId))
}

func (c *Client) BuyerOrderExpress(orderId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_order_express", paramsWith(extraParams, "order_id", orderId))
}

func (c *Client) BuyerOrderMessage(orderId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_order_message", paramsWith(extraParams, "order_id", orderId))
}

func (c *Client) BuyerAddressList(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_address_list", extraParams)
}

func (c *Client) BuyerAddressAdd(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_address_add", extraParams)
}

func (c *Client) BuyerAddressRemove(addressId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_address_remove", paramsWith(extraParams, "address_id", addressId))
}

func (c *Client) BuyerAddressModify(addressId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_address_modify", paramsWith(extraParams, "address_id", addressId))
}

func (c *Client) BuyerAddressClear(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_address_clear", extraParams)
}

func (c *Client) BuyerInfo(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("buyer_info", extraParams)
}

func (c *Client) BuyerToken(nick, session string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "nick", nick)
	p = paramsWith(p, "session", session)
	return c.request("buyer_token", p)
}

// ==================== Seller Operation APIs (Taobao only) ====================

func (c *Client) SellerOrderList(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("seller_order_list", extraParams)
}

func (c *Client) SellerOrderDetail(orderId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("seller_order_detail", paramsWith(extraParams, "order_id", orderId))
}

func (c *Client) SellerOrderClose(orderId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("seller_order_close", paramsWith(extraParams, "order_id", orderId))
}

func (c *Client) SellerOrderMessage(orderId, remark string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "order_id", orderId)
	p = paramsWith(p, "remark", remark)
	return c.request("seller_order_message", p)
}

func (c *Client) SellerAuctionList(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("seller_auction_list", extraParams)
}

func (c *Client) SellerAuction(itemId, status string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "item_id", itemId)
	p = paramsWith(p, "status", status)
	return c.request("seller_auction", p)
}

func (c *Client) SellerItemAdd(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("seller_item_add", extraParams)
}

func (c *Client) SellerCatProps(token, catId string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "token", token)
	p = paramsWith(p, "catid", catId)
	return c.request("seller_cat_props", p)
}

func (c *Client) ProductAdd(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("product_add", extraParams)
}

// ==================== Other APIs ====================

func (c *Client) ItemFee(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_fee", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemPassword(password string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_password", paramsWith(extraParams, "password", password))
}

func (c *Client) ItemGetSales(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_sales", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ItemListWeight(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_list_weight", extraParams)
}

func (c *Client) ItemSyncAdd(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_sync_add", extraParams)
}

func (c *Client) ItemSyncGet(syncId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_sync_get", paramsWith(extraParams, "sync_id", syncId))
}

func (c *Client) UploadImg(image string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("upload_img", paramsWith(extraParams, "image", image))
}

func (c *Client) Img2Text(img string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("img2text", paramsWith(extraParams, "img", img))
}

func (c *Client) TbkOrderQuery(tbkParams string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("tbk_order_query", paramsWith(extraParams, "tbk_params", tbkParams))
}

func (c *Client) Custom(apiName string, params map[string]string) (map[string]interface{}, error) {
	return c.request(apiName, params)
}

// ==================== JD Specific APIs ====================

func (c *Client) JdItemGetPro(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_pro", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemVideo(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_video", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemSku(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_sku", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemPrice(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_price", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemCatGet(cid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_cat_get", paramsWith(extraParams, "cid", cid))
}

func (c *Client) JdItemGetDesc(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_desc", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemSearchPro(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_pro", paramsWith(extraParams, "q", q))
}

func (c *Client) JdItemHistoryPrice(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_history_price", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemRecommend(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_recommend", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemPassword(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_password", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemReviewApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_review_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemQuestionAnswer(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_question_answer", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JdItemQuestionAnswerInfo(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_question_answer_info", paramsWith(extraParams, "num_iid", numIid))
}

// ==================== Pinduoduo APIs ====================

func (c *Client) PddItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) PddItemSearch(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", paramsWith(extraParams, "q", q))
}

func (c *Client) PddItemSearchData(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_data", paramsWith(extraParams, "q", q))
}

func (c *Client) PddItemSearchSuggest(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_suggest", paramsWith(extraParams, "q", q))
}

func (c *Client) PddItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) PddItemGetAppPro(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app_pro", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) PddItemGetEasy(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_easy", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) PddCatGet(cid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("cat_get", paramsWith(extraParams, "cid", cid))
}

func (c *Client) PddCustom(apiName string, params map[string]string) (map[string]interface{}, error) {
	return c.request(apiName, params)
}

// ==================== Micro APIs ====================

func (c *Client) MicroItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	p = paramsWith(p, "page", page)
	return c.request("item_search", p)
}

func (c *Client) MicroItemSearchShop(userId, page, sort, sortOrder string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "userid", userId)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "sort", sort)
	p = paramsWith(p, "sort_order", sortOrder)
	return c.request("item_search_shop", p)
}

func (c *Client) MicroItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) MicroItemFee(numIid, areaId string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	p = paramsWith(p, "area_id", areaId)
	return c.request("item_fee", p)
}

// ==================== Shopee APIs ====================

func (c *Client) ShopeeItemSearch(q, page, sort, startPrice, endPrice, pageSize string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "sort", sort)
	p = paramsWith(p, "start_price", startPrice)
	p = paramsWith(p, "end_price", endPrice)
	p = paramsWith(p, "page_size", pageSize)
	return c.request("item_search", p)
}

func (c *Client) ShopeeItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ShopeeItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ShopeeItemSearchShop(shopId, page, sort string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "shopid", shopId)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "sort", sort)
	return c.request("item_search_shop", p)
}

func (c *Client) ShopeeSellerInfo(shopId string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("seller_info", paramsWith(extraParams, "shopid", shopId))
}

func (c *Client) ShopeeItemReview(numIid, page, sort string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "sort", sort)
	return c.request("item_review", p)
}

// ==================== Aliexpress APIs ====================

func (c *Client) AliexpressItemGet(numIid, clang, cCode string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	p = paramsWith(p, "clang", clang)
	p = paramsWith(p, "c_code", cCode)
	return c.request("item_get", p)
}

func (c *Client) AliexpressItemSearch(q, cat, startPrice, endPrice, sort, page, pageSize, discountOnly, sellerInfo string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	p = paramsWith(p, "cat", cat)
	p = paramsWith(p, "start_price", startPrice)
	p = paramsWith(p, "end_price", endPrice)
	p = paramsWith(p, "sort", sort)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "page_size", pageSize)
	p = paramsWith(p, "discount_only", discountOnly)
	p = paramsWith(p, "seller_info", sellerInfo)
	return c.request("item_search", p)
}

func (c *Client) AliexpressItemSearchImg(imgId, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "imgid", imgId)
	p = paramsWith(p, "page", page)
	return c.request("item_search_img", p)
}

func (c *Client) AliexpressCatGet(cid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("cat_get", paramsWith(extraParams, "cid", cid))
}

func (c *Client) AliexpressItemCatSearch(q, country, currency, lang, page, price, freight, nation, sort, typ, catId string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	p = paramsWith(p, "country", country)
	p = paramsWith(p, "currency", currency)
	p = paramsWith(p, "lang", lang)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "price", price)
	p = paramsWith(p, "freight", freight)
	p = paramsWith(p, "nation", nation)
	p = paramsWith(p, "sort", sort)
	p = paramsWith(p, "type", typ)
	p = paramsWith(p, "catid", catId)
	return c.request("item_cat_search", p)
}

func (c *Client) AliexpressItemReview(numIid, country, currency, lang, page, sort string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	p = paramsWith(p, "country", country)
	p = paramsWith(p, "currency", currency)
	p = paramsWith(p, "lang", lang)
	p = paramsWith(p, "page", page)
	p = paramsWith(p, "sort", sort)
	return c.request("item_review", p)
}

func (c *Client) AliexpressItemQuestionAnswerInfo(numIid, country, currency, lang, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	p = paramsWith(p, "country", country)
	p = paramsWith(p, "currency", currency)
	p = paramsWith(p, "lang", lang)
	p = paramsWith(p, "page", page)
	return c.request("item_question_answer_info", p)
}

// ==================== Temu APIs ====================

func (c *Client) TemuItemSearch(q string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", paramsWith(extraParams, "q", q))
}

// ==================== Tglobal APIs ====================

func (c *Client) TglobalItemGet(numIid, accessToken string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	if accessToken != "" {
		p = paramsWith(p, "access_token", accessToken)
	}
	return c.request("item_get", p)
}

func (c *Client) TglobalItemSearch(q, lang string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	if lang != "" {
		p = paramsWith(p, "lang", lang)
	}
	return c.request("item_search", p)
}

func (c *Client) TglobalItemSearchImg(imgId, lang string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "imgid", imgId)
	if lang != "" {
		p = paramsWith(p, "lang", lang)
	}
	return c.request("item_search_img", p)
}

func (c *Client) TglobalUploadImg(image string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("upload_img", paramsWith(extraParams, "image", image))
}

func (c *Client) TglobalCustom(apiName string, params map[string]string) (map[string]interface{}, error) {
	return c.request(apiName, params)
}

// ==================== Lazada APIs ====================

func (c *Client) LazadaItemGet(numIid, nation string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	if nation != "" {
		p = paramsWith(p, "nation", nation)
	}
	return c.request("item_get", p)
}

func (c *Client) LazadaItemSearch(q, nation, page, pageSize string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	if nation != "" {
		p = paramsWith(p, "nation", nation)
	}
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	if pageSize != "" {
		p = paramsWith(p, "page_size", pageSize)
	}
	return c.request("item_search", p)
}

func (c *Client) LazadaItemReview(numIid, nation, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	if nation != "" {
		p = paramsWith(p, "nation", nation)
	}
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	return c.request("item_review", p)
}

func (c *Client) LazadaItemPassword(password string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_password", paramsWith(extraParams, "password", password))
}

// ==================== Amazon APIs ====================

func (c *Client) AmazonItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AmazonItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AmazonItemSearch(q, cat, startPrice, endPrice, sort, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	if cat != "" {
		p = paramsWith(p, "cat", cat)
	}
	if startPrice != "" {
		p = paramsWith(p, "start_price", startPrice)
	}
	if endPrice != "" {
		p = paramsWith(p, "end_price", endPrice)
	}
	if sort != "" {
		p = paramsWith(p, "sort", sort)
	}
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	return c.request("item_search", p)
}

func (c *Client) AmazonItemReview(numIid, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	return c.request("item_review", p)
}

// ==================== Ebay APIs ====================

func (c *Client) EbayItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) EbayItemSearch(q, cat, startPrice, endPrice, sort, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	if cat != "" {
		p = paramsWith(p, "cat", cat)
	}
	if startPrice != "" {
		p = paramsWith(p, "start_price", startPrice)
	}
	if endPrice != "" {
		p = paramsWith(p, "end_price", endPrice)
	}
	if sort != "" {
		p = paramsWith(p, "sort", sort)
	}
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	return c.request("item_search", p)
}

// ==================== Kgcg APIs ====================

func (c *Client) KgcgItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) KgcgItemSearch(q, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	return c.request("item_search", p)
}

// ==================== Alibaba APIs ====================

func (c *Client) AlibabaItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AlibabaItemSearch(q, cat, startPrice, endPrice, sort, page, pageSize, sellerInfo string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	if cat != "" {
		p = paramsWith(p, "cat", cat)
	}
	if startPrice != "" {
		p = paramsWith(p, "start_price", startPrice)
	}
	if endPrice != "" {
		p = paramsWith(p, "end_price", endPrice)
	}
	if sort != "" {
		p = paramsWith(p, "sort", sort)
	}
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	if pageSize != "" {
		p = paramsWith(p, "page_size", pageSize)
	}
	if sellerInfo != "" {
		p = paramsWith(p, "seller_info", sellerInfo)
	}
	return c.request("item_search", p)
}

func (c *Client) AlibabaItemSearchPopular(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_popular", extraParams)
}

func (c *Client) AlibabaItemSearchBuyTogether(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search_buytogether", extraParams)
}

func (c *Client) AlibabaTransactionHistory(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("transaction_history", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AlibabaItemSearchShop(shopId, page, pageSize string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "shop_id", shopId)
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	if pageSize != "" {
		p = paramsWith(p, "page_size", pageSize)
	}
	return c.request("item_search_shop", p)
}

func (c *Client) AlibabaItemSearchImg(imgId, page, pageSize string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "imgid", imgId)
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	if pageSize != "" {
		p = paramsWith(p, "page_size", pageSize)
	}
	return c.request("item_search_img", p)
}

func (c *Client) AlibabaItemReview(numIid, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	return c.request("item_review", p)
}

// ==================== Walgreens APIs ====================

func (c *Client) WalgreensItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) WalgreensItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

// ==================== Mic APIs ====================

func (c *Client) MicItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) MicItemSearch(q, page string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "q", q)
	if page != "" {
		p = paramsWith(p, "page", page)
	}
	return c.request("item_search", p)
}

// ==================== Shop APIs ====================

func (c *Client) ShopItemGet(numIid, skuId string, extraParams map[string]string) (map[string]interface{}, error) {
	p := paramsWith(extraParams, "num_iid", numIid)
	if skuId != "" {
		p = paramsWith(p, "sku_id", skuId)
	}
	return c.request("item_get", p)
}

func (c *Client) ShopItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

func (c *Client) ShopItemArea(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_area", extraParams)
}

// ==================== Target APIs ====================

func (c *Client) TargetItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) TargetItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

// ==================== Wildberries APIs ====================

func (c *Client) WildberriesItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) WildberriesItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

// ==================== Daraz APIs ====================

func (c *Client) DarazItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) DarazItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

func (c *Client) DarazCatGet(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("cat_get", extraParams)
}

// ==================== Mercado APIs ====================

func (c *Client) MercadoItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) MercadoItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

// ==================== Dmm APIs ====================

func (c *Client) DmmItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) DmmItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

// ==================== Walmart APIs ====================

func (c *Client) WalmartItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) WalmartItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

func (c *Client) WalmartCatGet(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("cat_get", extraParams)
}

// ==================== Jumia APIs ====================

func (c *Client) JumiaItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JumiaItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

// ==================== Ozon APIs ====================

func (c *Client) OzonItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) OzonItemSearch(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_search", extraParams)
}

// ==================== Xiecheng (Hotel/Travel) APIs ====================

func (c *Client) XiechengItemLocalCuisine(areaId string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if areaId != "" {
		params["area_id"] = areaId
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_local_cuisine", params)
}

func (c *Client) XiechengItemLocalRestaurant(areaId string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if areaId != "" {
		params["area_id"] = areaId
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_local_restaurant", params)
}

func (c *Client) XiechengItemSearchScenic(areaId string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if areaId != "" {
		params["area_id"] = areaId
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search_scenic", params)
}

func (c *Client) XiechengItemGetScenic(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_scenic", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) XiechengItemImgScenic(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_img_scenic", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) XiechengItemReviewScenic(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_review_scenic", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) XiechengItemSearchHotel(q string, city string, page string, sort string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if city != "" {
		params["city"] = city
	}
	if page != "" {
		params["page"] = page
	}
	if sort != "" {
		params["sort"] = sort
	}
	return c.request("item_search_hotel", params)
}

func (c *Client) XiechengItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

// ==================== Damai (Hotel/Travel) APIs ====================

func (c *Client) DamaiItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) DamaiItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Ftx (Hotel/Travel) APIs ====================

func (c *Client) FtxItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) FtxItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Youxia (Hotel/Travel) APIs ====================

func (c *Client) YouxiaItemSearchApp(cityId string, page string, checkin string, checkout string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if cityId != "" {
		params["cityid"] = cityId
	}
	if page != "" {
		params["page"] = page
	}
	if checkin != "" {
		params["checkin"] = checkin
	}
	if checkout != "" {
		params["checkout"] = checkout
	}
	return c.request("item_search_app", params)
}

func (c *Client) YouxiaItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YouxiaItemArea(cityName string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_area", paramsWith(extraParams, "cityname", cityName))
}

// ==================== Huazhu (Hotel/Travel) APIs ====================

func (c *Client) HuazhuItemSearchApp(cityId string, page string, checkin string, checkout string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if cityId != "" {
		params["cityid"] = cityId
	}
	if page != "" {
		params["page"] = page
	}
	if checkin != "" {
		params["checkin"] = checkin
	}
	if checkout != "" {
		params["checkout"] = checkout
	}
	return c.request("item_search_app", params)
}

func (c *Client) HuazhuItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) HuazhuItemArea(cityName string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_area", paramsWith(extraParams, "cityname", cityName))
}

// ==================== Dongcheng (Hotel/Travel) APIs ====================

func (c *Client) DongchengItemGetApp(itemId string, checkin string, checkout string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if itemId != "" {
		params["item_id"] = itemId
	}
	if checkin != "" {
		params["checkin"] = checkin
	}
	if checkout != "" {
		params["checkout"] = checkout
	}
	return c.request("item_get_app", params)
}

func (c *Client) DongchengItemSearchApp(cityId string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if cityId != "" {
		params["cityid"] = cityId
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search_app", params)
}

func (c *Client) DongchengItemArea(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_area", extraParams)
}

// ==================== Xinlimei (Hotel/Travel) APIs ====================

func (c *Client) XinlimeiItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) XinlimeiItemSearchApp(cityId string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if cityId != "" {
		params["cityid"] = cityId
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search_app", params)
}

func (c *Client) XinlimeiItemArea(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_area", extraParams)
}

// ==================== Klook (Hotel/Travel) APIs ====================

func (c *Client) KlookItemGetApp(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get_app", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) KlookItemSearchApp(cityId string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if cityId != "" {
		params["cityid"] = cityId
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search_app", params)
}

func (c *Client) KlookItemArea(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_area", extraParams)
}

// ==================== Vip (Vertical E-commerce) APIs ====================

func (c *Client) VipItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) VipItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

func (c *Client) VipCatGet(extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("cat_get", extraParams)
}

func (c *Client) VipItemSearchImg(imgUrl string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if imgUrl != "" {
		params["img_url"] = imgUrl
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search_img", params)
}

// ==================== Mogujie (Vertical E-commerce) APIs ====================

func (c *Client) MogujieItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) MogujieItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Vancl (Vertical E-commerce) APIs ====================

func (c *Client) VanclItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) VanclItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Alimama (Vertical E-commerce) APIs ====================

func (c *Client) AlimamaItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AlimamaItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

func (c *Client) AlimamaSellerInfo(nick string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("seller_info", paramsWith(extraParams, "nick", nick))
}

func (c *Client) AlimamaItemLink(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_link", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AlimamaItemId(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_id", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AlimamaItemSearchPro(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search_pro", params)
}

// ==================== Jumei (Vertical E-commerce) APIs ====================

func (c *Client) JumeiItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JumeiItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Hc360 (Vertical E-commerce) APIs ====================

func (c *Client) Hc360ItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) Hc360ItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Huobutou (Vertical E-commerce) APIs ====================

func (c *Client) HuobutouItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) HuobutouItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== K3 (Vertical E-commerce) APIs ====================

func (c *Client) K3ItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) K3ItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Souhaohuo (Vertical E-commerce) APIs ====================

func (c *Client) SouhaohuoItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) SouhaohuoItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Taobaoke (Vertical E-commerce) APIs ====================

func (c *Client) TaobaokeItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) TaobaokeItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Kaola (Vertical E-commerce) APIs ====================

func (c *Client) KaolaItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) KaolaItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Csg (Vertical E-commerce) APIs ====================

func (c *Client) CsgItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) CsgItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Yhd (Vertical E-commerce) APIs ====================

func (c *Client) YhdItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YhdItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Ymatou (Vertical E-commerce) APIs ====================

func (c *Client) YmatouItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YmatouItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Smzdm (Vertical E-commerce) APIs ====================

func (c *Client) SmzdmItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) SmzdmItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Yqzwd (Vertical E-commerce) APIs ====================

func (c *Client) YqzwdItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YqzwdItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Ylw (Vertical E-commerce) APIs ====================

func (c *Client) YlwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YlwItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Fzw (Vertical E-commerce) APIs ====================

func (c *Client) FzwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) FzwItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Yiwugo (Vertical E-commerce) APIs ====================

func (c *Client) YiwugoItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YiwugoItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Vvic (Vertical E-commerce) APIs ====================

func (c *Client) VvicItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) VvicItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Suning (Vertical E-commerce) APIs ====================

func (c *Client) SuningItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) SuningItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Dewu (Vertical E-commerce) APIs ====================

func (c *Client) DewuItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) DewuItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Xmz (Vertical E-commerce) APIs ====================

func (c *Client) XmzItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) XmzItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Shihuo (Vertical E-commerce) APIs ====================

func (c *Client) ShihuoItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ShihuoItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Qx (Vertical E-commerce) APIs ====================

func (c *Client) QxItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) QxItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Deli (Vertical E-commerce) APIs ====================

func (c *Client) DeliItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) DeliItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Colipu (Vertical E-commerce) APIs ====================

func (c *Client) ColipuItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ColipuItemSearch(q string, cat string, startPrice string, endPrice string, sort string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if cat != "" {
		params["cat"] = cat
	}
	if startPrice != "" {
		params["start_price"] = startPrice
	}
	if endPrice != "" {
		params["end_price"] = endPrice
	}
	if sort != "" {
		params["sort"] = sort
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Vipmro (Hardware Industry) APIs ====================

func (c *Client) VipmroItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

// ==================== Jiancaiwang (Hardware Industry) APIs ====================

func (c *Client) JiancaiwangItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JiancaiwangItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Huagw (Hardware Industry) APIs ====================

func (c *Client) HuagwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) HuagwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Ickey (Hardware Industry) APIs ====================

func (c *Client) IckeyItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) IckeyItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Zkw (Hardware Industry) APIs ====================

func (c *Client) ZkwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ZkwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== China (Hardware Industry) APIs ====================

func (c *Client) ChinaItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ChinaItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Zggk (Hardware Industry) APIs ====================

func (c *Client) ZggkItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ZggkItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Schneider (Hardware Industry) APIs ====================

func (c *Client) SchneiderItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) SchneiderItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Dp123 (Hardware Industry) APIs ====================

func (c *Client) Dp123ItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) Dp123ItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Misumi (Hardware Industry) APIs ====================

func (c *Client) MisumiItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) MisumiItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Jmweb (News Info) APIs ====================

func (c *Client) JmwebItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JmwebItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Pbweb (News Info) APIs ====================

func (c *Client) PbwebItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) PbwebItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Txxw (News Info) APIs ====================

func (c *Client) TxxwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) TxxwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Zhw (News Info) APIs ====================

func (c *Client) ZhwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ZhwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Xjb (News Info) APIs ====================

func (c *Client) XjbItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) XjbItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Weixin (Social Media) APIs ====================

func (c *Client) WeixinItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) WeixinItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Smallredbook (Social Media) APIs ====================

func (c *Client) SmallredbookItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) SmallredbookItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Hksp (Social Media) APIs ====================

func (c *Client) HkspItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) HkspItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Shortvideo (Social Media) APIs ====================

func (c *Client) ShortvideoItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ShortvideoItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Ks (Social Media) APIs ====================

func (c *Client) KsItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) KsItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Bili (Social Media) APIs ====================

func (c *Client) BiliItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) BiliItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Tiktok (Social Media) APIs ====================

func (c *Client) TiktokItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) TiktokItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Youtube (Social Media) APIs ====================

func (c *Client) YoutubeItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YoutubeItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Ahs (Second Hand) APIs ====================

func (c *Client) AhsItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AhsItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Goodfish (Second Hand) APIs ====================

func (c *Client) GoodfishItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) GoodfishItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Wuziw (Second Hand) APIs ====================

func (c *Client) WuziwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) WuziwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Zhuanzhuan (Second Hand) APIs ====================

func (c *Client) ZhuanzhuanItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ZhuanzhuanItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Sqw (Enterprise) APIs ====================

func (c *Client) SqwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) SqwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Tyc (Enterprise) APIs ====================

func (c *Client) TycItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) TycItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Aqc (Enterprise) APIs ====================

func (c *Client) AqcItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AqcItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Dangdang (Online Bookstore) APIs ====================

func (c *Client) DangdangItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) DangdangItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Kfz (Online Bookstore) APIs ====================

func (c *Client) KfzItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) KfzItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Cnbook (Online Bookstore) APIs ====================

func (c *Client) CnbookItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) CnbookItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Jxsggzy (Bidding Info) APIs ====================

func (c *Client) JxsggzyItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JxsggzyItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Natlh (Bidding Info) APIs ====================

func (c *Client) NatlhItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) NatlhItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Cgyzb (Bidding Info) APIs ====================

func (c *Client) CgyzbItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) CgyzbItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Bbw (Bidding Info) APIs ====================

func (c *Client) BbwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) BbwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Hbw (Bidding Info) APIs ====================

func (c *Client) HbwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) HbwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Czw (Bidding Info) APIs ====================

func (c *Client) CzwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) CzwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Zxw (Bidding Info) APIs ====================

func (c *Client) ZxwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ZxwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Anjuke (Real Estate) APIs ====================

func (c *Client) AnjukeItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AnjukeItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Wbtc (Real Estate) APIs ====================

func (c *Client) WbtcItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) WbtcItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Anjiago (Real Estate) APIs ====================

func (c *Client) AnjiagoItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) AnjiagoItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Beike (Real Estate) APIs ====================

func (c *Client) BeikeItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) BeikeItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Yhby (Merchant Data) APIs ====================

func (c *Client) YhbyItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YhbyItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Yidaba (Merchant Data) APIs ====================

func (c *Client) YidabaItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) YidabaItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Ebdoor (Merchant Data) APIs ====================

func (c *Client) EbdoorItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) EbdoorItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Mkbl (Merchant Data) APIs ====================

func (c *Client) MkblItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) MkblItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Zqw (Merchant Data) APIs ====================

func (c *Client) ZqwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ZqwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Swz007 (Merchant Data) APIs ====================

func (c *Client) Swz007ItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) Swz007ItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Sole (Merchant Data) APIs ====================

func (c *Client) SoleItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) SoleItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Zhaosw (Merchant Data) APIs ====================

func (c *Client) ZhaoswItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) ZhaoswItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== B2b (Merchant Data) APIs ====================

func (c *Client) B2bItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) B2bItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Jmw (Merchant Data) APIs ====================

func (c *Client) JmwItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JmwItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}

// ==================== Jingcheng (Merchant Data) APIs ====================

func (c *Client) JingchengItemGet(numIid string, extraParams map[string]string) (map[string]interface{}, error) {
	return c.request("item_get", paramsWith(extraParams, "num_iid", numIid))
}

func (c *Client) JingchengItemSearch(q string, page string, extraParams map[string]string) (map[string]interface{}, error) {
	params := mergeParams(map[string]string{}, extraParams)
	if q != "" {
		params["q"] = q
	}
	if page != "" {
		params["page"] = page
	}
	return c.request("item_search", params)
}
