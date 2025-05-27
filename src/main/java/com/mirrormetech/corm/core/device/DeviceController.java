package com.mirrormetech.corm.core.device;

import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.device.domain.dto.DeviceBindDTO;
import com.mirrormetech.corm.core.device.domain.dto.DeviceUpdateDTO;
import com.mirrormetech.corm.core.device.domain.vo.DeviceVO;
import com.mirrormetech.corm.core.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceServiceImpl;

    @PostMapping("/bind")
    public ApiResult bindDevice(@RequestBody DeviceBindDTO dto) {
        deviceServiceImpl.bindDevice(dto);
        return ApiResult.success();
    }

    @GetMapping("/list")
    public ApiResult<List<DeviceVO>> findAll() {
        return ApiResult.success(deviceServiceImpl.createDeviceVOList(null));
    }

    @PutMapping("update")
    public ApiResult updateDevice(@Validated @RequestBody DeviceUpdateDTO dto) {
        deviceServiceImpl.updateDevice(dto);
        return ApiResult.success();
    }
}
