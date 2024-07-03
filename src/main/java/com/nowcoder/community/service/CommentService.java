package com.nowcoder.community.service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> findCommentByPostId(Page page, int postId){
        return commentMapper.selectByPostId(postId, page.getOffset(), page.getLimit());
    }
    public List<Comment> findReplyByContentId(int contentId){
        return commentMapper.selectReplyByContentId(contentId);
    }
    public void addComment(Comment comment){
        commentMapper.insertCommment(comment);
    }
    public int findCommentCount(int entityType,int entityId){
        return commentMapper.selectReplyCount(entityType,entityId);
    }
}
