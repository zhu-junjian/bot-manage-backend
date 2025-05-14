package com.mirrormetech.corm.core.category.domain;

import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.domain.vo.SecondLevelCategoryVO;
import com.mirrormetech.corm.core.category.infra.DO.FirstLevelCategoryDO;
import com.mirrormetech.corm.core.category.infra.DO.SecondLevelCategoryDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Component
public class SecondLevelCategoryFactory {

    public SecondLevelCategoryDO createDO(SecondLevelCategory secondLevelCategory){
        SecondLevelCategoryDO secondLevelCategoryDO = new SecondLevelCategoryDO();
        BeanUtils.copyProperties(secondLevelCategory, secondLevelCategoryDO);
        return secondLevelCategoryDO;
    }

    public SecondLevelCategory create(SecondLevelCategoryDO secondLevelCategoryDO){
        SecondLevelCategory secondLevelCategory = new SecondLevelCategory();
        BeanUtils.copyProperties(secondLevelCategoryDO, secondLevelCategory);
        return secondLevelCategory;
    }

    public SecondLevelCategoryDO createDOByDTO(CategoryDTO categoryDTO){
        SecondLevelCategoryDO secondLevelCategoryDO = new SecondLevelCategoryDO();
        secondLevelCategoryDO.setName(categoryDTO.getCategoryName());
        secondLevelCategoryDO.setParentId(categoryDTO.getFirstLevelCategoryId());
        return secondLevelCategoryDO;
    }

    public List<SecondLevelCategoryVO> createVOListByDO(List<SecondLevelCategoryDO> secondLevelCategoryDOList){
        List<SecondLevelCategoryVO> secondLevelCategoryVOList = new ArrayList<>();
        for (SecondLevelCategoryDO secondLevelCategoryDO : secondLevelCategoryDOList) {
            SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
            BeanUtils.copyProperties(secondLevelCategoryDO, secondLevelCategoryVO);
            secondLevelCategoryVOList.add(secondLevelCategoryVO);
        }
        return secondLevelCategoryVOList;
    }
}
