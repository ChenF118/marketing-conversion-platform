package com.fans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fans.entity.Weixin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface WeixinMapper extends BaseMapper<Weixin> {

    /** WeixinCt::cotrweixin —— hasWhere('Wxgroup') + with('Wxgroup') */
    @Select("""
            <script>
            SELECT w.*
            FROM wx_atr.manage_wxnumber w
            LEFT JOIN wx_atr.manage_wxgroup g ON g.id = w.uid
            <where>
                <if test="q.groupName != null">AND g.name = #{q.groupName}</if>
                <if test="q.wxhLike != null">AND w.wxh LIKE CONCAT('%', #{q.wxhLike}, '%')</if>
                <if test="q.online != null">AND w.online = #{q.online}</if>
                <if test="q.ownerId != null">AND w.ownerId = #{q.ownerId}</if>
                <if test="q.erweimaNull">AND w.erweima IS NULL</if>
                <if test="q.erweimaNotEmpty">AND w.erweima IS NOT NULL AND w.erweima != ''</if>
            </where>
            <if test="q.orderClause != null">ORDER BY ${q.orderClause}</if>
            <if test="q.orderClause == null">ORDER BY w.id DESC</if>
            LIMIT #{offset}, #{size}
            </script>
            """)
    List<Weixin> selectCotrPage(@Param("q") Map<String, Object> q,
                                @Param("offset") long offset,
                                @Param("size") long size);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM wx_atr.manage_wxnumber w
            LEFT JOIN wx_atr.manage_wxgroup g ON g.id = w.uid
            <where>
                <if test="q.groupName != null">AND g.name = #{q.groupName}</if>
                <if test="q.wxhLike != null">AND w.wxh LIKE CONCAT('%', #{q.wxhLike}, '%')</if>
                <if test="q.online != null">AND w.online = #{q.online}</if>
                <if test="q.ownerId != null">AND w.ownerId = #{q.ownerId}</if>
                <if test="q.erweimaNull">AND w.erweima IS NULL</if>
                <if test="q.erweimaNotEmpty">AND w.erweima IS NOT NULL AND w.erweima != ''</if>
            </where>
            </script>
            """)
    long countCotr(@Param("q") Map<String, Object> q);

    /** Wxgroup::withCount('Weixin') 的批量版本 */
    @Select("""
            <script>
            SELECT uid, COUNT(*) AS cnt
            FROM wx_atr.manage_wxnumber
            WHERE uid IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">#{id}</foreach>
            GROUP BY uid
            </script>
            """)
    List<Map<String, Object>> countByGroupIds(@Param("ids") List<Integer> ids);
}
