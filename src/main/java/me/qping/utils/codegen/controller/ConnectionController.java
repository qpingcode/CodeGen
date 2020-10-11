package me.qping.utils.codegen.controller;

import me.qping.common.model.AjaxMsg;
import me.qping.utils.codegen.bean.h2.DBConnection;
import me.qping.utils.codegen.dao.DBConnectionDao;
import me.qping.utils.codegen.util.SnowFlakeId;
import me.qping.utils.database.DataBaseUtilBuilder;
import me.qping.utils.database.connect.DataBaseType;
import me.qping.utils.database.metadata.bean.TableMeta;
import me.qping.utils.database.util.MetaDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/connection")
public class ConnectionController {

    SnowFlakeId worker = new SnowFlakeId(1, 1, 1);

    @Autowired
    DBConnectionDao dbConnectionDao;

    @RequestMapping("/findAll")
    @ResponseBody
    public List<DBConnection> findAll() {
        return dbConnectionDao.findAllByOrderByIdDesc();
    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public boolean deleteById(Long id) {
        DBConnection connection = dbConnectionDao.findById(id).orElse(null);
        if (connection != null) {
            dbConnectionDao.delete(connection);
        }
        return true;
    }

    @RequestMapping("/save")
    @ResponseBody
    public Long save(DBConnection connection) {
        if (connection.getId() == null) {
            connection.setId(worker.nextId());
        }
        dbConnectionDao.save(connection);
        return connection.getId();
    }

    @RequestMapping("/getTableNames")
    @ResponseBody
    public AjaxMsg getTableNames(Long connectionId) {
        DBConnection c = dbConnectionDao.findById(connectionId).orElse(null);

        if (c == null) {
            return AjaxMsg.fail().setMsg("无法找到该连接");
        }

        //  boolean useServiceName, String schema)
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

            List<TableMeta> tables = dbutil.getTables();
            return AjaxMsg.success().setData(tables);
        } catch (Exception e) {
            return AjaxMsg.fail().setMsg(e.getMessage());
        }
    }

}
