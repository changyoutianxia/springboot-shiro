package com.ch.springbootshiro.mapper;

import com.ch.springbootshiro.po.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface PermissionMapper {
    @Select("select id,permission from permissions where id=#{id}")
    Permission findUserPermissionById(Integer id);

    List<Permission> findUserPermissionListByUserName(String userName);

}
