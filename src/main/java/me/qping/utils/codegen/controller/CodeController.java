package me.qping.utils.codegen.controller;

import me.qping.common.model.AjaxMsg;
import me.qping.utils.codegen.CodeGenUtil;
import me.qping.utils.codegen.bean.h2.DBConnection;
import me.qping.utils.codegen.dao.DBConnectionDao;
import me.qping.utils.database.DataBaseUtilBuilder;
import me.qping.utils.database.connect.DataBaseType;
import me.qping.utils.database.metadata.bean.TableMeta;
import me.qping.utils.database.util.MetaDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/code")
public class CodeController {

    @Autowired
    DBConnectionDao dbConnectionDao;

    @RequestMapping("/generate")
    @ResponseBody
    public AjaxMsg generate(Long connectionId, String tableName, String schemaName){
        DBConnection c = dbConnectionDao.findById(connectionId).orElse(null);

        schemaName = schemaName == null ? "rxthinking-swagger": schemaName;

        if(c == null){
            return AjaxMsg.fail().setMsg("无法找到该连接");
        }

        MetaDataUtil dbutil = null;
        try {
            dbutil = DataBaseUtilBuilder.init(
                    DataBaseType.valueOf(c.getDatabaseType()),
                    c.getHost(),
                    c.getPort() + "",
                    c.getDatabaseName(),
                    c.getUsername(),
                    c.getPassword(),
                    false,
                    null
            ).build();

            Map<String,String> userParams = new HashMap<>();
            TableMeta tableMeta = dbutil.getTableInfo(tableName);

            CodeGenUtil.doExecute(userParams, tableMeta, schemaName);


            return AjaxMsg.success();
        } catch (Exception e) {
            return AjaxMsg.fail().setMsg(e.getMessage());
        }
    }

}
