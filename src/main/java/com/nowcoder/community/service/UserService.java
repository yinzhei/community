package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class UserService implements CommunityConstant {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String path;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String,Object> registerUser(User user){
        Map<String,Object> res = new HashMap<>();
        if (user==null){
            logger.error("user参数为空");
            return res;
        }
        if (user.getUsername()==null){
            res.put("username","用户名不能为空");
            return res;
        }
        if (user.getPassword()==null){
            res.put("password","密码不能为空");
            return res;
        }
        if (user.getEmail()==null){
            res.put("email","邮箱不能为空");
            return res;
        }
        if (userMapper.selectByName(user.getUsername())!=null){
            res.put("username","该账户名已被注册");
            return res;
        }
        if (userMapper.selectByEmail(user.getEmail())!=null){
            res.put("email","该邮箱已被注册");
            return res;
        }
        // 插入一名用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setCreateTime(new Date());
        user.setStatus(0);
        user.setType(0);
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setActivationCode(CommunityUtil.generateUUID());
        userMapper.insertUser(user);

        // 发送激活邮件

        String url = domain + path + "/activation/" + user.getId() + "/" +user.getActivationCode();
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation.html",context);
        mailClient.sendMail(user.getEmail(),"nowcoder激活邮件",content);

        return res;
    }
    public int  activationService(int userId,String act_code){
        User user =  userMapper.selectById(userId);
        if (user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }else{
            if (user.getActivationCode()==act_code){
                userMapper.updateStatus(userId,1);
                return ACTIVATION_SUCCESS;
            }else{
                return ACTIVATION_FAIL;
            }
        }

    }

}
