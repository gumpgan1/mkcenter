package com.mktech.dao;

import org.springframework.stereotype.Repository;

import com.mktech.entity.SnUserTicket;

@Repository
public interface SnUserTicketDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SnUserTicket record);

    int insertSelective(SnUserTicket record);

    SnUserTicket selectByPrimaryKey(Integer id);
    
    SnUserTicket selectByTicket(String ticket);

    int updateByPrimaryKeySelective(SnUserTicket record);

    int updateByPrimaryKey(SnUserTicket record);
    
    int updateStatusForLoggout(String ticket);
    
}