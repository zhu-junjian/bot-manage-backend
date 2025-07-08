package com.mirrormetech.corm.core.message;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.message.domain.dto.LikeInfoDTO;
import com.mirrormetech.corm.core.message.domain.dto.UnreadMsgDTO;
import com.mirrormetech.corm.core.message.domain.dto.UpdateReceiveStatus;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * 消息域
 */
@RestController
@RequestMapping("/api/v1/msg")
@RequiredArgsConstructor
public class MessageController {

    @Qualifier("chatMessageServiceImpl")
    private final ChatMessageService chatSessionService;

    /**
     * 分页查询某个会话的历史聊天记录
     * TODO 查询时需要携带当前的用户ID 判断是否更新消息未读数
     * @param chatSessionId 会话的ID
     * @return 分页的消息列表  按时间倒序排序
     */
    @GetMapping
    public ApiResult<Page<ChatMessageDO>> getChatMessages(@RequestParam(value = "chatSessionId") Long chatSessionId,
                                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Page<ChatMessageDO> chatMessageDOPage = chatSessionService.selectChatMessageByChatId(new Page<>(page, size), chatSessionId);
        return ApiResult.success(chatMessageDOPage);
    }

    /**
     * 根据消息ID修改设备接收状态
     * @param msgId 消息ID
     * @param updateReceiveStatus 修改内容
     * @return 影响行数 >0 修改成功
     */
    @PutMapping("/deviceStatus/{msgId}")
    public ApiResult<ChatMessageDO> updateDeviceStatus(@RequestBody UpdateReceiveStatus updateReceiveStatus, @PathVariable Long msgId) {
        chatSessionService.updateDeviceReceiveStatus(msgId, updateReceiveStatus.getStatus());
        return ApiResult.success();
    }

    /**
     * 消息页首页未读数
     * 未读数查询 某个用户所有会话未读消息总数
     * @param userId 被查询的用户ID
     * @return 所有未读消息信息
     */
    @GetMapping("/{userId}")
    public ApiResult<UnreadMsgDTO> getUnreadInfo(@PathVariable("userId") Long userId) {
        UnreadMsgDTO unreadInfo = chatSessionService.getUnreadInfo(userId);
        return ApiResult.success(unreadInfo);
    }

    /**
     * 查询用户被点赞列表
     * @param receiverId 被查询用户
     * @param page 分页的页码
     * @param size 单页的数量
     * @return 用户被点赞列表支持分页
     */
    @GetMapping("/likeList")
    public ApiResult<Page<LikeInfoDTO>> getLikeInfo(@RequestParam(value = "receiverId") Long receiverId,
                                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Page<LikeInfoDTO> result = chatSessionService.getLikeInfo(new Page<>(page, size), receiverId);
        return ApiResult.success(result);
    }

    /**
     * 用户被关注列表
     * @param receiverId 被查询用户
     * @param page 分页的页码
     * @param size 单页的数量
     * @return 用户被关注列表支持分页
     */
    @GetMapping("/followList")
    public ApiResult<Page<LikeInfoDTO>> getFollowInfo(@RequestParam(value = "receiverId") Long receiverId,
                                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Page<LikeInfoDTO> result = chatSessionService.getFollowInfo(new Page<>(page, size), receiverId);
        return ApiResult.success(result);
    }
}
