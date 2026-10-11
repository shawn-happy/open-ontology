package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 用户 Mapper
 */
@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    SystemUser selectByUsername(@Param("username") String username);

    SystemUser selectByPhone(@Param("phone") String phone);

    SystemUser selectByEmail(@Param("email") String email);

    void updateLoginInfo(@Param("id") Long id, @Param("ip") String ip, @Param("loginAt") LocalDateTime loginAt);

    void updateFailCount(@Param("id") Long id, @Param("count") Integer count, @Param("lockUntil") LocalDateTime lockUntil);
}
