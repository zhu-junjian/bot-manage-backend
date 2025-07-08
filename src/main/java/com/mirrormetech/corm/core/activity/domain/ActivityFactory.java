package com.mirrormetech.corm.core.activity.domain;

import com.mirrormetech.corm.core.activity.infra.ActivityDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * 活动工厂对象
 */
@Component
public class ActivityFactory {

    /**
     * ActivityDO to Activity
     * @param activityDO database object
     * @return domain object
     */
    public Activity create(ActivityDO activityDO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityDO, activity);
        return activity;
    }

    /**
     * Activity to ActivityDO
     * @param activity domain object
     * @return database object
     */
    public ActivityDO createDO(Activity activity) {
        ActivityDO activityDO = new ActivityDO();
        BeanUtils.copyProperties(activity, activityDO);
        return activityDO;
    }
}
