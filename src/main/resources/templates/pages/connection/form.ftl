<html>
<head>
    <link rel="stylesheet" href="/layui/css/layui.css">
</head>

<body>
<div class="layui-container" >
    <form class="layui-form" action="/connection/save" id="linkForm" style="margin-top: 20px;">

        <div class="layui-form-item">
            <label class="layui-form-label">数据库类型</label>
            <div class="layui-input-inline"  style="width:500px;">
                <select name="databaseType">
                    <option value="1">MySQL</option>
                    <option value="2">SQLServer</option>
                    <option value="3">Oracle</option>
                    <option value="6">Postgre</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">连接名</label>
            <div class="layui-input-inline">
                <input type="text" name="name" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">IP地址</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="host" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">端口地址</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="port" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">用户名</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="username" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="password" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">数据库名</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="databaseName" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" id="linkCreateBtn" lay-filter="formDemo">创建连接</button>
            <#--<button type="reset" class="layui-btn layui-btn-primary">重置</button>-->
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
        $("#linkCreateBtn").click(function () {
            $("#linkForm").ajaxSubmit(function () {
                parent.loadConnection()
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            })
            return false;
        })

    })

</script>
</html>