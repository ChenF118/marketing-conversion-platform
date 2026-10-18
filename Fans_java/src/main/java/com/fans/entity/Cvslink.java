package com.fans.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fans.common.ser.CvsmodeSer;
import com.fans.common.ser.CvstypeSer;

import java.time.LocalDateTime;

/** monitor_atr.cvsmsg_link */
@TableName(value = "cvsmsg_link", schema = "monitor_atr")
public class Cvslink {

    @TableId(type = IdType.AUTO)
    public Integer id;

    @com.baomidou.mybatisplus.annotation.TableField("ownerId")
    public Integer ownerId;

    /** 落地页ID */
    public Integer uid;

    public String type;

    @JsonSerialize(using = CvstypeSer.class)
    public Integer cvstype;

    public Integer cvscount;

    @JsonSerialize(using = CvsmodeSer.class)
    public Integer cvsmode;

    public String code;

    /** 转化时长（秒） */
    public Integer second;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updateTime;
}
