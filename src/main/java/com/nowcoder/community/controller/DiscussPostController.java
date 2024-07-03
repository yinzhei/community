package com.nowcoder.community.controller;

import com.alibaba.fastjson.JSON;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.SensitiveFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String discussPost(Model model, Page page){
        page.setLimit(10);
        page.setPath("http://127.0.0.1:8080/community/index");
        page.setRows(discussPostService.selectRows());
        List<DiscussPost> discussPosts = discussPostService.selectDiscuss( page,0);


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

    @RequestMapping(value = "/postDetail/{postId}",method = RequestMethod.GET)
    public String postDetail(Model model,Page page,@PathVariable("postId") int postId){
        DiscussPost post = discussPostService.findByPostId(postId);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("post",post);
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("http://127.0.0.1:8080/community/postDetail/"+postId);
        page.setRows(post.getCommentCount());
        List<Comment> comments= commentService.findCommentByPostId(page,postId);
        List<HashMap<String,Object>>  res = new ArrayList<>();
        for (Comment comment:comments){
            User userComment = userService.findUserById(comment.getUserId());
            HashMap<String,Object> map = new HashMap<>();
            List<Comment> replys = commentService.findReplyByContentId(comment.getId());
            List<HashMap<String,Object>>  replyMap = new ArrayList<>();
            for (Comment reply:replys){
                 HashMap<String,Object> tem = new HashMap<>();
                 User user2 = userService.findUserById(reply.getUserId());
                 tem.put("reply",reply);
                 tem.put("user",user2);
                tem.put("target",null);
                 if (reply.getTargetId()!=0){
                     User target = userService.findUserById(reply.getTargetId());
                     tem.put("target",target);
                 }
                 replyMap.add(tem);
            }
            map.put("user",userComment);
            map.put("comment",comment);
            map.put("replys",replyMap);
            map.put("replyNum",commentService.findCommentCount(2,comment.getId()));
            res.add(map);
        }
        model.addAttribute("comments",res);
        return "site/discuss-detail.html";
    }


}
