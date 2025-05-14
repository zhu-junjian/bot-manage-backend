package com.mirrormetech.corm.core.device.domain.repository;

import com.mirrormetech.corm.core.device.domain.dto.DeviceBindDTO;
import com.mirrormetech.corm.core.device.domain.dto.DeviceUpdateDTO;
import com.mirrormetech.corm.core.device.infra.DO.DeviceDO;

import java.util.List;

public interface DeviceRepository {

    void bindDevice(DeviceBindDTO dto);

    List<DeviceDO> findAllByUserId(Long userId);

    void updateDevice(DeviceUpdateDTO dto);
}
