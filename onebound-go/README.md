# OneBound SDK (Go)

轻量级电商与社媒数据采集 SDK，支持 90+ 主流平台。

## 安装

```bash
go get github.com/onebound/onebound-go
```

## 快速开始

```go
package main

import (
    "fmt"
    "github.com/onebound/onebound-go/onebound"
)

func main() {
    client := onebound.NewClient("taobao", "your-key", "your-secret")

    // 获取商品详情
    result, err := client.ItemGet("674436494131", nil)
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

获取 API Key：[点击注册](http://console.open.onebound.cn/console/?i=c-46989)

## 功能特性

- 多域名自动切换（5 个 API 网关，失败自动轮换）
- 错误码拦截（4013/4016 登录失效自动提示）
- 统一架构设计，接口一致

## License

MIT