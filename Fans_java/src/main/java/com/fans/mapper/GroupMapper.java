package com.fans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fans.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    /** withCount('Landing') 的批量版本 */
    @Select("""
            <script>
            SELECT uid, COUNT(*) AS cnt
            FROM monitor_atr.edition_landing
            WHERE uid IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">#{id}</foreach>
            GROUP BY uid
            </script>
            """)
    List<Map<String, Object>> countLandingByUids(@Param("ids") List<Integer> ids);
}
