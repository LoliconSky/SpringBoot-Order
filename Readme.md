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

## 使用缓存
导入 springboot 的 cache 依赖，在需要缓存的方法上使用 `@Cacheable(cacheNames="name", key="123")` 注解，如果执行某一个方法需要刷新缓存，就使用 `@CachePut(cacheNames="name", key="123")` 这个注解，保证 name 和 key 一致就可以刷新缓存。
另外，也可以使用 `@CacheEvict(cacheNames="name", key="123")` 注解来失效缓存。

key 如果省略不写默认是方法的参数，为简化代码，可以在类上使用 `@CacheConfig(cacheNames="name")` 来抽取公共部分。

还可以使用 condition、unless 属性用表达式来限制满足指定条件时才缓存。

默认使用的是 SimpleCacheConfiguration，即使用 ConcurrentMapCacheManager 来实现缓存。

但是例如，如果项目引入了 Redis，那么缓存的数据（方法的返回值）就会序列化到 Redis 进行存储，下次调用就不会再进方法了。

也就是：Spring Boot 会在侦测到存在 Redis 的依赖并且 Redis 的配置是可用的情况下，使用 RedisCacheManager 初始化 CacheManager。

https://my.oschina.net/u/3452433/blog/1831026

## 其他
测试在 SpringBoot 中使用 @Autowired 如果有两个相同的类型，在名字匹配的情况下也会注入成功，类似 @Resources

参考：WechatController 这个类

不明所以。。都不用使用 @Qualifier 了吗

---

关于 RequestContextHolder 对象，在 AOP 中使用的。

> Spring Web 提供了一个工具类 RequestContextHolder用来在当前线程中暴露当前请求及其属性RequestAttributes。
> 这样的话在整个请求处理过程中，在当前线程中通过此工具类就可以获取对象RequestAttributes，从而就可以访问当前请求及其属性。
> RequestAttributes是一个接口，用于抽象一个请求的所有属性对象。

``` java
ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
HttpServletRequest request = attributes.getRequest();

RequestContextHolder.getRequestAttributes();
RequestContextHolder.currentRequestAttributes();
```

https://blog.csdn.net/andy_zhang2007/article/details/83269849