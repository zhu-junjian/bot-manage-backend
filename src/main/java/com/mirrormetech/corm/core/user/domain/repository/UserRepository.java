package com.mirrormetech.corm.core.user.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.user.domain.vo.UserUpdateVO;
import com.mirrormetech.corm.core.user.infra.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@Mapper
public interface UserRepository {

    boolean userExists(Long userId);

    User findByUsername(String username);

    int insertUser(User user);
    
}
