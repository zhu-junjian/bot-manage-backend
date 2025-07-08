package com.mirrormetech.corm.core.user.domain.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RelationQuery {

    @Min(1)
    private Long sourceId;

    @Min(1)
    private Long targetId;
}
