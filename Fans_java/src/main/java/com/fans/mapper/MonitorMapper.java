package com.fans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fans.entity.Monitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MonitorMapper extends BaseMapper<Monitor> {

    /** MonitorCt::getmonitor —— hasWhere('Landing') 过滤 */
    @Select("""
            <script>
            SELECT m.*
            FROM monitor_atr.cvsmsg_one m
            INNER JOIN monitor_atr.edition_landing l ON l.id = m.uid
            <where>
                <if test="q.uid != null">AND m.uid = #{q.uid}</if>
                <if test="q.date1 != null">AND m.cvsdate >= #{q.date1}</if>
                <if test="q.date2 != null">AND m.cvsdate <![CDATA[ <= ]]> #{q.date2}</if>
                <if test="q.report != null">AND m.report = #{q.report}</if>
                <if test="q.urlLike != null">AND l.url LIKE CONCAT('%', #{q.urlLike}, '%')</if>
                <if test="q.gid != null">AND l.gid = #{q.gid}</if>
                <if test="q.ownerId != null">AND l.ownerId = #{q.ownerId}</if>
            </where>
            ORDER BY m.cvsdate DESC, m.id DESC
            LIMIT #{offset}, #{size}
            </script>
            """)
    List<Monitor> selectPageWithLanding(@Param("q") Map<String, Object> q,
                                        @Param("offset") long offset,
                                        @Param("size") long size);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM monitor_atr.cvsmsg_one m
            INNER JOIN monitor_atr.edition_landing l ON l.id = m.uid
            <where>
                <if test="q.uid != null">AND m.uid = #{q.uid}</if>
                <if test="q.date1 != null">AND m.cvsdate >= #{q.date1}</if>
                <if test="q.date2 != null">AND m.cvsdate <![CDATA[ <= ]]> #{q.date2}</if>
                <if test="q.report != null">AND m.report = #{q.report}</if>
                <if test="q.urlLike != null">AND l.url LIKE CONCAT('%', #{q.urlLike}, '%')</if>
                <if test="q.gid != null">AND l.gid = #{q.gid}</if>
                <if test="q.ownerId != null">AND l.ownerId = #{q.ownerId}</if>
            </where>
            </script>
            """)
    long countWithLanding(@Param("q") Map<String, Object> q);

    @Select("""
            <script>
            SELECT COUNT(*) FROM monitor_atr.cvsmsg_one WHERE cvsdate = #{day}
            <if test="ownerId != null">AND uid IN (SELECT id FROM monitor_atr.edition_landing WHERE ownerId = #{ownerId})</if>
            </script>
            """)
    long countPv(@Param("day") String day, @Param("ownerId") Integer ownerId);

    /** UV 统计：MySQL 5.5 无 JSON 函数，按 city 去重 */
    @Select("""
            <script>
            SELECT COUNT(DISTINCT city) FROM monitor_atr.cvsmsg_one WHERE cvsdate = #{day}
            <if test="ownerId != null">AND uid IN (SELECT id FROM monitor_atr.edition_landing WHERE ownerId = #{ownerId})</if>
            </script>
            """)
    long countUv(@Param("day") String day, @Param("ownerId") Integer ownerId);

    @Select("""
            <script>
            SELECT COUNT(DISTINCT city) FROM monitor_atr.cvsmsg_one
            WHERE cvsdate = #{day} AND cvstime IS NOT NULL
            <if test="ownerId != null">AND uid IN (SELECT id FROM monitor_atr.edition_landing WHERE ownerId = #{ownerId})</if>
            </script>
            """)
    long countConverted(@Param("day") String day, @Param("ownerId") Integer ownerId);
}
