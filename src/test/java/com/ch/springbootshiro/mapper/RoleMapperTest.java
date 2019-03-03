package com.ch.springbootshiro.mapper;

import com.ch.springbootshiro.po.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@MapperScan("com.ch.springbootshiro.mapper")
public class RoleMapperTest {

    @Autowired
    RoleMapper roleMapper;

    @Test
    public void findUserRoleListByUserName() {
        List<Role> roleList = roleMapper.findUserRoleListByUserName("zhangsan");
        Set<String> roleSet = new HashSet<>();

        roleList.stream().forEach(currentRole->{roleSet.add(currentRole.getRole());});
        System.out.println(roleSet.size());
    }
}