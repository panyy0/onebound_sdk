# OneBound SDK (Python)

轻量级电商与社媒数据采集 SDK，支持 90+ 主流平台。

## 安装

```bash
pip install onebound-sdk
```

## 快速开始

### 环境变量配置（推荐）

```bash
export ONEBOUND_KEY=your-key
export ONEBOUND_SECRET=your-secret
```

或在项目根目录创建 `.env` 文件：

```
ONEBOUND_KEY=your-key
ONEBOUND_SECRET=your-secret
```

### 使用

```python
from onebound import OneBoundClient

# 方式一：通过环境变量自动读取 key/secret
client = OneBoundClient("taobao")

# 方式二：直接传入 key/secret
client = OneBoundClient("taobao", "your-key", "your-secret")

# 获取商品详情
item = client.taobao_item_get(num_iid="674436494131")
print(item)

# 搜索商品
results = client.taobao_item_search(q="手机")
print(results)
```

### 支持的平台

taobao, 1688, jd, pinduoduo, micro, shopee, aliexpress, temu, tglobal, lazada, amazon, ebay, kgcg, alibaba, walgreens, mic, shop, target, wildberries, daraz, mercado, dmm, walmart, jumia, ozon, xiecheng, damai, ftx, youxia, huazhu, dongcheng, xinlimei, klook, vip, mogujie, vancl, alimama, jumei, hc360, huobutou, k3, souhaohuo, taobaoke, kaola, csg, yhd, ymatou, smzdm, yqzwd, ylw, fzw, yiwugo, vvic, suning, dewu, xmz, shihuo, qx, deli, colipu, vipmro, jiancaiwang, huagw, ickey, zkw, china, zggk, schneider, dp123, misumi, jmweb, pbweb, txxw, zhw, xjb, weixin, smallredbook, hksp, shortvideo, ks, bili, tiktok, youtube, ahs, goodfish, wuziw, zhuanzhuan, sqw, tyc, aqc, dangdang, kfz, cnbook, jxsggzy, zblh, cgyzb, bbw, hbw, czw, zxw, anjuke, wbtc, anjiago, beike, yhby, yidaba, ebdoor, mkbl, zqw, 007swz, sole, zhaosw, b2b, jmw, jingcheng

获取 API Key：[点击注册](http://console.open.onebound.cn/console/?i=c-46989)

## 功能特性

- 多域名自动切换（5 个 API 网关，失败自动轮换）
- 错误码拦截（4013/4016 登录失效自动提示）
- 环境变量支持（ONEBOUND_KEY / ONEBOUND_SECRET）

## License

MIT