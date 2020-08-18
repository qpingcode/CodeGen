package me.qping.utils.codegen.controller;

import me.qping.utils.codegen.bean.h2.DBConnection;
import me.qping.utils.codegen.dao.DBConnectionDao;
import me.qping.utils.codegen.util.SnowFlakeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping("/connection")
public class DBConnectionController {

    SnowFlakeId worker = new SnowFlakeId(1,1,1);

    @Autowired
    DBConnectionDao dbConnectionDao;

    @RequestMapping("/findAll")
    @ResponseBody
    public List<DBConnection> findAll(){
        return dbConnectionDao.findAllByOrderByIdDesc();
    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public boolean deleteById(Long id){
        DBConnection connection = dbConnectionDao.findById(id).orElse(null);
        if(connection != null){
            dbConnectionDao.delete(connection);
        }
        return true;
    }

    @RequestMapping("/save")
    @ResponseBody
    public Long save(DBConnection connection){
        if(connection.getId() == null){
            connection.setId(worker.nextId());
        }
        dbConnectionDao.save(connection);
        return connection.getId();
    }

}
