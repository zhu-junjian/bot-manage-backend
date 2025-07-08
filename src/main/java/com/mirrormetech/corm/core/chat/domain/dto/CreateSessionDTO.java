package com.mirrormetech.corm.core.chat.domain.dto;


import lombok.Data;

@Data
public class CreateSessionDTO {

    public Long initiator;

    public Long recipient;

    public Integer sessionType;
}
