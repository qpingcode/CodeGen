<html>
<head>
    <link rel="stylesheet" href="/layui/css/layui.css">
</head>

<body>
<div class="layui-container" >
    <form class="layui-form" action="/mongo/get" id="getForm" style="margin-top: 20px;">
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" id="getBtn" lay-filter="formDemo">抓取数据</button>
            </div>
        </div>
    </form>

    <form class="layui-form" action="/mongo/test" id="testForm" style="margin-top: 20px;">
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" id="testBtn" lay-filter="formDemo">测试端口</button>
            </div>
        </div>
    </form>

</div>




</body>
<script src="/layui/layui.all.js"></script>
<script src="/jquery/jquery.min.js"></script>
<script src="/jquery/jquery.form.min.js"></script>
<script>

    $(function () {

        $("#getBtn").click(function () {
            $("#getForm").ajaxSubmit(function () {
                parent.loadConnection()
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            })
            return false;
        })


        $("#testBtn").click(function () {
            $("#testForm").ajaxSubmit(function () {
                parent.loadConnection()
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            })
            return false;
        })

    })

</script>
</html>