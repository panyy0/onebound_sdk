# OneBound SDK

> 想找数据微调 AI 大模型？想让 AI 自动化运营自己的网点？奈何没有足够的高质量数据？自己写爬虫太容易被封、效率低、维护成本高？

**OneBound SDK** 是一个轻量级的跨语言数据采集工具，为开发者提供稳定、高效、易维护的电商与社媒数据获取方案。通过统一接口封装，支持 90+ 主流平台，一次集成即可在 Java / Go / Python 三种语言中自由调用。

[![Maven Central](https://img.shields.io/maven-central/v/cn.onebound/onebound-sdk)]()
[![PyPI](https://img.shields.io/pypi/v/onebound-sdk)]()
[![Go Reference](https://pkg.go.dev/badge/github.com/onebound/onebound-go)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)]()

---

## 项目简介

### 这个项目是什么？

OneBound SDK 本质上是一个**数据采集中间件**，介于业务代码和目标平台之间，提供标准化的 API 调用层：

```
业务代码  →  OneBound SDK  →  API 网关  →  目标平台
```

开发者无需关心请求签名、IP 封禁、重试策略、错误处理等底层逻辑，只需要调用 `item_get`、`item_search` 等简单接口，即可获取结构化数据。

### 解决了什么问题？

| 痛点 | 自建爬虫 | OneBound SDK |
|------|----------|--------------|
| IP 被封 | 频繁降级、接入代理池，成本高 | 多域名自动切换，API 层面解决 |
| 维护困难 | 平台改版导致爬虫失效，维护成本高 | 我们维护 SDK，你只管用 |
| 多语言支持 | 需要为 Java/Go/Python 各自写一套 | 三套 SDK 接口一致，一键切换 |
| 反爬升级 | UA、Cookie、签名算法轮番更新 | SDK 内部处理，业务代码零改动 |

### 适用人群

- **AI 开发者**：需要商品标题/评论/描述等文本数据训练 NLP 模型
- **数据分析团队**：需要多平台结构化数据做市场分析
- **电商运营**：需要批量采集竞品数据做运营决策
- **独立开发者**：想快速做个选品工具、数据监控面板

---

## 支持平台一览

### 国内主流电商

| 平台 | 数据类型 |
|------|----------|
| 某宝 | 商品详情、搜索、订单、买家秀等 57+ 接口 |
| 1688 阿里批发 | 工厂溯源、同款查找、批发价等 28+ 接口 |
| 某东 | 商品详情、历史价格、SKU等 26+ 接口 |
| 某多 | 社交电商全接口 11+ |
| 微店 | 移动电商接口 4+ |

### 海外电商平台

| 平台 | 数据类型 |
|------|----------|
| Shopee (东南亚) | 商品、搜索、店铺、评价 6+ 接口 |
| AliExpress (速卖通) | 全球电商 7+ 接口 |
| Lazada | 东南亚电商 4+ 接口 |
| Amazon | 亚马逊电商 4+ 接口 |
| eBay | 全球电商 2+ 接口 |
| Walmart | 沃尔玛电商 3+ 接口 |
| Temu | 拼多多海外版 1+ 接口 |
| Ozon / Jumia / Mercado / Wildberries | 新兴市场电商 |

### 社交媒体 & 内容平台

| 平台 | 数据类型 |
|------|----------|
| 某信 | 公众号、小程序数据 |
| 某书 | 种草笔记、博主数据 |
| 某音 | 短视频、带货数据 |
| 某手 | 直播、短视频数据 |
| 某站 | 视频、UP主数据 |
| YouTube | 海外视频数据 |

### 酒店旅游

携程、大麦网、房天下、游虾、花筑、东呈、心里美、客路旅行

### 更多垂直领域

唯品会、蘑菇街、返利网、楚楚街、折八百、识货、得物、1688找货网、五金工业网、建材采购、新闻资讯、招投标信息、房产数据、商户数据... **90+ 平台持续更新中**

---

## 功能特性

- **多语言 SDK**：Java (Maven)、Go (pkg.go.dev)、Python (PyPI) 三大主流语言
- **多域名自动切换**：主备 5 个 API 域名，失败自动轮换，高可用
- **错误码拦截**：4013/4016 等登录失效错误自动提示充值地址
- **统一架构设计**：核心代码复用，接口一致，上手简单
- **支持平台最全**：覆盖国内外主流电商、社交、内容平台 90+

---

## 快速开始

### 获取 API Key 和 Secret

通过[注册链接](http://console.open.onebound.cn/console/?i=c-46989)注册账号后，即可在控制台获得你的 API Key 和 Secret。

### 配置凭据

拿到 Key 和 Secret 后，根据你使用的语言，按以下方式配置：

**方式一：环境变量（推荐，所有语言通用）**

```bash
export ONEBOUND_KEY=your-key
export ONEBOUND_SECRET=your-secret
```

也可以在项目根目录创建 `.env` 文件（已被 `.gitignore` 忽略，不会提交到仓库）：

```
ONEBOUND_KEY=your-key
ONEBOUND_SECRET=your-secret
```

**方式二：直接传入构造函数**

| 语言 | 示例 |
|------|------|
| Python | `OneBoundClient("taobao", "your-key", "your-secret")` |
| Go | `onebound.NewClient("taobao", "your-key", "your-secret")` |
| Java | `new OneBoundClient("taobao", "your-key", "your-secret")` |

> **Python SDK** 支持从环境变量自动读取：如果构造时省略 key/secret，会自动从 `ONEBOUND_KEY` 和 `ONEBOUND_SECRET` 环境变量获取。Go 和 Java SDK 目前需要直接传入。

### Java (Maven 引用)

```xml
<dependency>
    <groupId>cn.onebound</groupId>
    <artifactId>onebound-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

**测试用例**

```java
import cn.onebound.sdk.OneBoundClient;

public class Test {
    public static void main(String[] args) {
        // 初始化客户端 (platform, key, secret)
        OneBoundClient client = new OneBoundClient("taobao", "your-key", "your-secret");
        
        // 获取商品详情
        Object item = client.taobao_item_get("123456789");
        System.out.println(item);
        
        // 搜索商品
        Object results = client.taobao_item_search("手机");
        System.out.println(results);
    }
}
```

---

### Go (go mod 引用)

```bash
go get github.com/onebound/onebound-go
```

**测试用例**

```go
package main

import (
    "fmt"
    "onebound"
)

func main() {
    client := onebound.NewClient("taobao", "your-key", "your-secret")
    
    // 获取商品详情
    result, err := client.ItemGet("123456789", nil)
    if err != nil {
        fmt.Println("Error:", err)
        return
    }
    fmt.Println(result)
    
    // 搜索商品
    result, err = client.ItemSearch("手机", nil)
    if err != nil {
        fmt.Println("Error:", err)
        return
    }
    fmt.Println(result)
}
```

---

### Python (pip 引用)

```bash
pip install onebound-sdk
```

**测试用例**

```python
from onebound import OneBoundClient

# 初始化客户端 (platform, key, secret)
client = OneBoundClient("taobao", "your-key", "your-secret")

# 获取商品详情
item = client.taobao_item_get("123456789")
print(item)

# 搜索商品
results = client.taobao_item_search(q="手机")
print(results)
```

---

## 截图展示

> 以下为企业控制台功能截图，扫码联系顾问获取更多演示：

### API 调用示例截图
<!-- [此处贴图：API 调用示例截图] -->

### 电商平台数据返回示例
<!-- [此处贴图：商品数据返回示例] -->

### 社媒平台数据示例
<!-- [此处贴图：某书/某音数据示例] -->

### 酒店旅游数据示例
<!-- [此处贴图：携程/酒店数据示例] -->

---

## 测试

```bash
# Python - 单元测试（mock）
cd onebound-python
pip install -e .
python -m unittest tests/test_client.py -v

# Python - 集成测试（真实 API，需要 ONEBOUND_KEY 和 ONEBOUND_SECRET）
python -m unittest tests/test_integration.py -v

# Go - 单元测试（mock HTTP server）
cd onebound-go
go test ./... -v
```

> 集成测试会覆盖所有支持的 API 大类，每个大类随机选择一个平台进行 `item_search` 和 `item_get` 测试。未开通权限的平台会自动跳过。

---

## 常见问题

**Q: 如何获取 API Key 和 Secret？**

A：参见上方 [获取 API Key 和 Secret](#获取-api-key-和-secret) 章节。

**Q: 接口调用频率有限制吗？**

A: 不同套餐频率不同，具体请咨询顾问。

**Q: 支持数据定制吗？**

A: 支持私有数据采集和定制开发，请通过邀请链接联系我们。

**Q: 有免费试用吗？**

A: 注册后联系顾问可获得试用额度。

---

## 联系顾问 / 获取 API Key

**👉 [点击这里，限时领取 API Key](http://console.open.onebound.cn/console/?i=c-46989)**



---

## License

MIT License - 欢迎 Star & Fork
