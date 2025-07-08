package com.mirrormetech.corm.core.message.infra.persistence;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.message.domain.dto.LikeInfoDTO;
import com.mirrormetech.corm.core.message.domain.dto.SessionUnreadDTO;
import com.mirrormetech.corm.core.message.domain.repository.ChatMessageMixedRepo;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 混合消息
 */
@Service
@RequiredArgsConstructor
public class MyBatisMessageRepoImpl implements ChatMessageMixedRepo {

    private final ChatMessageMapper chatMessageMapper;

    /**
     * 已读状态修改，批量修改会话消息的已读状态
     * @param chatSessionId 会话ID
     * @param msgIds 已读的消息ID
     * @return 影响行数
     */
    public int updateRead(Long chatSessionId, Long[] msgIds) {
        return chatMessageMapper.updateRead(chatSessionId, msgIds);
    }

    /**
     * 分页查询会话的历史消息
     * @param page 分页条件
     * @param chatSessionId 会话Id
     * @return 历史消息记录
     */
    public Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId){
        return chatMessageMapper.selectChatMessageByChatId(page, chatSessionId);
    }

    /**
     * 某个具体聊天会话的未读消息 因为点赞和关注消息的chat_session_id=0 所以这里不需要对msg_type进行过滤
     * @param id 会话表的主键ID
     * @return 此会话未读消息数
     */
    public int getChatUnreadCountById(Long id, Long receiverId) {
        return chatMessageMapper.selectChatSessionUnreadBySessionId(id, receiverId);
    }

    /**
     * 根据消息ID修改设备接收状态
     * @param msgId 消息ID
     * @param status 设备接收状态
     * @return 影响行数 >0 修改成功
     */
    public int updateDeviceReceiveStatus(Long msgId, Integer status) {
        return chatMessageMapper.updateDeviceReceiveStatus(msgId, status);
    }

    /**
     * 根据用户ID查询其被赞消息列表 支持分页
     * TODO cm.* 使用覆盖索引
     * @param page 分页查询条件
     * @param receiverId 被赞者用户ID
     * @param msgType 消息类型 5-点赞 6-关注
     * @return 分页数据
     * WHERE cm.receiver_id = #{receiverId} AND msg_type = #{msgType}
     * Page<List<LikeInfoDTO>> page,
     */
    @Override
    public Page<LikeInfoDTO> selectLikeInfo(Page<List<LikeInfoDTO>> page, Long receiverId, Integer msgType) {
        return chatMessageMapper.selectLikeInfo(page, receiverId, msgType);
    }

    /**
     * 根据用户ID查询其 所有未读消息数
     * @param userId 被查询用户
     * @return 未读消息总数
     */
    @Override
    public int getAllUnreadCount(Long userId) {
        return chatMessageMapper.selectAllUnread(userId);
    }

    /**
     * 根据用户ID查询其 所有未读点赞数
     * @param userId 被查询用户
     * @return 未读点赞消息数
     */
    @Override
    public int getUnreadLikeCount(Long userId) {
        return chatMessageMapper.selectLikeMsgUnread(userId);
    }

    /**
     * 根据用户ID查询其 所有未读关注数
     * @param userId 被查询用户
     * @return 未读关注数
     */
    @Override
    public int getUnreadFollowCount(Long userId) {
        return chatMessageMapper.selectFollowMsgUnread(userId);
    }

    /**
     * 根据用户ID查询其 所有非点赞 关注 消息数
     * @param userId 被查询用户
     * @return 未读消息数
     */
    @Override
    public int getUnreadMsgCount(Long userId) {
        return chatMessageMapper.selectAllMsgUnread(userId);
    }

    /**
     * 根据用户ID查询其所有会话未读消息数 未读消息>0
     * @param userId 被查询用户
     * @return chatSessionId count
     */
    @Override
    public List<SessionUnreadDTO> getUnreadMsgCountWithSessionId(Long userId) {
        return chatMessageMapper.selectAllSessionUnread(userId);
    }

    @Override
    public int getUnreadMsgCountBySessionId(Long userId, String sessionId) {
        return 0;
    }
}
