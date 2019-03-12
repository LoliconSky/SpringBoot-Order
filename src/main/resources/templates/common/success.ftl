!<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <title>操作成功</title>
</head>
<body>
    <div class="container">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <div class="alert alert-dismissable alert-success">
                    <h4>
                        操作成功!
                    </h4> <strong>${msg!""}</strong> 3 秒后自动跳转 <a href="${url}" class="alert-link">立即跳转</a>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    setTimeout('location.href="${url}"', 3000);
</script>
</html>