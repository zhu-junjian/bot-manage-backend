package com.mirrormetech.corm.core.device.domain;

import com.mirrormetech.corm.core.device.domain.vo.DeviceVO;
import com.mirrormetech.corm.core.device.infra.DO.DeviceDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Component
public class DeviceFactory {

    public List<DeviceVO> createDeviceVOList(List<DeviceDO> deviceDOList) {
        List<DeviceVO> deviceVOList = new ArrayList<>();
        for (DeviceDO deviceDO : deviceDOList) {
            DeviceVO deviceVO = new DeviceVO();
            BeanUtils.copyProperties(deviceDO, deviceVO);
            deviceVOList.add(deviceVO);
        }
        return deviceVOList;
    }
}
