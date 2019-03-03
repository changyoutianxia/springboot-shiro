package com.ch.springbootshiro.service;

import com.ch.springbootshiro.po.Permission;
import com.ch.springbootshiro.po.Role;
import com.ch.springbootshiro.po.User;

import java.util.List;

public interface UserService {
    User findUserByUserNameAndStatus(String username, String status);

    List<Role> findRoleListByUserName(String userName);

    List<Permission> findPermissionListByUserName(String userName);
}
