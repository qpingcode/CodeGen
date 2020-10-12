package me.qping.utils.codegen.controller;

import me.qping.common.model.AjaxMsg;
import me.qping.utils.codegen.CodeGenUtil;
import me.qping.utils.codegen.bean.build.BuildConfig;
import me.qping.utils.codegen.bean.build.Copyright;
import me.qping.utils.codegen.bean.h2.DBConnection;
import me.qping.utils.codegen.dao.DBConnectionDao;
import me.qping.utils.codegen.util.DownloadFileUtil;
import me.qping.utils.codegen.util.ZipUtil;
import me.qping.utils.database.DataBaseUtilBuilder;
import me.qping.utils.database.connect.DataBaseType;
import me.qping.utils.database.metadata.bean.TableMeta;
import me.qping.utils.database.util.MetaDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/code")
public class CodeController {

    @Autowired
    DBConnectionDao dbConnectionDao;

    @RequestMapping(value = "/generate", method = RequestMethod.GET, produces ="application/json;charset=UTF-8")
    @ResponseBody
    public Object generate(Long connectionId, String ignoreTableName, String ignoreColumnName,  String basePackage, String projectNameEn, String tableName, String schemaName, Copyright copyright){
        DBConnection c = dbConnectionDao.findById(connectionId).orElse(null);

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

            TableMeta tableMeta = dbutil.getTableInfo(tableName);

            Map<String,String> userParams = new HashMap<>();
            userParams.put("ignoreTableName", ignoreTableName);
            userParams.put("ignoreColumnName", ignoreColumnName);
            userParams.put("basePackage", basePackage);
            userParams.put("projectNameEn", projectNameEn);

            copyright = copyright == null ? new Copyright() : copyright;
            schemaName = schemaName == null ? "springboot-jpa": schemaName;

            // 生成代码
            long batchId = CodeGenUtil.doExecute(userParams, tableMeta, schemaName, copyright);

            // 压缩文件
            String zipName = ZipUtil.compress(BuildConfig.getOutputPath() + batchId);

            // 下载
            ResponseEntity<InputStreamResource> response = DownloadFileUtil.download(BuildConfig.getOutputPath() + batchId, zipName, zipName);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
