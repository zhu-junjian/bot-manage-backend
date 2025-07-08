package com.mirrormetech.corm.core.post.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.post.domain.Post;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/06
 */
@Repository
@Mapper
public interface PostMapper extends BaseMapper<PostDO> {

    Page<PostDO> selectPostDOs(Page<PostDO> page, @Param("req") QueryListDTO queryListDTO);

    Page<Post> selectPosts(Page<Post> page, @Param("req") QueryListDTO queryListDTO);

    @Select("SELECT " +
            "  pr.*, " +
            "  COUNT(upl.id) AS like_count" +
            " FROM post_record pr" +
            " LEFT JOIN user_post_like upl " +
            "  ON pr.id = upl.post_id" +
            "  AND upl.status = 0 " +
            "  AND pr.status = 0  " +
            " WHERE pr.user_id = #{userId} " +
            " GROUP BY pr.id " +
            " ORDER BY pr.publish_time DESC ")
    Page<PostDO> selectUserWorksByUserId(Page<Object> page, @Param("userId") Long userId);

    @Select("<script>" +
            " SELECT post_id FROM user_post_like upl " +
            " WHERE upl.user_id = #{userId} " +
            " AND upl.status = 0 " +
            " AND upl.post_id IN " +
            " <foreach item='id' collection='targetIds' open='(' separator=',' close=')'>" +
            "   #{id}" +
            " </foreach>" +
            " </script>")
    List<Long> selectLikedPostIdsByUserId(@Param("userId") Long userId, List<Long> targetIds);

    @Select("<script>" +
            " SELECT pr.*," +
            " COUNT( upl.id ) AS like_count FROM post_record pr LEFT JOIN user_post_like upl ON pr.id = upl.post_id AND upl.STATUS = 0 " +
            " WHERE pr.STATUS = 0 " +
            " AND pr.id IN  " +
            " <foreach item='id' collection='targetIds' open='(' separator=',' close=')'>" +
            "   #{id}" +
            " </foreach>" +
            " GROUP BY pr.id ORDER BY pr.publish_time DESC "+
            " </script>")
    Page<PostDO> selectUserLikedWorksByUserId(Page<Object> page,List<Long> targetIds);

    /**
     * 查询
     * @param userId
     * @return
     */
    @Select("SELECT post_id FROM user_post_like upl LEFT JOIN post_record pr ON pr.id = upl.post_id  " +
            "WHERE pr.STATUS = 0 AND upl.user_id = #{userId} AND upl.status = 0 ")
    List<Long> selectPostIdByUserId(Page<Object> page, Long userId);

    @Update("UPDATE post_record pr SET pr.nick_name = #{nickName} WHERE pr.user_id = #{userId}")
    int updatePosterNickName(Long userId, String nickName);
}
