package com.zjy.baseframework.common;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CommonServiceTest {

    @Test
    public void buildTreeTest() {
        List<OrgVO> orgVOS = CommonService.buildTree(getOrgList());
        String s = JSON.toJSONString(orgVOS);
        System.out.println(s);
    }

    private List<OrgVO> getOrgList() {
        List<OrgVO> list = new ArrayList<>();
        OrgVO org;

//        org = new OrgVO();
//        org.setId("1");
//        org.setPId("");
//        org.setName("1");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("2");
//        org.setPId("1");
//        org.setName("2");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("3");
//        org.setPId("1");
//        org.setName("3");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("4");
//        org.setPId("2");
//        org.setName("4");
//        org.setSeq(5);
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("5");
//        org.setPId("2");
//        org.setName("5");
//        org.setSeq(4);
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("6");
//        org.setPId("3");
//        org.setName("6");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("7");
//        org.setPId("");
//        org.setName("7");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("8");
//        org.setPId("7");
//        org.setName("8");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("9");
//        org.setPId("8");
//        org.setName("9");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("10");
//        org.setName("10");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("11");
//        org.setPId("10");
//        org.setName("11");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("12");
//        org.setPId("10");
//        org.setName("12");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("13");
//        org.setPId("11");
//        org.setName("13");
//        list.add(org);
//
//        org = new OrgVO();
//        org.setId("14");
//        org.setPId("11");
//        org.setName("14");
//        list.add(org);

        return list;
    }
}