package com.ch.springbootshiro.service.impl;

import com.ch.springbootshiro.mapper.PermissionMapper;
import com.ch.springbootshiro.mapper.RoleMapper;
import com.ch.springbootshiro.mapper.UserMapper;
import com.ch.springbootshiro.po.Permission;
import com.ch.springbootshiro.po.Role;
import com.ch.springbootshiro.po.User;
import com.ch.springbootshiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User findUserByUserNameAndStatus(String username, String status) {
        return userMapper.findUserByUserNameAndStatus(username, null);
    }

    @Override
    public List<Role> findRoleListByUserName(String userName) {
        return roleMapper.findUserRoleListByUserName(userName);
    }

    @Override
    public List<Permission> findPermissionListByUserName(String userName) {
        return permissionMapper.findUserPermissionListByUserName(userName);
    }

}
