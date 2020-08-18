package me.qping.utils.codegen.bean.h2;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Entity
@Data
@Table(name = "DB_CONNECTION")
public class DBConnection {

    public static final int TYPE_MYSQL = 1;
    public static final int TYPE_SQLSERVER = 2;
    public static final int TYPE_ORACLE = 3;
    public static final int TYPE_POSTGRESQL = 6;

    @Id
    @Column(name = "id")
    @JsonSerialize(using=ToStringSerializer.class)  // fix js中long类型精度丢失的问题
    Long id;

    @Basic
    @Column(name = "database_type")
    Integer databaseType; // 数据库类型：1、mysql 2、sqlserver 3、oracle 6、postgresql

    @Basic
    @Column(name = "name")
    String name;

    @Basic
    @Column(name = "host")
    String host;

    @Basic
    @Column(name = "port")
    Integer port;

    @Basic
    @Column(name = "username")
    String username;

    @Basic
    @Column(name = "password")
    String password;

    @Basic
    @Column(name = "database_name")
    String databaseName;

}
