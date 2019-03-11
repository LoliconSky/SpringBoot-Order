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