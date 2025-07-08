package com.mirrormetech.corm.core.user.infra.persistence;

import com.mirrormetech.corm.core.user.domain.repository.UserRepository;
import com.mirrormetech.corm.core.user.domain.vo.UserUpdateVO;
import com.mirrormetech.corm.core.user.domain.vo.UserVO;
import com.mirrormetech.corm.core.user.infra.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户仓储层接口
 */
@Service
@RequiredArgsConstructor
public class MyBatisUserRepoImpl implements UserRepository {

    private final UserMapper userMapper;

    /**
     * 修改用户资料 & 返回被修改后的用户信息摘要
     * @param userUpdateVO 修改内容 & 用户ID
     */
    public UserUpdateVO updateUserProfile(UserUpdateVO userUpdateVO) {
        userMapper.updateUserProfile(userUpdateVO, userUpdateVO.getId());
        return userMapper.selectUpdatedByUserId(userUpdateVO.getId());
    }

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户详情
     */
    public UserVO getUserById(Long userId) {
        return userMapper.selectByUserId(userId);
    }

    /**
     * 用户是否存在
     * @param userId 用户ID
     * @return 是否存在
     */
    @Override
    public boolean userExists(Long userId) {
        return userMapper.selectByUserId(userId) != null;
    }

    /**
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }
}
