package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface LoginTicketMapper {
    void insertTicket(LoginTicket loginTicket);
    LoginTicket selectByTicket(String ticket);
    void updateStatus(int status,String ticket);
}
