package com.mirrormetech.corm.core.category.domain;

import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.domain.repository.FirstLevelCategoryRepository;
import com.mirrormetech.corm.core.category.domain.repository.SecondLevelCategoryRepository;
import com.mirrormetech.corm.core.category.domain.vo.FirstLevelCategoryVO;
import com.mirrormetech.corm.core.category.infra.DO.FirstLevelCategoryDO;
import com.mirrormetech.corm.core.category.infra.DO.SecondLevelCategoryDO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Service
@RequiredArgsConstructor
public class CategoryDomainService {

    @Qualifier("myBatisFirstLevelCategoryRepository")
    private final FirstLevelCategoryRepository myBatisFirstLevelCategoryRepository;

    @Qualifier("myBatisSecondLevelCategoryRepository")
    private final SecondLevelCategoryRepository myBatisSecondLevelCategoryRepository;

    private final FirstLevelCategoryFactory firstLevelCategoryFactory;

    private final SecondLevelCategoryFactory secondLevelCategoryFactory;

    public void create(CategoryDTO categoryDTO) {
        //1.校验参数 此处只校验level级别; 其余根据level级别在其实体内完成父节点是否合法校验等
        Integer level = categoryDTO.getCategoryLevel();
        String categoryName = categoryDTO.getCategoryName();
        if (level == null || 0 >= level || level >= 3) {
            throw new ServiceException(ExceptionCode.MISSING_OR_ERROR_CATEGORY_LEVEL);
        }
        if (categoryName == null) {
            throw new ServiceException(ExceptionCode.MISSING_CATEGORY_NAME);
        }
        //2.根据不同级别进入不同函数,返回内容为最终的实体列表
        //2.1在函数内部再次进行参数校验
        //4.包装实体
        //5.保存数据
        switch (level) {
            case 1 -> saveFirstLevelCategory(categoryDTO);
            case 2 -> saveSecondLevelCategory(categoryDTO);
        }
    }

    /**
     * 大类列表查询
     * @return 所有一类 二类元素列表集合
     */
    public List<FirstLevelCategoryVO> findAll() {
        List<FirstLevelCategoryDO> firstLevelCategories = myBatisFirstLevelCategoryRepository.selectAll();
        List<FirstLevelCategoryVO> voListByDO = firstLevelCategoryFactory.createVOListByDO(firstLevelCategories);
        voListByDO.forEach(firstLevelCategoryVO -> {
            List<SecondLevelCategoryDO> mySecondLevelCategories = new ArrayList<>(myBatisSecondLevelCategoryRepository.findByParentId(firstLevelCategoryVO.getId()));
            firstLevelCategoryVO.setSecondLevelCategories(secondLevelCategoryFactory.createVOListByDO(mySecondLevelCategories));
        });
        return voListByDO;
    }

    public void saveFirstLevelCategory(CategoryDTO categoryDTO) {
        // 1. 如果是一级大类 需要先从数据库查询出所有一级大类别表,并完成重复名称校验
        List<FirstLevelCategoryDO> firstLevelCategories = myBatisFirstLevelCategoryRepository.selectAll();
        if (firstLevelCategories != null) {
            boolean nameExist = firstLevelCategories.stream().
                    anyMatch(firstLevelCategory -> firstLevelCategory.getName().equals(categoryDTO.getCategoryName()));
            if (nameExist) {
                throw new ServiceException(ExceptionCode.CATEGORY_NAME_EXISTS);
            }
        }
        // 2.包装实体
        FirstLevelCategoryDO category = firstLevelCategoryFactory.createDOByDTO(categoryDTO);
        // 3.保存数据
        myBatisFirstLevelCategoryRepository.insert(category);
    }

    public void saveSecondLevelCategory(CategoryDTO categoryDTO) {
        //校验父节点合法性
        FirstLevelCategoryDO firstLevelCategory = myBatisFirstLevelCategoryRepository.selectById(categoryDTO.getFirstLevelCategoryId());
        if (firstLevelCategory == null) {
            throw new ServiceException(ExceptionCode.FIRST_LEVEL_CATEGORY_NOT_EXISTS);
        }
        List<SecondLevelCategoryDO> secondLevelCategoryDOList = myBatisSecondLevelCategoryRepository.findAll();
        if (secondLevelCategoryDOList != null) {
            boolean nameExist = secondLevelCategoryDOList.stream().
                    anyMatch(secondLevelCategoryDO -> secondLevelCategoryDO.getName().equals(categoryDTO.getCategoryName()));
            if (nameExist) {
                throw new ServiceException(ExceptionCode.CATEGORY_NAME_EXISTS);
            }
        }
        // 1 包装newSubCategory
        SecondLevelCategoryDO newSubCategory = secondLevelCategoryFactory.createDOByDTO(categoryDTO);
        // 保存入库
        myBatisSecondLevelCategoryRepository.insert(newSubCategory);
    }
}
