package com.mirrormetech.corm.core.user.domain;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.user.infra.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserRepository extends BaseMapper<User> {

    User findByUsername(String username);
}
