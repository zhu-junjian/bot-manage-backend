package com.mirrormetech.corm.core.user.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.user.infra.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleRepository extends BaseMapper<Role> {

    List<Role> findRolesByUserId(Long id);
}
