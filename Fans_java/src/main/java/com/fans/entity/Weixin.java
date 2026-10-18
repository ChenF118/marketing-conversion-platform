package com.fans.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fans.common.ser.ControlSer;
import com.fans.common.ser.IntBoolSer;

import java.time.LocalDateTime;

/** wx_atr.manage_wxnumber */
@TableName(value = "manage_wxnumber", schema = "wx_atr")
public class Weixin {

    @TableId(type = IdType.AUTO)
    public Integer id;

    @com.baomidou.mybatisplus.annotation.TableField("ownerId")
    public Integer ownerId;

    /** 微信分组ID */
    public Integer uid;

    public String wxh;
    public String wxname;
    public String erweima;
    public String sex;

    @JsonSerialize(using = IntBoolSer.class)
    public Integer online;

    @JsonSerialize(using = ControlSer.class)
    public Integer control;

    public Integer level;
    public String remarks;
    public Integer visits;

    /** 原列名 `copy`，Java 侧改为 copyCount，JSON 仍输出 copy */
    @TableField("copyCount")
    @JsonProperty("copy")
    public Integer copyCount;

    public Integer report;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updateTime;
}
