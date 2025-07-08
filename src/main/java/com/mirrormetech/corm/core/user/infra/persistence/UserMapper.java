package com.mirrormetech.corm.core.user.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.user.domain.vo.UserUpdateVO;
import com.mirrormetech.corm.core.user.domain.vo.UserVO;
import com.mirrormetech.corm.core.user.infra.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User findByUsername(String username);

    void updateUserProfile(@Param("req") UserUpdateVO vo, @Param("userId") Long userId);

    // 根据用户名查询用户
    @Select("SELECT * FROM tb_user WHERE id = #{id}")
    UserVO selectByUserId(@Param("id") Long id);

    @Select("SELECT * FROM tb_user WHERE id = #{id}")
    UserUpdateVO selectUpdatedByUserId(@Param("id") Long id);

    // 根据用户名查询用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    // 根据邮箱查询用户
    @Select("SELECT * FROM user WHERE email = #{email}")
    User selectByEmail(@Param("email") String email);

    // 根据手机号查询用户
    @Select("SELECT * FROM user WHERE phone_num = #{phoneNum}")
    User selectByPhone(@Param("phoneNum") String phoneNum);

    // 根据UID查询用户
    @Select("SELECT * FROM user WHERE uid = #{uid}")
    User selectByUid(@Param("uid") String uid);

    // 根据状态查询用户
    @Select("SELECT * FROM user WHERE status = #{status}")
    List<User> selectByStatus(@Param("status") Integer status);

    // 根据用户类型查询用户
    @Select("SELECT * FROM user WHERE user_type = #{userType}")
    List<User> selectByUserType(@Param("userType") Integer userType);

    // 根据昵称模糊查询用户
    @Select("SELECT * FROM user WHERE nick_name LIKE CONCAT('%', #{nickName}, '%')")
    List<User> selectByNickName(@Param("nickName") String nickName);

    // 更新用户状态
    @Update("UPDATE user SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    // 更新用户头像
    @Update("UPDATE user SET avatar_url = #{avatarUrl} WHERE id = #{id}")
    int updateAvatar(@Param("id") Long id, @Param("avatarUrl") String avatarUrl);

}
