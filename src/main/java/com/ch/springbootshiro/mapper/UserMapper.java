package com.ch.springbootshiro.mapper;

import com.ch.springbootshiro.po.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User findUserByUserNameAndStatus(@Param("username") String username,@Param("status") String status);
}
