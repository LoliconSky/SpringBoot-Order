基于 SpringBoot 的微信点餐系统

分为卖家端（Web 管理后台）和买家端（微信端，使用 Vue）

- 日志的配置与使用
- @DynamicUpdate使用
- SpringDataJPA
- 表单校验
- json自定义格式化输出（订单），忽略null值输出

## 分页

示例代码：
``` java
PageRequest pageRequest = PageRequest.of(0, 2);
Page<ProductInfo> page = productService.findAll(pageRequest);
System.out.println(page.getTotalElements());
Assert.assertNotEquals(0, page.getTotalElements());


// 创建排序规则
Sort sort = new Sort(Sort.Direction.DESC, "id");
// 创建分页对象
Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
```

其中，findAll 是已经默认实现的方法，接收一个 Pageable 类型的分页参数，PageRequest 间接实现了 Pageable

## 微信登陆
使用第三方 SDK 避免重复造轮子，由于个人公众号没有权限，所以未测试

由于进制转换的问题，规定金额相差精度不超过 0.01 也就是两数相减不大于 0.01 就认为相等。

使用 freemarker 等模板引擎，返回的 ModelAndView 中 View 的名字是可以带路径的。。。
参考 WechatPayController

## 使用WebSocket
最简单的 demo，首先引入依赖：
``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

然后加入配置文件：

``` java
@Component
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```

最后就可以使用了：

``` java
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    /**
     * 向所有的连接客户端推送消息
     * @param message
     */
    public void sendMessage(String message) {
        for (WebSocket webSocket: webSocketSet) {
            log.info("【websocket消息】广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

WebSocket 实质上借用 HTTP 请求进行握手