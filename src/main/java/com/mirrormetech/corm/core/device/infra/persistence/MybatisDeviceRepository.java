package com.mirrormetech.corm.core.device.infra.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.core.device.domain.dto.DeviceBindDTO;
import com.mirrormetech.corm.core.device.domain.dto.DeviceUpdateDTO;
import com.mirrormetech.corm.core.device.domain.repository.DeviceRepository;
import com.mirrormetech.corm.core.device.infra.DO.DeviceDO;
import com.mirrormetech.corm.core.device.infra.DO.UserDeviceDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.serial.SerialException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Repository
@RequiredArgsConstructor
public class MybatisDeviceRepository implements DeviceRepository {

    private final DeviceMapper deviceMapper;

    private final UserDeviceMapper userDeviceMapper;

    public void bindDevice(DeviceBindDTO dto) {
        DeviceDO deviceDO = deviceMapper.selectOne(new QueryWrapper<DeviceDO>().eq("serial_num", dto.getSn()));
        if(deviceDO == null) {
            throw new ServiceException(ExceptionCode.DEVICE_SN_INVALID.getExceptionCode(),
                    ExceptionCode.DEVICE_SN_INVALID.getExceptionMsg());
        }
        UserDeviceDO userDeviceDO = new UserDeviceDO();
        userDeviceDO.setUserId(dto.getUserId());
        userDeviceDO.setDeviceId(deviceDO.getId());
        userDeviceMapper.insert(userDeviceDO);
    }

    @Override
    public List<DeviceDO> findAllByUserId(Long userId) {
        List<DeviceDO> deviceDOList = new ArrayList<>();
        List<UserDeviceDO> userBindings = userDeviceMapper.selectList(new QueryWrapper<UserDeviceDO>().eq("user_id", userId));
        userBindings.forEach(userBinding -> {
            deviceDOList.add(deviceMapper.selectOne(new QueryWrapper<DeviceDO>().eq("id", userBinding.getDeviceId())));
        });
        return deviceDOList;
    }

    public void updateDevice(DeviceUpdateDTO dto) {
        UpdateWrapper<DeviceDO> updateWrapper = new UpdateWrapper<DeviceDO>().set("name", dto.getName()).set("avatar_url", dto.getAvatarUrl()).eq("id", dto.getId());
        deviceMapper.update(null,updateWrapper);
    }
}
