package com.wtx.myblog.mapper;

import com.wtx.myblog.model.LeaveMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/25
 * @description
 */
@Mapper
@Repository
public interface LeaveMessageMapper {
    void publishLeaveMessage(LeaveMessage leaveMessage);

    List<LeaveMessage> getAllLeaveMessage(String pageName);

    int getIsRead(@Param("pageName") String pageName, @Param("pId") int pId, @Param("likerId") int likerId);

    String findLeaveMessageById(int id);

    String findleaveMessageDateById(int id);

    List<LeaveMessage> getAllLeaveMessageReply(String pageName, int id);
}
