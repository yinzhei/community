package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
public class CommentController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @LoginRequired
    @RequestMapping(value = "/addComment/{postId}",method = RequestMethod.POST)
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String addComment(Comment comment, @PathVariable("postId")int postID){
        comment.setCreateTime(new Date());
        comment.setStatus(0);
        comment.setUserId(hostHolder.getUser().getId());
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        commentService.addComment(comment);
        if (comment.getEntityType()==1){
           int count = commentService.findCommentCount(comment.getEntityType(),comment.getEntityId());
           discussPostService.updateCommentCount(postID,count);
        }
        return "redirect:/postDetail/"+postID;
    }
}
