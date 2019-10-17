# 项目用途
根据数据库结构批量生成代码，如：bean、dao、service、controller 等

# 使用方式
第一步：在 CodeGenUtil.java 中修改数据库连接地址
```
// mysql 初始化方式
// 方式一
DataBaseMetaData metadata = DataBaseMetaData.builder()
    .mysql("localhost", "3306", "databaseName", "username", "password")
    .build();
// 其他数据库类似，具体可参考 https://github.com/qpingcode/DataBaseUtil  

```

第二步：确定需要转成 javaBean 的表
TableMeta tableMeta = metadata.analyze("card_report");

第三步：确定代码模版，现定义了 springboot 的代码模版，可自定义
String schemaName = "springboot";

第四步：修改build.json
- output 最终文件生成目录
- javas.basePath 基础包名，如: com.test.proj

第五步：运行 main 方法，完成文件生成


# 项目架构
## 目录结构说明
```yaml
schemas
  - springboot
    - tpl
      - Bean.ftl
      - Dao.ftl
      - Service.ftl
      - Controller.ftl
    - build.json
```    

| 目录结构        | 名称     | 作用                                             |
| -------------- | -------- | ------------------------------------------------ |
| schemas        | 架构目录 | 根目录                                           |
| - springboot   | 某个架构 | 存放模版和build.json                             |
|   - tpl        | 模版目录 | 存放各种模版                                     |
|   - build.json | 配置文件 | 定义代码的路径结构，生成的文件名格式、版权等信息 |

## build.json 
```json
{
    "output": "...",  // 代码文件生成后存放路径
    "copyright":{},   // 版权信息，包含日期、作者等
    "javas":[],       // java 文件定义列表，对应 JavaGenerator 来处理
    "table": {}       // 数据库表信息，通过 DataBaseMetaData.analyze 获取
}
```



