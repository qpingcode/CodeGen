package me.qping.utils.codegen.dao;

import me.qping.utils.codegen.bean.h2.DBConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName CategoryRepository
 * @Author qping
 * @Date 2019-08-07
 * @Version 1.0
 **/
public interface DBConnectionDao extends JpaRepository<DBConnection, Long>, JpaSpecificationExecutor<DBConnection> {
    List<DBConnection> findAllByOrderByIdDesc();
}