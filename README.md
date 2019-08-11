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

| 目录结构     | 名称     | 作用                                             |
| ------------ | -------- | ------------------------------------------------ |
| schemas      | 架构目录 | 根目录                                           |
| - springboot | 某个架构 | 存放模版和build.json                             |
| - tpl        | 模版目录 | 存放各种模版                                     |
| - build.json | 配置文件 | 定义代码的路径结构，生成的文件名格式、版权等信息 |

## build.json 
```json
{
    "output": "...",  // 代码文件生成后存放路径
    "copyright":{},   // 版权信息，包含日期、作者等
    "javas":[],       // java 文件定义列表，对应 JavaGenerator 来处理
    "table": {}       // 数据库表信息，通过 DataBaseMetaData.analyze 获取
}
```

