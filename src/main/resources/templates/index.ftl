<html>
<head>
    <title>代码生成工具</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <style>
        .layui-form-label {
            width: 120px;
        }

        .layui-elem-field legend {
            color: blue;
        }
    </style>
</head>

<body>
<div class="layui-container" >
    <form class="layui-form" action="/code/generate" method="get" id="form">

        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <legend>数据库属性</legend>
        </fieldset>


        <div class="layui-form-item">
            <label class="layui-form-label">数据库连接</label>
            <div class="layui-input-inline">
                <select name="connectionId" id="connectionId" lay-filter="db"></select>
            </div>
            <div class="layui-input-inline"  style="margin-top:10px">
                <a id="connectionCreate" href="javascript:void(0)">新建</a> &emsp;
                <a id="connectionDelete" href="javascript:void(0)">删除</a>
            </div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">表名</label>
            <div class="layui-input-inline"  style="width:500px;">
                <select name="tableName" id="tableName">
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">模版选择</label>
            <div class="layui-input-inline"  style="width:500px;">
                <select name="schemaName" id="schemaName">
                    <option value="rxthinking">rxthinking</option>
                    <option value="springboot-jpa">springboot-jpa</option>
                </select>
            </div>
        </div>

        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <legend>自定义参数</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">表名前缀忽略</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="ignoreTableName" id="ignoreTableName" value="T_,TB_,V_,VW_,DT_,t_,tb_,v_,vw_,dt_" class="layui-input"></input>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">字段名前缀忽略</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="ignoreColumnName" id="ignoreColumnName" value="F_,C_,COL_,f_,c_,col_" class="layui-input"></input>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">包名前缀</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="basePackage" id="basePackage" value="com.rxthinking.test" class="layui-input"></input>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">项目英文简写</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="projectNameEn" id="projectNameEn" value="test" class="layui-input"></input>
            </div>
        </div>

        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <legend>版权信息</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">作者</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="author" id="author" class="layui-input"></input>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">创建日期</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" name="date" id="date" class="layui-input"></input>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">版本号</label>
            <div class="layui-input-inline"  style="width:500px;">
                <input type="text" value="1.0" name="version" id="version" class="layui-input"></input>
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
                data:{id: $("#connectionId").val()},
                dataType: "json",
                success: function (data) {
                    loadConnection()
                }
            })
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
                $('#tableName').html('');
                $.each(ajaxmsg.data, function (index, row) {
                    var option = new Option(row.name, row.name);
                    if(index == 0){
                        option.defaultSelected = true;
                    }
                    $('#tableName').append(option);
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
                $('#connectionId').html('');
                $.each(data, function (index, row) {
                    var option = new Option(row.name, row.id);
                    if(index == 0){
                        option.defaultSelected = true;
                        getTableNames(row.id)
                    }
                    $('#connectionId').append(option);
                })
                layui.form.render("select");
            }
        })
    }

</script>
</html>