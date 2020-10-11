<html>
<head>
    <link rel="stylesheet" href="/layui/css/layui.css">
</head>

<body>
<div class="layui-container" >
    <form class="layui-form" action="/api/task/create" id="form">

        <div class="layui-form-item">
            <label class="layui-form-label">数据库连接</label>
            <div class="layui-input-inline">
                <select name="dbConnectionChoose" id="dbConnectionChoose" lay-filter="db"></select>
            </div>
            <div class="layui-input-inline"  style="margin-top:10px">
                <a id="connectionCreate" href="javascript:void(0)">新建</a> &emsp;
                <a id="connectionDelete" href="javascript:void(0)">删除</a>
            </div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">表名</label>
            <div class="layui-input-inline"  style="width:500px;">
                <select name="tableChoose" id="tableChoose">
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">模版选择</label>
            <div class="layui-input-inline"  style="width:500px;">
                <select name="templateChoose" id="templateChoose">
                </select>
            </div>
        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" id="genBtn" lay-filter="formDemo">生成代码</button>
                <#--<button type="reset" class="layui-btn layui-btn-primary">重置</button>-->
            </div>
        </div>
    </form>
</div>




</body>
<script src="/layui/layui.all.js"></script>
<script src="/jquery/jquery.min.js"></script>
<script>

    $(function () {


        loadConnection();

        $("#connectionCreate").click(function () {
            layer.open({
                type: 2,
                offset: '20px',
                title: '创建数据库连接',
                shadeClose: true,
                shade: 0.8,
                area: ['800px', '90%'],
                content: '/pages/connection/form'
            });
        })

        $("#connectionDelete").click(function () {
            $.ajax({
                url: "/connection/deleteById",
                type: "post",
                cache: false,
                data:{id: $("#dbConnectionChoose").val()},
                dataType: "json",
                success: function (data) {
                    loadConnection()
                }
            })
        })



        $("#genBtn").click(function () {

        })


        layui.form.on('select(db)', function(data){
            getTableNames(data.value);
        });


    })

    function getTableNames(id){
        $.ajax({
            url: "/connection/getTableNames",
            type: "post",
            data: {connectionId: id},
            cache: false,
            dataType: "json",
            success: function (ajaxmsg) {
                if(ajaxmsg.code != 'success'){
                    alert(ajaxmsg.msg);
                    return;
                }
                $('#tableChoose').html('');
                $.each(ajaxmsg.data, function (index, row) {
                    var option = new Option(row.name, row.name);
                    if(index == 0){
                        option.defaultSelected = true;
                    }
                    $('#tableChoose').append(option);
                })
                layui.form.render("select");
            }
        })
    }

    function loadConnection(){
        $.ajax({
            url: "/connection/findAll",
            type: "post",
            cache: false,
            dataType: "json",
            success: function (data) {
                if(!data) return;
                $('#dbConnectionChoose').html('');
                $.each(data, function (index, row) {
                    var option = new Option(row.name, row.id);
                    if(index == 0){
                        option.defaultSelected = true;
                        getTableNames(row.id)
                    }
                    $('#dbConnectionChoose').append(option);
                })
                layui.form.render("select");
            }
        })
    }

</script>
</html>