package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class UserIntercepter implements HandlerInterceptor {
    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(UserIntercepter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie  cookie = cookieUtil.getCookie(request,"loginTicket");
        if (cookie==null){
            return true;
        }
        String ticket  =  cookie.getValue();

        LoginTicket loginTicket = userService.findLoginTicket(ticket);
        if (loginTicket==null || loginTicket.getStatus()==0 || loginTicket.getExpired().before(new Date())){
            return true;
        }
        User user = userService.findUserById(loginTicket.getUserId());
        hostHolder.setUser(user);
        logger.debug("preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        modelAndView.addObject("loginUser",hostHolder.getUser());
        logger.debug("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clean();
        logger.debug("afterCompletion");
    }
}
