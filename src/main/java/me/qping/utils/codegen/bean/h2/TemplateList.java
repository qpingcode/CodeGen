package me.qping.utils.codegen.bean.h2;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "TEMPLATE_LIST")
public class TemplateList {

    @Id
    @Column(name = "id")
    @JsonSerialize(using=ToStringSerializer.class)  // fix js中long类型精度丢失的问题
    Long id;

    @Basic
    @Column(name = "name")
    String name;

    @Basic
    @Column(name = "create_date")
    Date createDate;

    @Basic
    @Column(name = "remark")
    String remark;

}
