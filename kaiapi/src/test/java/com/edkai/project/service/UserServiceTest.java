package com.edkai.project.service;

import com.edkai.project.model.entity.InterfaceCharge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 用户服务测试
 *
 * @author kai
 */

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private InterfaceChargeMapper interfaceChargeMapper;

    @Test
    public void test1() {
        List<Long> list = Arrays.asList(1L, 2L, 3L);
        List<InterfaceCharge> interfaceChargeList = interfaceChargeMapper.listByInterfaceIds(list);
        interfaceChargeList.forEach(System.out::println);
    }
}