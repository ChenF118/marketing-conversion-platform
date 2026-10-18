package com.fans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fans.entity.Cvslink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CvslinkMapper extends BaseMapper<Cvslink> {

    /** CvslinkCt::getbelongs —— hasWhere('landing') + 落地页条件 */
    @Select("""
            <script>
            SELECT c.*
            FROM monitor_atr.cvsmsg_link c
            INNER JOIN monitor_atr.edition_landing l ON l.id = c.uid
            <where>
                <if test="q.uid != null">AND l.uid = #{q.uid}</if>
                <if test="q.urlLike != null">AND l.url LIKE CONCAT('%', #{q.urlLike}, '%')</if>
                <if test="q.ownerId != null">AND c.ownerId = #{q.ownerId}</if>
            </where>
            ORDER BY c.id ASC
            LIMIT #{offset}, #{size}
            </script>
            """)
    List<Cvslink> selectBelongsPage(@Param("q") Map<String, Object> q,
                                    @Param("offset") long offset,
                                    @Param("size") long size);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM monitor_atr.cvsmsg_link c
            INNER JOIN monitor_atr.edition_landing l ON l.id = c.uid
            <where>
                <if test="q.uid != null">AND l.uid = #{q.uid}</if>
                <if test="q.urlLike != null">AND l.url LIKE CONCAT('%', #{q.urlLike}, '%')</if>
                <if test="q.ownerId != null">AND c.ownerId = #{q.ownerId}</if>
            </where>
            </script>
            """)
    long countBelongs(@Param("q") Map<String, Object> q);

    /** 落地页侧：按页面 URL 找第一条启用中的统计链接（CvslinkCt::forlandingget 的真实逻辑） */
    @Select("""
            <script>
            SELECT c.*
            FROM monitor_atr.cvsmsg_link c
            INNER JOIN monitor_atr.edition_landing l ON l.id = c.uid
            WHERE l.enable = 1
            <if test="words != null and words != ''">AND l.url LIKE CONCAT('%', #{words}, '%')</if>
            ORDER BY c.id ASC
            LIMIT 1
            </script>
            """)
    Cvslink selectFirstForLanding(@Param("words") String words);

    /** CvslinkCt::getcvsrate —— 只取 cvstype，其余字段用于装配 Landing */
    @Select("""
            <script>
            SELECT c.id, c.uid, c.cvstype
            FROM monitor_atr.cvsmsg_link c
            INNER JOIN monitor_atr.edition_landing l ON l.id = c.uid
            <where>
                <if test="q.uid != null">AND l.uid = #{q.uid}</if>
                <if test="q.landingId != null">AND l.id = #{q.landingId}</if>
                <if test="q.urlLike != null">AND l.url LIKE CONCAT('%', #{q.urlLike}, '%')</if>
                <if test="q.ownerId != null">AND c.ownerId = #{q.ownerId}</if>
                <if test="q.date1 != null">AND l.neardate >= #{q.date1}</if>
                <if test="q.date2 != null">AND l.neardate <![CDATA[ <= ]]> #{q.date2}</if>
                <if test="q.cvstype != null">AND c.cvstype = #{q.cvstype}</if>
            </where>
            ORDER BY c.id ASC
            LIMIT #{offset}, #{size}
            </script>
            """)
    List<Cvslink> selectRatePage(@Param("q") Map<String, Object> q,
                                 @Param("offset") long offset,
                                 @Param("size") long size);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM monitor_atr.cvsmsg_link c
            INNER JOIN monitor_atr.edition_landing l ON l.id = c.uid
            <where>
                <if test="q.uid != null">AND l.uid = #{q.uid}</if>
                <if test="q.landingId != null">AND l.id = #{q.landingId}</if>
                <if test="q.urlLike != null">AND l.url LIKE CONCAT('%', #{q.urlLike}, '%')</if>
                <if test="q.ownerId != null">AND c.ownerId = #{q.ownerId}</if>
                <if test="q.date1 != null">AND l.neardate >= #{q.date1}</if>
                <if test="q.date2 != null">AND l.neardate <![CDATA[ <= ]]> #{q.date2}</if>
                <if test="q.cvstype != null">AND c.cvstype = #{q.cvstype}</if>
            </where>
            </script>
            """)
    long countRate(@Param("q") Map<String, Object> q);
}
