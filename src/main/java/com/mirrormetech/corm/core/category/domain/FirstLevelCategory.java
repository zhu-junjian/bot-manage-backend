package com.mirrormetech.corm.core.category.domain;

import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mysql.cj.util.StringUtils;
import lombok.Data;

import java.util.List;

/**
 * 内容发布的  一级大类 领域模型
 * 将引起领域模型状态变更的动作收敛在内部
 * @author spencer
 * @date 2025/04/27
 */
@Data
public class FirstLevelCategory {

    /**
     * 大类主键ID
     */
    private Long id;

    /**
     * 大类名称
     */
    private String name;

    /**
     * 二级大类列表
     * 一级大类下的 二级大类
     */
    private List<SecondLevelCategory> secondLevelCategories;

    public FirstLevelCategory() {}

    /**
     * 向当前一级大类 下 添加 二级大类
     * @param newSubCategory 新增的二级大类对象列表
     *               secondLevelCategories 当前一级大类下所有二级大类的集合
     */
    public void addSubCategory(SecondLevelCategory newSubCategory) {
        // 1.校验参数
        if(StringUtils.isNullOrEmpty(newSubCategory.getName()) || null == newSubCategory.getParentId()){
            throw new ServiceException(ExceptionCode.MISSING_PARENT_FIRST_LEVEL);
        }
        // 2.子类重命名校验
        if (secondLevelCategories != null) {
            boolean nameExists = secondLevelCategories.stream().anyMatch(c -> c.getName().equals(newSubCategory.getName()));
            if(nameExists){
                throw new ServiceException(ExceptionCode.CATEGORY_NAME_EXISTS);
            }
        }
        //TODO 3.MAX_NUM 校验


        // 4.封装为完整的一级 二级对象传入仓储层 下水道逻辑 入库
        secondLevelCategories.add(newSubCategory);

        //TODO  5.其他异步 领域事件
    }

}
