package me.qping.utils.codegen.bean.datagrab;

import lombok.Data;
import me.qping.utils.excel.anno.Excel;

/**
 * @ClassName FrontEnd
 * @Description TODO
 * @Author qping
 * @Date 2021/5/12 11:54
 * @Version 1.0
 **/
@Data
public class FrontEnd {
    @Excel(name = "医院")
    String hospital;

    @Excel(name = "地址")
    String ip;

    @Excel(name = "端口")
    int port;

    @Excel(name = "用户名")
    String username;

    @Excel(name = "用户密码")
    String password;

    @Excel(name = "公司")
    String company;

    @Excel(name = "etl机构id")
    int orgId;

}
