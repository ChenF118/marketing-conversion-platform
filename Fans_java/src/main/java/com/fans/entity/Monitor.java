package com.fans.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fans.common.ser.JsonTextSer;
import com.fans.common.ser.MonitorTypeSer;

import java.time.LocalDateTime;

/** monitor_atr.cvsmsg_one */
@TableName(value = "cvsmsg_one", schema = "monitor_atr")
public class Monitor {

    @TableId(type = IdType.AUTO)
    public Integer id;

    /** 事件唯一ID，用于防重复上报 */
    @com.baomidou.mybatisplus.annotation.TableField("eventId")
    public String eventId;

    /** 落地页ID */
    public Integer uid;

    public String cvsdate;
    public String cvstime;

    public Integer stay;

    @JsonSerialize(using = MonitorTypeSer.class)
    public Integer type;

    public String target;

    /** city 以 JSON 文本存储 */
    @JsonSerialize(using = JsonTextSer.class)
    public String city;

    public String equipment;
    public String rate;
    public String wx;
    public String search;
    public Integer total;
    public Integer report;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updateTime;
}
