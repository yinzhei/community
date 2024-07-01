package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostDao;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private DiscussPostDao discussPostDao;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(999);
        System.out.println(user);

//        user = userMapper.selectByName("liubei");
//        System.out.println(user);
//
//        user = userMapper.selectByEmail("nowcoder101@sina.com");
//        System.out.println(user);
    }

    @Test
    public void testInsertTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(2);
        loginTicket.setStatus(0);
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis()+3600*24*1000));
        loginTicketMapper.insertTicket(loginTicket);
        loginTicketMapper.updateStatus(1,loginTicket.getTicket());
        LoginTicket loginTicket1 = loginTicketMapper.selectByTicket(loginTicket.getTicket());
        System.out.println(loginTicket1);
//        System.out.println(loginTicket1);
    }

    @Test
    public void testInsertPost() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(123);
        discussPost.setContent("test");
        discussPost.setScore(123);
        discussPost.setTitle("test");
        discussPost.setType(0);
        discussPost.setCommentCount(0);
        discussPost.setStatus(0);
        discussPost.setCreate_time(new Date());
        discussPostDao.insertPost(discussPost);
    }


    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hello");
        System.out.println(rows);
    }

}
