package com.mirrormetech.corm.core.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationVO {
    private Long sourceId;
    private Long targetId;
    private Integer relation;
}
