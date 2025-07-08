package com.mirrormetech.corm.core.chat.domain;

import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.chat.domain.VO.ChatSessionVO;
import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ChatSessionFactory {

    /**
     * 将数据库传输对象转换为展示对象 为后续添加其他展示属性做准备
     * @param chatSessionDO 数据库传输对象
     * @return 聊天会话展示对象
     */
    public ChatSessionVO DOToVO(ChatSessionDO chatSessionDO) {
        ChatSessionVO vo = new ChatSessionVO();
        BeanUtils.copyProperties(chatSessionDO, vo);
        return vo;
    }

    /**
     * 按照发起者 和 接收者  创建会话DO
     * @param initiator 发起者ID
     * @param recipient 接收者ID
     * @return 会话实体
     */
    public ChatSessionDO createDO(Long initiator, Long recipient, Integer sessionType){
        ChatSessionDO chatSessionDO = new ChatSessionDO();
        Long user1Id = Math.min(recipient, initiator);
        Long user2Id = Math.max(recipient, initiator);
        switch (sessionType){
            case 0:
                chatSessionDO.setSessionId(user1Id+"_"+user2Id);
            case 1:
                chatSessionDO.setSessionId(user1Id+"+"+user2Id);
        }
        chatSessionDO.setUser1Id(user1Id);
        chatSessionDO.setUser2Id(user2Id);
        chatSessionDO.setCreateTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        return chatSessionDO;
    }
}
