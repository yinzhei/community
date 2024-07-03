package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostDao;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostDao discussPostDao;

    public List<DiscussPost> selectDiscuss(Page page, int userId){
        int offset = page.getOffset();
        int limit = page.getLimit();
        return discussPostDao.selectDiscuss(offset,limit,userId);
    }
    public int selectRows(){
        return discussPostDao.selectRows();
    }
    public void addPost(DiscussPost discussPost){
        discussPostDao.insertPost(discussPost);
    }
    public DiscussPost findByPostId(int postId){
        return discussPostDao.selectByPostId(postId);
    }
    public void updateCommentCount(int postId,int count){
        discussPostDao.updateCommentCount(postId,count);
    }

}
