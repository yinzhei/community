package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscussPostDao {
    List<DiscussPost> selectDiscuss(int offset,int limit,int userId);
    int selectRows();
    void insertPost(DiscussPost discussPost);
    DiscussPost selectByPostId(int postId);
    void updateCommentCount(int id,int commentCount);
}
