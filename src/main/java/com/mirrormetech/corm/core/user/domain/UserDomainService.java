package com.mirrormetech.corm.core.user.domain;

import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.common.security.SecurityContextUtil;
import com.mirrormetech.corm.core.like.domain.PostLikeDomainService;
import com.mirrormetech.corm.core.post.domain.PostDomainService;
import com.mirrormetech.corm.core.user.domain.dto.FollowUserDTO;
import com.mirrormetech.corm.core.user.domain.vo.UserUpdateVO;
import com.mirrormetech.corm.core.user.domain.vo.UserVO;
import com.mirrormetech.corm.core.user.infra.User;
import com.mirrormetech.corm.core.user.infra.persistence.MyBatisFollowRepoImpl;
import com.mirrormetech.corm.core.user.infra.persistence.MyBatisUserRepoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户领域服务
 * 封装对应用层的接口
 * 复用领域能力
 */
@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final MyBatisUserRepoImpl myBatisUserRepo;

    private final FollowDomainService followDomainService;

    private final MyBatisFollowRepoImpl myBatisFollowRepo;

    private final SecurityContextUtil securityContextUtil;

    private final PostLikeDomainService postLikeDomainService;

    private final PostDomainService postDomainService;

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户详情
     */
    public UserVO getUserById(Long userId) {
        return myBatisUserRepo.getUserById(userId);
    }

    /**
     * 被修改的用户ID & 需修改内容
     * @param userVO 修改内容
     */
    @Transactional
    public UserUpdateVO updateUserProfile(UserUpdateVO userVO) {
        /*
            1.判断当前登录用户 与 userId是否一致
            2.判断当前登录用户是否为管理员类型
         */
        User user = securityContextUtil.getCurrentUser();
        Long userId = userVO.getId();
        if(!(user.isCurrent(userId) || user.getUserType().equals(UserType.ADMINISTRATION.getCode()))){
            throw new ServiceException(ExceptionCode.NO_AUTH_TO_UPDATE);
        }
        UserUpdateVO userUpdateVO = myBatisUserRepo.updateUserProfile(userVO);
        // 如果修改昵称 需要同步修改 post_record表中其发布作品的nickName
        if(userVO.getNickName() != null){
            postDomainService.updatePosterNickNameByUserId(userId, userVO.getNickName());
        }
        return userUpdateVO;
    }

    /**
     * 对用户实体  关注实体能力的复用
     * @param followUserDTO 源用户 与 目标用户
     * @return 目标用户信息 & 目标用户与源用户关注关系
     */
    public UserVO getUserProfileWithRelation(FollowUserDTO followUserDTO) {

        /*
         * 1.参数校验
         * 2.调用 用户域 查询用户信息
         * 3.调用 follow域 查询关注关系 粉丝数 关注数
         * 4.调用 内容域 查询作品点赞数
         * 5.日志/领域事件
         * 6.factory包装数据返回
         */
        //参数校验
        Long targetUserId = followUserDTO.getTargetUserId();
        Long sourceUserId = followUserDTO.getSourceUserId()==null?securityContextUtil.getCurrentUserId():followUserDTO.getSourceUserId();
        if (targetUserId == null || !myBatisUserRepo.userExists(targetUserId) || !myBatisUserRepo.userExists(sourceUserId)) {
            throw  new ServiceException(ExceptionCode.QUERY_ERROR_USER_NOT_EXIST);
        }
        // 查询 用户信息
        UserVO userVO = myBatisUserRepo.getUserById(targetUserId);
        // 查询源用户与目标用户的关注关系
        FollowRelation relation = myBatisFollowRepo.getRelation(sourceUserId, targetUserId);
        // 查询目标用户的粉丝数
        userVO.setFollowers(followDomainService.getFollowersByUserId(targetUserId));
        // 目标用户的关注数
        userVO.setFollowings(followDomainService.getFollowingsByUserId(targetUserId));

        //查询内容域 用户作品的点赞数
        Integer targetUserWorksCount = postLikeDomainService.userWorksLikeCount(targetUserId);

        userVO.setRelation(relation.getCode());
        userVO.setSourceUserId(sourceUserId);
        userVO.setTargetUserId(targetUserId);
        userVO.setTotalLikes(targetUserWorksCount);
        userVO.setIsOwnProfile(targetUserId.equals(sourceUserId));
        return userVO;
    }

}
