package com.ch.springbootshiro.mapper;

import com.ch.springbootshiro.po.RolePermission;
import org.apache.ibatis.annotations.Select;


public interface RoleAndPermissionMapper {
    @Select("select id,role,permission from roles_permissions where id=#{id}")
    RolePermission findUserRolePermissionById(Integer id);
}
