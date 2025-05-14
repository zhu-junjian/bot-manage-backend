package com.mirrormetech.corm.core.device.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.device.infra.DO.DeviceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper extends BaseMapper<DeviceDO> {
}
