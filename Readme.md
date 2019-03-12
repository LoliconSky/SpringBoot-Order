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