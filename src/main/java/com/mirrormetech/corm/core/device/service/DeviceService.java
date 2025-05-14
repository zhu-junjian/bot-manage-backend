package com.mirrormetech.corm.core.device.service;

import com.mirrormetech.corm.core.device.domain.dto.DeviceBindDTO;
import com.mirrormetech.corm.core.device.domain.dto.DeviceUpdateDTO;
import com.mirrormetech.corm.core.device.domain.vo.DeviceVO;

import java.util.List;

public interface DeviceService {

    void bindDevice(DeviceBindDTO dto);

    List<DeviceVO> createDeviceVOList(Long userId);

    void updateDevice(DeviceUpdateDTO dto);
}
