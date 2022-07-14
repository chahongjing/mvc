package com.zjy.baseframework.common;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CommonServiceTest {

    @Test
    public void buildTreeTest() {
        List<OrgVO> orgVOS = CommonService.buildTree(getOrgList());
        printOrgTree(orgVOS);
    }

    private List<OrgVO> getOrgList() {
        List<OrgVO> list = new ArrayList<>();
        OrgVO org;

        org = new OrgVO();
        org.setId(1L);
        org.setPid(null);
        org.setName("1");
        list.add(org);

        org = new OrgVO();
        org.setId(2L);
        org.setPid(1L);
        org.setName("2");
        list.add(org);

        org = new OrgVO();
        org.setId(3L);
        org.setPid(1L);
        org.setName("3");
        list.add(org);

        org = new OrgVO();
        org.setId(4L);
        org.setPid(2L);
        org.setName("4");
        org.setSeq(5);
        list.add(org);

        org = new OrgVO();
        org.setId(5L);
        org.setPid(2L);
        org.setName("5");
        org.setSeq(4);
        list.add(org);

        org = new OrgVO();
        org.setId(6L);
        org.setPid(3L);
        org.setName("6");
        list.add(org);

        org = new OrgVO();
        org.setId(7L);
        org.setPid(null);
        org.setName("7");
        list.add(org);

        org = new OrgVO();
        org.setId(8L);
        org.setPid(7L);
        org.setName("8");
        list.add(org);

        org = new OrgVO();
        org.setId(9L);
        org.setPid(8L);
        org.setName("9");
        list.add(org);

        org = new OrgVO();
        org.setId(10L);
        org.setName("10");
        list.add(org);

        org = new OrgVO();
        org.setId(11L);
        org.setPid(10L);
        org.setName("11");
        list.add(org);

        org = new OrgVO();
        org.setId(12L);
        org.setPid(10L);
        org.setName("12");
        list.add(org);

        org = new OrgVO();
        org.setId(13L);
        org.setPid(11L);
        org.setName("13");
        list.add(org);

        org = new OrgVO();
        org.setId(14L);
        org.setPid(11L);
        org.setName("14");
        list.add(org);

        return list;
    }

    private void printOrgTree(List<OrgVO> tree) {
        for (OrgVO vo : tree) {
            System.out.printf("id:%d, pid:%d, name:%s, seq:%d%n", vo.getId(), vo.getPid(), vo.getName(), vo.getSeq());
            if (!CollectionUtils.isEmpty(vo.getChildren())) {
                printOrgTree(vo.getChildren());
            }
        }
    }
}