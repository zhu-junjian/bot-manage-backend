package com.mirrormetech.corm.core.device.domain;

import com.mirrormetech.corm.core.device.domain.dto.DeviceBindDTO;
import com.mirrormetech.corm.core.device.domain.dto.DeviceUpdateDTO;
import com.mirrormetech.corm.core.device.domain.repository.DeviceRepository;
import com.mirrormetech.corm.core.device.domain.vo.DeviceVO;
import com.mirrormetech.corm.core.device.infra.DO.DeviceDO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Service
@RequiredArgsConstructor
public class DeviceDomainService {

    @Qualifier("mybatisDeviceRepository")
    private final DeviceRepository myBatisDeviceRepository;

    private final DeviceFactory deviceFactory;

    public void bindDevice(DeviceBindDTO deviceBindDTO) {
        myBatisDeviceRepository.bindDevice(deviceBindDTO);
    }

    public List<DeviceVO> findAllByUserId(Long userId) {
        return deviceFactory.createDeviceVOList(myBatisDeviceRepository.findAllByUserId(userId));
    }

    public void updateDevice(DeviceUpdateDTO deviceUpdateDTO) {
        myBatisDeviceRepository.updateDevice(deviceUpdateDTO);
    }
}
