package com.mirrormetech.corm.core.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author spencer
 * @date 2025/04/18
 */
@Data
@AllArgsConstructor
public class RegisterResponse {

    private String code;
    private String msg;
}
