package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String discussPost(Model model, Page page){
        List<DiscussPost> discussPosts = discussPostService.selectDiscuss((page.getCurrent()-1)*10 ,0);
        page.setRows(discussPostService.selectRows());

        List<Map<String,Object>> res = new ArrayList<>();
        for (DiscussPost discussPost : discussPosts){
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserById(discussPost.userId);
            map.put("post",discussPost);
            map.put("user",user);
            res.add(map);
        }
        model.addAttribute("discussPosts",res);
        model.addAttribute("page",page);
        return "index.html";
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> test(Model model, Page page){
        List<DiscussPost> discussPosts = discussPostService.selectDiscuss((page.getCurrent()-1)*10 ,0);
        page.setRows(discussPostService.selectRows());
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> res = new ArrayList<>();
        for (DiscussPost discussPost : discussPosts){
            User user = userService.findUserById(discussPost.userId);
            map.put("post",discussPost);
            map.put("user",user);
            res.add(map);
        }
        return res;
    }
}
