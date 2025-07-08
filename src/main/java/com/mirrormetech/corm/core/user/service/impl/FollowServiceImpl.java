package com.mirrormetech.corm.core.user.service.impl;

import com.mirrormetech.corm.core.user.domain.FollowDomainService;
import com.mirrormetech.corm.core.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowDomainService followDomainService;

    public void followUser(Long followerId, Long followingId){
        followDomainService.followUser(followerId, followingId);
    }
}
