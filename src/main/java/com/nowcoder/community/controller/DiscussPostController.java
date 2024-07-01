package com.nowcoder.community.controller;

import com.alibaba.fastjson.JSON;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.SensitiveFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
//@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String discussPost(Model model, Page page){
        List<DiscussPost> discussPosts = discussPostService.selectDiscuss((page.getCurrent()-1)*10 ,0);
        page.setRows(discussPostService.selectRows());

        List<Map<String,Object>> res = new ArrayList<>();
        for (DiscussPost discussPost : discussPosts){
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserById(discussPost.getUserId());
            map.put("post",discussPost);
            map.put("user",user);
            res.add(map);
        }
        model.addAttribute("discussPosts",res);
        model.addAttribute("page",page);
        return "index.html";
    }

    @LoginRequired
    @RequestMapping(value = "/addPost",method = RequestMethod.POST)
    @ResponseBody
    public String addPost(Model model,DiscussPost discussPost){
        if (StringUtils.isBlank(discussPost.getTitle())){
            return CommunityUtil.getJsonString(0,"标题不能为空");
        }
        if (StringUtils.isBlank(discussPost.getContent())){
            return CommunityUtil.getJsonString(0,"内容不能为空");
        }
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        discussPost.setUserId(hostHolder.getUser().getId());
        discussPost.setScore(0);
        discussPost.setType(0);
        discussPost.setCommentCount(0);
        discussPost.setStatus(0);
        discussPost.setCreate_time(new Date());

        discussPostService.addPost(discussPost);
        return CommunityUtil.getJsonString(1,"操作成功");
    }


    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> test(Model model, Page page){
        List<DiscussPost> discussPosts = discussPostService.selectDiscuss((page.getCurrent()-1)*10 ,0);
        page.setRows(discussPostService.selectRows());
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> res = new ArrayList<>();
        for (DiscussPost discussPost : discussPosts){
            User user = userService.findUserById(discussPost.getUserId());
            map.put("post",discussPost);
            map.put("user",user);
            res.add(map);
        }
        return res;
    }
}
