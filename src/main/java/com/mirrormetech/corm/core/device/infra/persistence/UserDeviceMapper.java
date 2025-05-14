package com.mirrormetech.corm.core.device.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.device.infra.DO.UserDeviceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Mapper
public interface UserDeviceMapper extends BaseMapper<UserDeviceDO> {
}
