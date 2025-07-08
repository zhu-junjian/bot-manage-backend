package com.mirrormetech.corm.core.chat.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.chat.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.chat.domain.dto.UpdateSessionCondition;
import com.mirrormetech.corm.core.chat.domain.dto.UpdateSessionDTO;
import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSessionDO> {

    /**
     * 将会话置为删除状态
     * @param sessionId 会话ID
     * @return 影响行数>0 修改成功
     */
    @Update("UPDATE chat_session cs SET cs.status = 1 WHERE cs.id = #{sessionId}")
    int deleteSession(@Param("sessionId") Long sessionId);

    /**
     * 置顶会话
     * @param sessionId 会话ID
     * @return 影响行数
     */
    @Update("UPDATE chat_session cs SET cs.is_top = 1, cs.top_time = CURRENT_TIMESTAMP  WHERE cs.id = #{sessionId}")
    int topSession(Long sessionId);

    /**
     * 取消置顶
     * @param sessionId 会话Id
     * @return 影响行数
     */
    @Update("UPDATE chat_session cs SET cs.is_top = 0, cs.top_time = CURRENT_TIMESTAMP  WHERE cs.id = #{sessionId}")
    int UnTopSession(Long sessionId);

    /**
     * 分页查询我的聊天会话列表
     * @param page 分页参数
     * @param req 查询条件 例如会话状态为 status=0
     * @return 符合条件的聊天会话列表
     */
    @Select("SELECT * FROM chat_session cs where (cs.user1_id = #{req.userId} OR cs.user2_id = #{req.userId}) AND cs.status = #{req.status} ORDER BY cs.is_top desc, cs.top_time desc")
    Page<ChatSessionDO> selectSessionsByUserId(Page<ChatSessionDO> page, @Param("req") QueryListDTO req);

    /**
     * 根据会话ID查询会话记录
     * @param sessionId 会话ID
     * @return 符合条件的记录
     */
    @Select("SELECT * FROM chat_session where session_id = #{sessionId}")
    ChatSessionDO selectBySessionId(String sessionId);

    /**
     * 打开聊天会话窗口时 使用
     * 仅更新update_time
     * @param sessionId 会话ID
     * @return 影响行数 >0 更新成功
     */
    @Update("UPDATE  chat_session cs SET cs.update_time = CURRENT_TIMESTAMP, cs.version = version +1 WHERE cs.version = #{version} and cs.session_id  = #{sessionId} ")
    int updateChatSession(String sessionId, Integer version);

    /**
     * 更新会话信息 有新消息的场景
     * @param updateSessionDTO 更新的内容
     * @param condition 更新的会话的参数条件
     * @return 影响行数 >0 更新成功
     */
    /*@Update(" UPDATE chat_session cs SET cs.update_time = CURRENT_TIMESTAMP, cs.version = version +1, last_msg_id = #{par.lastMsgId}, " +
            "last_msg_content = #{par.lastMsgContent}, last_msg_type = #{par.lastMsgType}, #{unreadField} = #{unreadField} + 1 " +
            " WHERE cs.version = #{con.version} and cs.session_id = #{con.sessionId}")
    int updateWithNewMessage(@Param("par") UpdateSessionDTO updateSessionDTO, @Param("con") UpdateSessionCondition condition);*/
    // 将未读数从session表中移除
    @Update(" UPDATE chat_session cs SET cs.update_time = CURRENT_TIMESTAMP, cs.version = version +1, last_msg_id = #{par.lastMsgId}, " +
            "last_msg_content = #{par.lastMsgContent}, last_msg_type = #{par.lastMsgType}, last_msg_time = #{par.lastMsgTime}" +
            " WHERE cs.version = #{con.version} and cs.session_id = #{con.sessionId}")
    int updateWithNewMessage(@Param("par") UpdateSessionDTO updateSessionDTO, @Param("con") UpdateSessionCondition condition);

    /**
     * 重置未读数 unreadField 重置的字段
     * @param updateSessionDTO 更新的内容
     * @param condition 更新的会话的参数条件
     * @return 影响行数 >0 更新成功
     */
    @Update("UPDATE chat_session " +
            "SET ${par.unreadField} = 0, " +
            "    version = version + 1, " +
            "    update_time = CURRENT_TIMESTAMP " +
            "WHERE session_id = #{con.sessionId} AND version = #{con.version}")
    int resetUnreadCount(@Param("par") UpdateSessionDTO updateSessionDTO, @Param("con") UpdateSessionCondition condition);

    /**
     * 查询当前事务中会话的版本号
     * 只查询版本号（减少数据传输）
     * @param sessionId 会话ID
     * @return 会话在当前事务开启时的版本号
     */
    @Select("SELECT version FROM chat_session WHERE session_id = #{sessionId}")
    Integer selectVersionById(@Param("sessionId") String sessionId);

}
