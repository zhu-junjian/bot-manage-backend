package com.mirrormetech.corm.core.device.service.impl;

import com.mirrormetech.corm.common.security.SecurityContextUtil;
import com.mirrormetech.corm.core.device.domain.DeviceDomainService;
import com.mirrormetech.corm.core.device.domain.dto.DeviceBindDTO;
import com.mirrormetech.corm.core.device.domain.dto.DeviceUpdateDTO;
import com.mirrormetech.corm.core.device.domain.vo.DeviceVO;
import com.mirrormetech.corm.core.device.infra.DO.DeviceDO;
import com.mirrormetech.corm.core.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceDomainService deviceDomainService;

    private final SecurityContextUtil securityContextUtil;

    public void bindDevice(DeviceBindDTO dto) {
        deviceDomainService.bindDevice(dto);
    }

    @Override
    public List<DeviceVO> createDeviceVOList(Long userId) {
        if (userId == null) {
            userId = securityContextUtil.getCurrentUserId();
        }
        return deviceDomainService.findAllByUserId(userId);
    }

    @Override
    public void updateDevice(DeviceUpdateDTO dto) {
        deviceDomainService.updateDevice(dto);
    }
}
