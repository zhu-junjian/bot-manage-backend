package com.mirrormetech.corm.core.activity.domain.repo;

import com.mirrormetech.corm.core.activity.domain.Activity;

/**
 * 领域层 - 仓储类
 * 定义仓储层接口
 */
public interface ActivityRepo {

    public Activity getActivityById(String id);
}
