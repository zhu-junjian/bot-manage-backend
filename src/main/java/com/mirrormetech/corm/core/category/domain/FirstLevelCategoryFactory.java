package com.mirrormetech.corm.core.category.domain;

import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.domain.vo.FirstLevelCategoryVO;
import com.mirrormetech.corm.core.category.infra.DO.FirstLevelCategoryDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * firstLevelCategory 对象转换或者生产的过程放在此处
 * @author spencer
 * @date 2025/05/08
 */
@Component
public class FirstLevelCategoryFactory {

    public FirstLevelCategoryDO createDO(FirstLevelCategory firstLevelCategory){
        FirstLevelCategoryDO firstLevelCategoryDO = new FirstLevelCategoryDO();
        BeanUtils.copyProperties(firstLevelCategory, firstLevelCategoryDO);
        return firstLevelCategoryDO;
    }

    public FirstLevelCategoryDO createDOByDTO(CategoryDTO categoryDTO){
        FirstLevelCategoryDO firstLevelCategoryDO = new FirstLevelCategoryDO();
        firstLevelCategoryDO.setName(categoryDTO.getCategoryName());
        return firstLevelCategoryDO;
    }

    public FirstLevelCategory create(FirstLevelCategoryDO firstLevelCategoryDO){
        FirstLevelCategory firstLevelCategory = new FirstLevelCategory();
        BeanUtils.copyProperties(firstLevelCategoryDO, firstLevelCategory);
        return firstLevelCategory;
    }

    /**
     * 将DO列表转换为前端展示的VO列表
     * @param firstLevelCategoryDOList
     * @return
     */
    public List<FirstLevelCategoryVO> createVOListByDO(List<FirstLevelCategoryDO> firstLevelCategoryDOList){
        List<FirstLevelCategoryVO> firstLevelCategoryVOList = new ArrayList<>();
        firstLevelCategoryDOList.forEach(firstLevelCategoryDO -> {
            FirstLevelCategoryVO firstLevelCategoryVO = new FirstLevelCategoryVO();
            BeanUtils.copyProperties(firstLevelCategoryDO, firstLevelCategoryVO);
            firstLevelCategoryVOList.add(firstLevelCategoryVO);

        });
        return firstLevelCategoryVOList;
    }
}
