package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectByPostId(int postId, int offset, int limit);
    List<Comment> selectReplyByContentId(int contentId);
    void insertCommment(Comment comment);
    int selectReplyCount(int entityType,int entityId);
}
