package com.bjzhianjia.scp.merge.demo.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.merge.demo.biz.UserBiz;
import com.bjzhianjia.scp.merge.demo.entity.User;

import java.util.List;

/**
 * @author scp
 * @create 2017/11/20.
 */
@RestController
@RequestMapping("user")
public class UserRest {
    @Autowired
    private UserBiz userBiz;
    private Logger logger = LoggerFactory.getLogger(UserRest.class);

    @RequestMapping("/merge/auto_list")
    public List<User> getAutoMergeList() {
        return userBiz.getAotoMergeUser();
    }

    @RequestMapping("/merge/list")
    public List<User> getMergeList() {
        return userBiz.getMergeUser();
    }

    @RequestMapping("/list")
    public List<User> getList() {
        return userBiz.getUser();
    }

}
