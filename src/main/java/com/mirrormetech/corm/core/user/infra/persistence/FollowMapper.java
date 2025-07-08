package com.mirrormetech.corm.core.user.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.user.domain.dto.UserFollowDTO;
import com.mirrormetech.corm.core.user.infra.DO.UserFollow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowMapper extends BaseMapper<UserFollow> {

    /**
     * 查询用户是否关注了多个目标用户
     * @param sourceId 源用户ID
     * @param targetIds 目标客户ID集合
     * @return 存在关注关系的ID集合
     */
    @Select("<script>" +
            "SELECT following_id FROM user_follow " +
            "WHERE follower_id = #{sourceId} " +
            "AND following_id IN " +
            "<foreach item='id' collection='targetIds' open='(' separator=',' close=')'>" +
            "   #{id}" +
            "</foreach>" +
            "</script>")
    List<Long> findFollowingIds(@Param("sourceId") Long sourceId,
                                @Param("targetIds") List<Long> targetIds);

    /**
     * 查询多个目标用户是否关注了当前用户
     * @param sourceId 源用户ID
     * @param targetIds 目标客户ID集合
     * @return 粉丝ID集合
     */
    @Select("<script>" +
            "SELECT follower_id FROM user_follow " +
            "WHERE following_id = #{sourceId} " +
            "AND follower_id IN " +
            "<foreach item='id' collection='targetIds' open='(' separator=',' close=')'>" +
            "   #{id}" +
            "</foreach>" +
            "</script>")
    List<Long> findFollowerIds(@Param("sourceId") Long sourceId,
                               @Param("targetIds") List<Long> targetIds);

    @Select("SELECT COUNT(1) > 0 FROM user_follow " +
            "WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    boolean existsFollowRelation(@Param("followerId") Long followerId,
                                 @Param("followingId") Long followingId);

    @Delete("DELETE FROM user_follow " +
            "WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    int deleteFollowRelation(@Param("followerId") Long followerId,
                             @Param("followingId") Long followingId);

    /**
     * userId 关注的 用户ids
     * @param page 分页查询条件
     * @param userId 目标用户ID
     * @return 关注用户信息列表
     */
    @Select("SELECT u.* FROM tb_user u JOIN user_follow uf ON u.id = uf.following_id where uf.follower_id = #{userId}")
    Page<UserFollowDTO> selectFollowingIds(Page<UserFollowDTO> page, @Param("userId") Long userId);

    /**
     * 根据用户ID查询用户当前粉丝列表
     * @param page 分页查询条件
     * @param userId 当前目标用户id
     * @return 粉丝用户信息列表
     */
    @Select("SELECT u.*  FROM tb_user u JOIN user_follow uf ON u.id = uf.follower_id where uf.following_id =  #{userId}")
    Page<UserFollowDTO> selectFollowerIds(Page<UserFollowDTO> page, @Param("userId") Long userId);

    Page<UserFollowDTO> selectFollowerId(Page<UserFollowDTO> page, @Param("userId") Long userId);

    /**
     * 查询用户关注数
     * @param userId 当前用户ID
     * @return 关注数
     */
    @Select("SELECT COUNT(*) FROM user_follow uf where uf.follower_id = #{userId}")
    Integer selectFollowingCount(@Param("userId") Long userId);

    /**
     * 查询用户粉丝数
     * @param userId 当前用户ID
     * @return 粉丝数
     */
    @Select("SELECT COUNT(*) FROM user_follow uf where uf.following_id = #{userId}")
    Integer selectFollowerCount(@Param("userId") Long userId);

}
