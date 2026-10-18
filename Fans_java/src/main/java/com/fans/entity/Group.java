package com.fans.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/** monitor_atr.edition_group */
@TableName(value = "edition_group", schema = "monitor_atr")
public class Group {

    @TableId(type = IdType.AUTO)
    public Integer id;

    @com.baomidou.mybatisplus.annotation.TableField("ownerId")
    public Integer ownerId;

    /** 原列名 `group`（MySQL 保留字），Java 侧改为 groupName，JSON 仍输出 group */
    @TableField("groupName")
    @JsonProperty("group")
    public String groupName;

    public String remarks;
    public Integer total;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updateTime;
}
