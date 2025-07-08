package com.mirrormetech.corm.core.message.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.message.domain.dto.LikeInfoDTO;
import com.mirrormetech.corm.core.message.domain.dto.SessionUnreadDTO;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessageDO> {

    /**
     * 分页查询会话的历史消息
     * @param page 分页条件
     * @param chatSessionId 会话Id
     * @return 历史消息记录
     */
    @Select("SELECT * FROM chat_message cm WHERE cm.chat_session_id = #{chatSessionId} AND cm.msg_type in (1, 2, 3, 4) ")
    Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId);

    /**
     * 根据消息ID修改设备接收状态
     * @param msgId 消息ID
     * @param status 设备接收状态
     * @return 影响行数 >0 修改成功
     */
    @Update("UPDATE chat_message cm SET cm.device_receive_status = #{status} WHERE cm.id = #{msgId} AND receiver_type = 1")
    int updateDeviceReceiveStatus(@Param("msgId") Long msgId, @Param("status") Integer status);

    /**
     * 根据sessionId 查询其聊天消息
     * @param chatSessionId 聊天会话ID
     * @return 会话ID下的所有聊天消息
     */
    @Select("SELECT * FROM chat_message cm WHERE cm.chat_session_id = #{chatSessionId} ORDER BY cm.send_time desc")
    List<ChatMessageDO> selectByChatSessionId(@Param("chatSessionId") Long chatSessionId);

    /**
     * 根据sessionId 查询此聊天会话中的消息
     * 分页查询
     * @param page 分页查询条件
     * @param chatSessionId 聊天会话ID
     * @return 符合条件的分页内容
     */
    @Select("SELECT * FROM chat_message cm WHERE cm.chat_session_id = #{chatSessionId} ORDER BY cm.send_time desc")
    Page<ChatMessageDO> pageSelectByChatSession(Page<ChatMessageDO> page, @Param("chatSessionId") String chatSessionId);

    /**
     * 已读状态修改，根据会话ID修改所有聊天消息的已读状态
     * @param chatSessionId 会话ID
     * @return 影响行数
     */
    @Update("UPDATE chat_message cm SET cm.is_read = 1 WHERE cm.chat_session_id = #{chatSessionId} ")
    int updateAllRead(Long chatSessionId);

    /**
     * 已读状态修改，批量修改会话消息的已读状态
     * @param chatSessionId 会话ID
     * @param msgIds 已读的消息ID
     * @return 影响行数
     */
    int updateRead(Long chatSessionId, Long[] msgIds);

    /**
     * 已读状态修改，根据receiver_id修改其所有被点赞消息的状态
     * @param receiverId 被点赞人ID 查询方ID
     * @return 影响行数
     */
    @Update("UPDATE chat_message cm SET cm.is_read = 1 WHERE cm.chat_session_id = 0 AND receiver_id = #{receiverId} AND msg_type = 5")
    int updateLikeMsgRead(Long receiverId);

    /**
     * 已读状态修改，根据receiver_id批量修改其被点赞消息的状态
     * @param receiverId 被点赞人ID 查询方ID
     * @param msgIds 点赞消息Ids
     * @return 影响行数
     */
    int updateLikeMsgReadByIds(Long receiverId, Long[] msgIds);

    /**
     * 查询某个用户的所有未读消息数 包含会话 点赞 关注等
     * @param receiverId 接收人 被点赞 被关注 查询者ID
     * @return 总未读数
     */
    @Select("SELECT count(*) FROM chat_message cm  WHERE cm.receiver_id = #{receiverId} AND is_read = 0 AND receiver_type = 0")
    int selectAllUnread(Long receiverId);

    /**
     * 查询用户所有未读点赞消息数
     * @param receiverId 被点赞人ID
     * @return 未读点赞数
     */
    @Select("SELECT count(*) FROM chat_message cm  WHERE cm.receiver_id = #{receiverId} AND is_read = 0 AND msg_type = 5")
    int selectLikeMsgUnread(Long receiverId);

    /**
     * 查询用户所有未读关注消息数
     * @param receiverId 被关注人ID
     * @return 未读关注数
     */
    @Select("SELECT count(*) FROM chat_message cm  WHERE cm.receiver_id = #{receiverId} AND is_read = 0 AND msg_type = 6")
    int selectFollowMsgUnread(Long receiverId);

    /**
     * 某个具体聊天会话的未读消息 因为点赞和关注消息的chat_session_id=0 所以这里不需要对msg_type进行过滤
     * @param chatSessionId 会话ID
     * @return 此会话未读消息数
     */
    @Select("SELECT count(*) FROM chat_message cm  WHERE cm.chat_session_id = #{chatSessionId} AND cm.receiver_id =#{receiverId} AND is_read = 0 AND msg_type in (1, 2, 3, 4)")
    int selectChatSessionUnreadBySessionId(Long chatSessionId, Long receiverId);

    /**
     * 查询用户所有未读消息总数
     * @param receiverId 用户ID
     * @return 未读消息总数
     * AND receiver_type = 0 避免和发送设备的消息混淆
     */
    @Select("SELECT count(*) FROM chat_message cm WHERE cm.receiver_id = #{receiverId} AND is_read = 0 AND msg_type in (1,2,3,4) AND receiver_type = 0 ")
    int selectAllMsgUnread(Long receiverId);

    @Select("SELECT chat_session_id as chatSessionId, COUNT(*) AS unread FROM chat_message WHERE receiver_id = #{receiverId} AND is_read = 0 AND chat_session_id >0 GROUP BY chat_session_id HAVING count(*) >0 ")
    List<SessionUnreadDTO> selectAllSessionUnread(@Param("receiverId") Long receiverId);

    /**
     * 根据用户ID查询其被赞消息列表 支持分页
     * TODO cm.* 使用覆盖索引
     * @param page 分页查询条件
     * @param receiverId 被赞者用户ID
     * @param msgType 消息类型 5-点赞 6-关注
     * @return 分页数据
     */
    @Select(" SELECT cm.*, u.nick_name , u.avatar_url FROM chat_message cm INNER JOIN tb_user u ON cm.sender_id = u.id WHERE cm.receiver_id = #{receiverId} AND msg_type = #{msgType} ")
    Page<LikeInfoDTO> selectLikeInfo(Page<List<LikeInfoDTO>> page, Long receiverId, Integer msgType);

}
