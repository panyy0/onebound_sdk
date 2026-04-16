import os
import requests
from typing import Dict, Optional, Any, List
from urllib.parse import urlencode

class OneBoundClient:
    
    API_URLS = [
        "https://api-gw.onebound.cn",
        "https://api-1.onebound.cn",
        "https://api-2.onebound.cn",
        "https://api-3.onebound.cn",
        "https://api-4.onebound.cn",
    ]
    
    DEFAULT_CACHE = "yes"
    DEFAULT_RESULT_TYPE = "json"
    DEFAULT_LANG = "cn"
    DEFAULT_TIMEOUT = 30
    
    ERROR_NEED_LOGIN = [4013, 4016]
    LOGIN_URL = "http://console.open.onebound.cn/console/?i=c-46989"
    
    SUPPORTED_PLATFORMS = (
        "taobao", "1688", "jd", "pinduoduo", "micro", "shopee", "aliexpress", "temu", 
        "tglobal", "lazada", "amazon", "ebay", "kgcg", "alibaba", "walgreens", "mic", 
        "shop", "target", "wildberries", "daraz", "mercado", "dmm", "walmart", "jumia", 
        "ozon", "xiecheng", "damai", "ftx", "youxia", "huazhu", "dongcheng", "xinlimei", 
        "klook", "vip", "mogujie", "vancl", "alimama", "jumei", "hc360", "huobutou", "k3", 
        "souhaohuo", "taobaoke", "kaola", "csg", "yhd", "ymatou", "smzdm", "yqzwd", "ylw", 
        "fzw", "yiwugo", "vvic", "suning", "dewu", "xmz", "shihuo", "qx", "deli", "colipu", 
        "vipmro", "jiancaiwang", "huagw", "ickey", "zkw", "china", "zggk", "schneider", 
        "dp123", "misumi", "jmweb", "pbweb", "txxw", "zhw", "xjb", "weixin", "smallredbook", 
        "hksp", "shortvideo", "ks", "bili", "tiktok", "youtube", "ahs", "goodfish", 
        "wuziw", "zhuanzhuan", "sqw", "tyc", "aqc", "dangdang", "kfz", "cnbook", "jxsggzy", 
        "zblh", "cgyzb", "bbw", "hbw", "czw", "zxw", "anjuke", "wbtc", "anjiago", "beike", 
        "yhby", "yidaba", "ebdoor", "mkbl", "zqw", "007swz", "sole", "zhaosw", "b2b", "jmw", 
        "jingcheng"
    )
    
    def __init__(self, platform: str, key: str = None, secret: str = None, api_url: str = None):
        self.platform = platform.lower()
        self.key = key or os.environ.get("ONEBOUND_KEY", "")
        self.secret = secret or os.environ.get("ONEBOUND_SECRET", "")
        self.api_url = api_url or self.API_URLS[0]
        self.cache = self.DEFAULT_CACHE
        self.result_type = self.DEFAULT_RESULT_TYPE
        self.lang = self.DEFAULT_LANG
        self.version = ""
        self._url_index = 0
        
        if self.platform not in self.SUPPORTED_PLATFORMS:
            raise ValueError(f"Unsupported platform: {self.platform}. Please check the documentation for supported platforms.")
    
    def _build_url(self, api_name: str, params: Dict[str, Any]) -> str:
        all_params = {
            "key": self.key,
            "secret": self.secret,
            "api_name": api_name,
            "cache": self.cache,
            "result_type": self.result_type,
            "lang": self.lang,
        }
        if self.version:
            all_params["version"] = self.version
        
        for k, v in params.items():
            if v is not None:
                all_params[k] = v
        
        base_url = self.api_url.rstrip("/")
        return f"{base_url}/{self.platform}/{api_name}/?{urlencode(all_params)}"
    
    def _rotate_url(self):
        self._url_index = (self._url_index + 1) % len(self.API_URLS)
        self.api_url = self.API_URLS[self._url_index]
    
    def _check_response(self, result: Dict) -> Dict:
        if isinstance(result, dict):
            error_code = result.get("error_code") or result.get("code")
            if error_code in self.ERROR_NEED_LOGIN:
                raise Exception(
                    f"API Error {error_code}: Please login at {self.LOGIN_URL} to register or recharge your account."
                )
        return result
    
    def _request(self, api_name: str, params: Dict[str, Any] = None, retry_count: int = 3) -> Dict:
        if params is None:
            params = {}
        
        headers = {
            "Accept-Encoding": "gzip",
            "Connection": "close"
        }
        
        for attempt in range(retry_count):
            try:
                url = self._build_url(api_name, params)
                response = requests.get(url, headers=headers, timeout=self.DEFAULT_TIMEOUT)
                response.raise_for_status()
                result = response.json()
                return self._check_response(result)
            except requests.exceptions.RequestException as e:
                if attempt < retry_count - 1:
                    self._rotate_url()
                    continue
                raise Exception(f"API request failed after {retry_count} attempts: {str(e)}")
            except Exception as e:
                error_str = str(e)
                if "4013" in error_str or "4016" in error_str:
                    raise
                if attempt < retry_count - 1:
                    self._rotate_url()
                    continue
                raise
    
    def _add_params(self, params: Dict, **kwargs) -> Dict:
        params.update({k: v for k, v in kwargs.items() if v is not None})
        return params
    
    def set_cache(self, cache: str):
        self.cache = cache
    
    def set_result_type(self, result_type: str):
        self.result_type = result_type
    
    def set_lang(self, lang: str):
        self.lang = lang
    
    def set_version(self, version: str):
        self.version = version

    # taobao
    def taobao_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def taobao_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # 1688
    def i1688_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def i1688_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # jd
    def jd_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def jd_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # pinduoduo
    def pinduoduo_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def pinduoduo_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # micro
    def micro_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def micro_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # shopee
    def shopee_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def shopee_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # aliexpress
    def aliexpress_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def aliexpress_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # temu
    def temu_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def temu_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # tglobal
    def tglobal_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def tglobal_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # lazada
    def lazada_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def lazada_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # amazon
    def amazon_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def amazon_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ebay
    def ebay_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ebay_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # kgcg
    def kgcg_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def kgcg_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # alibaba
    def alibaba_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def alibaba_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # walgreens
    def walgreens_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def walgreens_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # mic
    def mic_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def mic_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # shop
    def shop_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def shop_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # target
    def target_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def target_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # wildberries
    def wildberries_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def wildberries_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # daraz
    def daraz_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def daraz_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # mercado
    def mercado_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def mercado_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # dmm
    def dmm_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def dmm_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # walmart
    def walmart_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def walmart_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # jumia
    def jumia_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def jumia_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ozon
    def ozon_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ozon_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # xiecheng
    def xiecheng_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def xiecheng_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # damai
    def damai_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def damai_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ftx
    def ftx_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ftx_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # youxia
    def youxia_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def youxia_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # huazhu
    def huazhu_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def huazhu_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # dongcheng
    def dongcheng_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def dongcheng_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # xinlimei
    def xinlimei_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def xinlimei_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # klook
    def klook_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def klook_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # vip
    def vip_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def vip_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # mogujie
    def mogujie_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def mogujie_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # vancl
    def vancl_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def vancl_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # alimama
    def alimama_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def alimama_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # jumei
    def jumei_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def jumei_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # hc360
    def hc360_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def hc360_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # huobutou
    def huobutou_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def huobutou_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # k3
    def k3_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def k3_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # souhaohuo
    def souhaohuo_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def souhaohuo_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # taobaoke
    def taobaoke_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def taobaoke_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # kaola
    def kaola_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def kaola_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # csg
    def csg_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def csg_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # yhd
    def yhd_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def yhd_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ymatou
    def ymatou_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ymatou_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # smzdm
    def smzdm_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def smzdm_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # yqzwd
    def yqzwd_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def yqzwd_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ylw
    def ylw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ylw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # fzw
    def fzw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def fzw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # yiwugo
    def yiwugo_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def yiwugo_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # vvic
    def vvic_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def vvic_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # suning
    def suning_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def suning_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # dewu
    def dewu_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def dewu_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # xmz
    def xmz_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def xmz_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # shihuo
    def shihuo_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def shihuo_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # qx
    def qx_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def qx_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # deli
    def deli_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def deli_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # colipu
    def colipu_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def colipu_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # vipmro
    def vipmro_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def vipmro_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # jiancaiwang
    def jiancaiwang_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def jiancaiwang_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # huagw
    def huagw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def huagw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ickey
    def ickey_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ickey_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # zkw
    def zkw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def zkw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # china
    def china_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def china_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # zggk
    def zggk_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def zggk_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # schneider
    def schneider_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def schneider_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # dp123
    def dp123_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def dp123_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # misumi
    def misumi_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def misumi_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # jmweb
    def jmweb_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def jmweb_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # pbweb
    def pbweb_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def pbweb_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # txxw
    def txxw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def txxw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # zhw
    def zhw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def zhw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # xjb
    def xjb_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def xjb_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # weixin
    def weixin_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def weixin_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # smallredbook
    def smallredbook_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def smallredbook_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # hksp
    def hksp_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def hksp_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # shortvideo
    def shortvideo_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def shortvideo_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ks
    def ks_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ks_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # bili
    def bili_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def bili_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # tiktok
    def tiktok_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def tiktok_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # youtube
    def youtube_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def youtube_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ahs
    def ahs_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ahs_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # goodfish
    def goodfish_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def goodfish_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # wuziw
    def wuziw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def wuziw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # zhuanzhuan
    def zhuanzhuan_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def zhuanzhuan_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # sqw
    def sqw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def sqw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # tyc
    def tyc_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def tyc_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # aqc
    def aqc_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def aqc_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # dangdang
    def dangdang_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def dangdang_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # kfz
    def kfz_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def kfz_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # cnbook
    def cnbook_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def cnbook_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # jxsggzy
    def jxsggzy_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def jxsggzy_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # zblh
    def zblh_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def zblh_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # cgyzb
    def cgyzb_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def cgyzb_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # bbw
    def bbw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def bbw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # hbw
    def hbw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def hbw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # czw
    def czw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def czw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # zxw
    def zxw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def zxw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # anjuke
    def anjuke_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def anjuke_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # wbtc
    def wbtc_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def wbtc_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # anjiago
    def anjiago_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def anjiago_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # beike
    def beike_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def beike_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # yhby
    def yhby_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def yhby_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # yidaba
    def yidaba_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def yidaba_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # ebdoor
    def ebdoor_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def ebdoor_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # mkbl
    def mkbl_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def mkbl_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # zqw
    def zqw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def zqw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # 007swz (using oo7 prefix since 007swz is not valid Python identifier)
    def oo7swz_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def oo7swz_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # sole
    def sole_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def sole_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # zhaosw
    def zhaosw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def zhaosw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # b2b
    def b2b_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def b2b_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # jmw
    def jmw_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def jmw_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)

    # jingcheng
    def jingcheng_item_get(self, num_iid: str = None, **kwargs) -> Dict:
        return self._request("item_get", self._add_params({"num_iid": num_iid}, **kwargs))

    def jingcheng_item_search(self, q: str = None, page: str = None, **kwargs) -> Dict:
        params = self._add_params(kwargs)
        if q is not None:
            params["q"] = q
        if page is not None:
            params["page"] = page
        return self._request("item_search", params)
