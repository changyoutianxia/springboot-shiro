package com.ch.springbootshiro.mapper;

import com.ch.springbootshiro.po.UserRole;

import java.util.List;


public interface UserRoleMapper {
    List<UserRole> findUserRoleListByUserId(String userId);
}
