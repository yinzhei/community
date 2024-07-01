package com.nowcoder.community.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
    public Cookie getCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies==null){
            return null;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(cookieName)){
                return  cookie;
            }
        }
        return null;
    }
}
