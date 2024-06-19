package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostDao;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostDao discussPostDao;

    public List<DiscussPost> selectDiscuss(int offset,int userId){
        return discussPostDao.selectDiscuss(offset,userId);
    }
    public int selectRows(){
        return discussPostDao.selectRows();
    }
}
