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