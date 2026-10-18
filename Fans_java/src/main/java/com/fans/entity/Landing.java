package com.fans.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fans.common.ser.IntBoolSer;

import java.time.LocalDateTime;

/** monitor_atr.edition_landing */
@TableName(value = "edition_landing", schema = "monitor_atr")
public class Landing {

    @TableId(type = IdType.AUTO)
    public Integer id;

    /** 数据归属管理员ID（0/空 = 历史数据，仅超管可见） */
    @com.baomidou.mybatisplus.annotation.TableField("ownerId")
    public Integer ownerId;

    /** 分组ID */
    public Integer uid;

    /** 微信分组ID */
    public Integer gid;

    public String url;
    public String remarks;

    @JsonSerialize(using = IntBoolSer.class)
    public Integer delivery;

    @JsonSerialize(using = IntBoolSer.class)
    public Integer track;

    @JsonSerialize(using = IntBoolSer.class)
    public Integer enable;

    public String erweima;

    /** 原列名 `from`，Java 侧改为 sourceUrl，JSON 仍输出 from */
    @TableField("sourceUrl")
    @JsonProperty("from")
    public String sourceUrl;

    public String weixin;
    public String mclick;
    public String mtouch;
    public String pclick;
    public String pselect;

    /** 原列名 `show`，Java 侧改为 visible，JSON 仍输出 show */
    @TableField("visible")
    @JsonProperty("show")
    @JsonSerialize(using = IntBoolSer.class)
    public Integer visible;

    public Integer share;
    public String same;
    public Integer total;
    public Integer cvscount;
    public Integer fans;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime neardate;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updateTime;
}
