<html>
<head>
    <title>卖家端-商品添加/更新</title>
    <#include "../common/header.ftl">
</head>
<body>
    <div id="wrapper" class="toggled">

        <#--边栏sidebar-->
        <#include "../common/nav.ftl">

        <#--主要内容content-->
        <div id="page-content-wrapper">
            <div class="container-fluid">
                <div class="row clearfix">
                    <div class="col-md-12 column">
                        <form role="form" method="post" action="/sell/seller/product/save">
                            <div class="form-group">
                                <label>名称</label>
                                <input name="productName" type="text" class="form-control" value="${(productInfo.productName)!''}"/>
                            </div>
                            <div class="form-group">
                                <label>价格</label>
                                <input name="productPrice" type="text" class="form-control" value="${(productInfo.productPrice)!''}"/>
                            </div>
                            <div class="form-group">
                                <label>库存</label>
                                <input name="productStock" type="number" class="form-control" value="${(productInfo.productStock)!''}"/>
                            </div>
                            <div class="form-group">
                                <label>描述</label>
                                <input name="productDescription" type="text" class="form-control" value="${(productInfo.productDescription)!''}"/>
                            </div>
                            <div class="form-group">
                                <label>图片URL</label>
                                <#if productInfo??>
                                    <img height="100" width="100" src="${(productInfo.productIcon)!''}" alt="">
                                </#if>
                                <input name="productIcon" type="text" class="form-control" value="${(productInfo.productIcon)!''}"/>
                            </div>
                            <div class="form-group">
                                <label>类目</label>
                                <select name="categoryType" class="form-control">
                                    <#list categoryList as category>
                                        <option value="${category.categoryType}"
                                                <#if (productInfo.categoryType)?? && productInfo.categoryType == category.categoryType>
                                                    selected
                                                </#if>
                                        >${category.categoryName}
                                        </option>
                                    </#list>
                                </select>
                            </div>
                            <input hidden type="text" name="productId" value="${(productInfo.productId)!''}">
                            <button type="submit" class="btn btn-default">提交</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
</body>
</html>